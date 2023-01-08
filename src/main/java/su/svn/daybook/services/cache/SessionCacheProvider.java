/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * SessionCacheProvider.java
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
import su.svn.daybook.models.domain.Session;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.PageService;
import su.svn.daybook.services.domain.SessionDataService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

@ApplicationScoped
public class SessionCacheProvider extends AbstractCacheProvider<UUID> {

    private static final Logger LOG = Logger.getLogger(SessionCacheProvider.class);

    @Inject
    CacheManager cacheManager;

    @Inject
    PageService pageService;

    @Inject
    SessionDataService sessionDataService;

    public SessionCacheProvider() {
        super(EventAddress.SESSION_GET, EventAddress.SESSION_PAGE, LOG);
    }

    @CacheResult(cacheName = EventAddress.SESSION_GET)
    public Uni<Session> get(@CacheKey UUID id) {
        LOG.tracef("get(%s)", id);
        return sessionDataService.get(id);
    }

    @CacheResult(cacheName = EventAddress.SESSION_PAGE)
    public Uni<Page<Answer>> getPage(@CacheKey PageRequest pageRequest) {
        LOG.tracef("getPage(%s)", pageRequest);
        return pageService.getPage(pageRequest, sessionDataService::count, sessionDataService::findRange, Answer::of);
    }

    @Override
    public Uni<Answer> invalidate(Answer answer) {
        return invalidateAllPagesCache(answer);
    }

    @Override
    public Uni<Answer> invalidateById(UUID id, Answer answer) {
        return invalidateCacheById(id).map(l -> answer);
    }

    @Override
    protected CacheManager getCacheManager() {
        return cacheManager;
    }
}
