/*
 * This file was last modified at 2021.12.06 18:12 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * DayBook3.java
 * $Id$
 */

package su.svn.daybook;

import io.smallrye.mutiny.Uni;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import javax.ws.rs.QueryParam;

import io.vertx.mutiny.core.eventbus.EventBus;
import io.vertx.mutiny.core.eventbus.Message;

import io.vertx.mutiny.core.Vertx;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@OpenAPIDefinition(
        tags = {
                @Tag(name="widget", description="Widget operations."),
                @Tag(name="gasket", description="Operations related to gaskets")
        },
        info = @Info(
                title="Example API",
                version = "1.0.1",
                contact = @Contact(
                        name = "Example API Support",
                        url = "http://exampleurl.com/contact",
                        email = "techsupport@example.com"),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"))
)
@Path("/")
public class DayBook3 {

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