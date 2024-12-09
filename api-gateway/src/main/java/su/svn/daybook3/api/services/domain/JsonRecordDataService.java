/*
 * This file was last modified at 2024-10-30 17:27 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * JsonRecordDataService.java
 * $Id$
 */

package su.svn.daybook3.api.services.domain;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import su.svn.daybook3.api.domain.entities.BaseRecord;
import su.svn.daybook3.api.domain.entities.JsonRecord;
import su.svn.daybook3.api.models.dto.ResourceJsonRecord;
import su.svn.daybook3.api.models.pagination.Page;
import su.svn.daybook3.api.models.pagination.PageRequest;
import su.svn.daybook3.api.services.PanachePageService;
import su.svn.daybook3.api.services.mappers.JsonRecordMapper;
import su.svn.daybook3.domain.messages.Answer;

import java.util.UUID;

@ApplicationScoped
public class JsonRecordDataService implements PanacheDataService<UUID, JsonRecord, ResourceJsonRecord> {

    private static final Logger LOG = Logger.getLogger(JsonRecordDataService.class);

    @Inject
    JsonRecordMapper mapper;

    @Inject
    PanachePageService pageService;

    @Override
    public Uni<UUID> add(ResourceJsonRecord o) {
        LOG.tracef("add(%s)", o);
        return addEntry(mapper.toBaseRecord(o), mapper.toEntity(o));
    }

    @Override
    public Uni<Long> count() {
        return JsonRecord.count();
    }

    @Override
    public Uni<UUID> delete(UUID id) {
        LOG.tracef("delete(%s)", id);
        return deleteEntry(id);
    }

    @Override
    public Uni<ResourceJsonRecord> get(UUID id) {
        LOG.tracef("get(%s)", id);
        return JsonRecord.findByJsonRecordById(id)
                .onItem()
                .ifNotNull()
                .transform(baseRecord -> mapper.toResource(baseRecord));
    }

    @Override
    public Uni<Page<Answer>> getPage(PageRequest pageRequest) {
        LOG.tracef("getPage(%s)", pageRequest);
        return Panache.withTransaction(() ->
                pageService.getPage(
                        pageRequest,
                        JsonRecord.getAllEnabledPanacheQuery(),
                        record -> Answer.of(mapper.toResource(record))
                )
        );
    }

    public Uni<UUID> put(ResourceJsonRecord o) {
        LOG.tracef("put(%s)", o);
        return putEntry(mapper.toEntity(o));
    }

    private Uni<UUID> addEntry(BaseRecord baseRecord, JsonRecord entry) {
        return JsonRecord.addJsonRecord(baseRecord, entry)
                .onItem()
                .transform(JsonRecord::id);
    }

    private Uni<UUID> deleteEntry(UUID id) {
        return JsonRecord.deleteJsonRecordById(id)
                .onItem()
                .transform(entity -> !entity ? id : null);
    }

    private Uni<UUID> putEntry(JsonRecord entry) {
        return JsonRecord.updateJsonRecord(entry)
                .onItem()
                .ifNotNull()
                .transform(JsonRecord::id);
    }
}
