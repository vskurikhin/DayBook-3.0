/*
 * This file was last modified at 2022.01.11 17:59 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * CodifiersResource.java
 * $Id$
 */

package su.svn.daybook.resources;

import io.smallrye.mutiny.Multi;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.enums.ResourcePath;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.Codifier;
import su.svn.daybook.services.CodifierService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.concurrent.atomic.AtomicInteger;

@Path(ResourcePath.CODIFIERS)
public class CodifiersResource {

    private static final Logger LOG = Logger.getLogger(CodifiersResource.class);

    @Inject
    CodifierService codifierService;

    @GET
    @Path(ResourcePath.ALL)
    public Multi<Codifier> all() {
        final AtomicInteger counter = new AtomicInteger();
        LOG.trace("all");
        return codifierService.getAll()
                .onItem()
                .invoke(counter::incrementAndGet)
                .onItem()
                .transform(this::extractTagLabel)
                .onTermination()
                .invoke(() -> LOG.debugf("all counter %s", counter.get()));
    }

    private Codifier extractTagLabel(Answer answer) {
        return (Codifier)(answer.getPayload() != null ? answer.getPayload() : new Codifier());
    }
}