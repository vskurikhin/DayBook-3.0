/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * WordDao.java
 * $Id$
 */

package su.svn.daybook.domain.dao;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import su.svn.daybook.annotations.Logged;
import su.svn.daybook.annotations.SQL;
import su.svn.daybook.domain.model.WordTable;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class WordDao extends AbstractDao<String, WordTable> {

    WordDao() {
        super(r -> r.getString(WordTable.ID), WordTable::from);
    }

    @Logged
    @SQL(WordTable.COUNT_DICTIONARY_WORD)
    public Uni<Optional<Long>> count() {
        return super.countSQL().map(Optional::ofNullable);
    }

    @Logged
    @SQL(WordTable.DELETE_FROM_DICTIONARY_WORD_WHERE_ID_$1)
    public Uni<Optional<String>> delete(String id) {
        return super.deleteSQL(id).map(Optional::ofNullable);
    }

    @Logged
    @SQL(WordTable.SELECT_ALL_FROM_DICTIONARY_WORD_ORDER_BY_ID_ASC)
    public Multi<WordTable> findAll() {
        return super.findAllSQL();
    }

    @Logged
    @SQL(WordTable.SELECT_FROM_DICTIONARY_WORD_WHERE_ID_$1)
    public Uni<Optional<WordTable>> findById(String id) {
        return super.findByIdSQL(id).map(Optional::ofNullable);
    }

    public Uni<Optional<WordTable>> findByWord(String word) {
        return findById(word);
    }

    @Logged
    @SQL(WordTable.SELECT_ALL_FROM_DICTIONARY_WORD_ORDER_BY_WORD_ASC_OFFSET_LIMIT)
    public Multi<WordTable> findRange(long offset, long limit) {
        return super.findRangeSQL(offset, limit);
    }

    @Logged
    @SQL(WordTable.INSERT_INTO_DICTIONARY_WORD)
    public Uni<Optional<String>> insert(WordTable entry) {
        return super.insertSQL(entry).map(Optional::ofNullable);
    }

    @Logged
    @SQL(WordTable.UPDATE_DICTIONARY_WORD_WHERE_ID_$1)
    public Uni<Optional<String>> update(WordTable entry) {
        return super.updateSQL(entry).map(Optional::ofNullable);
    }
}
