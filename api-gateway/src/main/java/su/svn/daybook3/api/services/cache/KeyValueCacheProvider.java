/*
 * This file was last modified at 2024-10-30 17:27 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * KeyValueCacheProvider.java
 * $Id$
 */

package su.svn.daybook3.api.services.cache;

import io.micrometer.core.annotation.Counted;
import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import su.svn.daybook3.api.annotations.PrincipalLogging;
import su.svn.daybook3.api.domain.enums.EventAddress;
import su.svn.daybook3.api.models.domain.KeyValue;
import su.svn.daybook3.api.models.pagination.Page;
import su.svn.daybook3.api.models.pagination.PageRequest;
import su.svn.daybook3.api.services.PageService;
import su.svn.daybook3.api.services.domain.KeyValueDataService;
import su.svn.daybook3.domain.messages.Answer;

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

    @Counted
    @PrincipalLogging
    @CacheResult(cacheName = EventAddress.KEY_VALUE_GET)
    public Uni<KeyValue> get(@CacheKey UUID id) {
        return keyValueDataService.get(id);
    }

    @Counted
    @PrincipalLogging
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
