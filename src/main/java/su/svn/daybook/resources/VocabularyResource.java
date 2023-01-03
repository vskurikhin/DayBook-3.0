/*
 * This file was last modified at 2022.01.11 17:44 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * VocabularyResource.java
 * $Id$
 */

package su.svn.daybook.resources;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.enums.ResourcePath;
import su.svn.daybook.models.domain.Vocabulary;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.domain.AbstractService;
import su.svn.daybook.services.domain.VocabularyService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path(ResourcePath.VOCABULARY)
public class VocabularyResource extends AbstractResource implements Resources<Long, Vocabulary> {

    @Inject
    VocabularyService service;

    @GET
    @Path(ResourcePath.ALL)
    @Produces("application/json")
    public Multi<Vocabulary> all(@QueryParam("get-all") Boolean getAll) {
        return getAll();
    }

    @GET
    @Path(ResourcePath.ID)
    @Produces("application/json")
    public Uni<Response> get(String id, @Context UriInfo uriInfo) {
        return request(EventAddress.VOCABULARY_GET, id, uriInfo);
    }

    @GET
    @Produces("application/json")
    public Uni<Response> page(@QueryParam("page") Long page, @QueryParam("limit") Short limit) {
        return requestPage(EventAddress.VOCABULARY_PAGE, new PageRequest(page, limit));
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Uni<Response> post(Vocabulary entry, @Context UriInfo uriInfo) {
        return request(EventAddress.VOCABULARY_ADD, entry, uriInfo);
    }


    @PUT
    @Consumes("application/json")
    @Produces("application/json")
    public Uni<Response> put(Vocabulary entry, @Context UriInfo uriInfo) {
        return request(EventAddress.VOCABULARY_PUT, entry, uriInfo);
    }

    @DELETE
    @Path(ResourcePath.ID)
    @Produces("application/json")
    public Uni<Response> delete(String id, @Context UriInfo uriInfo) {
        return request(EventAddress.VOCABULARY_DEL, id, uriInfo);
    }

    @ServerExceptionMapper
    public RestResponse<String> exception(Throwable x) {
        return badRequest(x);
    }

    @Override
    public AbstractService<Long, Vocabulary> getService() {
        return service;
    }
}