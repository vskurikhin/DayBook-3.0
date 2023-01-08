/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * CodifierCacheProvider.java
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
import su.svn.daybook.models.domain.Codifier;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.PageService;
import su.svn.daybook.services.domain.CodifierDataService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class CodifierCacheProvider extends AbstractCacheProvider<String> {

    private static final Logger LOG = Logger.getLogger(CodifierCacheProvider.class);

    @Inject
    CacheManager cacheManager;

    @Inject
    PageService pageService;

    @Inject
    CodifierDataService codifierDataService;

    public CodifierCacheProvider() {
        super(EventAddress.CODIFIER_GET, EventAddress.CODIFIER_PAGE, LOG);
    }

    @CacheResult(cacheName = EventAddress.CODIFIER_GET)
    public Uni<Codifier> get(@CacheKey String id) {
        LOG.tracef("get(%s)", id);
        return codifierDataService.get(id);
    }

    @CacheResult(cacheName = EventAddress.CODIFIER_PAGE)
    public Uni<Page<Answer>> getPage(@CacheKey PageRequest pageRequest) {
        LOG.tracef("getPage(%s)", pageRequest);
        return pageService.getPage(pageRequest, codifierDataService::count, codifierDataService::findRange, Answer::of);
    }

    @Override
    public Uni<Answer> invalidate(Answer answer) {
        return invalidateAllPagesCache(answer);
    }

    @Override
    public Uni<Answer> invalidateById(String id, Answer answer) {
        return invalidateCacheById(id).map(l -> answer);
    }

    @Override
    protected CacheManager getCacheManager() {
        return cacheManager;
    }
}
