/*
 * This file was last modified at 2024-10-30 14:17 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * SessionCacheProvider.java
 * $Id$
 */

package su.svn.daybook3.auth.services.cache;

import io.micrometer.core.annotation.Counted;
import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheManager;
import io.quarkus.cache.CacheResult;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import su.svn.daybook3.auth.annotations.PrincipalLogging;
import su.svn.daybook3.auth.domain.enums.EventAddress;
import su.svn.daybook3.auth.models.domain.Session;
import su.svn.daybook3.auth.models.pagination.Page;
import su.svn.daybook3.auth.models.pagination.PageRequest;
import su.svn.daybook3.auth.services.PageService;
import su.svn.daybook3.auth.services.domain.SessionDataService;
import su.svn.daybook3.domain.messages.Answer;

import java.util.UUID;

@ApplicationScoped
public class SessionCacheProvider extends AbstractCacheProvider<UUID, Session> {

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

    @PostConstruct
    public void setup() {
        super.setup(UUID.class, Session.class);
    }

    @Counted
    @PrincipalLogging
    @CacheResult(cacheName = EventAddress.SESSION_GET)
    public Uni<Session> get(@CacheKey UUID id) {
        return sessionDataService.get(id);
    }

    @Counted
    @PrincipalLogging
    @CacheResult(cacheName = EventAddress.SESSION_PAGE)
    public Uni<Page<Answer>> getPage(@CacheKey PageRequest pageRequest) {
        return pageService.getPage(pageRequest, sessionDataService::count, sessionDataService::findRange, Answer::of);
    }

    @Override
    public Uni<Answer> invalidate(Answer answer) {
        return invalidateAllPagesCache(answer);
    }

    @Override
    public Uni<Answer> invalidateByKey(UUID id, Answer answer) {
        return invalidateCacheByKey(id).map(l -> answer);
    }

    @PrincipalLogging
    public Uni<Answer> invalidateByUserName(String userName, Answer answer) {
        return super.invalidateByOther(userName, answer, Session::userName, Session::id);
    }
}
