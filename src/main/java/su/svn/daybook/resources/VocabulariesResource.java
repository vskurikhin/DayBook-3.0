/*
 * This file was last modified at 2022.01.11 17:44 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * VocabularyResource.java
 * $Id$
 */

package su.svn.daybook.resources;

import io.smallrye.mutiny.Multi;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.enums.ResourcePath;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.Vocabulary;
import su.svn.daybook.services.VocabularyService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.concurrent.atomic.AtomicInteger;

@Path(ResourcePath.VOCABULARIES)
public class VocabulariesResource {

    private static final Logger LOG = Logger.getLogger(CodifiersResource.class);

    @Inject
    VocabularyService vocabularyService;

    @GET
    @Path(ResourcePath.ALL)
    public Multi<Vocabulary> all() {
        final AtomicInteger counter = new AtomicInteger();
        LOG.trace("all");
        return vocabularyService.getAll()
                .onItem()
                .invoke(counter::incrementAndGet)
                .onItem()
                .transform(this::extract)
                .onTermination()
                .invoke(() -> LOG.debugf("all counter %s", counter.get()));
    }

    private Vocabulary extract(Answer answer) {
        return (Vocabulary)(answer.getPayload() != null ? answer.getPayload() : new Vocabulary());
    }
}