/*
 * This file was last modified at 2024-05-14 21:25 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * I18nDao.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.domain.dao;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import su.svn.daybook3.api.gateway.annotations.PrincipalLogging;
import su.svn.daybook3.api.gateway.annotations.SQL;
import su.svn.daybook3.api.gateway.domain.model.I18nTable;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class I18nDao extends AbstractDao<Long, I18nTable> implements DaoIface<Long, I18nTable> {

    I18nDao() {
        super(I18nTable.ID, r -> r.getLong(I18nTable.ID), I18nTable::from);
    }

    @Override
    @PrincipalLogging
    @SQL(I18nTable.COUNT_DICTIONARY_I18N)
    public Uni<Optional<Long>> count() {
        return super.countSQL().map(Optional::ofNullable);
    }

    @Override
    @PrincipalLogging
    @SQL(I18nTable.DELETE_FROM_DICTIONARY_I18N_WHERE_ID_$1_RETURNING_S)
    public Uni<Optional<Long>> delete(Long id) {
        return super
                .deleteSQL(id)
                .map(Optional::ofNullable);
    }

    @Override
    @SQL(I18nTable.SELECT_ALL_FROM_DICTIONARY_I18N_ORDER_BY_S)
    public Multi<I18nTable> findAll() {
        return super.findAllSQL();
    }

    @Override
    @PrincipalLogging
    @SQL(I18nTable.SELECT_FROM_DICTIONARY_I18N_WHERE_ID_$1)
    public Uni<Optional<I18nTable>> findById(Long id) {
        return super
                .findByIdSQL(id)
                .map(Optional::ofNullable);
    }

    @PrincipalLogging
    @SQL(I18nTable.SELECT_FROM_DICTIONARY_I18N_WHERE_KEY_$1_$2)
    public Uni<Optional<I18nTable>> findByKey(Long languageId, String message) {
        return super
                .findByKeySQL(List.of(languageId, message))
                .map(Optional::ofNullable);
    }

    @PrincipalLogging
    @SQL(I18nTable.SELECT_FROM_DICTIONARY_I18N_WHERE_LANGUAGE_ID_$1)
    public Multi<I18nTable> findByLanguageId(Long languageId) {
        return super.findBy("findByLanguageId", languageId);
    }

    @SQL(I18nTable.SELECT_FROM_DICTIONARY_I18N_WHERE_MESSAGE_$1)
    public Multi<I18nTable> findByMessage(String message) {
        return super.findBy("findByMessage", message);
    }

    @Override
    @SQL(I18nTable.SELECT_ALL_FROM_DICTIONARY_I18N_ORDER_BY_S_OFFSET_$1_LIMIT_$2)
    public Multi<I18nTable> findRange(long offset, long limit) {
        return super.findRangeSQL(offset, limit);
    }

    @Override
    @PrincipalLogging
    @SQL
    public Uni<Optional<Long>> insert(I18nTable entry) {
        return super
                .insertSQL(entry)
                .map(Optional::ofNullable);
    }

    @Override
    @PrincipalLogging
    @SQL
    public Uni<Optional<I18nTable>> insertEntry(I18nTable entry) {
        return super
                .insertSQLEntry(entry)
                .map(Optional::ofNullable);
    }

    @Override
    @PrincipalLogging
    @SQL(I18nTable.UPDATE_DICTIONARY_I18N_WHERE_ID_$1_RETURNING_S)
    public Uni<Optional<Long>> update(I18nTable entry) {
        return super
                .updateSQL(entry)
                .map(Optional::ofNullable);
    }

    @Override
    @PrincipalLogging
    @SQL(I18nTable.UPDATE_DICTIONARY_I18N_WHERE_ID_$1_RETURNING_S)
    public Uni<Optional<I18nTable>> updateEntry(I18nTable entry) {
        return super
                .updateSQLEntry(entry)
                .map(Optional::ofNullable);
    }
}
