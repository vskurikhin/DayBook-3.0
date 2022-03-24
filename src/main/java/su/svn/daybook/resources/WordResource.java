/*
 * This file was last modified at 2022.03.24 13:26 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * WordResource.java
 * $Id$
 */

package su.svn.daybook.resources;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.eventbus.EventBus;
import io.vertx.mutiny.core.eventbus.Message;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.enums.ResourcePath;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.Word;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path(ResourcePath.WORD)
public class WordResource {

    @Inject
    EventBus bus;

    @GET
    @Path(ResourcePath.ID)
    @Produces("application/json")
    public Uni<Response> get(String word) {
        return request(EventAddress.WORD_GET, word);
    }

    @POST
    @Path(ResourcePath.ADD)
    @Consumes("application/json")
    @Produces("application/json")
    public Uni<Response> add(Word word) {
        return request(EventAddress.WORD_ADD, word);
    }

    @PUT
    @Path(ResourcePath.PUT)
    @Consumes("application/json")
    @Produces("application/json")
    public Uni<Response> put(Word word) {
        return request(EventAddress.WORD_PUT, word);
    }

    @DELETE
    @Path(ResourcePath.ID)
    @Produces("application/json")
    public Uni<Response> delete(String id) {
        return request(EventAddress.WORD_DEL, id);
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