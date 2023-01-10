/*
 * This file was last modified at 2022.01.11 17:44 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * CodifierResource.java
 * $Id$
 */

package su.svn.daybook.resources;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.enums.ResourcePath;
import su.svn.daybook.models.domain.Codifier;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.models.AbstractService;
import su.svn.daybook.services.models.CodifierService;

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

@Path(ResourcePath.CODIFIER)
public class CodifierResource extends AbstractResource implements Resource<String, Codifier> {

    @Operation(hidden = true)
    @GET
    @Path(ResourcePath.ID)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> get(String id, @Context UriInfo uriInfo) {
        return request(EventAddress.CODIFIER_GET, id, uriInfo);
    }

    @Operation(hidden = true)
    @GET
    @Path(ResourcePath.PAGE)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> page(@QueryParam("page") Long page, @QueryParam("limit") Short limit) {
        return requestPage(EventAddress.CODIFIER_PAGE, new PageRequest(page, limit));
    }

    @Operation(hidden = true)
    @POST
    @Path(ResourcePath.NONE)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> post(Codifier entry, @Context UriInfo uriInfo) {
        return request(EventAddress.CODIFIER_ADD, entry, uriInfo);
    }

    @Operation(hidden = true)
    @PUT
    @Path(ResourcePath.NONE)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> put(Codifier entry, @Context UriInfo uriInfo) {
        return request(EventAddress.CODIFIER_PUT, entry, uriInfo);
    }

    @Operation(hidden = true)
    @DELETE
    @Path(ResourcePath.ID)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> delete(String id, @Context UriInfo uriInfo) {
        return request(EventAddress.CODIFIER_DEL, id, uriInfo);
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
        public AbstractService<String, Codifier> getService() {
            return service;
        }
    }
}