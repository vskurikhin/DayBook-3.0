/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * RoleCacheProvider.java
 * $Id$
 */

package su.svn.daybook.services.cache;

import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheManager;
import io.quarkus.cache.CacheResult;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.dao.RoleDao;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.RoleTable;
import su.svn.daybook.models.domain.Role;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.PageService;
import su.svn.daybook.services.mappers.RoleMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class RoleCacheProvider extends AbstractCacheProvider<UUID> {

    private static final Logger LOG = Logger.getLogger(RoleCacheProvider.class);

    @Inject
    CacheManager cacheManager;

    @Inject
    PageService pageService;

    @Inject
    RoleDao roleDao;

    @Inject
    RoleMapper roleMapper;

    public RoleCacheProvider() {
        super(EventAddress.ROLE_GET, EventAddress.ROLE_PAGE, LOG);
    }

    @CacheResult(cacheName = EventAddress.ROLE_GET)
    public Uni<Role> get(@CacheKey UUID id) {
        LOG.tracef("get(%s)", id);
        return roleDao
                .findById(id)
                .map(Optional::get)
                .map(roleMapper::convertToModel);
    }

    @CacheResult(cacheName = EventAddress.ROLE_PAGE)
    public Uni<Page<Answer>> getPage(@CacheKey PageRequest pageRequest) {
        LOG.tracef("getPage(%s)", pageRequest);
        return pageService.getPage(pageRequest, roleDao::count, roleDao::findRange, this::answerOfModel);
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

    private Answer answerOfModel(RoleTable table) {
        return Answer.of(roleMapper.convertToModel(table));
    }
}
