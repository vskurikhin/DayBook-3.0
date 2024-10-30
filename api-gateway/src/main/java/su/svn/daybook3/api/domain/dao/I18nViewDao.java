/*
 * This file was last modified at 2024-10-30 17:26 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * I18nViewDao.java
 * $Id$
 */

package su.svn.daybook3.api.domain.dao;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import su.svn.daybook3.annotations.SQL;
import su.svn.daybook3.api.annotations.PrincipalLogging;
import su.svn.daybook3.api.domain.model.I18nView;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class I18nViewDao extends AbstractViewDao<Long, I18nView> implements DaoViewIface<Long, I18nView> {

    I18nViewDao() {
        super(I18nView.ID, r -> r.getLong(I18nView.ID), I18nView::from);
    }

    @PrincipalLogging
    @SQL(I18nView.COUNT_DICTIONARY_I18N_VIEW)
    public Uni<Optional<Long>> count() {
        return super.countSQL().map(Optional::ofNullable);
    }

    @SQL(I18nView.SELECT_ALL_FROM_DICTIONARY_I18N_VIEW_ORDER_BY_S)
    public Multi<I18nView> findAll() {
        return super.findAllSQL();
    }

    @PrincipalLogging
    @SQL(I18nView.SELECT_FROM_DICTIONARY_I18N_VIEW_WHERE_ID_$1)
    public Uni<Optional<I18nView>> findById(Long id) {
        return super
                .findByIdSQL(id)
                .map(Optional::ofNullable);
    }

    @PrincipalLogging
    @SQL(I18nView.SELECT_FROM_DICTIONARY_I18N_VIEW_WHERE_LANGUAGE_$1_MESSAGE_$2)
    public Uni<Optional<I18nView>> findByKey(String language, String message) {
        return super
                .findByKeySQL(List.of(language, message))
                .map(Optional::ofNullable);
    }

    @SQL(I18nView.SELECT_FROM_DICTIONARY_I18N_VIEW_WHERE_VALUE_$1)
    public Multi<I18nView> findByValue(String message) {
        return super.findByValueSQL(message);
    }

    @SQL(I18nView.SELECT_ALL_FROM_DICTIONARY_I18N_VIEW_ORDER_BY_S_OFFSET_$1_LIMIT_$2)
    public Multi<I18nView> findRange(long offset, long limit) {
        return super.findRangeSQL(offset, limit);
    }
}
