/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * @Name@CacheProvider.java
 * $Id$
 */

package su.svn.daybook.services.cache;

import io.quarkus.cache.Cache@Key@;
import io.quarkus.cache.CacheManager;
import io.quarkus.cache.CacheResult;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.dao.@Name@Dao;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.@Name@Table;
import su.svn.daybook.models.domain.@Name@;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.PageService;
import su.svn.daybook.services.mappers.@Name@Mapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class @Name@CacheProvider extends AbstractCacheProvider<@IdType@> {

    private static final Logger LOG = Logger.getLogger(@Name@CacheProvider.class);

    @Inject
    CacheManager cacheManager;

    @Inject
    PageService pageService;

    @Inject
    @Name@Dao @name@Dao;

    @Inject
    @Name@Mapper @name@Mapper;

    public @Name@CacheProvider() {
        super(EventAddress.@TABLE@_GET, EventAddress.@TABLE@_PAGE, LOG);
    }

    @CacheResult(cacheName = EventAddress.@TABLE@_GET)
    public Uni<@Name@> get(@CacheKey @IdType@ id) {
        LOG.tracef("get(%s)", id);
        return @name@Dao
                .findById(id)
                .map(Optional::get)
                .map(@name@Mapper::convertToModel);
    }

    @CacheResult(cacheName = EventAddress.@TABLE@_PAGE)
    public Uni<Page<Answer>> getPage(@CacheKey PageRequest pageRequest) {
        LOG.tracef("getPage(%s)", pageRequest);
        return pageService.getPage(pageRequest, @name@Dao::count, @name@Dao::findRange, this::answerOfModel);
    }

    @Override
    public Uni<Answer> invalidate(Answer answer) {
        return invalidateAllPagesCache(answer);
    }

    @Override
    public Uni<Answer> invalidateById(@IdType@ id, Answer answer) {
        return invalidateCacheById(id).map(l -> answer);
    }

    @Override
    protected CacheManager getCacheManager() {
        return cacheManager;
    }

    private Answer answerOfModel(@Name@Table table) {
        return Answer.of(@name@Mapper.convertToModel(table));
    }
}
