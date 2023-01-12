/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * KeyValueCacheProvider.java
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
import su.svn.daybook.models.domain.KeyValue;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.PageService;
import su.svn.daybook.services.domain.KeyValueDataService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

@ApplicationScoped
public class KeyValueCacheProvider extends AbstractCacheProvider<UUID, KeyValue> {

    private static final Logger LOG = Logger.getLogger(KeyValueCacheProvider.class);

    @Inject
    PageService pageService;

    @Inject
    KeyValueDataService keyValueDataService;

    public KeyValueCacheProvider() {
        super(EventAddress.KEY_VALUE_GET, EventAddress.KEY_VALUE_PAGE, LOG);
    }

    @Logged
    @CacheResult(cacheName = EventAddress.KEY_VALUE_GET)
    public Uni<KeyValue> get(@CacheKey UUID id) {
        return keyValueDataService.get(id);
    }

    @Logged
    @CacheResult(cacheName = EventAddress.KEY_VALUE_PAGE)
    public Uni<Page<Answer>> getPage(@CacheKey PageRequest pageRequest) {
        return pageService.getPage(pageRequest, keyValueDataService::count, keyValueDataService::findRange, Answer::of);
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
