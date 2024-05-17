/*
 * This file was last modified at 2024-05-22 13:35 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * CodifierResource.java
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
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import su.svn.daybook3.api.gateway.annotations.PrincipalLogging;
import su.svn.daybook3.api.gateway.domain.enums.EventAddress;
import su.svn.daybook3.api.gateway.domain.enums.ResourcePath;
import su.svn.daybook3.api.gateway.models.domain.Codifier;
import su.svn.daybook3.api.gateway.models.pagination.PageRequest;
import su.svn.daybook3.api.gateway.services.models.CodifierService;
import su.svn.daybook3.api.gateway.services.models.MultiAnswerAllService;

@PrincipalLogging
@Path(ResourcePath.CODIFIER)
public class CodifierResource extends AbstractResource {

    @Operation(hidden = true)
    @GET
    @Path(ResourcePath.ID)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> get(String id) {
        return request(EventAddress.CODIFIER_GET, id);
    }

    @Operation(hidden = true)
    @GET
    @Path(ResourcePath.PAGE)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> page(@QueryParam("page") int page, @QueryParam("limit") short limit) {
        return requestPage(EventAddress.CODIFIER_PAGE, new PageRequest(page, limit));
    }

    @Operation(hidden = true)
    @POST
    @Path(ResourcePath.NONE)
    @RolesAllowed("ADMIN")
    @SecurityRequirement(name = "day-book")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> post(Codifier entry) {
        return request(EventAddress.CODIFIER_ADD, entry);
    }

    @Operation(hidden = true)
    @PUT
    @Path(ResourcePath.NONE)
    @RolesAllowed("ADMIN")
    @SecurityRequirement(name = "day-book")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> put(Codifier entry) {
        return request(EventAddress.CODIFIER_PUT, entry);
    }

    @Operation(hidden = true)
    @DELETE
    @Path(ResourcePath.ID)
    @RolesAllowed("ADMIN")
    @SecurityRequirement(name = "day-book")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> delete(String id) {
        return request(EventAddress.CODIFIER_DEL, id);
    }

    @ServerExceptionMapper
    public RestResponse<String> exception(Throwable x) {
        return exceptionMapper(x);
    }

    @Path(ResourcePath.CODIFIERS)
    public static class CodifierResources implements Resources<String, Codifier> {

        @Inject
        CodifierService service;

        @Operation(hidden = true)
        @GET
        @Path(ResourcePath.ALL)
        @Produces(MediaType.APPLICATION_JSON)
        public Multi<Codifier> all() {
            return getAll();
        }

        @Override
        public MultiAnswerAllService getService() {
            return service;
        }
    }
}