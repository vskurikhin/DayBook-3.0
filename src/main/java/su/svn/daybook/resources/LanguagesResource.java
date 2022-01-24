/*
 * This file was last modified at 2022.01.11 17:44 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * LanguageResource.java
 * $Id$
 */

package su.svn.daybook.resources;

import io.smallrye.mutiny.Multi;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.enums.ResourcePath;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.Language;
import su.svn.daybook.services.LanguageService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.concurrent.atomic.AtomicInteger;

@Path(ResourcePath.LANGUAGES)
public class LanguagesResource {

    private static final Logger LOG = Logger.getLogger(LanguagesResource.class);

    @Inject
    LanguageService languageService;

    @GET
    @Path(ResourcePath.ALL)
    @Produces("application/json")
    public Multi<Language> all() {
        final AtomicInteger counter = new AtomicInteger();
        LOG.trace("all");
        return languageService.getAll()
                .onItem()
                .invoke(counter::incrementAndGet)
                .onItem()
                .transform(this::extract)
                .onTermination()
                .invoke(() -> LOG.debugf("all counter %s", counter.get()));
    }

    private Language extract(Answer answer) {
        return (Language)(answer.getPayload() != null ? answer.getPayload() : new Language());
    }
}