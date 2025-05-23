/*
 * This file was last modified at 2024-10-30 14:17 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * UserTransactionalJob.java
 * $Id$
 */

package su.svn.daybook3.auth.domain.transact;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowIterator;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;
import su.svn.daybook3.annotations.TransactionAction;
import su.svn.daybook3.annotations.TransactionActions;
import su.svn.daybook3.auth.domain.model.RoleTable;
import su.svn.daybook3.auth.domain.model.UserNameTable;
import su.svn.daybook3.auth.models.Constants;
import su.svn.daybook3.enums.TupleMapperEnum;
import su.svn.daybook3.transaction.many_to_many.AbstractManyToManyJob;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@ApplicationScoped
public class UserTransactionalJob extends AbstractManyToManyJob<UUID, UserNameTable, UUID, RoleTable, String, String> {

    private static final Logger LOG = Logger.getLogger(UserTransactionalJob.class);

    public UserTransactionalJob() {
        super(UserNameTable::userName, LOG);
    }

    @Override
    @TransactionActions({
            @TransactionAction(
                    value = """
                            SELECT count(*) FROM unnest($1::text[]) AS r(role)
                             WHERE role NOT IN (SELECT role FROM security.role);
                            """,
                    tupleMapper = TupleMapperEnum.StringTuple,
                    name = Constants.CHECK_NOT_IN_RELATIVE),
            @TransactionAction(name = Constants.INSERT_INTO_MAIN),
            @TransactionAction(
                    value = """
                            INSERT INTO security.user_has_roles
                             (user_name, role)
                              (SELECT username, role
                                 FROM (VALUES ($1, $2)) AS r(username, role)
                                WHERE (username, role) NOT IN
                                 (SELECT user_name, role FROM security.user_has_roles));
                            """,
                    name = Constants.INSERT_INTO_RELATION_BY_2_VALUES),
            @TransactionAction(
                    value = """
                            INSERT INTO security.user_has_roles
                             (user_name, role)
                              (SELECT user_name, role
                                 FROM (VALUES ($1, $2), ($3, $4)) AS r(user_name, role)
                                WHERE (user_name, role) NOT IN
                                 (SELECT user_name, role FROM security.user_has_roles));
                            """,
                    name = Constants.INSERT_INTO_RELATION_BY_4_VALUES),
            @TransactionAction(
                    value = """
                            DELETE FROM security.user_has_roles
                             WHERE (user_name, role) IN
                                   (SELECT user_name, role
                                      FROM security.user_has_roles
                                     WHERE user_name = $1
                                       AND NOT (role = ANY ($2)));
                            """,
                    name = Constants.CLEAR_HAS_RELATION),
            @TransactionAction(
                    value = """
                            DELETE FROM security.user_has_roles
                             WHERE user_name = $1
                            """,
                    name = Constants.CLEAR_ALL_HAS_RELATION_BY_FIELD),
    })
    public Uni<Optional<UUID>> insert(@Nonnull UserNameTable table, @Nonnull Collection<String> collection) {
        return super.doInsert(table, collection);
    }

    @Override
    @TransactionActions({
            @TransactionAction(
                    value = """
                            SELECT count(*) FROM unnest($1::text[]) AS r(role)
                             WHERE role NOT IN (SELECT role FROM security.role);
                            """,
                    tupleMapper = TupleMapperEnum.StringTuple,
                    name = Constants.CHECK_NOT_IN_RELATIVE),
            @TransactionAction(name = Constants.UPDATE_MAIN_TABLE),
            @TransactionAction(
                    value = """
                            INSERT INTO security.user_has_roles
                             (user_name, role)
                              (SELECT username, role
                                 FROM (VALUES ($1, $2)) AS r(username, role)
                                WHERE (username, role) NOT IN
                                 (SELECT user_name, role FROM security.user_has_roles));
                            """,
                    name = Constants.INSERT_INTO_RELATION_BY_2_VALUES),
            @TransactionAction(
                    value = """
                            INSERT INTO security.user_has_roles
                             (user_name, role)
                              (SELECT user_name, role
                                 FROM (VALUES ($1, $2), ($3, $4)) AS r(user_name, role)
                                WHERE (user_name, role) NOT IN
                                 (SELECT user_name, role FROM security.user_has_roles));
                            """,
                    name = Constants.INSERT_INTO_RELATION_BY_4_VALUES),
            @TransactionAction(
                    value = """
                            DELETE FROM security.user_has_roles
                             WHERE (user_name, role) IN
                                   (SELECT user_name, role
                                      FROM security.user_has_roles
                                     WHERE user_name = $1
                                       AND NOT (role = ANY ($2)));
                            """,
                    name = Constants.CLEAR_HAS_RELATION),
            @TransactionAction(
                    value = """
                            DELETE FROM security.user_has_roles
                             WHERE user_name = $1
                            """,
                    name = Constants.CLEAR_ALL_HAS_RELATION_BY_FIELD),
    })
    public Uni<Optional<UUID>> update(@Nonnull UserNameTable table, @Nonnull Collection<String> collection) {
        return super.doUpdate(table, collection);
    }

    @Override
    @TransactionActions({
            @TransactionAction(
                    value = """
                            DELETE FROM security.user_has_roles
                             WHERE user_name = $1
                            """,
                    name = Constants.CLEAR_ALL_HAS_RELATION_BY_FIELD),
            @TransactionAction(name = Constants.DELETE_MAIN),
    })
    public Uni<Optional<UUID>> delete(@Nonnull UserNameTable table) {
        return super.doDelete(table);
    }

    @Override
    protected Function<RowIterator<Row>, Optional<?>> iteratorNextMapper(String actionName) {
        return switch (actionName) {
            case Constants.INSERT_INTO_MAIN, Constants.UPDATE_MAIN_TABLE, Constants.DELETE_MAIN -> iterator ->
                    iterator.hasNext() ? Optional.of(iterator.next().getUUID(UserNameTable.ID)) : Optional.empty();
            default -> throw new IllegalStateException("Unexpected value: " + actionName);
        };
    }

    @Override
    protected Function<Optional<?>, Optional<UUID>> castOptionalMainId() {
        return o -> o.flatMap(l -> (l instanceof UUID result) ? Optional.of(result) : Optional.empty());
    }

    @Override
    protected Function<Optional<?>, Optional<UUID>> castOptionalRelativeId() {
        return o -> o.flatMap(l -> (l instanceof UUID result) ? Optional.of(result) : Optional.empty());
    }
}
