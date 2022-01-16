/*
 * This file was last modified at 2022.01.11 17:44 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * VocabularyResource.java
 * $Id$
 */

package su.svn.daybook.resources;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.eventbus.EventBus;
import io.vertx.mutiny.core.eventbus.Message;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.enums.ResourcePath;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.Vocabulary;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path(ResourcePath.VOCABULARY)
public class VocabularyResource {

    @Inject
    EventBus bus;

    @GET
    @Path(ResourcePath.ID)
    @Produces("application/json")
    public Uni<Response> get(String id) {
        return request(EventAddress.VOCABULARY_GET, id);
    }

    @POST
    @Path(ResourcePath.ADD)
    @Consumes("application/json")
    @Produces("application/json")
    public Uni<Response> add(Vocabulary Vocabulary) {
        return request(EventAddress.VOCABULARY_ADD, Vocabulary);
    }

    @PUT
    @Path(ResourcePath.PUT)
    @Consumes("application/json")
    @Produces("application/json")
    public Uni<Response> put(Vocabulary Vocabulary) {
        return request(EventAddress.VOCABULARY_PUT, Vocabulary);
    }

    @DELETE
    @Path(ResourcePath.ID)
    @Produces("application/json")
    public Uni<Response> delete(String id) {
        return request(EventAddress.VOCABULARY_DEL, id);
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