/*
 * This file was last modified at 2024-10-30 17:26 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * SettingDao.java
 * $Id$
 */

package su.svn.daybook3.api.domain.dao;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import su.svn.daybook3.annotations.SQL;
import su.svn.daybook3.api.annotations.PrincipalLogging;
import su.svn.daybook3.api.domain.model.SettingTable;

import java.util.Optional;

@ApplicationScoped
public class SettingDao extends AbstractDao<Long, SettingTable> implements DaoIface<Long, SettingTable> {

    SettingDao() {
        super(SettingTable.ID, r -> r.getLong(SettingTable.ID), SettingTable::from);
    }

    @Override
    @PrincipalLogging
    @SQL(SettingTable.COUNT_DICTIONARY_SETTING)
    public Uni<Optional<Long>> count() {
        return super.countSQL().map(Optional::ofNullable);
    }

    @Override
    @PrincipalLogging
    @SQL(SettingTable.DELETE_FROM_DICTIONARY_SETTING_WHERE_ID_$1_RETURNING_S)
    public Uni<Optional<Long>> delete(Long id) {
        return super
                .deleteSQL(id)
                .map(Optional::ofNullable);
    }

    @Override
    @SQL(SettingTable.SELECT_ALL_FROM_DICTIONARY_SETTING_ORDER_BY_S)
    public Multi<SettingTable> findAll() {
        return super.findAllSQL();
    }

    @Override
    @PrincipalLogging
    @SQL(SettingTable.SELECT_FROM_DICTIONARY_SETTING_WHERE_ID_$1)
    public Uni<Optional<SettingTable>> findById(Long id) {
        return super
                .findByIdSQL(id)
                .map(Optional::ofNullable);
    }

    @PrincipalLogging
    @SQL(SettingTable.SELECT_FROM_DICTIONARY_SETTING_WHERE_KEY_$1)
    public Uni<Optional<SettingTable>> findByKey(String variable) {
        return super
                .findByKeySQL(variable)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<SettingTable>> findByVariable(String value) {
        return this.findByKey(value);
    }

    @SQL(SettingTable.SELECT_FROM_DICTIONARY_SETTING_WHERE_VALUE_$1)
    public Multi<SettingTable> findByValue(String value) {
        return super.findByValueSQL(value);
    }

    @Override
    @SQL(SettingTable.SELECT_ALL_FROM_DICTIONARY_SETTING_ORDER_BY_S_OFFSET_$1_LIMIT_$2)
    public Multi<SettingTable> findRange(long offset, long limit) {
        return super.findRangeSQL(offset, limit);
    }

    @Override
    @PrincipalLogging
    @SQL
    public Uni<Optional<Long>> insert(SettingTable entry) {
        return super
                .insertSQL(entry)
                .map(Optional::ofNullable);
    }

    @Override
    @PrincipalLogging
    @SQL
    public Uni<Optional<SettingTable>> insertEntry(SettingTable entry) {
        return super
                .insertSQLEntry(entry)
                .map(Optional::ofNullable);
    }

    @Override
    @PrincipalLogging
    @SQL(SettingTable.UPDATE_DICTIONARY_SETTING_WHERE_ID_$1_RETURNING_S)
    public Uni<Optional<Long>> update(SettingTable entry) {
        return super
                .updateSQL(entry)
                .map(Optional::ofNullable);
    }

    @Override
    @PrincipalLogging
    @SQL(SettingTable.UPDATE_DICTIONARY_SETTING_WHERE_ID_$1_RETURNING_S)
    public Uni<Optional<SettingTable>> updateEntry(SettingTable entry) {
        return super
                .updateSQLEntry(entry)
                .map(Optional::ofNullable);
    }
}
