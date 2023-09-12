/*
 * This file was last modified at 2023.11.19 16:20 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * StanzaViewDao.java
 * $Id$
 */

package su.svn.daybook.domain.dao;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import su.svn.daybook.annotations.PrincipalLogging;
import su.svn.daybook.annotations.SQL;
import su.svn.daybook.domain.model.StanzaView;

import java.util.Optional;

@ApplicationScoped
public class StanzaViewDao extends AbstractViewDao<Long, StanzaView> implements DaoViewIface<Long, StanzaView> {

    StanzaViewDao() {
        super(StanzaView.ID, r -> r.getLong(StanzaView.ID), StanzaView::from);
    }

    @PrincipalLogging
    @SQL(StanzaView.COUNT_DICTIONARY_STANZA_VIEW)
    public Uni<Optional<Long>> count() {
        return super.countSQL().map(Optional::ofNullable);
    }

    @SQL(StanzaView.SELECT_ALL_FROM_DICTIONARY_STANZA_VIEW_ORDER_BY_S)
    public Multi<StanzaView> findAll() {
        return super.findAllSQL();
    }

    @PrincipalLogging
    @SQL(StanzaView.SELECT_FROM_DICTIONARY_STANZA_VIEW_WHERE_ID_$1)
    public Uni<Optional<StanzaView>> findById(Long id) {
        return super
                .findByIdSQL(id)
                .map(Optional::ofNullable);
    }

    @PrincipalLogging
    @SQL(StanzaView.SELECT_FROM_DICTIONARY_STANZA_VIEW_WHERE_NAME_$1)
    public Multi<StanzaView> findByName(String name) {
        return super.findBy("findByName", name);
    }

    @PrincipalLogging
    @SQL(StanzaView.SELECT_FROM_DICTIONARY_STANZA_VIEW_WHERE_PARENT_ID_$1)
    public Multi<StanzaView> findByParentId(Long parentId) {
        return super.findBy("findByParentId", parentId);
    }

    @SQL(StanzaView.SELECT_ALL_FROM_DICTIONARY_STANZA_VIEW_ORDER_BY_S_OFFSET_$1_LIMIT_$2)
    public Multi<StanzaView> findRange(long offset, long limit) {
        return super.findRangeSQL(offset, limit);
    }
}
