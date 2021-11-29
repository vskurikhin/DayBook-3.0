package su.svn.daybook.resources;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.eventbus.EventBus;
import io.vertx.mutiny.core.eventbus.Message;
import su.svn.daybook.model.TagLabel;

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
        return bus.<TagLabel>request("tag-get", id)
                .onItem()
                .transform(this::getResponse)
                .onItem()
                .transform(Response.ResponseBuilder::build);
    }

    @POST
    @Path("/add")
    public Uni<String> add(TagLabel tagLabel) {
        return bus.<String>request("tag-add", tagLabel)
                .onItem()
                .transform(Message::body);
    }

    private Response.ResponseBuilder getResponse(Message<?> message) {
        return message.body() != null
                ? Response.ok(message.body())
                : Response.status(Response.Status.NOT_FOUND);
    }
}
