/*
 * This file was last modified at 2024-10-30 17:28 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * KeyValueResource.java
 * $Id$
 */

package su.svn.daybook3.api.resources;

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
import su.svn.daybook3.api.annotations.PrincipalLogging;
import su.svn.daybook3.api.domain.enums.EventAddress;
import su.svn.daybook3.api.domain.enums.ResourcePath;
import su.svn.daybook3.api.models.domain.KeyValue;
import su.svn.daybook3.api.models.pagination.PageRequest;
import su.svn.daybook3.api.services.models.KeyValueService;
import su.svn.daybook3.api.services.models.MultiAnswerAllService;

import java.util.UUID;

@PrincipalLogging
@Path(ResourcePath.KEY_VALUE)
public class KeyValueResource extends AbstractResource {

    @Operation(hidden = true)
    @GET
    @Path(ResourcePath.ID)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> get(UUID id) {
        return request(EventAddress.KEY_VALUE_GET, id);
    }

    @Operation(hidden = true)
    @GET
    @Path(ResourcePath.PAGE)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> page(@QueryParam("page") int page, @QueryParam("limit") short limit) {
        return requestPage(EventAddress.KEY_VALUE_PAGE, new PageRequest(page, limit));
    }

    @Operation(hidden = true)
    @POST
    @Path(ResourcePath.NONE)
    @RolesAllowed("ADMIN")
    @SecurityRequirement(name = "day-book")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> post(KeyValue entry) {
        return request(EventAddress.KEY_VALUE_ADD, entry);
    }

    @Operation(hidden = true)
    @PUT
    @Path(ResourcePath.NONE)
    @RolesAllowed("ADMIN")
    @SecurityRequirement(name = "day-book")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> put(KeyValue entry) {
        return request(EventAddress.KEY_VALUE_PUT, entry);
    }

    @Operation(hidden = true)
    @DELETE
    @Path(ResourcePath.ID)
    @RolesAllowed("ADMIN")
    @SecurityRequirement(name = "day-book")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> delete(UUID id) {
        return request(EventAddress.KEY_VALUE_DEL, id);
    }

    @ServerExceptionMapper
    public RestResponse<String> exception(Throwable x) {
        return exceptionMapper(x);
    }

    @Path(ResourcePath.KEY_VALUES)
    public static class KeyValueResources implements Resources<UUID, KeyValue> {

        @Inject
        KeyValueService service;

        @Operation(hidden = true)
        @GET
        @Path(ResourcePath.ALL)
        @Produces(MediaType.APPLICATION_JSON)
        public Multi<KeyValue> all() {
            return getAll();
        }

        @Override
        public MultiAnswerAllService getService() {
            return service;
        }
    }
}