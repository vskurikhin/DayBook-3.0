/*
 * This file was last modified at 2023.11.20 00:10 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * LanguageResource.java
 * $Id$
 */

package su.svn.daybook.resources;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
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
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import su.svn.daybook.annotations.PrincipalLogging;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.enums.ResourcePath;
import su.svn.daybook.models.domain.Language;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.models.AbstractService;
import su.svn.daybook.services.models.LanguageService;

@PrincipalLogging
@Path(ResourcePath.LANGUAGE)
public class LanguageResource extends AbstractResource implements Resource<Long, Language> {

    // @Operation(hidden = true)
    @GET
    @Path(ResourcePath.ID)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> get(Long id, @Context UriInfo uriInfo) {
        return request(EventAddress.LANGUAGE_GET, id, uriInfo);
    }

    // @Operation(hidden = true)
    @GET
    @Path(ResourcePath.PAGE)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> page(@QueryParam("page") Long page, @QueryParam("limit") Short limit) {
        return requestPage(EventAddress.LANGUAGE_PAGE, new PageRequest(page, limit));
    }

    // @Operation(hidden = true)
    @POST
    @Path(ResourcePath.NONE)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> post(Language entry, @Context UriInfo uriInfo) {
        return request(EventAddress.LANGUAGE_ADD, entry, uriInfo);
    }

    // @Operation(hidden = true)
    @PUT
    @Path(ResourcePath.NONE)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> put(Language entry, @Context UriInfo uriInfo) {
        return request(EventAddress.LANGUAGE_PUT, entry, uriInfo);
    }

    // @Operation(hidden = true)
    @DELETE
    @Path(ResourcePath.ID)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> delete(Long id, @Context UriInfo uriInfo) {
        return request(EventAddress.LANGUAGE_DEL, id, uriInfo);
    }

    @ServerExceptionMapper
    public RestResponse<String> exception(Throwable x) {
        return exceptionMapper(x);
    }

    @Path(ResourcePath.LANGUAGES)
    public static class LanguageResources implements Resources<Long, Language> {

        @Inject
        LanguageService service;

        @Operation(hidden = true)
        @GET
        @Path(ResourcePath.ALL)
        @Produces(MediaType.APPLICATION_JSON)
        public Multi<Language> all() {
            return getAll();
        }

        @Override
        public AbstractService<Long, Language> getService() {
            return service;
        }
    }
}