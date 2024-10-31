/*
 * This file was last modified at 2024-10-30 17:27 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * WordCacheProvider.java
 * $Id$
 */

package su.svn.daybook3.api.services.cache;

import io.micrometer.core.annotation.Counted;
import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import su.svn.daybook3.api.annotations.PrincipalLogging;
import su.svn.daybook3.api.domain.enums.EventAddress;
import su.svn.daybook3.api.models.domain.Word;
import su.svn.daybook3.api.models.pagination.Page;
import su.svn.daybook3.api.models.pagination.PageRequest;
import su.svn.daybook3.api.services.PageService;
import su.svn.daybook3.api.services.domain.WordDataService;
import su.svn.daybook3.domain.messages.Answer;

@ApplicationScoped
public class WordCacheProvider extends AbstractCacheProvider<String, Word> {

    private static final Logger LOG = Logger.getLogger(WordCacheProvider.class);

    @Inject
    PageService pageService;

    @Inject
    WordDataService wordDataService;

    public WordCacheProvider() {
        super(EventAddress.WORD_GET, EventAddress.WORD_PAGE, LOG);
    }

    @Counted
    @PrincipalLogging
    @CacheResult(cacheName = EventAddress.WORD_GET)
    public Uni<Word> get(@CacheKey String id) {
        return wordDataService.get(id);
    }

    @Counted
    @PrincipalLogging
    @CacheResult(cacheName = EventAddress.WORD_PAGE)
    public Uni<Page<Answer>> getPage(@CacheKey PageRequest pageRequest) {
        return pageService.getPage(pageRequest, wordDataService::count, wordDataService::findRange, Answer::of);
    }

    @Override
    public Uni<Answer> invalidate(Answer answer) {
        return invalidateAllPagesCache(answer);
    }

    @Override
    public Uni<Answer> invalidateByKey(String id, Answer answer) {
        return invalidateCacheByKey(id).map(l -> answer);
    }
}
