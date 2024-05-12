/*
 * This file was last modified at 2024-05-14 21:25 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * CodifierDao.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.domain.dao;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import su.svn.daybook3.api.gateway.annotations.PrincipalLogging;
import su.svn.daybook3.api.gateway.annotations.SQL;
import su.svn.daybook3.api.gateway.domain.model.CodifierTable;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class CodifierDao extends AbstractDao<String, CodifierTable> implements DaoIface<String, CodifierTable> {

    CodifierDao() {
        super(CodifierTable.ID, r -> r.getString(CodifierTable.ID), CodifierTable::from);
    }

    @PrincipalLogging
    @SQL(CodifierTable.COUNT_DICTIONARY_CODIFIER)
    public Uni<Optional<Long>> count() {
        return super.countSQL().map(Optional::ofNullable);
    }

    @PrincipalLogging
    @SQL(CodifierTable.DELETE_FROM_DICTIONARY_CODIFIER_WHERE_ID_$1_RETURNING_S)
    public Uni<Optional<String>> delete(String id) {
        return super.deleteSQL(id).map(Optional::ofNullable);
    }

    @SQL(CodifierTable.SELECT_ALL_FROM_DICTIONARY_CODIFIER_ORDER_BY_S)
    public Multi<CodifierTable> findAll() {
        return super.findAllSQL();
    }

    @PrincipalLogging
    @SQL(CodifierTable.SELECT_FROM_DICTIONARY_CODIFIER_WHERE_ID_$1)
    public Uni<Optional<CodifierTable>> findById(String id) {
        return super.findByIdSQL(id).map(Optional::ofNullable);
    }

    @PrincipalLogging
    @SQL(CodifierTable.SELECT_FROM_DICTIONARY_CODIFIER_WHERE_KEY_$1)
    public Uni<Optional<CodifierTable>> findByKey(String key) {
        return super.findByKeySQL(key).map(Optional::ofNullable);
    }

    @SQL(CodifierTable.SELECT_FROM_DICTIONARY_CODIFIER_WHERE_VALUE_$1)
    public Multi<CodifierTable> findByValue(String value) {
        return super.findByValueSQL(value);
    }

    @SQL(CodifierTable.SELECT_ALL_FROM_DICTIONARY_CODIFIER_ORDER_BY_S_OFFSET_$1_LIMIT_$2)
    public Multi<CodifierTable> findRange(long offset, long limit) {
        return super.findRangeSQL(offset, limit);
    }

    @PrincipalLogging
    @SQL
    public Uni<Optional<String>> insert(CodifierTable entry) {
        return super.insertSQL(entry).map(Optional::ofNullable);
    }

    @PrincipalLogging
    @SQL
    public Uni<Optional<CodifierTable>> insertEntry(CodifierTable entry) {
        return super.insertSQLEntry(entry).map(Optional::ofNullable);
    }

    @PrincipalLogging
    @SQL(CodifierTable.UPDATE_DICTIONARY_CODIFIER_WHERE_ID_$1_RETURNING_S)
    public Uni<Optional<String>> update(CodifierTable entry) {
        return super.updateSQL(entry).map(Optional::ofNullable);
    }

    @PrincipalLogging
    @SQL(CodifierTable.UPDATE_DICTIONARY_CODIFIER_WHERE_ID_$1_RETURNING_S)
    public Uni<Optional<CodifierTable>> updateEntry(CodifierTable entry) {
        return super.updateSQLEntry(entry).map(Optional::ofNullable);
    }
}
