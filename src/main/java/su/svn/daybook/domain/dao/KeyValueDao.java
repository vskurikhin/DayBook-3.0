/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * KeyValueDao.java
 * $Id$
 */

package su.svn.daybook.domain.dao;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import su.svn.daybook.annotations.Logged;
import su.svn.daybook.annotations.SQL;
import su.svn.daybook.domain.model.KeyValueTable;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class KeyValueDao extends AbstractDao<UUID, KeyValueTable> {

    KeyValueDao() {
        super(r -> r.getUUID(KeyValueTable.ID), KeyValueTable::from);
    }

    @Logged
    @SQL(KeyValueTable.COUNT_DICTIONARY_KEY_VALUE)
    public Uni<Optional<Long>> count() {
        return super.countSQL().map(Optional::ofNullable);
    }

    @Logged
    @SQL(KeyValueTable.DELETE_FROM_DICTIONARY_KEY_VALUE_WHERE_ID_$1)
    public Uni<Optional<UUID>> delete(UUID id) {
        return super.deleteSQL(id).map(Optional::ofNullable);
    }

    @Logged
    @SQL(KeyValueTable.SELECT_ALL_FROM_DICTIONARY_KEY_VALUE_ORDER_BY_ID_ASC)
    public Multi<KeyValueTable> findAll() {
        return super.findAllSQL();
    }

    @Logged
    @SQL(KeyValueTable.SELECT_FROM_DICTIONARY_KEY_VALUE_WHERE_ID_$1)
    public Uni<Optional<KeyValueTable>> findById(UUID id) {
        return super.findByIdSQL(id).map(Optional::ofNullable);
    }

    @Logged
    @SQL(KeyValueTable.SELECT_ALL_FROM_DICTIONARY_KEY_VALUE_ORDER_BY_ID_ASC_OFFSET_LIMIT)
    public Multi<KeyValueTable> findRange(long offset, long limit) {
        return super.findRangeSQL(offset, limit);
    }

    @Logged
    @SQL
    public Uni<Optional<UUID>> insert(KeyValueTable entry) {
        return super.insertSQL(entry).map(Optional::ofNullable);
    }

    @Logged
    @SQL(KeyValueTable.UPDATE_DICTIONARY_KEY_VALUE_WHERE_ID_$1)
    public Uni<Optional<UUID>> update(KeyValueTable entry) {
        return super.updateSQL(entry).map(Optional::ofNullable);
    }
}
