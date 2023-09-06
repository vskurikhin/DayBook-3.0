/*
 * This file was last modified at 2023.09.06 17:04 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * UserResource.java
 * $Id$
 */

package su.svn.daybook.resources;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import su.svn.daybook.annotations.PrincipalLogging;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.enums.ResourcePath;
import su.svn.daybook.models.domain.User;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.models.security.AuthRequest;
import su.svn.daybook.services.models.AbstractService;
import su.svn.daybook.services.models.UserService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.util.UUID;

@PrincipalLogging
@Path(ResourcePath.USER)
public class UserResource extends AbstractResource {

    @Operation(summary = "Get user")
    @GET
    @Path(ResourcePath.ID)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> get(UUID id, @Context UriInfo uriInfo) {
        return request(EventAddress.USER_GET, id, uriInfo);
    }

    @Operation(summary = "Get page with list of users")
    @GET
    @Path(ResourcePath.PAGE)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> page(
            @Parameter(required = true) @QueryParam("page") Long page,
            @Parameter(required = true) @QueryParam("limit") Short limit) {
        return requestPage(EventAddress.USER_PAGE, new PageRequest(page, limit));
    }

    @Operation(summary = "Create user")
    @RequestBody(required = true, content = {
            @Content(
                    schema = @Schema(implementation = AuthRequest.class),
                    examples = @ExampleObject(
                            name = "default", value = """
                            {
                               "userName": "user99",
                               "password": "pass99",
                               "roles": [
                                 "USER"
                               ],
                               "visible": true,
                               "flags": 0
                            }
                            """
                    ))
    })
    @POST
    @Path(ResourcePath.NONE)
    @RolesAllowed("ADMIN")
    @SecurityRequirement(name = "day-book")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> post(User entry, @Context UriInfo uriInfo) {
        return request(EventAddress.USER_ADD, entry, uriInfo);
    }

    @Operation(summary = "Update user")
    @PUT
    @Path(ResourcePath.NONE)
    @RolesAllowed({"ADMIN", "USER"})
    @SecurityRequirement(name = "day-book")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> put(User entry, @Context UriInfo uriInfo) {
        return request(EventAddress.USER_PUT, entry, uriInfo);
    }

    @Operation(summary = "Delete user")
    @DELETE
    @Path(ResourcePath.ID)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> delete(UUID id, @Context UriInfo uriInfo) {
        return request(EventAddress.USER_DEL, id, uriInfo);
    }

    @ServerExceptionMapper
    public RestResponse<String> exception(Throwable x) {
        return exceptionMapper(x);
    }

    @Path(ResourcePath.USERS)
    public static class UserResources implements Resources<UUID, User> {

        @Inject
        UserService service;

        @Operation(hidden = true)
        @GET
        @Path(ResourcePath.ALL)
        @Produces(MediaType.APPLICATION_JSON)
        public Multi<User> all() {
            return getAll();
        }

        public AbstractService<UUID, User> getService() {
            return service;
        }
    }
}
