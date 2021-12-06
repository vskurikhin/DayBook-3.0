/*
 * This file was last modified at 2021.02.27 11:33 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * AbstractRecordService.java
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