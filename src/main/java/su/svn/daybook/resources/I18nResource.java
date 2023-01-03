/*
 * This file was last modified at 2022.01.11 17:44 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * I18nResource.java
 * $Id$
 */

package su.svn.daybook.resources;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.enums.ResourcePath;
import su.svn.daybook.models.domain.I18n;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.domain.AbstractService;
import su.svn.daybook.services.domain.I18nService;

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

@Path(ResourcePath.I18N)
public class I18nResource extends AbstractResource implements Resources<Long, I18n> {

    @Inject
    I18nService service;

    @GET
    @Path(ResourcePath.ALL)
    @Produces("application/json")
    public Multi<I18n> all(@QueryParam("get-all") Boolean getAll) {
        return getAll();
    }

    @GET
    @Path(ResourcePath.ID)
    @Produces("application/json")
    public Uni<Response> get(Long id, @Context UriInfo uriInfo) {
        return request(EventAddress.I18N_GET, id, uriInfo);
    }

    @GET
    @Produces("application/json")
    public Uni<Response> page(@QueryParam("page") Long page, @QueryParam("limit") Short limit) {
        return requestPage(EventAddress.I18N_PAGE, new PageRequest(page, limit));
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Uni<Response> post(I18n entry, @Context UriInfo uriInfo) {
        return request(EventAddress.I18N_ADD, entry, uriInfo);
    }


    @PUT
    @Consumes("application/json")
    @Produces("application/json")
    public Uni<Response> put(I18n entry, @Context UriInfo uriInfo) {
        return request(EventAddress.I18N_PUT, entry, uriInfo);
    }

    @DELETE
    @Path(ResourcePath.ID)
    @Produces("application/json")
    public Uni<Response> delete(Long id, @Context UriInfo uriInfo) {
        return request(EventAddress.I18N_DEL, id, uriInfo);
    }

    @ServerExceptionMapper
    public RestResponse<String> exception(Throwable x) {
        return badRequest(x);
    }

    @Override
    public AbstractService<Long, I18n> getService() {
        return service;
    }
}