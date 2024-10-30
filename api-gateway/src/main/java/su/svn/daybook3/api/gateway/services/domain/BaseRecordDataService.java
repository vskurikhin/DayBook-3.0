/*
 * This file was last modified at 2024-10-30 09:48 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * BaseRecordDataService.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.services.domain;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import su.svn.daybook3.api.gateway.domain.entities.BaseRecord;
import su.svn.daybook3.api.gateway.models.dto.ResourceBaseRecord;
import su.svn.daybook3.api.gateway.models.pagination.Page;
import su.svn.daybook3.api.gateway.models.pagination.PageRequest;
import su.svn.daybook3.api.gateway.services.PanachePageService;
import su.svn.daybook3.api.gateway.services.mappers.BaseRecordMapper;
import su.svn.daybook3.domain.messages.Answer;

import java.util.UUID;

@ApplicationScoped
public class BaseRecordDataService implements PanacheDataService<UUID, BaseRecord, ResourceBaseRecord> {

    private static final Logger LOG = Logger.getLogger(BaseRecordDataService.class);

    @Inject
    BaseRecordMapper mapper;

    @Inject
    PanachePageService pageService;

    @Override
    public Uni<UUID> add(ResourceBaseRecord o) {
        LOG.tracef("add(%s)", o);
        return addEntry(mapper.toEntity(o));
    }

    @Override
    public Uni<Long> count() {
        return BaseRecord.count();
    }

    @Override
    public Uni<UUID> delete(UUID id) {
        LOG.tracef("delete(%s)", id);
        return deleteEntry(id);
    }

    @Override
    public Uni<ResourceBaseRecord> get(UUID id) {
        LOG.tracef("get(%s)", id);
        return BaseRecord.findByBaseRecordById(id)
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
                        getAllEnabledAndTypePanacheQuery(),
                        record -> Answer.of(mapper.toResource(record))
                )
        );
    }

    public Uni<UUID> put(ResourceBaseRecord o) {
        LOG.tracef("put(%s)", o);
        return putEntry(mapper.toEntity(o));
    }

    private Uni<UUID> addEntry(BaseRecord entry) {
        return BaseRecord.addBaseRecord(entry)
                .onItem()
                .transform(BaseRecord::id);
    }

    private Uni<UUID> deleteEntry(UUID id) {
        return BaseRecord.deleteBaseRecordById(id)
                .onItem()
                .transform(entity -> !entity ? id : null);
    }

    private PanacheQuery<BaseRecord> getAllEnabledAndTypePanacheQuery() {
        return BaseRecord.find("#" + BaseRecord.LIST_ENABLED_WITH_TYPE, BaseRecord.ENABLED_BASE_TYPE);
    }

    private Uni<UUID> putEntry(BaseRecord entry) {
        return BaseRecord.updateBaseRecord(entry)
                .onItem()
                .transform(BaseRecord::id);
    }
}
