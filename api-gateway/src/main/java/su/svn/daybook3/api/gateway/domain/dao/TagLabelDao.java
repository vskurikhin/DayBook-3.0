/*
 * This file was last modified at 2024-05-14 21:36 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * TagLabelDao.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.domain.dao;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import su.svn.daybook3.api.gateway.annotations.PrincipalLogging;
import su.svn.daybook3.api.gateway.annotations.SQL;
import su.svn.daybook3.api.gateway.domain.model.TagLabelTable;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class TagLabelDao extends AbstractDao<String, TagLabelTable> implements DaoIface<String, TagLabelTable> {

    TagLabelDao() {
        super(TagLabelTable.ID, r -> r.getString(TagLabelTable.ID), TagLabelTable::from);
    }

    @Override
    @PrincipalLogging
    @SQL(TagLabelTable.COUNT_DICTIONARY_TAG_LABEL)
    public Uni<Optional<Long>> count() {
        return super.countSQL().map(Optional::ofNullable);
    }

    @Override
    @PrincipalLogging
    @SQL(TagLabelTable.DELETE_FROM_DICTIONARY_TAG_LABEL_WHERE_ID_$1_RETURNING_S)
    public Uni<Optional<String>> delete(String id) {
        return super
                .deleteSQL(id)
                .map(Optional::ofNullable);
    }

    @Override
    @SQL(TagLabelTable.SELECT_ALL_FROM_DICTIONARY_TAG_LABEL_ORDER_BY_S)
    public Multi<TagLabelTable> findAll() {
        return super.findAllSQL();
    }

    @Override
    @PrincipalLogging
    @SQL(TagLabelTable.SELECT_FROM_DICTIONARY_TAG_LABEL_WHERE_ID_$1)
    public Uni<Optional<TagLabelTable>> findById(String id) {
        return super
                .findByIdSQL(id)
                .map(Optional::ofNullable);
    }

    @PrincipalLogging
    @SQL(TagLabelTable.SELECT_FROM_DICTIONARY_TAG_LABEL_WHERE_KEY_$1)
    public Uni<Optional<TagLabelTable>> findByKey(String label) {
        return super
                .findByKeySQL(label)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<TagLabelTable>> findByLabel(String label) {
        return findByKey(label);
    }

    @Override
    @SQL(TagLabelTable.SELECT_ALL_FROM_DICTIONARY_TAG_LABEL_ORDER_BY_S_OFFSET_$1_LIMIT_$2)
    public Multi<TagLabelTable> findRange(long offset, long limit) {
        return super.findRangeSQL(offset, limit);
    }

    @Override
    @PrincipalLogging
    @SQL
    public Uni<Optional<String>> insert(TagLabelTable entry) {
        return super
                .insertSQL(entry)
                .map(Optional::ofNullable);
    }

    @Override
    @PrincipalLogging
    @SQL
    public Uni<Optional<TagLabelTable>> insertEntry(TagLabelTable entry) {
        return super
                .insertSQLEntry(entry)
                .map(Optional::ofNullable);
    }

    @Override
    @PrincipalLogging
    @SQL(TagLabelTable.UPDATE_DICTIONARY_TAG_LABEL_WHERE_ID_$1_RETURNING_S)
    public Uni<Optional<String>> update(TagLabelTable entry) {
        return super
                .updateSQL(entry)
                .map(Optional::ofNullable);
    }

    @Override
    @PrincipalLogging
    @SQL(TagLabelTable.UPDATE_DICTIONARY_TAG_LABEL_WHERE_ID_$1_RETURNING_S)
    public Uni<Optional<TagLabelTable>> updateEntry(TagLabelTable entry) {
        return super
                .updateSQLEntry(entry)
                .map(Optional::ofNullable);
    }
}
