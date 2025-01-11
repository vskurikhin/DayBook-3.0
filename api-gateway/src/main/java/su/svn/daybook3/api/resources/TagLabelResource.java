/*
 * This file was last modified at 2024-10-30 17:28 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * TagLabelResource.java
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
import su.svn.daybook3.api.models.domain.TagLabel;
import su.svn.daybook3.api.models.pagination.PageRequest;
import su.svn.daybook3.api.services.models.MultiAnswerAllService;
import su.svn.daybook3.api.services.models.TagLabelService;

@PrincipalLogging
@Path(ResourcePath.TAG_LABEL)
public class TagLabelResource extends AbstractResource {

    @Operation(summary = "Get tag label")
    @GET
    @Path(ResourcePath.ID)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> get(String id) {
        return request(EventAddress.TAG_LABEL_GET, id);
    }

    @Operation(summary = "Get page with list of tag labels")
    @GET
    @Path(ResourcePath.PAGE)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> page(@QueryParam("page") int page, @QueryParam("limit") short limit) {
        return requestPage(EventAddress.TAG_LABEL_PAGE, new PageRequest(page, limit));
    }

    @Operation(summary = "Create tag label")
    @POST
    @Path(ResourcePath.NONE)
    @RolesAllowed("ADMIN")
    @SecurityRequirement(name = "day-book")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> post(TagLabel entry) {
        return request(EventAddress.TAG_LABEL_ADD, entry);
    }

    @Operation(summary = "Update tag label")
    @PUT
    @Path(ResourcePath.NONE)
    @RolesAllowed({"ADMIN", "USER"})
    @SecurityRequirement(name = "day-book")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> put(TagLabel entry) {
        return request(EventAddress.TAG_LABEL_PUT, entry);
    }

    @Operation(summary = "Delete tag label")
    @DELETE
    @Path(ResourcePath.ID)
    @RolesAllowed({"ADMIN"})
    @SecurityRequirement(name = "day-book")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> delete(String id) {
        return request(EventAddress.TAG_LABEL_DEL, id);
    }

    @ServerExceptionMapper
    public RestResponse<String> exception(Throwable x) {
        return exceptionMapper(x);
    }

    @Path(ResourcePath.TAG_LABELS)
    public static class TagLabelResources implements Resources<String, TagLabel> {

        @Inject
        TagLabelService service;

        @Operation(hidden = true)
        @GET
        @Path(ResourcePath.ALL)
        @Produces(MediaType.APPLICATION_JSON)
        public Multi<TagLabel> all() {
            return getAll();
        }

        @Override
        public MultiAnswerAllService getService() {
            return service;
        }
    }
}