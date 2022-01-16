/*
 * This file was last modified at 2022.01.11 17:44 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * WordResource.java
 * $Id$
 */

package su.svn.daybook.resources;

import io.smallrye.mutiny.Multi;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.enums.ResourcePath;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.Word;
import su.svn.daybook.services.WordService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.concurrent.atomic.AtomicInteger;

@Path(ResourcePath.WORDS)
public class WordsResource {

    private static final Logger LOG = Logger.getLogger(WordsResource.class);

    @Inject
    WordService wordService;

    @GET
    @Path(ResourcePath.ALL)
    @Produces("application/json")
    public Multi<Word> all() {
        final AtomicInteger counter = new AtomicInteger();
        LOG.trace("all");
        return wordService.getAll()
                .onItem()
                .invoke(counter::incrementAndGet)
                .onItem()
                .transform(this::extract)
                .onTermination()
                .invoke(() -> LOG.debugf("all counter %s", counter.get()));
    }

    private Word extract(Answer answer) {
        return (Word)(answer.getPayload() != null ? answer.getPayload() : new Word());
    }
}