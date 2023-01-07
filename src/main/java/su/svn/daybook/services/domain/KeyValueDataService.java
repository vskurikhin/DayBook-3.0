/*
 * This file was last modified at 2023.01.07 11:52 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * KeyValueDataService.java
 * $Id$
 */

package su.svn.daybook.services.domain;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.dao.KeyValueDao;
import su.svn.daybook.domain.model.KeyValueTable;
import su.svn.daybook.models.domain.KeyValue;
import su.svn.daybook.services.mappers.KeyValueMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

@ApplicationScoped
public class KeyValueDataService implements DataService<UUID, KeyValueTable, KeyValue> {

    private static final Logger LOG = Logger.getLogger(KeyValueDataService.class);

    @Inject
    KeyValueDao keyValueDao;

    @Inject
    KeyValueMapper keyValueMapper;

    public Uni<UUID> add(KeyValue o) {
        LOG.tracef("add(%s)", o);
        return addEntry(keyValueMapper.convertToDomain(o));
    }

    private Uni<UUID> addEntry(KeyValueTable entry) {
        return keyValueDao
                .insert(entry)
                .map(o -> lookup(o, entry));
    }

    public Uni<Long> count() {
        return keyValueDao
                .count()
                .map(o -> lookupLong(o, "count for KeyValueTable"));
    }

    public Multi<KeyValue> findRange(long offset, long limit) {
        LOG.tracef("findRange(%d, %d)", offset, limit);
        return keyValueDao
                .findRange(offset, limit)
                .map(keyValueMapper::convertToModel);
    }

    public Uni<KeyValue> get(UUID id) {
        LOG.tracef("get(%s)", id);
        return keyValueDao
                .findById(id)
                .map(o -> lookup(o, id))
                .map(keyValueMapper::convertToModel);
    }

    public Multi<KeyValue> getAll() {
        LOG.tracef("getAll()");
        return keyValueDao
                .count()
                .onItem()
                .transformToMulti(count -> getAllIfNotOverSize(count, keyValueDao::findAll))
                .map(keyValueMapper::convertToModel);
    }

    public Uni<UUID> put(KeyValue o) {
        LOG.tracef("put(%s)", o);
        return putEntry(keyValueMapper.convertToDomain(o));
    }

    private Uni<UUID> putEntry(KeyValueTable entry) {
        return keyValueDao
                .update(entry)
                .map(o -> lookup(o, entry));
    }

    public Uni<UUID> delete(UUID id) {
        LOG.tracef("delete(%s)", id);
        return deleteEntry(id);
    }

    private Uni<UUID> deleteEntry(UUID id) {
        return keyValueDao
                .delete(id)
                .map(o -> lookupId(o, id));
    }
}
