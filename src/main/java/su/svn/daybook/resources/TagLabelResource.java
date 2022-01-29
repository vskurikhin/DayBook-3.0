/*
 * This file was last modified at 2021.12.06 18:10 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * TagLabelResource.java
 * $Id$
 */

package su.svn.daybook.resources;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.eventbus.EventBus;
import io.vertx.mutiny.core.eventbus.Message;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.enums.ResourcePath;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.TagLabel;
import su.svn.daybook.domain.model.TagLabel;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path(ResourcePath.TAG_LABEL)
public class TagLabelResource {

    @Inject
    EventBus bus;

    @GET
    @Path(ResourcePath.ID)
    @Produces("application/json")
    public Uni<Response> get(String id) {
        return request(EventAddress.TAG_GET, id);
    }

    @POST
    @Path(ResourcePath.ADD)
    @Consumes("application/json")
    @Produces("application/json")
    public Uni<Response> add(TagLabel tagLabel) {
        return request(EventAddress.TAG_ADD, tagLabel);
    }

    @PUT
    @Path(ResourcePath.PUT)
    @Consumes("application/json")
    @Produces("application/json")
    public Uni<Response> put(TagLabel tagLabel) {
        return request(EventAddress.TAG_PUT, tagLabel);
    }

    @DELETE
    @Path(ResourcePath.ID)
    @Produces("application/json")
    public Uni<Response> delete(String id) {
        return request(EventAddress.TAG_DEL, id);
    }

    private Uni<Response> request(String address, Object o) {
        return bus.<Answer>request(address, o)
                .onItem()
                .transform(this::createResponseBuilder)
                .onItem()
                .transform(Response.ResponseBuilder::build);
    }

    private Response.ResponseBuilder createResponseBuilder(Message<Answer> message) {
        if (message.body() == null) {
            return Response.status(406, "body is null");
        }
        return message.body().getPayload() != null
                ? Response.ok(message.body().getPayload())
                : Response.status(message.body().getError(), message.body().getMessage());
    }
}