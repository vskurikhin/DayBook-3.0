/*
 * This file was last modified at 2024.02.20 16:39 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * UserViewDao.java
 * $Id$
 */

package su.svn.daybook.domain.dao;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import su.svn.daybook.annotations.PrincipalLogging;
import su.svn.daybook.annotations.SQL;
import su.svn.daybook.domain.model.UserView;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UserViewDao extends AbstractViewDao<UUID, UserView> {

    UserViewDao() {
        super(UserView.ID, r -> r.getUUID(UserView.ID), UserView::from);
    }

    @PrincipalLogging
    @SQL(UserView.COUNT_SECURITY_USER_VIEW)
    public Uni<Optional<Long>> count() {
        return super.countSQL().map(Optional::ofNullable);
    }

    @SQL(UserView.SELECT_ALL_FROM_SECURITY_USER_VIEW_ORDER_BY_USER_NAME_ASC)
    public Multi<UserView> findAll() {
        return super.findAllSQL();
    }

    @PrincipalLogging
    @SQL(UserView.SELECT_FROM_SECURITY_USER_VIEW_WHERE_ID_$1)
    public Uni<Optional<UserView>> findById(UUID id) {
        return super
                .findByIdSQL(id)
                .map(Optional::ofNullable);
    }

    @PrincipalLogging
    @SQL(UserView.SELECT_FROM_SECURITY_USER_VIEW_WHERE_USER_NAME_$1)
    public Uni<Optional<UserView>> findByKey(String username) {
        return super
                .findByKeySQL(username)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<UserView>> findByUserName(String username) {
        return findByKey(username);
    }

    @SQL(UserView.SELECT_ALL_FROM_SECURITY_USER_VIEW_ORDER_BY_USER_NAME_ASC_OFFSET_LIMIT)
    public Multi<UserView> findRange(long offset, long limit) {
        return super.findRangeSQL(offset, limit);
    }
}
