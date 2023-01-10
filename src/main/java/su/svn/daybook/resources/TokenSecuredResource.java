package su.svn.daybook.resources;

import io.quarkus.security.identity.CurrentIdentityAssociation;
import io.quarkus.security.identity.SecurityIdentity;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.jboss.logging.Logger;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

@Path("/")
public class TokenSecuredResource {

    private static final Logger LOG = Logger.getLogger(TokenSecuredResource.class);

    @Inject
    JsonWebToken jwt;

    // @Operation(hidden = true)
    @GET()
    @Path("permit-all")
    @PermitAll
    @SecurityRequirement(name = "day-book")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello(@Context SecurityContext ctx) {
        return getResponseString(ctx);
    }

    private String getResponseString(SecurityContext ctx) {
        String name;
        if (ctx.getUserPrincipal() == null) {
            name = "anonymous";
        } else if (!ctx.getUserPrincipal().getName().equals(jwt.getName())) {
            throw new InternalServerErrorException("Principal and JsonWebToken names do not match");
        } else {
            name = ctx.getUserPrincipal().getName();
        }
        SecurityIdentity identity = CurrentIdentityAssociation.current();

        var principal = identity.getPrincipal();
        var roles = identity.getRoles();
        return String.format("hello + %s,"
                        + " isHttps: %s,"
                        + " authScheme: %s,"
                        + " principal: %s,"
                        + " roles: %s"
                        + " hasJWT: %s",
                name, ctx.isSecure(), ctx.getAuthenticationScheme(), principal, roles, hasJwt());
    }

    // @Operation(hidden = true)
    @GET
    @Path("rolse-allowed")
    @RolesAllowed("GUEST")
    @SecurityRequirement(name = "day-book")
    @Produces(MediaType.TEXT_PLAIN)
    public String helloRolesAllowed(@Context SecurityContext ctx) {
        return getResponseString(ctx)
                + ", sub: " + jwt.getClaim("sub").toString()
                + ", aud: " + jwt.getClaim("aud").toString();
    }


    private boolean hasJwt() {
        return jwt.getClaimNames() != null;
    }
}
