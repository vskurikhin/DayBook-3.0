package su.svn.daybook.resources;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.eventbus.EventBus;
import io.vertx.mutiny.core.eventbus.Message;
import su.svn.daybook.domain.model.TagLabel;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/tags")
public class TagLabelsResource {

    @Inject
    EventBus bus;

    @GET
    @Path("/all")
    public Uni<TagLabel> all() {
        return bus.<TagLabel>request("tags", "all")
                .onItem()
                .transform(Message::body);
    }
}
