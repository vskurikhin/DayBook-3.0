/*
 * This file was last modified at 2024-10-30 14:17 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * AuthResource.java
 * $Id$
 */

package su.svn.daybook3.auth.resources;

import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import su.svn.daybook3.auth.annotations.PrincipalLogging;
import su.svn.daybook3.auth.domain.enums.EventAddress;
import su.svn.daybook3.auth.domain.enums.ResourcePath;
import su.svn.daybook3.auth.models.security.AuthRequest;

@PrincipalLogging
@Path(ResourcePath.AUTH)
public class AuthResource extends AbstractResource {

    @PermitAll
    @POST
    @Path(ResourcePath.LOGIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RequestBody(required = true, content = {
            @Content(
                    schema = @Schema(implementation = AuthRequest.class),
                    examples = @ExampleObject(
                            name = "default", value = """
                            {
                              "username": "root",
                              "password": "password"
                            }
                            """
                    ))
    })
    public Uni<Response> login(AuthRequest authRequest) {
        return request(EventAddress.LOGIN_REQUEST, authRequest);
    }

    @ServerExceptionMapper
    public RestResponse<String> exception(Throwable x) {
        return exceptionMapper(x);
    }
}