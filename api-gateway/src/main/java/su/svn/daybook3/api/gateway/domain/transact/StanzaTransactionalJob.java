/*
 * This file was last modified at 2024-10-30 08:35 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * StanzaTransactionalJob.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.domain.transact;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowIterator;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.lang3.tuple.Pair;
import org.jboss.logging.Logger;
import su.svn.daybook3.annotations.TransactionAction;
import su.svn.daybook3.annotations.TransactionActions;
import su.svn.daybook3.api.gateway.domain.model.SettingTable;
import su.svn.daybook3.api.gateway.domain.model.StanzaTable;
import su.svn.daybook3.api.gateway.domain.transact.one_to_many.AbstractOneToManyJob;
import su.svn.daybook3.api.gateway.models.Constants;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@ApplicationScoped
public class StanzaTransactionalJob extends AbstractOneToManyJob<Long, StanzaTable, Long, SettingTable> {

    private static final Logger LOG = Logger.getLogger(StanzaTransactionalJob.class);

    public StanzaTransactionalJob() {
        super(LOG);
    }

    @Override
    @TransactionActions({
            @TransactionAction(
                    value = """
                            INSERT INTO dictionary.stanza
                             (id, name, description, parent_id, user_name, enabled, visible, flags)
                                VALUES($1, $2, $3, $4, $5, $6, $7, $8)
                                ON CONFLICT (id) DO
                                   UPDATE SET
                                    name = $2,
                                    description = $3,
                                    parent_id = $4,
                                    enabled = $6,
                                    visible = $7,
                                    flags = $8
                              RETURNING id;
                            """,
                    name = Constants.UPSERT_MAIN_TABLE),
            @TransactionAction(
                    value = """
                            UPDATE dictionary.setting
                               SET stanza_id = DEFAULT
                             WHERE stanza_id = $1 AND NOT (id = ANY ($2))
                            """,
                    name = Constants.CLEAR_RELATION),
            @TransactionAction(
                    value = """
                            UPDATE dictionary.setting
                               SET stanza_id = $1
                             WHERE id = ANY ($2)
                            """,
                    name = Constants.UPDATE_RELATION),
    })
    public Uni<List<Long>> upsert(Collection<Pair<StanzaTable, Collection<SettingTable>>> collection) {
        return super.doUpsert(collection);
    }

    @Override
    @TransactionActions({
            @TransactionAction(
                    value = """
                            UPDATE dictionary.setting
                               SET stanza_id = DEFAULT
                             WHERE stanza_id = $1
                            """,
                    name = Constants.CLEAR_ALL_HAS_RELATION_BY_ID),
            @TransactionAction(name = Constants.DELETE_MAIN),
    })
    public Uni<Optional<Long>> delete(@Nonnull StanzaTable table) {
        return super.doDelete(table);
    }

    @Override
    protected Function<RowIterator<Row>, Optional<?>> iteratorNextMapper(String actionName) {
        return switch (actionName) {
            case Constants.INSERT_INTO_MAIN,
                 Constants.UPDATE_MAIN_TABLE,
                 Constants.UPSERT_MAIN_TABLE,
                 Constants.DELETE_MAIN -> iterator ->
                    iterator.hasNext()
                            ? Optional.of(iterator.next().getLong(StanzaTable.ID))
                            : Optional.empty();
            default -> throw new IllegalStateException("Unexpected value: " + actionName);
        };
    }

    @Override
    protected Function<Optional<?>, Optional<Long>> castOptionalMainId() {
        return o -> o.flatMap(l -> (l instanceof Long result) ? Optional.of(result) : Optional.empty());
    }

    @Override
    protected Function<Optional<?>, Optional<Long>> castOptionalSubId() {
        return o -> o.flatMap(l -> (l instanceof Long result) ? Optional.of(result) : Optional.empty());
    }
}
