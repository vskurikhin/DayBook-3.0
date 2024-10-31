/*
 * This file was last modified at 2024-10-30 14:17 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * RoleCacheProvider.java
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
import su.svn.daybook3.auth.models.domain.Role;
import su.svn.daybook3.auth.models.pagination.Page;
import su.svn.daybook3.auth.models.pagination.PageRequest;
import su.svn.daybook3.auth.services.PageService;
import su.svn.daybook3.auth.services.domain.RoleDataService;
import su.svn.daybook3.domain.messages.Answer;

import java.util.UUID;

@ApplicationScoped
public class RoleCacheProvider extends AbstractCacheProvider<UUID, Role> {

    private static final Logger LOG = Logger.getLogger(RoleCacheProvider.class);

    @Inject
    PageService pageService;

    @Inject
    RoleDataService roleDataService;

    public RoleCacheProvider() {
        super(EventAddress.ROLE_GET, EventAddress.ROLE_PAGE, LOG);
    }

    @Counted
    @PrincipalLogging
    @CacheResult(cacheName = EventAddress.ROLE_GET)
    public Uni<Role> get(@CacheKey UUID id) {
        return roleDataService.get(id);
    }

    @Counted
    @PrincipalLogging
    @CacheResult(cacheName = EventAddress.ROLE_PAGE)
    public Uni<Page<Answer>> getPage(@CacheKey PageRequest pageRequest) {
        return pageService.getPage(pageRequest, roleDataService::count, roleDataService::findRange, Answer::of);
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
