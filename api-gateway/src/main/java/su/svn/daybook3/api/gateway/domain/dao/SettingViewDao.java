/*
 * This file was last modified at 2024-05-14 21:25 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * SettingViewDao.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.domain.dao;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import su.svn.daybook3.api.gateway.annotations.PrincipalLogging;
import su.svn.daybook3.api.gateway.annotations.SQL;
import su.svn.daybook3.api.gateway.domain.model.SettingView;

import java.util.Collections;
import java.util.Optional;

@ApplicationScoped
public class SettingViewDao extends AbstractViewDao<Long, SettingView> implements DaoViewIface<Long, SettingView> {

    SettingViewDao() {
        super(SettingView.ID, r -> r.getLong(SettingView.ID), SettingView::from);
    }

    @PrincipalLogging
    @SQL(SettingView.COUNT_DICTIONARY_SETTING_VIEW)
    public Uni<Optional<Long>> count() {
        return super.countSQL().map(Optional::ofNullable);
    }

    @SQL(SettingView.SELECT_ALL_FROM_DICTIONARY_SETTING_VIEW_ORDER_BY_S)
    public Multi<SettingView> findAll() {
        return super.findAllSQL();
    }

    @PrincipalLogging
    @SQL(SettingView.SELECT_FROM_DICTIONARY_SETTING_VIEW_WHERE_ID_$1)
    public Uni<Optional<SettingView>> findById(Long id) {
        return super
                .findByIdSQL(id)
                .map(Optional::ofNullable);
    }

    @PrincipalLogging
    @SQL(SettingView.SELECT_FROM_DICTIONARY_SETTING_VIEW_WHERE_VARIABLE_$1)
    public Uni<Optional<SettingView>> findByKey(String variable) {
        return super
                .findByKeySQL(Collections.singletonList(variable))
                .map(Optional::ofNullable);
    }

    @SQL(SettingView.SELECT_FROM_DICTIONARY_SETTING_VIEW_WHERE_VALUE_$1)
    public Multi<SettingView> findByValue(String value) {
        return super.findByValueSQL(value);
    }

    @SQL(SettingView.SELECT_ALL_FROM_DICTIONARY_SETTING_VIEW_ORDER_BY_S_OFFSET_$1_LIMIT_$2)
    public Multi<SettingView> findRange(long offset, long limit) {
        return super.findRangeSQL(offset, limit);
    }
}
