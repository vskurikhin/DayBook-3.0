/*
 * This file was last modified at 2022.01.11 17:44 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * KeyValueResource.java
 * $Id$
 */

package su.svn.daybook.resources;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import su.svn.daybook.annotations.Logged;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.enums.ResourcePath;
import su.svn.daybook.models.domain.KeyValue;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.models.AbstractService;
import su.svn.daybook.services.models.KeyValueService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.UUID;

@Path(ResourcePath.KEY_VALUE) @Logged
public class KeyValueResource extends AbstractResource implements Resource<UUID, KeyValue> {

    @Operation(hidden = true)
    @GET
    @Path(ResourcePath.ID)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> get(UUID id, @Context UriInfo uriInfo) {
        return request(EventAddress.KEY_VALUE_GET, id, uriInfo);
    }

    @Operation(hidden = true)
    @GET
    @Path(ResourcePath.PAGE)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> page(@QueryParam("page") Long page, @QueryParam("limit") Short limit) {
        return requestPage(EventAddress.KEY_VALUE_PAGE, new PageRequest(page, limit));
    }

    @Operation(hidden = true)
    @POST
    @Path(ResourcePath.NONE)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> post(KeyValue entry, @Context UriInfo uriInfo) {
        return request(EventAddress.KEY_VALUE_ADD, entry, uriInfo);
    }

    @Operation(hidden = true)
    @PUT
    @Path(ResourcePath.NONE)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> put(KeyValue entry, @Context UriInfo uriInfo) {
        return request(EventAddress.KEY_VALUE_PUT, entry, uriInfo);
    }

    @Operation(hidden = true)
    @DELETE
    @Path(ResourcePath.ID)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> delete(UUID id, @Context UriInfo uriInfo) {
        return request(EventAddress.KEY_VALUE_DEL, id, uriInfo);
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
        public AbstractService<UUID, KeyValue> getService() {
            return service;
        }
    }
}