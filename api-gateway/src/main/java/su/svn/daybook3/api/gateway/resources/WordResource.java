/*
 * This file was last modified at 2024-05-21 17:47 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * WordResource.java
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
import su.svn.daybook3.api.gateway.models.domain.Word;
import su.svn.daybook3.api.gateway.models.pagination.PageRequest;
import su.svn.daybook3.api.gateway.services.models.MultiAnswerAllService;
import su.svn.daybook3.api.gateway.services.models.WordService;

@Path(ResourcePath.WORD)
@PrincipalLogging
public class WordResource extends AbstractResource {

    @Operation(hidden = true)
    @GET
    @Path(ResourcePath.ID)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> get(String id) {
        return request(EventAddress.WORD_GET, id);
    }

    @Operation(hidden = true)
    @GET
    @Path(ResourcePath.PAGE)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> page(@QueryParam("page") Long page, @QueryParam("limit") Short limit) {
        return requestPage(EventAddress.WORD_PAGE, new PageRequest(page, limit));
    }

    @Operation(hidden = true)
    @POST
    @Path(ResourcePath.NONE)
    @RolesAllowed("ADMIN")
    @SecurityRequirement(name = "day-book")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> post(Word entry) {
        return request(EventAddress.WORD_ADD, entry);
    }

    @Operation(hidden = true)
    @PUT
    @Path(ResourcePath.NONE)
    @RolesAllowed("ADMIN")
    @SecurityRequirement(name = "day-book")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> put(Word entry) {
        return request(EventAddress.WORD_PUT, entry);
    }

    @Operation(hidden = true)
    @DELETE
    @Path(ResourcePath.ID)
    @RolesAllowed("ADMIN")
    @SecurityRequirement(name = "day-book")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> delete(String id) {
        return request(EventAddress.WORD_DEL, id);
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
        public MultiAnswerAllService getService() {
            return service;
        }
    }
}