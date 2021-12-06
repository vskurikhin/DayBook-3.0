/*
 * This file was last modified at 2021.12.06 18:10 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * TagLabelsResource.java
 * $Id$
 */

package su.svn.daybook.resources;

import io.smallrye.mutiny.Multi;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.TagLabel;
import su.svn.daybook.services.TagLabelService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.concurrent.atomic.AtomicInteger;

@Path("/tags")
public class TagLabelsResource {

    private static final Logger LOG = Logger.getLogger(TagLabelsResource.class);

    @Inject
    TagLabelService tagLabelService;

    @GET
    @Path("/all")
    public Multi<TagLabel> all() {
        final AtomicInteger counter = new AtomicInteger();
        LOG.trace("all");
        return tagLabelService.getAll()
                .onItem()
                .invoke(counter::incrementAndGet)
                .onItem()
                .transform(this::extractTagLabel)
                .onTermination()
                .invoke(() -> LOG.debugf("all counter %s", counter.get()));
    }

    private TagLabel extractTagLabel(Answer answer) {
        return (TagLabel)(answer.getPayload() != null ? answer.getPayload() : new TagLabel());
    }
}