/*
 * This file was last modified at 2024-05-22 13:36 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * RoleResource.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.resources;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
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
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import su.svn.daybook3.api.gateway.annotations.PrincipalLogging;
import su.svn.daybook3.api.gateway.domain.enums.EventAddress;
import su.svn.daybook3.api.gateway.domain.enums.ResourcePath;
import su.svn.daybook3.api.gateway.models.domain.Role;
import su.svn.daybook3.api.gateway.models.pagination.PageRequest;
import su.svn.daybook3.api.gateway.models.security.AuthRequest;
import su.svn.daybook3.api.gateway.services.models.MultiAnswerAllService;
import su.svn.daybook3.api.gateway.services.models.RoleService;

import java.util.UUID;

@PrincipalLogging
@Path(ResourcePath.ROLE)
public class RoleResource extends AbstractResource {

    @Operation(summary = "Get role")
    @GET
    @Path(ResourcePath.ID)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> get(UUID id) {
        return request(EventAddress.ROLE_GET, id);
    }

    @Operation(summary = "Get page with roles of user")
    @GET
    @Path(ResourcePath.PAGE)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> page(@QueryParam("page") Long page, @QueryParam("limit") Short limit) {
        return requestPage(EventAddress.ROLE_PAGE, new PageRequest(page, limit));
    }

    @Operation(summary = "Create role")
    @RequestBody(required = true, content = {@Content(schema = @Schema(implementation = AuthRequest.class), examples = @ExampleObject(name = "default", value = """
            {
               "role": "GUEST",
               "description": "string",
               "visible": true,
               "flags": 0
             }
            """))})
    @RolesAllowed("ADMIN")
    @POST
    @Path(ResourcePath.NONE)
    @SecurityRequirement(name = "day-book")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> post(Role entry) {
        return request(EventAddress.ROLE_ADD, entry);
    }

    @Operation(summary = "Update role")
    @RolesAllowed("ADMIN")
    @PUT
    @Path(ResourcePath.NONE)
    @SecurityRequirement(name = "day-book")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> put(Role entry) {
        return request(EventAddress.ROLE_PUT, entry);
    }

    @Operation(summary = "Delete role")
    @DELETE
    @Path(ResourcePath.ID)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> delete(UUID id) {
        return request(EventAddress.ROLE_DEL, id);
    }

    @ServerExceptionMapper
    public RestResponse<String> exception(Throwable x) {
        return exceptionMapper(x);
    }

    @Path(ResourcePath.ROLES)
    public static class RoleResources implements Resources<UUID, Role> {

        @Inject
        RoleService service;

        @Operation(hidden = true)
        @GET
        @Path(ResourcePath.ALL)
        @Produces(MediaType.APPLICATION_JSON)
        public Multi<Role> all() {
            return getAll();
        }

        @Override
        public MultiAnswerAllService getService() {
            return service;
        }
    }
}