/*
 * This file was last modified at 2024-10-30 17:27 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * JsonRecordCacheProvider.java
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
import su.svn.daybook3.api.models.dto.ResourceJsonRecord;
import su.svn.daybook3.api.models.pagination.Page;
import su.svn.daybook3.api.models.pagination.PageRequest;
import su.svn.daybook3.api.services.domain.JsonRecordDataService;
import su.svn.daybook3.domain.messages.Answer;

import java.util.UUID;

@ApplicationScoped
public class JsonRecordCacheProvider extends AbstractCacheProvider<UUID, ResourceJsonRecord> {

    private static final Logger LOG = Logger.getLogger(JsonRecordCacheProvider.class);

    @Inject
    JsonRecordDataService jsonRecordsDataService;

    public JsonRecordCacheProvider() {
        super(EventAddress.JSON_RECORD_GET, EventAddress.JSON_RECORD_PAGE, LOG);
    }

    @Counted
    @PrincipalLogging
    @CacheResult(cacheName = EventAddress.JSON_RECORD_GET)
    public Uni<ResourceJsonRecord> get(@CacheKey UUID id) {
        return jsonRecordsDataService.get(id);
    }

    @Counted
    @PrincipalLogging
    @CacheResult(cacheName = EventAddress.JSON_RECORD_PAGE)
    public Uni<Page<Answer>> getPage(@CacheKey PageRequest pageRequest) {
        return jsonRecordsDataService.getPage(pageRequest);
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
