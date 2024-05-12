/*
 * This file was last modified at 2024-05-14 21:25 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * StanzaDao.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.domain.dao;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import su.svn.daybook3.api.gateway.annotations.PrincipalLogging;
import su.svn.daybook3.api.gateway.annotations.SQL;
import su.svn.daybook3.api.gateway.domain.model.StanzaTable;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class StanzaDao extends AbstractDao<Long, StanzaTable> implements DaoIface<Long, StanzaTable> {

    StanzaDao() {
        super(StanzaTable.ID, r -> r.getLong(StanzaTable.ID), StanzaTable::from);
    }

    @Override
    @PrincipalLogging
    @SQL(StanzaTable.COUNT_DICTIONARY_STANZA)
    public Uni<Optional<Long>> count() {
        return super.countSQL().map(Optional::ofNullable);
    }

    @Override
    @PrincipalLogging
    @SQL(StanzaTable.DELETE_FROM_DICTIONARY_STANZA_WHERE_ID_$1_RETURNING_S)
    public Uni<Optional<Long>> delete(Long id) {
        return super
                .deleteSQL(id)
                .map(Optional::ofNullable);
    }

    @Override
    @SQL(StanzaTable.SELECT_ALL_FROM_DICTIONARY_STANZA_ORDER_BY_S)
    public Multi<StanzaTable> findAll() {
        return super.findAllSQL();
    }

    @Override
    @PrincipalLogging
    @SQL(StanzaTable.SELECT_FROM_DICTIONARY_STANZA_WHERE_ID_$1)
    public Uni<Optional<StanzaTable>> findById(Long id) {
        return super
                .findByIdSQL(id)
                .map(Optional::ofNullable);
    }

    @PrincipalLogging
    @SQL(StanzaTable.SELECT_FROM_DICTIONARY_STANZA_WHERE_NAME_$1)
    public Multi<StanzaTable> findByName(String name) {
        return super.findBy("findByName", name);
    }

    @SQL(StanzaTable.SELECT_FROM_DICTIONARY_STANZA_WHERE_PARENT_ID_$1)
    public Multi<StanzaTable> findByParentId(Long parentId) {
        return super.findBy("findByParentId", parentId);
    }

    @Override
    @SQL(StanzaTable.SELECT_ALL_FROM_DICTIONARY_STANZA_ORDER_BY_S_OFFSET_$1_LIMIT_$2)
    public Multi<StanzaTable> findRange(long offset, long limit) {
        return super.findRangeSQL(offset, limit);
    }

    @Override
    @PrincipalLogging
    @SQL
    public Uni<Optional<Long>> insert(StanzaTable entry) {
        return super
                .insertSQL(entry)
                .map(Optional::ofNullable);
    }

    @Override
    @PrincipalLogging
    @SQL
    public Uni<Optional<StanzaTable>> insertEntry(StanzaTable entry) {
        return super
                .insertSQLEntry(entry)
                .map(Optional::ofNullable);
    }

    @Override
    @PrincipalLogging
    @SQL(StanzaTable.UPDATE_DICTIONARY_STANZA_WHERE_ID_$1_RETURNING_S)
    public Uni<Optional<Long>> update(StanzaTable entry) {
        return super
                .updateSQL(entry)
                .map(Optional::ofNullable);
    }

    @Override
    @PrincipalLogging
    @SQL(StanzaTable.UPDATE_DICTIONARY_STANZA_WHERE_ID_$1_RETURNING_S)
    public Uni<Optional<StanzaTable>> updateEntry(StanzaTable entry) {
        return super
                .updateSQLEntry(entry)
                .map(Optional::ofNullable);
    }
}
