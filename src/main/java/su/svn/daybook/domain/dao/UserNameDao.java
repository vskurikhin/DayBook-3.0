/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * UserNameDao.java
 * $Id$
 */

package su.svn.daybook.domain.dao;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import su.svn.daybook.annotations.PrincipalLogging;
import su.svn.daybook.annotations.SQL;
import su.svn.daybook.domain.model.UserNameTable;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UserNameDao extends AbstractDao<UUID, UserNameTable> {

    UserNameDao() {
        super(UserNameTable.ID, r -> r.getUUID(UserNameTable.ID), UserNameTable::from);
    }

    @PrincipalLogging
    @SQL(UserNameTable.COUNT_SECURITY_USER_NAME)
    public Uni<Optional<Long>> count() {
        return super.countSQL().map(Optional::ofNullable);
    }

    @PrincipalLogging
    @SQL(UserNameTable.DELETE_FROM_SECURITY_USER_NAME_WHERE_ID_$1)
    public Uni<Optional<UUID>> delete(UUID id) {
        return super.deleteSQL(id).map(Optional::ofNullable);
    }

    @SQL(UserNameTable.SELECT_ALL_FROM_SECURITY_USER_NAME_ORDER_BY_ID_ASC)
    public Multi<UserNameTable> findAll() {
        return super.findAllSQL();
    }

    @PrincipalLogging
    @SQL(UserNameTable.SELECT_FROM_SECURITY_USER_NAME_WHERE_ID_$1)
    public Uni<Optional<UserNameTable>> findById(UUID id) {
        return super.findByIdSQL(id).map(Optional::ofNullable);
    }

    @SQL(UserNameTable.SELECT_ALL_FROM_SECURITY_USER_NAME_ORDER_BY_ID_ASC_OFFSET_LIMIT)
    public Multi<UserNameTable> findRange(long offset, long limit) {
        return super.findRangeSQL(offset, limit);
    }

    @PrincipalLogging
    @SQL
    public Uni<Optional<UUID>> insert(UserNameTable entry) {
        return super.insertSQL(entry).map(Optional::ofNullable);
    }

    @PrincipalLogging
    @SQL(UserNameTable.UPDATE_SECURITY_USER_NAME_WHERE_ID_$1)
    public Uni<Optional<UUID>> update(UserNameTable entry) {
        return super.updateSQL(entry).map(Optional::ofNullable);
    }
}
