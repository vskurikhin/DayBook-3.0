/*
 * This file was last modified at 2022.01.11 17:44 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * WordResource.java
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
import su.svn.daybook.models.domain.Word;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.models.AbstractService;
import su.svn.daybook.services.models.WordService;

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

@Path(ResourcePath.WORD)
@Logged
public class WordResource extends AbstractResource {

    @GET
    @Path(ResourcePath.ID)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> get(String id, @Context UriInfo uriInfo) {
        return request(EventAddress.WORD_GET, id, uriInfo);
    }

    @GET
    @Path(ResourcePath.PAGE)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> page(@QueryParam("page") Long page, @QueryParam("limit") Short limit) {
        return requestPage(EventAddress.WORD_PAGE, new PageRequest(page, limit));
    }

    @POST
    @Path(ResourcePath.NONE)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> post(Word entry, @Context UriInfo uriInfo) {
        return request(EventAddress.WORD_ADD, entry, uriInfo);
    }

    @PUT
    @Path(ResourcePath.NONE)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> put(Word entry, @Context UriInfo uriInfo) {
        return request(EventAddress.WORD_PUT, entry, uriInfo);
    }

    @DELETE
    @Path(ResourcePath.ID)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> delete(String id, @Context UriInfo uriInfo) {
        return request(EventAddress.WORD_DEL, id, uriInfo);
    }

    @ServerExceptionMapper
    public RestResponse<String> exception(Throwable x) {
        return exceptionMapper(x);
    }

    @Path(ResourcePath.WORDS)
    public static class WordResources implements Resources<String, Word> {

        @Inject
        WordService service;

        @Operation(hidden = true)
        @GET
        @Path(ResourcePath.ALL)
        @Produces(MediaType.APPLICATION_JSON)
        public Multi<Word> all() {
            return getAll();
        }

        @Override
        public AbstractService<String, Word> getService() {
            return service;
        }
    }
}