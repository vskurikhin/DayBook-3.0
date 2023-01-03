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
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.enums.ResourcePath;
import su.svn.daybook.models.domain.KeyValue;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.domain.AbstractService;
import su.svn.daybook.services.domain.KeyValueService;

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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path(ResourcePath.KEY_VALUE)
public class KeyValueResource extends AbstractResource implements Resources<Long, KeyValue> {

    @Inject
    KeyValueService service;

    @GET
    @Path(ResourcePath.ALL)
    @Produces("application/json")
    public Multi<KeyValue> all(@QueryParam("get-all") Boolean getAll) {
        return getAll();
    }

    @GET
    @Path(ResourcePath.ID)
    @Produces("application/json")
    public Uni<Response> get(Long id, @Context UriInfo uriInfo) {
        return request(EventAddress.KEY_VALUE_GET, id, uriInfo);
    }

    @GET
    @Produces("application/json")
    public Uni<Response> page(@QueryParam("page") Long page, @QueryParam("limit") Short limit) {
        return requestPage(EventAddress.KEY_VALUE_PAGE, new PageRequest(page, limit));
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Uni<Response> post(KeyValue entry, @Context UriInfo uriInfo) {
        return request(EventAddress.KEY_VALUE_ADD, entry, uriInfo);
    }


    @PUT
    @Consumes("application/json")
    @Produces("application/json")
    public Uni<Response> put(KeyValue entry, @Context UriInfo uriInfo) {
        return request(EventAddress.KEY_VALUE_PUT, entry, uriInfo);
    }

    @DELETE
    @Path(ResourcePath.ID)
    @Produces("application/json")
    public Uni<Response> delete(Long id, @Context UriInfo uriInfo) {
        return request(EventAddress.KEY_VALUE_DEL, id, uriInfo);
    }

    @ServerExceptionMapper
    public RestResponse<String> exception(Throwable x) {
        return badRequest(x);
    }

    @Override
    public AbstractService<Long, KeyValue> getService() {
        return service;
    }
}