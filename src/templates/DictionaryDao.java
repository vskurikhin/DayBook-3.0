/*
 * This file was last modified at 2023.09.07 16:35 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * @Name@Dao.java
 * $Id$
 */

package su.svn.daybook.domain.dao;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import su.svn.daybook.annotations.PrincipalLogging;
import su.svn.daybook.annotations.SQL;
import su.svn.daybook.domain.model.@Name@Table;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class @Name@Dao extends AbstractDao<@IdType@, @Name@Table> implements DaoIface<@IdType@, @Name@Table> {

    @Name@Dao() {
        super(@Name@Table.ID, r -> r.get@IdType@(@Name@Table.ID), @Name@Table::from);
    }

    @Override
    @PrincipalLogging
    @SQL(@Name@Table.COUNT_@SCHEMA@_@TABLE@)
    public Uni<Optional<Long>> count() {
        return super.countSQL().map(Optional::ofNullable);
    }

    @Override
    @PrincipalLogging
    @SQL(@Name@Table.DELETE_FROM_@SCHEMA@_@TABLE@_WHERE_ID_$1_RETURNING_S)
    public Uni<Optional<@IdType@>> delete(@IdType@ id) {
        return super.deleteSQL(id).map(Optional::ofNullable);
    }

    @Override
    @SQL(@Name@Table.SELECT_ALL_FROM_@SCHEMA@_@TABLE@_ORDER_BY_S)
    public Multi<@Name@Table> findAll() {
        return super.findAllSQL();
    }

    @Override
    @PrincipalLogging
    @SQL(@Name@Table.SELECT_FROM_@SCHEMA@_@TABLE@_WHERE_ID_$1)
    public Uni<Optional<@Name@Table>> findById(@IdType@ id) {
        return super.findByIdSQL(id).map(Optional::ofNullable);
    }

    @PrincipalLogging
    @SQL(@Name@Table.SELECT_FROM_@SCHEMA@_@TABLE@_WHERE_KEY_$1)
    public Uni<Optional<@Name@Table>> findByKey(@KType@ @key@) {
        return super.findByKeySQL(@key@).map(Optional::ofNullable);
    }

    @SQL(@Name@Table.SELECT_FROM_@SCHEMA@_@TABLE@_WHERE_VALUE_$1)
    public Multi<@Name@Table> findBy@Value@(@VType@ @value@) {
        return super.findByValueSQL(@value@);
    }

    @Override
    @SQL(@Name@Table.SELECT_ALL_FROM_@SCHEMA@_@TABLE@_ORDER_BY_S_OFFSET_$1_LIMIT_$2)
    public Multi<@Name@Table> findRange(long offset, long limit) {
        return super.findRangeSQL(offset, limit);
    }

    @Override
    @PrincipalLogging
    @SQL
    public Uni<Optional<@IdType@>> insert(@Name@Table entry) {
        return super.insertSQL(entry).map(Optional::ofNullable);
    }

    @Override
    @PrincipalLogging
    @SQL
    public Uni<Optional<@Name@Table>> insertEntry(@Name@Table entry) {
        return super.insertSQLEntry(entry).map(Optional::ofNullable);
    }

    @Override
    @PrincipalLogging
    @SQL(@Name@Table.UPDATE_@SCHEMA@_@TABLE@_WHERE_ID_$1_RETURNING_S)
    public Uni<Optional<@IdType@>> update(@Name@Table entry) {
        return super.updateSQL(entry).map(Optional::ofNullable);
    }

    @Override
    @PrincipalLogging
    @SQL(@Name@Table.UPDATE_@SCHEMA@_@TABLE@_WHERE_ID_$1_RETURNING_S)
    public Uni<Optional<@Name@Table>> updateEntry(@Name@Table entry) {
        return super.updateSQLEntry(entry).map(Optional::ofNullable);
    }
}
