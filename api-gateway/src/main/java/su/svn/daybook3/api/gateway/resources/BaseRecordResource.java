/*
 * This file was last modified at 2024-05-14 21:35 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * BaseRecordResource.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.resources;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import su.svn.daybook3.api.gateway.annotations.PrincipalLogging;
import su.svn.daybook3.api.gateway.converters.mappers.BaseRecordMapper;
import su.svn.daybook3.api.gateway.domain.entities.BaseRecord;
import su.svn.daybook3.api.gateway.domain.enums.ResourcePath;

import java.net.URI;
import java.util.UUID;

import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;
import static jakarta.ws.rs.core.Response.Status.OK;

@PrincipalLogging
@Path(ResourcePath.BASE_RECORD)
public class BaseRecordResource {

    @Inject
    BaseRecordMapper baseRecordMapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getResponseBaseRecords() {
        return BaseRecord.getAllBaseRecord()
                .map(records -> records.stream().map(baseRecordMapper::toResource).toList())
                .onItem()
                .transform(Response::ok)
                .onItem()
                .transform(Response.ResponseBuilder::build);
    }

    @GET
    @Path("{id}")
    public Uni<Response> getSingleBaseRecord(@PathParam("id") UUID id) {
        return BaseRecord.findByBaseRecordId(id)
                .map(baseRecord -> baseRecordMapper.toResource(baseRecord))
                .onItem()
                .ifNotNull()
                .transform(product -> Response.ok(product).build())
                .onItem()
                .ifNull()
                .continueWith(Response.ok().status(NOT_FOUND)::build);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> add(BaseRecord baseRecord) {
        return BaseRecord.addBaseRecord(baseRecord)
                .onItem()
                .transform(record -> URI.create(ResourcePath.BASE_RECORD + record.id()))
                .onItem()
                .transform(Response::created)
                .onItem()
                .transform(Response.ResponseBuilder::build);
    }

    @PUT
    @Path("{id}")
    public Uni<Response> update(@PathParam("id") UUID id, BaseRecord baseRecord) {
        if (baseRecord == null || baseRecord.description() == null) {
            throw new WebApplicationException("Product description was not set on request.", 422);
        }
        return BaseRecord.updateBaseRecord(id, baseRecord)
                .onItem()
                .ifNotNull()
                .transform(entity -> Response.ok(entity).build())
                .onItem()
                .ifNull()
                .continueWith(Response.ok().status(NOT_FOUND)::build);
    }

    @DELETE
    @Path("{id}")
    public Uni<Response> delete(@PathParam("id") UUID id) {
        return BaseRecord.deleteBaseRecord(id)
                .onItem()
                .transform(entity -> !entity
                        ? Response.serverError().status(NOT_FOUND).build()
                        : Response.ok().status(OK).build()
                );
    }
}
