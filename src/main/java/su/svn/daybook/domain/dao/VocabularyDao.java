/*
 * This file was last modified at 2023.01.22 18:04 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * VocabularyDao.java
 * $Id$
 */

package su.svn.daybook.domain.dao;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.Tuple;
import su.svn.daybook.annotations.PrincipalLogging;
import su.svn.daybook.annotations.SQL;
import su.svn.daybook.domain.model.VocabularyTable;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class VocabularyDao extends AbstractDao<Long, VocabularyTable> implements DaoIface<Long, VocabularyTable> {

    VocabularyDao() {
        super(VocabularyTable.ID, r -> r.getLong(VocabularyTable.ID), VocabularyTable::from);
    }

    @Override
    @PrincipalLogging
    @SQL(VocabularyTable.COUNT_DICTIONARY_VOCABULARY)
    public Uni<Optional<Long>> count() {
        return super.countSQL().map(Optional::ofNullable);
    }

    @Override
    @PrincipalLogging
    @SQL(VocabularyTable.DELETE_FROM_DICTIONARY_VOCABULARY_WHERE_ID_$1_RETURNING_S)
    public Uni<Optional<Long>> delete(Long id) {
        return super.deleteSQL(id).map(Optional::ofNullable);
    }

    @Override
    @SQL(VocabularyTable.SELECT_ALL_FROM_DICTIONARY_VOCABULARY_ORDER_BY_S)
    public Multi<VocabularyTable> findAll() {
        return super.findAllSQL();
    }

    @Override
    @PrincipalLogging
    @SQL(VocabularyTable.SELECT_FROM_DICTIONARY_VOCABULARY_WHERE_ID_$1)
    public Uni<Optional<VocabularyTable>> findById(Long id) {
        return super.findByIdSQL(id).map(Optional::ofNullable);
    }

    @PrincipalLogging
    @SQL(VocabularyTable.SELECT_FROM_DICTIONARY_VOCABULARY_WHERE_KEY_$1)
    public Multi<VocabularyTable> findByWord(String word) {
        return client
                .preparedQuery(VocabularyTable.SELECT_FROM_DICTIONARY_VOCABULARY_WHERE_KEY_$1)
                .execute(Tuple.of(word))
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .map(VocabularyTable::from);
    }

    @SQL(VocabularyTable.SELECT_FROM_DICTIONARY_VOCABULARY_WHERE_VALUE_$1)
    public Multi<VocabularyTable> findByValue(String value) {
        return super.findByValueSQL(value);
    }

    @Override
    @PrincipalLogging
    @SQL(VocabularyTable.SELECT_ALL_FROM_DICTIONARY_VOCABULARY_ORDER_BY_S_OFFSET_$1_LIMIT_$2)
    public Multi<VocabularyTable> findRange(long offset, long limit) {
        return super.findRangeSQL(offset, limit);
    }

    @Override
    @PrincipalLogging
    @SQL
    public Uni<Optional<Long>> insert(VocabularyTable entry) {
        return super.insertSQL(entry).map(Optional::ofNullable);
    }

    @Override
    @PrincipalLogging
    @SQL
    public Uni<Optional<VocabularyTable>> insertEntry(VocabularyTable entry) {
        return super.insertSQLEntry(entry).map(Optional::ofNullable);
    }

    @Override
    @PrincipalLogging
    @SQL(VocabularyTable.UPDATE_DICTIONARY_VOCABULARY_WHERE_ID_$1_RETURNING_S)
    public Uni<Optional<Long>> update(VocabularyTable entry) {
        return super.updateSQL(entry).map(Optional::ofNullable);
    }

    @Override
    @PrincipalLogging
    @SQL(VocabularyTable.UPDATE_DICTIONARY_VOCABULARY_WHERE_ID_$1_RETURNING_S)
    public Uni<Optional<VocabularyTable>> updateEntry(VocabularyTable entry) {
        return super.updateSQLEntry(entry).map(Optional::ofNullable);
    }
}
