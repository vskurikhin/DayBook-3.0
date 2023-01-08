/*
 * This file was last modified at 2023.01.05 15:20 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * AuthResource.java
 * $Id$
 */

package su.svn.daybook.resources;

import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.enums.ResourcePath;
import su.svn.daybook.models.security.AuthRequest;
import su.svn.daybook.models.security.AuthResponse;
import su.svn.daybook.services.security.LoginService;
import su.svn.daybook.services.security.TokenService;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Set;
import java.util.UUID;

@Path(ResourcePath.AUTH)
public class AuthResource extends AbstractResource {

    private static final Logger LOG = Logger.getLogger(AuthResource.class);

    @Inject
    LoginService loginService;

    @Inject
    TokenService tokenService;

    @PermitAll
    @POST
    @Path(ResourcePath.LOGIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> login(AuthRequest authRequest, @Context UriInfo uriInfo) {
        return request(EventAddress.LOGIN_REQUEST, authRequest, uriInfo);/* loginService
                .login(authRequest)
                .map(this::getBuild)
                .onFailure()
                .recoverWithUni(this::responseUnauthorized);*/
    }

    private Response getBuild(Set<String> roles) {
        return Response
                .ok(new AuthResponse(generateToken(roles)))
                .build();
    }

    private String generateToken(Set<String> roles) {
        return tokenService.generate(new UUID(0, 0).toString(), roles);
    }

    private Uni<Response> responseUnauthorized(Throwable throwable) {
        LOG.error(throwable);
        return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED).build());
    }
}