/*
 * This file was last modified at 2024-05-20 16:32 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * BaseRecordCacheProvider.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.services.cache;

import io.micrometer.core.annotation.Counted;
import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import su.svn.daybook3.api.gateway.annotations.PrincipalLogging;
import su.svn.daybook3.api.gateway.domain.enums.EventAddress;
import su.svn.daybook3.api.gateway.domain.messages.Answer;
import su.svn.daybook3.api.gateway.models.dto.ResourceBaseRecord;
import su.svn.daybook3.api.gateway.models.pagination.Page;
import su.svn.daybook3.api.gateway.models.pagination.PageRequest;
import su.svn.daybook3.api.gateway.services.domain.BaseRecordDataService;

import java.util.UUID;

@ApplicationScoped
public class BaseRecordCacheProvider extends AbstractCacheProvider<UUID, ResourceBaseRecord> {

    private static final Logger LOG = Logger.getLogger(BaseRecordCacheProvider.class);

    @Inject
    BaseRecordDataService baseRecordsDataService;

    public BaseRecordCacheProvider() {
        super(EventAddress.BASE_RECORD_GET, EventAddress.BASE_RECORD_PAGE, LOG);
    }

    @Counted
    @PrincipalLogging
    @CacheResult(cacheName = EventAddress.BASE_RECORD_GET)
    public Uni<ResourceBaseRecord> get(@CacheKey UUID id) {
        return baseRecordsDataService.get(id);
    }

    @Counted
    @PrincipalLogging
    @CacheResult(cacheName = EventAddress.BASE_RECORD_PAGE)
    public Uni<Page<Answer>> getPage(@CacheKey PageRequest pageRequest) {
        return baseRecordsDataService.getPage(pageRequest);
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
