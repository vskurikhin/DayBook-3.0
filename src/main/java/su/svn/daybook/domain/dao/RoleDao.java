/*
 * This file was last modified at 2023.09.06 17:04 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * RoleDao.java
 * $Id$
 */

package su.svn.daybook.domain.dao;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import su.svn.daybook.annotations.PrincipalLogging;
import su.svn.daybook.annotations.SQL;
import su.svn.daybook.domain.model.RoleTable;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class RoleDao extends AbstractDao<UUID, RoleTable> {

    @Inject
    io.vertx.mutiny.pgclient.PgPool client;

    RoleDao() {
        super(RoleTable.ID, r -> r.getUUID(RoleTable.ID), RoleTable::from);
    }

    @PrincipalLogging
    @SQL(RoleTable.COUNT_SECURITY_ROLE)
    public Uni<Optional<Long>> count() {
        return super.countSQL().map(Optional::ofNullable);
    }

    @PrincipalLogging
    @SQL(RoleTable.DELETE_FROM_SECURITY_ROLE_WHERE_ID_$1)
    public Uni<Optional<UUID>> delete(UUID id) {
        return super.deleteSQL(id).map(Optional::ofNullable);
    }

    @SQL(RoleTable.SELECT_ALL_FROM_SECURITY_ROLE_ORDER_BY_ID_ASC)
    public Multi<RoleTable> findAll() {
        return super.findAllSQL();
    }

    @PrincipalLogging
    @SQL(RoleTable.SELECT_FROM_SECURITY_ROLE_WHERE_ID_$1)
    public Uni<Optional<RoleTable>> findById(UUID id) {
        return super.findByIdSQL(id).map(Optional::ofNullable);
    }

    @SQL(RoleTable.SELECT_ALL_FROM_SECURITY_ROLE_ORDER_BY_ID_ASC_OFFSET_LIMIT)
    public Multi<RoleTable> findRange(long offset, long limit) {
        return super.findRangeSQL(offset, limit);
    }

    @PrincipalLogging
    @SQL
    public Uni<Optional<UUID>> insert(RoleTable entry) {
        return super.insertSQL(entry).map(Optional::ofNullable);
    }

    @PrincipalLogging
    @SQL(RoleTable.UPDATE_SECURITY_ROLE_WHERE_ID_$1)
    public Uni<Optional<UUID>> update(RoleTable entry) {
        return super.updateSQL(entry).map(Optional::ofNullable);
    }
}
