/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * LanguageCacheProvider.java
 * $Id$
 */

package su.svn.daybook.services.cache;

import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheManager;
import io.quarkus.cache.CacheResult;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.models.domain.Language;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.PageService;
import su.svn.daybook.services.domain.LanguageDataService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class LanguageCacheProvider extends AbstractCacheProvider<Long> {

    private static final Logger LOG = Logger.getLogger(LanguageCacheProvider.class);

    @Inject
    CacheManager cacheManager;

    @Inject
    PageService pageService;

    @Inject
    LanguageDataService languageDataService;

    public LanguageCacheProvider() {
        super(EventAddress.LANGUAGE_GET, EventAddress.LANGUAGE_PAGE, LOG);
    }

    @CacheResult(cacheName = EventAddress.LANGUAGE_GET)
    public Uni<Language> get(@CacheKey Long id) {
        LOG.tracef("get(%s)", id);
        return languageDataService.get(id);
    }

    @CacheResult(cacheName = EventAddress.LANGUAGE_PAGE)
    public Uni<Page<Answer>> getPage(@CacheKey PageRequest pageRequest) {
        LOG.tracef("getPage(%s)", pageRequest);
        return pageService.getPage(pageRequest, languageDataService::count, languageDataService::findRange, Answer::of);
    }

    @Override
    public Uni<Answer> invalidate(Answer answer) {
        return invalidateAllPagesCache(answer);
    }

    @Override
    public Uni<Answer> invalidateById(Long id, Answer answer) {
        return invalidateCacheById(id).map(l -> answer);
    }

    @Override
    protected CacheManager getCacheManager() {
        return cacheManager;
    }
}
