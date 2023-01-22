/*
 * This file was last modified at 2023.01.22 14:59 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * SessionJWTAuthMechanism.java
 * $Id$
 */

package su.svn.daybook.services.security;

import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.quarkus.arc.Priority;
import io.quarkus.security.AuthenticationFailedException;
import io.quarkus.security.identity.IdentityProviderManager;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.AuthenticationRequest;
import io.quarkus.security.identity.request.TokenAuthenticationRequest;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.quarkus.smallrye.jwt.runtime.auth.JWTAuthMechanism;
import io.quarkus.smallrye.jwt.runtime.auth.JsonWebTokenCredential;
import io.quarkus.vertx.http.runtime.security.ChallengeData;
import io.quarkus.vertx.http.runtime.security.HttpAuthenticationMechanism;
import io.quarkus.vertx.http.runtime.security.HttpCredentialTransport;
import io.quarkus.vertx.http.runtime.security.HttpSecurityUtils;
import io.smallrye.jwt.auth.AbstractBearerTokenExtractor;
import io.smallrye.jwt.auth.principal.DefaultJWTTokenParser;
import io.smallrye.jwt.auth.principal.JWTAuthContextInfo;
import io.smallrye.jwt.auth.principal.JWTCallerPrincipal;
import io.smallrye.jwt.auth.principal.ParseException;
import io.smallrye.mutiny.Uni;
import io.vertx.core.http.Cookie;
import io.vertx.ext.web.RoutingContext;
import org.eclipse.microprofile.jwt.Claims;
import org.jboss.logging.Logger;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.JwtContext;
import su.svn.daybook.models.domain.Session;
import su.svn.daybook.models.security.SessionPrincipal;
import su.svn.daybook.services.cache.SessionCacheProvider;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

import static io.vertx.core.http.HttpHeaders.COOKIE;

@Alternative
@Priority(1)
@ApplicationScoped
public class SessionJWTAuthMechanism implements HttpAuthenticationMechanism {

    private static final Logger LOG = Logger.getLogger(SessionJWTAuthMechanism.class);

    @Inject
    JWTAuthMechanism delegate;

    @SuppressWarnings("QsPrivateBeanMembersInspection")
    @Inject
    private JWTAuthContextInfo authContextInfo;

    @Inject
    SessionCacheProvider sessionCacheProvider;

    @Override
    public Uni<SecurityIdentity> authenticate(RoutingContext context, IdentityProviderManager identityProviderManager) {
        // do some custom action and delegate
        LOG.tracef("authenticate(%s, %s)", context, identityProviderManager);
        return delegate
                .authenticate(context, identityProviderManager)
                .flatMap(securityIdentity -> rebuildSecurityIdentity(context, securityIdentity));
    }

    private Uni<SecurityIdentity> rebuildSecurityIdentity(RoutingContext context, SecurityIdentity securityIdentity) {
        var rb = new SessionSecurityIdentityReBuilder(authContextInfo, sessionCacheProvider, context);
        return rb.apply(securityIdentity);
    }

    @Override
    public Uni<ChallengeData> getChallenge(RoutingContext context) {
        LOG.tracef("getChallenge(%s)", context);
        return delegate.getChallenge(context);
    }

    @Override
    public Set<Class<? extends AuthenticationRequest>> getCredentialTypes() {
        LOG.tracef("getCredentialTypes(): %s", delegate);
        return delegate.getCredentialTypes();
    }

    @Override
    public Uni<HttpCredentialTransport> getCredentialTransport(RoutingContext context) {
        LOG.tracef("getCredentialTransport(%s)", context);
        return delegate.getCredentialTransport(context);
    }

    private static class SessionSecurityIdentityReBuilder implements Function<SecurityIdentity, Uni<SecurityIdentity>> {

        private final JWTAuthContextInfo authContextInfo;

        private final SessionCacheProvider sessionCacheProvider;

        private final RoutingContext context;

        private final DefaultJWTTokenParser parser = new DefaultJWTTokenParser();

        SessionSecurityIdentityReBuilder(
                JWTAuthContextInfo authContextInfo,
                SessionCacheProvider sessionCacheProvider,
                RoutingContext context) {
            this.authContextInfo = authContextInfo;
            this.sessionCacheProvider = sessionCacheProvider;
            this.context = context;
        }

        @Override
        public Uni<SecurityIdentity> apply(SecurityIdentity securityIdentity) {
            return rebuild(securityIdentity);
        }

        private Uni<SecurityIdentity> rebuild(SecurityIdentity securityIdentity) {
            var jwtToken = new VertxBearerTokenExtractor(authContextInfo, context).getBearerToken();
            if (jwtToken != null) {
                return createPrincipal(jwtToken)
                        .map(jwtPrincipal -> rebuild(securityIdentity, jwtToken, jwtPrincipal));
            }
            return Uni.createFrom().optional(Optional.empty());
        }

        private SecurityIdentity rebuild(SecurityIdentity securityIdentity, String token, JWTCallerPrincipal principal) {
            var request = createTokenAuthenticationRequest(context, token);
            return QuarkusSecurityIdentity
                    .builder(securityIdentity)
                    .setPrincipal(principal)
                    .addCredential(request.getToken())
                    .addRoles(principal.getGroups())
                    .build();
        }

        private TokenAuthenticationRequest createTokenAuthenticationRequest(RoutingContext context, String jwtToken) {
            return (TokenAuthenticationRequest) HttpSecurityUtils.setRoutingContextAttribute(
                    new TokenAuthenticationRequest(new JsonWebTokenCredential(jwtToken)), context
            );
        }

        private Uni<JWTCallerPrincipal> createPrincipal(final String token) {
            try {
                var jwtContext = parser.parse(token, authContextInfo);
                var type = jwtContext.getJoseObjects().get(0).getHeader("typ");
                var subName = jwtContext.getJwtClaims().getClaimValue(Claims.sub.name());
                LOG.tracef("authenticate->createPrincipal->subject: %s", subName);
                if (subName instanceof String subject) {
                    return sessionCacheProvider
                            .get(UUID.fromString(subject))
                            .map(session -> createPrincipal(type, jwtContext, session));
                }
                LOG.debug("Authentication failed Subject not found");
                throw new AuthenticationFailedException("Authentication failed Subject not found");
            } catch (AuthenticationFailedException | IllegalArgumentException | ParseException e) {
                LOG.debug("Authentication failed", e);
                throw new AuthenticationFailedException(e);
            }
        }

        private JWTCallerPrincipal createPrincipal(String type, JwtContext jwtContext, Session session) {
            try {
                return new SessionPrincipal(
                        type,
                        jwtContext.getJwtClaims(),
                        session.userName(),
                        session.roles(),
                        UUID.fromString(jwtContext.getJwtClaims().getAudience().get(0))
                );
            } catch (IllegalArgumentException | MalformedClaimException e) {
                throw new AuthenticationFailedException(e);
            }
        }
    }

    private static class VertxBearerTokenExtractor extends AbstractBearerTokenExtractor {
        private RoutingContext httpExchange;

        VertxBearerTokenExtractor(JWTAuthContextInfo authContextInfo, RoutingContext exchange) {
            super(authContextInfo);
            this.httpExchange = exchange;
        }

        @Override
        protected String getHeaderValue(String headerName) {
            return httpExchange.request().headers().get(headerName);
        }

        @Override
        protected String getCookieValue(String cookieName) {
            String cookieHeader = httpExchange.request().headers().get(COOKIE);

            if (cookieHeader != null && httpExchange.cookieCount() == 0) {
                Set<io.netty.handler.codec.http.cookie.Cookie> nettyCookies = ServerCookieDecoder.STRICT.decode(cookieHeader);
                for (io.netty.handler.codec.http.cookie.Cookie cookie : nettyCookies) {
                    if (cookie.name().equals(cookieName)) {
                        return cookie.value();
                    }
                }
            }
            Cookie cookie = httpExchange.getCookie(cookieName);
            return cookie != null ? cookie.getValue() : null;
        }
    }
}