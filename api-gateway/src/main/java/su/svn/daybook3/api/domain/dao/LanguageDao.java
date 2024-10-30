/*
 * This file was last modified at 2024-10-30 17:26 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * LanguageDao.java
 * $Id$
 */

package su.svn.daybook3.api.domain.dao;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import su.svn.daybook3.annotations.SQL;
import su.svn.daybook3.api.annotations.PrincipalLogging;
import su.svn.daybook3.api.domain.model.LanguageTable;

import java.util.Optional;

@ApplicationScoped
public class LanguageDao extends AbstractDao<Long, LanguageTable> implements DaoIface<Long, LanguageTable> {

    LanguageDao() {
        super(LanguageTable.ID, r -> r.getLong(LanguageTable.ID), LanguageTable::from);
    }

    @Override
    @PrincipalLogging
    @SQL(LanguageTable.COUNT_DICTIONARY_LANGUAGE)
    public Uni<Optional<Long>> count() {
        return super.countSQL().map(Optional::ofNullable);
    }

    @Override
    @PrincipalLogging
    @SQL(LanguageTable.DELETE_FROM_DICTIONARY_LANGUAGE_WHERE_ID_$1_RETURNING_S)
    public Uni<Optional<Long>> delete(Long id) {
        return super
                .deleteSQL(id)
                .map(Optional::ofNullable);
    }

    @Override
    @SQL(LanguageTable.SELECT_ALL_FROM_DICTIONARY_LANGUAGE_ORDER_BY_S)
    public Multi<LanguageTable> findAll() {
        return super.findAllSQL();
    }

    @Override
    @PrincipalLogging
    @SQL(LanguageTable.SELECT_FROM_DICTIONARY_LANGUAGE_WHERE_ID_$1)
    public Uni<Optional<LanguageTable>> findById(Long id) {
        return super
                .findByIdSQL(id)
                .map(Optional::ofNullable);
    }

    @PrincipalLogging
    @SQL(LanguageTable.SELECT_FROM_DICTIONARY_LANGUAGE_WHERE_KEY_$1)
    public Uni<Optional<LanguageTable>> findByKey(String language) {
        return super
                .findByKeySQL(language)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<LanguageTable>> findByLanguage(String language) {
        return findByKey(language);
    }

    @Override
    @SQL(LanguageTable.SELECT_ALL_FROM_DICTIONARY_LANGUAGE_ORDER_BY_S_OFFSET_$1_LIMIT_$2)
    public Multi<LanguageTable> findRange(long offset, long limit) {
        return super.findRangeSQL(offset, limit);
    }

    @Override
    @PrincipalLogging
    @SQL
    public Uni<Optional<Long>> insert(LanguageTable entry) {
        return super
                .insertSQL(entry)
                .map(Optional::ofNullable);
    }

    @Override
    @PrincipalLogging
    @SQL
    public Uni<Optional<LanguageTable>> insertEntry(LanguageTable entry) {
        return super
                .insertSQLEntry(entry)
                .map(Optional::ofNullable);
    }

    @Override
    @PrincipalLogging
    @SQL(LanguageTable.UPDATE_DICTIONARY_LANGUAGE_WHERE_ID_$1_RETURNING_S)
    public Uni<Optional<Long>> update(LanguageTable entry) {
        return super
                .updateSQL(entry)
                .map(Optional::ofNullable);
    }

    @Override
    @PrincipalLogging
    @SQL(LanguageTable.UPDATE_DICTIONARY_LANGUAGE_WHERE_ID_$1_RETURNING_S)
    public Uni<Optional<LanguageTable>> updateEntry(LanguageTable entry) {
        return super
                .updateSQLEntry(entry)
                .map(Optional::ofNullable);
    }
}
