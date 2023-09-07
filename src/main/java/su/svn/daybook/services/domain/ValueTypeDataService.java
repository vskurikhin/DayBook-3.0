/*
 * This file was last modified at 2023.09.07 16:35 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * ValueTypeDataService.java
 * $Id$
 */

package su.svn.daybook.services.domain;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.dao.ValueTypeDao;
import su.svn.daybook.domain.model.ValueTypeTable;
import su.svn.daybook.models.domain.ValueType;
import su.svn.daybook.services.mappers.ValueTypeMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ValueTypeDataService implements DataService<Long, ValueTypeTable, ValueType> {

    private static final Logger LOG = Logger.getLogger(ValueTypeDataService.class);

    @Inject
    ValueTypeDao valueTypeDao;

    @Inject
    ValueTypeMapper valueTypeMapper;

    public Uni<Long> add(ValueType o) {
        LOG.tracef("add(%s)", o);
        return addEntry(valueTypeMapper.convertToDomain(o));
    }

    private Uni<Long> addEntry(ValueTypeTable entry) {
        return valueTypeDao
                .insert(entry)
                .map(o -> lookup(o, entry));
    }

    public Uni<Long> count() {
        return valueTypeDao
                .count()
                .map(o -> lookupLong(o, "count for ValueTypeTable"));
    }

    public Multi<ValueType> findRange(long offset, long limit) {
        LOG.tracef("findRange(%d, %d)", offset, limit);
        return valueTypeDao
                .findRange(offset, limit)
                .map(valueTypeMapper::convertToModel);
    }

    public Uni<ValueType> get(Long id) {
        LOG.tracef("get(%s)", id);
        return valueTypeDao
                .findById(id)
                .map(o -> lookup(o, id))
                .map(valueTypeMapper::convertToModel);
    }

    public Multi<ValueType> getAll() {
        LOG.tracef("getAll()");
        return valueTypeDao
                .count()
                .onItem()
                .transformToMulti(count -> getAllIfNotOverSize(count, valueTypeDao::findAll))
                .map(valueTypeMapper::convertToModel);
    }

    public Uni<Long> put(ValueType o) {
        LOG.tracef("put(%s)", o);
        return putEntry(valueTypeMapper.convertToDomain(o));
    }

    private Uni<Long> putEntry(ValueTypeTable entry) {
        return valueTypeDao
                .update(entry)
                .map(o -> lookup(o, entry));
    }

    public Uni<Long> delete(Long id) {
        LOG.tracef("delete(%s)", id);
        return deleteEntry(id);
    }

    private Uni<Long> deleteEntry(Long id) {
        return valueTypeDao
                .delete(id)
                .map(o -> lookupId(o, id));
    }
}
