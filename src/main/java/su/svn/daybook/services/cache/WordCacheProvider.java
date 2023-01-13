/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * WordCacheProvider.java
 * $Id$
 */

package su.svn.daybook.services.cache;

import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.annotations.Logged;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.models.domain.Word;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.PageService;
import su.svn.daybook.services.domain.WordDataService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.NoSuchElementException;

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

    @Logged
    @CacheResult(cacheName = EventAddress.WORD_GET)
    public Uni<Word> get(@CacheKey String id) {
         return wordDataService.get(id);
    }

    @Logged
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
