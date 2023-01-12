/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * @Name@Dao.java
 * $Id$
 */

package su.svn.daybook.domain.dao;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import su.svn.daybook.annotations.Logged;
import su.svn.daybook.annotations.SQL;
import su.svn.daybook.domain.model.@Name@Table;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class @Name@Dao extends AbstractDao<@IdType@, @Name@Table> {

    @Name@Dao() {
        super(r -> r.get@IdType@(@Name@Table.ID), @Name@Table::from);
    }

    @Logged
    @SQL(@Name@Table.COUNT_@SCHEMA@_@TABLE@)
    public Uni<Optional<Long>> count() {
        return super.countSQL().map(Optional::ofNullable);
    }

    @Logged
    @SQL(@Name@Table.DELETE_FROM_@SCHEMA@_@TABLE@_WHERE_ID_$1)
    public Uni<Optional<@IdType@>> delete(@IdType@ id) {
        return super.deleteSQL(id).map(Optional::ofNullable);
    }

    @Logged
    @SQL(@Name@Table.SELECT_ALL_FROM_@SCHEMA@_@TABLE@_ORDER_BY_ID_ASC)
    public Multi<@Name@Table> findAll() {
        return super.findAllSQL();
    }

    @Logged
    @SQL(@Name@Table.SELECT_FROM_@SCHEMA@_@TABLE@_WHERE_ID_$1)
    public Uni<Optional<@Name@Table>> findById(@IdType@ id) {
        return super.findByIdSQL(id).map(Optional::ofNullable);
    }

    @Logged
    @SQL(@Name@Table.SELECT_ALL_FROM_@SCHEMA@_@TABLE@_ORDER_BY_ID_ASC_OFFSET_LIMIT)
    public Multi<@Name@Table> findRange(long offset, long limit) {
        return super.findRangeSQL(offset, limit);
    }

    @Logged
    @SQL
    public Uni<Optional<@IdType@>> insert(@Name@Table entry) {
        return super.insertSQL(entry).map(Optional::ofNullable);
    }

    @Logged
    @SQL(@Name@Table.UPDATE_@SCHEMA@_@TABLE@_WHERE_ID_$1)
    public Uni<Optional<@IdType@>> update(@Name@Table entry) {
        return super.updateSQL(entry).map(Optional::ofNullable);
    }
}
