/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * UserNameCacheProvider.java
 * $Id$
 */

package su.svn.daybook.services.cache;

import io.micrometer.core.annotation.Counted;
import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.annotations.Logged;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.models.domain.User;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.PageService;
import su.svn.daybook.services.domain.UserDataService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
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

    @Logged
    @Counted
    @CacheResult(cacheName = EventAddress.USER_GET)
    public Uni<User> get(@CacheKey UUID id) {
        return userDataService.get(id);
    }

    @Logged
    @Counted
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
