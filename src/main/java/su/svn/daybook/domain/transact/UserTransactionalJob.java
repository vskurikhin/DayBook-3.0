/*
 * This file was last modified at 2023.01.06 11:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * UserTransactionalJob2.java
 * $Id$
 */

package su.svn.daybook.domain.transact;

import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowIterator;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.model.UserNameTable;

import javax.annotation.Nonnull;
import javax.inject.Singleton;
import javax.ws.rs.core.Context;
import java.util.UUID;

@Singleton
public class UserTransactionalJob extends AbstractHasRelationJob<UUID, UserNameTable, String, String> {

    private static final Logger LOG = Logger.getLogger(UserTransactionalJob.class);

    public static final String COUNT_NOT_EXISTS_ROLES = """
            SELECT count(*) FROM unnest($1::text[]) AS r(role)
             WHERE role NOT IN (SELECT role FROM security.role);
            """;
    public static final String UPDATE_SECURITY_USER_HAS_ROLES_2 = """
            INSERT INTO security.user_has_roles
             (user_name, role)
              (SELECT username, role
                 FROM (VALUES ($1, $2)) AS r(username, role)
                WHERE (username, role) NOT IN
                 (SELECT user_name, role FROM security.user_has_roles));
            """;
    public static final String UPDATE_SECURITY_USER_HAS_ROLES_4 = """
            INSERT INTO security.user_has_roles
             (user_name, role)
              (SELECT user_name, role
                 FROM (VALUES ($1, $2), ($3, $4)) AS r(user_name, role)
                WHERE (user_name, role) NOT IN
                 (SELECT user_name, role FROM security.user_has_roles));
            """;
    public static final String CLEAR_SECURITY_USER_HAS_ROLES = """
            DELETE
            FROM security.user_has_roles
            WHERE (user_name, role) IN
                  (SELECT user_name, role
                   FROM security.user_has_roles
                   WHERE user_name = $1
                     AND NOT (role = ANY ($2)));
            """;
    public static final String DELETE_FROM_SECURITY_USER_HAS_ROLES_WHERE_USER_NAME_$1 = """
            DELETE FROM security.user_has_roles
             WHERE user_name = $1
            """;

    public UserTransactionalJob(@Context PgPool client) {
        super(client, LOG);
    }

    @Nonnull
    @Override
    protected String caseInsertSql(UserNameTable table) {
        return table.id() != null
                ? UserNameTable.INSERT_INTO_SECURITY_USER_NAME
                : UserNameTable.INSERT_INTO_SECURITY_USER_NAME_DEFAULT_ID;
    }

    @Override
    protected UUID getKeyByIteratorNext(RowIterator<Row> iterator) {
        return iterator.hasNext() ? iterator.next().getUUID(UserNameTable.ID) : null;
    }

    @Nonnull
    @Override
    protected String sqlCountOfNotExisted() {
        return COUNT_NOT_EXISTS_ROLES;
    }

    @Nonnull
    @Override
    protected String sqlClearHasRelation() {
        return CLEAR_SECURITY_USER_HAS_ROLES;
    }

    @Nonnull
    @Override
    protected String sqlDeleteFromTable() {
        return UserNameTable.DELETE_FROM_SECURITY_USER_NAME_WHERE_ID_$1;
    }

    @Nonnull
    @Override
    protected String sqlDeleteFromHasRelationByEntry() {
        return DELETE_FROM_SECURITY_USER_HAS_ROLES_WHERE_USER_NAME_$1;
    }

    @Nonnull
    @Override
    protected String sqlUpdateTable() {
        return UserNameTable.UPDATE_SECURITY_USER_NAME_WHERE_ID_$1;
    }

    @Nonnull
    @Override
    protected String sqlUpdateHasRelation2() {
        return UPDATE_SECURITY_USER_HAS_ROLES_2;
    }

    @Nonnull
    @Override
    protected String sqlUpdateHasRelation4() {
        return UPDATE_SECURITY_USER_HAS_ROLES_4;
    }
}
