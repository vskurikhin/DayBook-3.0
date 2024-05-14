/*
 * This file was last modified at 2024-05-14 21:36 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * KeyValueDao.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.domain.dao;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import su.svn.daybook3.api.gateway.annotations.PrincipalLogging;
import su.svn.daybook3.api.gateway.annotations.SQL;
import su.svn.daybook3.api.gateway.domain.model.KeyValueTable;

import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigInteger;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class KeyValueDao extends AbstractDao<UUID, KeyValueTable> implements DaoIface<UUID, KeyValueTable> {

    KeyValueDao() {
        super(KeyValueTable.ID, r -> r.getUUID(KeyValueTable.ID), KeyValueTable::from);
    }

    @Override
    @PrincipalLogging
    @SQL(KeyValueTable.COUNT_DICTIONARY_KEY_VALUE)
    public Uni<Optional<Long>> count() {
        return super.countSQL().map(Optional::ofNullable);
    }

    @Override
    @PrincipalLogging
    @SQL(KeyValueTable.DELETE_FROM_DICTIONARY_KEY_VALUE_WHERE_ID_$1_RETURNING_S)
    public Uni<Optional<UUID>> delete(UUID id) {
        return super
                .deleteSQL(id)
                .map(Optional::ofNullable);
    }

    @Override
    @SQL(KeyValueTable.SELECT_ALL_FROM_DICTIONARY_KEY_VALUE_ORDER_BY_S)
    public Multi<KeyValueTable> findAll() {
        return super.findAllSQL();
    }

    @Override
    @PrincipalLogging
    @SQL(KeyValueTable.SELECT_FROM_DICTIONARY_KEY_VALUE_WHERE_ID_$1)
    public Uni<Optional<KeyValueTable>> findById(UUID id) {
        return super
                .findByIdSQL(id)
                .map(Optional::ofNullable);
    }

    @PrincipalLogging
    @SQL(KeyValueTable.SELECT_FROM_DICTIONARY_KEY_VALUE_WHERE_KEY_$1)
    public Uni<Optional<KeyValueTable>> findByKey(BigInteger key) {
        return super
                .findByKeySQL(key)
                .map(Optional::ofNullable);
    }

    @SQL(KeyValueTable.SELECT_FROM_DICTIONARY_KEY_VALUE_WHERE_VALUE_$1)
    public Multi<KeyValueTable> findByValue(JsonObject value) {
        return super.findByValueSQL(value);
    }

    @Override
    @SQL(KeyValueTable.SELECT_ALL_FROM_DICTIONARY_KEY_VALUE_ORDER_BY_S_OFFSET_$1_LIMIT_$2)
    public Multi<KeyValueTable> findRange(long offset, long limit) {
        return super.findRangeSQL(offset, limit);
    }

    @Override
    @PrincipalLogging
    @SQL
    public Uni<Optional<UUID>> insert(KeyValueTable entry) {
        return super
                .insertSQL(entry)
                .map(Optional::ofNullable);
    }

    @Override
    @PrincipalLogging
    @SQL
    public Uni<Optional<KeyValueTable>> insertEntry(KeyValueTable entry) {
        return super
                .insertSQLEntry(entry)
                .map(Optional::ofNullable);
    }

    @Override
    @PrincipalLogging
    @SQL(KeyValueTable.UPDATE_DICTIONARY_KEY_VALUE_WHERE_ID_$1_RETURNING_S)
    public Uni<Optional<UUID>> update(KeyValueTable entry) {
        return super
                .updateSQL(entry)
                .map(Optional::ofNullable);
    }

    @Override
    @PrincipalLogging
    @SQL(KeyValueTable.UPDATE_DICTIONARY_KEY_VALUE_WHERE_ID_$1_RETURNING_S)
    public Uni<Optional<KeyValueTable>> updateEntry(KeyValueTable entry) {
        return super
                .updateSQLEntry(entry)
                .map(Optional::ofNullable);
    }
}
