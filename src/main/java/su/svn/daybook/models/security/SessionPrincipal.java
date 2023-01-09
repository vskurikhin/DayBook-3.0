package su.svn.daybook.models.security;

import io.smallrye.jwt.auth.principal.DefaultJWTCallerPrincipal;
import org.eclipse.microprofile.jwt.Claims;
import org.jose4j.jwt.JwtClaims;

import java.util.Set;
import java.util.UUID;

public class SessionPrincipal extends DefaultJWTCallerPrincipal {

    private final String name;

    private final Set<String> groups;

    private final String tokenType;

    private final UUID requestId;

    public SessionPrincipal(String tokenType, JwtClaims claimsSet, String name, Set<String> groups, UUID requestId) {
        super(tokenType, claimsSet);
        this.name = name;
        this.groups = groups;
        this.tokenType = tokenType;
        this.requestId = requestId;
    }

    @Override
    public Set<String> getGroups() {
        return this.groups;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public String getSessionId() {
        return super.getName();
    }

    public UUID getRequestId() {
        return requestId;
    }

    @Override
    public String toString() {
        return "SessionPrincipal{" +
                "id='" + super.getTokenID() + '\'' +
                ", name='" + name + '\'' +
                ", groups=" + groups +
                ", expiration=" + super.getExpirationTime() +
                ", notBefore=" + super.getClaim(Claims.nbf.name()) +
                ", issuedAt=" + super.getIssuedAtTime() +
                ", issuer='" + super.getIssuer() + '\'' +
                ", audience=" + super.getAudience() +
                ", subject='" + super.getSubject() + '\'' +
                ", type='" + tokenType + '\'' +
                ", issuedFor='" + super.getClaim("azp") + '\'' +
                ", authTime=" + super.getClaim("auth_time") +
                ", givenName='" + super.getClaim("given_name") + '\'' +
                ", familyName='" + super.getClaim("family_name") + '\'' +
                ", middleName='" + super.getClaim("middle_name") + '\'' +
                ", nickName='" + super.getClaim("nickname") + '\'' +
                ", preferredUsername='" + super.getClaim("preferred_username") + '\'' +
                ", email='" + super.getClaim("email") + '\'' +
                ", emailVerified=" + super.getClaim(Claims.email_verified.name()) +
                ", allowedOrigins=" + super.getClaim("allowedOrigins") +
                ", updatedAt=" + super.getClaim("updated_at") +
                ", acr='" + super.getClaim("acr") +
                '}';
    }
}
