package su.svn.daybook;

import io.smallrye.mutiny.Uni;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import javax.ws.rs.QueryParam;

import io.vertx.mutiny.core.eventbus.EventBus;
import io.vertx.mutiny.core.eventbus.Message;

import io.vertx.mutiny.core.Vertx;

@Path("/")
public class DayBook3 {

    private static final String URL
            = "https://en.wikipedia.org"
            + "/w/api.php?action=parse&page=Quarkus&format=json&prop=langlinks";

    @Inject
    EventBus bus;

    private final Vertx vertx;

    @Inject
    public DayBook3(Vertx vertx) {
        this.vertx = vertx;
    }

    @GET
    @Path("/hello")
    public Uni<String> hello(@QueryParam("name") String name) {
        return bus.<String>request("greetings", name)
                .onItem()
                .transform(Message::body);
    }
}