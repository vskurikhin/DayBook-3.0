/*
 * This file was last modified at 2024-10-30 14:17 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * UserCacheProvider.java
 * $Id$
 */

package su.svn.daybook3.auth.services.cache;

import io.micrometer.core.annotation.Counted;
import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import su.svn.daybook3.auth.annotations.PrincipalLogging;
import su.svn.daybook3.auth.domain.enums.EventAddress;
import su.svn.daybook3.auth.models.domain.User;
import su.svn.daybook3.auth.models.pagination.Page;
import su.svn.daybook3.auth.models.pagination.PageRequest;
import su.svn.daybook3.auth.services.PageService;
import su.svn.daybook3.auth.services.domain.UserDataService;
import su.svn.daybook3.domain.messages.Answer;

import java.util.UUID;

@ApplicationScoped
public class UserCacheProvider extends AbstractCacheProvider<UUID, User> {

    private static final Logger LOG = Logger.getLogger(UserCacheProvider.class);

    @Inject
    PageService pageService;

    @Inject
    UserDataService userDataService;

    public UserCacheProvider() {
        super(EventAddress.USER_GET, EventAddress.USER_PAGE, LOG);
    }

    @Counted
    @PrincipalLogging
    @CacheResult(cacheName = EventAddress.USER_GET)
    public Uni<User> get(@CacheKey UUID id) {
        return userDataService.get(id);
    }

    @Counted
    @PrincipalLogging
    @CacheResult(cacheName = EventAddress.USER_PAGE)
    public Uni<Page<Answer>> getPage(@CacheKey PageRequest pageRequest) {
        return pageService.getPage(pageRequest, userDataService::count, userDataService::findRange, Answer::of);
    }

    @Override
    public Uni<Answer> invalidate(Answer answer) {
        return invalidateAllPagesCache(answer);
    }

    @Override
    public Uni<Answer> invalidateByKey(UUID id, Answer answer) {
        return invalidateCacheByKey(id).map(l -> answer);
    }
}
