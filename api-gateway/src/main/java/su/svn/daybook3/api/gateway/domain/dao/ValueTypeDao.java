/*
 * This file was last modified at 2024-05-14 21:25 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * ValueTypeDao.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.domain.dao;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import su.svn.daybook3.api.gateway.annotations.PrincipalLogging;
import su.svn.daybook3.api.gateway.annotations.SQL;
import su.svn.daybook3.api.gateway.domain.model.ValueTypeTable;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class ValueTypeDao extends AbstractDao<Long, ValueTypeTable> implements DaoIface<Long, ValueTypeTable> {

    ValueTypeDao() {
        super(ValueTypeTable.ID, r -> r.getLong(ValueTypeTable.ID), ValueTypeTable::from);
    }

    @Override
    @PrincipalLogging
    @SQL(ValueTypeTable.COUNT_DICTIONARY_VALUE_TYPE)
    public Uni<Optional<Long>> count() {
        return super.countSQL().map(Optional::ofNullable);
    }

    @Override
    @PrincipalLogging
    @SQL(ValueTypeTable.DELETE_FROM_DICTIONARY_VALUE_TYPE_WHERE_ID_$1_RETURNING_S)
    public Uni<Optional<Long>> delete(Long id) {
        return super
                .deleteSQL(id)
                .map(Optional::ofNullable);
    }

    @Override
    @SQL(ValueTypeTable.SELECT_ALL_FROM_DICTIONARY_VALUE_TYPE_ORDER_BY_S)
    public Multi<ValueTypeTable> findAll() {
        return super.findAllSQL();
    }

    @Override
    @PrincipalLogging
    @SQL(ValueTypeTable.SELECT_FROM_DICTIONARY_VALUE_TYPE_WHERE_ID_$1)
    public Uni<Optional<ValueTypeTable>> findById(Long id) {
        return super
                .findByIdSQL(id)
                .map(Optional::ofNullable);
    }

    @PrincipalLogging
    @SQL(ValueTypeTable.SELECT_FROM_DICTIONARY_VALUE_TYPE_WHERE_KEY_$1)
    public Uni<Optional<ValueTypeTable>> findByKey(String valueType) {
        return super
                .findByKeySQL(valueType)
                .map(Optional::ofNullable);
    }

    @Override
    @SQL(ValueTypeTable.SELECT_ALL_FROM_DICTIONARY_VALUE_TYPE_ORDER_BY_S_OFFSET_$1_LIMIT_$2)
    public Multi<ValueTypeTable> findRange(long offset, long limit) {
        return super.findRangeSQL(offset, limit);
    }

    @Override
    @PrincipalLogging
    @SQL
    public Uni<Optional<Long>> insert(ValueTypeTable entry) {
        return super
                .insertSQL(entry)
                .map(Optional::ofNullable);
    }

    @Override
    @PrincipalLogging
    @SQL
    public Uni<Optional<ValueTypeTable>> insertEntry(ValueTypeTable entry) {
        return super
                .insertSQLEntry(entry)
                .map(Optional::ofNullable);
    }

    @Override
    @PrincipalLogging
    @SQL(ValueTypeTable.UPDATE_DICTIONARY_VALUE_TYPE_WHERE_ID_$1_RETURNING_S)
    public Uni<Optional<Long>> update(ValueTypeTable entry) {
        return super
                .updateSQL(entry)
                .map(Optional::ofNullable);
    }

    @Override
    @PrincipalLogging
    @SQL(ValueTypeTable.UPDATE_DICTIONARY_VALUE_TYPE_WHERE_ID_$1_RETURNING_S)
    public Uni<Optional<ValueTypeTable>> updateEntry(ValueTypeTable entry) {
        return super
                .updateSQLEntry(entry)
                .map(Optional::ofNullable);
    }
}
