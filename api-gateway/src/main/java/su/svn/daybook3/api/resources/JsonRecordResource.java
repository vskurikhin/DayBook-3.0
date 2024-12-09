/*
 * This file was last modified at 2024-10-30 17:28 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * JsonRecordResource.java
 * $Id$
 */

package su.svn.daybook3.api.resources;

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
import su.svn.daybook3.api.domain.enums.ResourcePath;
import su.svn.daybook3.api.models.dto.NewJsonRecord;
import su.svn.daybook3.api.models.dto.UpdateJsonRecord;
import su.svn.daybook3.api.models.pagination.PageRequest;
import su.svn.daybook3.api.services.mappers.JsonRecordMapper;
import su.svn.daybook3.api.services.models.JsonRecordService;
import su.svn.daybook3.domain.messages.Request;

import java.util.UUID;

@PrincipalLogging
@Path(ResourcePath.JSON_RECORD)
public class JsonRecordResource
        extends AbstractResource {

    @Inject
    JsonRecordMapper mapper;

    @Inject
    JsonRecordService service;

    @Operation(summary = "Get JSON record")
    @GET
    @Path(ResourcePath.ID)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> get(UUID id) {

        var request = new Request<>(id, authContext.getPrincipal());

        return service.get(request)
                .map(this::createResponseBuilder)
                .map(Response.ResponseBuilder::build);
    }

    @Operation(summary = "Get page with list of JSON record")
    @GET
    @Path(ResourcePath.PAGE)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> page(@QueryParam("page") int page, @QueryParam("limit") short limit) {

        var pageRequest = new PageRequest(page, limit);
        var request = new Request<>(pageRequest, authContext.getPrincipal());

        return service.getPage(request)
                .map(this::createPageResponseBuilder)
                .map(Response.ResponseBuilder::build);
    }

    @Operation(summary = "Create JSON record")
    @POST
    @Path(ResourcePath.NONE)
    @RolesAllowed("ADMIN")
    @SecurityRequirement(name = "day-book")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> post(NewJsonRecord entry) {

        var record = mapper.toResource(entry)
                .toBuilder()
                .userName("root")
                .build();
        var request = new Request<>(record, authContext.getPrincipal());

        return service.add(request)
                .map(this::createResponseBuilder)
                .map(Response.ResponseBuilder::build);
    }

    @Operation(summary = "Update JSON record")
    @PUT
    @Path(ResourcePath.NONE)
    @RolesAllowed("ADMIN")
    @SecurityRequirement(name = "day-book")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> put(UpdateJsonRecord entry) {

        var record = mapper.toResource(entry)
                .toBuilder()
                .userName("root")
                .build();
        var request = new Request<>(record, authContext.getPrincipal());

        return service.put(request)
                .map(this::createResponseBuilder)
                .map(Response.ResponseBuilder::build);
    }

    @Operation(summary = "Delete JSON record")
    @DELETE
    @Path(ResourcePath.ID)
    @RolesAllowed("ADMIN")
    @SecurityRequirement(name = "day-book")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> delete(UUID id) {

        var request = new Request<>(id, authContext.getPrincipal());

        return service.delete(request)
                .map(this::createResponseBuilder)
                .map(Response.ResponseBuilder::build);
    }

    @ServerExceptionMapper
    public RestResponse<String> exception(Throwable x) {
        return exceptionMapper(x);
    }
}