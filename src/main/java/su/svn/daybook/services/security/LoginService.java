/*
 * This file was last modified at 2023.01.05 15:20 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * LoginService.java
 * $Id$
 */

package su.svn.daybook.services.security;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.annotations.Logged;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.messages.ApiResponse;
import su.svn.daybook.domain.messages.Request;
import su.svn.daybook.domain.model.SessionTable;
import su.svn.daybook.models.security.AuthRequest;
import su.svn.daybook.models.security.User;
import su.svn.daybook.services.ExceptionAnswerService;
import su.svn.daybook.services.cache.LoginCacheProvider;
import su.svn.daybook.services.cache.SessionCacheProvider;
import su.svn.daybook.services.domain.LoginDataService;

import javax.annotation.Nonnull;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.UUID;

@ApplicationScoped
@Logged
public class LoginService {

    private static final Logger LOG = Logger.getLogger(LoginService.class);

    @Inject
    LoginCacheProvider loginCacheProvider;

    @Inject
    LoginDataService loginDataService;

    @Inject
    SessionCacheProvider sessionCacheProvider;

    @Inject
    PBKDF2Encoder passwordEncoder;

    @Inject
    TokenService tokenService;

    @Inject
    AuthRequestContext authRequestContext;

    @Inject
    ExceptionAnswerService exceptionAnswerService;

    @ConsumeEvent(EventAddress.LOGIN_REQUEST)
    public Uni<Answer> login(@Nonnull Request<AuthRequest> request) {
        LOG.tracef("login(%s): requestId: %s", request.payload(), authRequestContext.getRequestId());
        authRequestContext.setAuthRequest(request.payload());
        return loginCacheProvider
                .get(request.payload().username())
                .flatMap(u -> authentication(User.from(u)))
                .onFailure(exceptionAnswerService::testAuthenticationFailedException)
                .recoverWithUni(exceptionAnswerService::authenticationFailedUniAnswer)
                .onTermination()
                .invoke(authRequestContext::close);
    }

    private Uni<Answer> authentication(User user) {
        var authRequest = authRequestContext.getAuthRequest();
        if (user.password() != null) {
            if (user.password().equals(passwordEncoder.encode(authRequest.password()))) {
                return generateToken(user)
                        .map(token -> Answer.of(ApiResponse.auth(token)))
                        .flatMap(answer -> sessionCacheProvider.invalidateByUserName(user.username(), answer));
            }
        }
        throw authRequestContext.authenticationFailed();
    }

    private Uni<String> generateToken(User user) {
        var roles = new HashSet<>(user.roles());
        roles.add("GUEST");
        SessionTable session = SessionTable.builder()
                .userName(user.username())
                .roles(roles)
                .validTime(tokenService.validTime())
                .build();
        return loginDataService
                .insert(session)
                .map(this::generateToken);
    }

    private String generateToken(UUID u) {
        var audience = authRequestContext.getRequestId().toString();
        return tokenService.generate(u.toString(), audience);
    }
}