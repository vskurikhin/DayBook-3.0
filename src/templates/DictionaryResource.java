/*
 * This file was last modified at 2023.09.07 16:35 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * @Name@Resource.java
 * $Id$
 */

package su.svn.daybook.resources;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import su.svn.daybook.annotations.PrincipalLogging;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.enums.ResourcePath;
import su.svn.daybook.models.domain.@Name@;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.models.AbstractService;
import su.svn.daybook.services.models.@Name@Service;

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
@Path(ResourcePath.@TABLE@)
public class @Name@Resource extends AbstractResource implements Resource<@IdType@, @Name@> {

    // @Operation(hidden = true)
    @GET
    @Path(ResourcePath.ID)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> get(@IdType@ id, @Context UriInfo uriInfo) {
        return request(EventAddress.@TABLE@_GET, id, uriInfo);
    }

    // @Operation(hidden = true)
    @GET
    @Path(ResourcePath.PAGE)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> page(@QueryParam("page") Long page, @QueryParam("limit") Short limit) {
        return requestPage(EventAddress.@TABLE@_PAGE, new PageRequest(page, limit));
    }

    // @Operation(hidden = true)
    @POST
    @Path(ResourcePath.NONE)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> post(@Name@ entry, @Context UriInfo uriInfo) {
        return request(EventAddress.@TABLE@_ADD, entry, uriInfo);
    }

    // @Operation(hidden = true)
    @PUT
    @Path(ResourcePath.NONE)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> put(@Name@ entry, @Context UriInfo uriInfo) {
        return request(EventAddress.@TABLE@_PUT, entry, uriInfo);
    }

    // @Operation(hidden = true)
    @DELETE
    @Path(ResourcePath.ID)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> delete(@IdType@ id, @Context UriInfo uriInfo) {
        return request(EventAddress.@TABLE@_DEL, id, uriInfo);
    }

    @ServerExceptionMapper
    public RestResponse<String> exception(Throwable x) {
        return exceptionMapper(x);
    }

    @Path(ResourcePath.@TABLE@S)
    public static class @Name@Resources implements Resources<@IdType@, @Name@> {

        @Inject
        @Name@Service service;

        @Operation(hidden = true)
        @GET
        @Path(ResourcePath.ALL)
        @Produces(MediaType.APPLICATION_JSON)
        public Multi<@Name@> all() {
            return getAll();
        }

        @Override
        public AbstractService<@IdType@, @Name@> getService() {
            return service;
        }
    }
}