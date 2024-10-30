/*
 * This file was last modified at 2024-10-31 13:11 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * SessionDao.java
 * $Id$
 */

package su.svn.daybook3.api.domain.dao;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import su.svn.daybook3.annotations.SQL;
import su.svn.daybook3.api.annotations.PrincipalLogging;
import su.svn.daybook3.api.domain.model.SessionTable;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class SessionDao extends AbstractDao<UUID, SessionTable> {

    SessionDao() {
        super(SessionTable.ID, r -> r.getUUID(SessionTable.ID), SessionTable::from);
    }

    @PrincipalLogging
    @SQL(SessionTable.COUNT_SECURITY_SESSION)
    public Uni<Optional<Long>> count() {
        return super.countSQL().map(Optional::ofNullable);
    }

    @PrincipalLogging
    @SQL(SessionTable.DELETE_FROM_SECURITY_SESSION_WHERE_ID_$1)
    public Uni<Optional<UUID>> delete(UUID id) {
        return super
                .deleteSQL(id)
                .map(Optional::ofNullable);
    }

    @SQL(SessionTable.SELECT_ALL_FROM_SECURITY_SESSION_ORDER_BY_ID_ASC)
    public Multi<SessionTable> findAll() {
        return super.findAllSQL();
    }

    @PrincipalLogging
    @SQL(SessionTable.SELECT_FROM_SECURITY_SESSION_WHERE_ID_$1)
    public Uni<Optional<SessionTable>> findById(UUID id) {
        return super
                .findByIdSQL(id)
                .map(Optional::ofNullable);
    }

    @SQL(SessionTable.SELECT_FROM_SECURITY_SESSION_WHERE_USER_NAME_$1)
    public Multi<SessionTable> findByValue(String username) {
        return super.findByValueSQL(username);
    }

    public Multi<SessionTable> findByName(String username) {
        return findByValue(username);
    }

    @PrincipalLogging
    @SQL(SessionTable.SELECT_ALL_FROM_SECURITY_SESSION_ORDER_BY_ID_ASC_OFFSET_LIMIT)
    public Multi<SessionTable> findRange(long offset, long limit) {
        return super.findRangeSQL(offset, limit);
    }

    @PrincipalLogging
    @SQL
    public Uni<Optional<UUID>> insert(SessionTable entry) {
        return super
                .insertSQL(entry)
                .map(Optional::ofNullable);
    }

    @PrincipalLogging
    @SQL(SessionTable.UPDATE_SECURITY_SESSION_WHERE_ID_$1)
    public Uni<Optional<UUID>> update(SessionTable entry) {
        return super
                .updateSQL(entry)
                .map(Optional::ofNullable);
    }
}
