/*
 * This file was last modified at 2023.11.19 16:20 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * StanzaTransactionalJob.java
 * $Id$
 */

package su.svn.daybook.domain.transact;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowIterator;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.lang3.tuple.Pair;
import org.jboss.logging.Logger;
import su.svn.daybook.annotations.TransactionAction;
import su.svn.daybook.annotations.TransactionActions;
import su.svn.daybook.domain.model.SettingTable;
import su.svn.daybook.domain.model.StanzaTable;
import su.svn.daybook.domain.transact.one_to_many.AbstractOneToManyJob;
import su.svn.daybook.models.Constants;

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
                             (id, name, description, parent_id, user_name, visible, flags)
                                VALUES(%s, $2, $3, $4, $5, $7, $8)
                                ON CONFLICT (name) DO
                                   UPDATE SET
                                    name = $2,
                                    description = $3,
                                    parent_id = $4,
                                    user_name = $5,
                                    enabled = true,
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
                             WHERE stanza_id = ANY ($1)
                            """,
                    name = Constants.CLEAR_RELATION),
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
                            ? Optional.of(iterator.next().getUUID(StanzaTable.ID))
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
