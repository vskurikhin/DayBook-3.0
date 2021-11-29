package su.svn.daybook.resources;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.eventbus.EventBus;
import io.vertx.mutiny.core.eventbus.Message;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.TagLabel;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/tag")
public class TagLabelResource {

    @Inject
    EventBus bus;

    @GET
    @Path("/{id}")
    public Uni<Response> get(String id) {
        return request("tag-get", id);
    }

    @POST
    @Path("/add")
    public Uni<Response> add(TagLabel tagLabel) {
        return request("tag-add", tagLabel);
    }

    private Uni<Response> request(String address, Object o) {
        return bus.<Answer>request(address, o)
                .onItem()
                .transform(this::getResponse)
                .onItem()
                .transform(Response.ResponseBuilder::build);
    }

    private Response.ResponseBuilder getResponse(Message<Answer> message) {
        return message.body().getPayload() != null
                ? Response.ok(message.body().getPayload())
                : Response.status(message.body().getError(), message.body().getMessage());
    }
}
