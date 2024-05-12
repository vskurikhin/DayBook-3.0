/*
 * This file was last modified at 2024-05-14 21:25 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * WordDao.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.domain.dao;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import su.svn.daybook3.api.gateway.annotations.PrincipalLogging;
import su.svn.daybook3.api.gateway.annotations.SQL;
import su.svn.daybook3.api.gateway.domain.model.WordTable;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class WordDao extends AbstractDao<String, WordTable> implements DaoIface<String, WordTable> {

    WordDao() {
        super(WordTable.ID, r -> r.getString(WordTable.ID), WordTable::from);
    }

    @PrincipalLogging
    @SQL(WordTable.COUNT_DICTIONARY_WORD)
    public Uni<Optional<Long>> count() {
        return super.countSQL().map(Optional::ofNullable);
    }

    @PrincipalLogging
    @SQL(WordTable.DELETE_FROM_DICTIONARY_WORD_WHERE_ID_$1)
    public Uni<Optional<String>> delete(String id) {
        return super
                .deleteSQL(id)
                .map(Optional::ofNullable);
    }

    @SQL(WordTable.SELECT_ALL_FROM_DICTIONARY_WORD_ORDER_BY_ID_ASC)
    public Multi<WordTable> findAll() {
        return super.findAllSQL();
    }

    @PrincipalLogging
    @SQL(WordTable.SELECT_FROM_DICTIONARY_WORD_WHERE_ID_$1)
    public Uni<Optional<WordTable>> findById(String id) {
        return super
                .findByIdSQL(id)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<WordTable>> findByWord(String word) {
        return findById(word);
    }

    @SQL(WordTable.SELECT_ALL_FROM_DICTIONARY_WORD_ORDER_BY_WORD_ASC_OFFSET_LIMIT)
    public Multi<WordTable> findRange(long offset, long limit) {
        return super.findRangeSQL(offset, limit);
    }

    @PrincipalLogging
    @SQL(WordTable.INSERT_INTO_DICTIONARY_WORD_RETURNING_S)
    public Uni<Optional<String>> insert(WordTable entry) {
        return super
                .insertSQL(entry)
                .map(Optional::ofNullable);
    }

    @Override
    @SQL(WordTable.INSERT_INTO_DICTIONARY_WORD_RETURNING_S)
    public Uni<Optional<WordTable>> insertEntry(WordTable entry) {
        return super
                .insertSQLEntry(entry)
                .map(Optional::ofNullable);
    }

    @PrincipalLogging
    @SQL(WordTable.UPDATE_DICTIONARY_WORD_WHERE_ID_$1_RETURNING_S)
    public Uni<Optional<String>> update(WordTable entry) {
        return super
                .updateSQL(entry)
                .map(Optional::ofNullable);
    }

    @Override
    @SQL(WordTable.UPDATE_DICTIONARY_WORD_WHERE_ID_$1_RETURNING_S)
    public Uni<Optional<WordTable>> updateEntry(WordTable entry) {
        return super
                .updateSQLEntry(entry)
                .map(Optional::ofNullable);
    }
}
