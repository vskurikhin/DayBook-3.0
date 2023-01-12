/*
 * This file was last modified at 2023.01.05 15:20 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * AuthResource.java
 * $Id$
 */

package su.svn.daybook.resources;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import su.svn.daybook.annotations.Logged;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.enums.ResourcePath;
import su.svn.daybook.models.security.AuthRequest;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path(ResourcePath.AUTH)
@Logged
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
    public Uni<Response> login(AuthRequest authRequest, @Context UriInfo uriInfo) {
        return request(EventAddress.LOGIN_REQUEST, authRequest, uriInfo);
    }


    @ServerExceptionMapper
    public RestResponse<String> exception(Throwable x) {
        return exceptionMapper(x);
    }
}