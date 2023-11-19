/*
 * This file was last modified at 2023.11.19 18:33 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * SettingTransactionalJob.java
 * $Id$
 */

package su.svn.daybook.domain.transact;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowIterator;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;
import su.svn.daybook.annotations.TransactionAction;
import su.svn.daybook.annotations.TransactionActions;
import su.svn.daybook.domain.enums.IteratorNextMapperEnum;
import su.svn.daybook.domain.enums.TupleMapperEnum;
import su.svn.daybook.domain.model.SettingTable;
import su.svn.daybook.domain.model.ValueTypeTable;
import su.svn.daybook.domain.transact.one_to_one.AbstractOneToOneJob;
import su.svn.daybook.models.Constants;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

@ApplicationScoped
public class SettingTransactionalJob extends AbstractOneToOneJob<Long, SettingTable, Long, ValueTypeTable, String> {

    private static final Logger LOG = Logger.getLogger(SettingTransactionalJob.class);

    private static final BiFunction<SettingTable, Long, SettingTable> SETTING_TABLE_BUILDER
            = (t, id) -> t.toBuilder().valueTypeId(id).build();

    private static final Function<String, ValueTypeTable> VALUE_TYPE_TABLE_BUILDER
            = s -> ValueTypeTable.builder().valueType(s).build();

    public SettingTransactionalJob() {
        super(SETTING_TABLE_BUILDER, VALUE_TYPE_TABLE_BUILDER, LOG);
    }

    @Override
    @TransactionActions({
            @TransactionAction(
                    value = """
                            SELECT id FROM dictionary.value_type
                             WHERE value_type = $1;
                            """,
                    tupleMapper = TupleMapperEnum.StringTuple,
                    name = Constants.FIND_FIELD_ID),
            @TransactionAction(name = Constants.INSERT_INTO_MAIN),
            @TransactionAction(name = Constants.INSERT_INTO_RELATION),
    })
    public Uni<Optional<Long>> insert(SettingTable table, String field) {
        return super.doInsert(table, field);
    }

    @Override
    @TransactionActions({
            @TransactionAction(
                    value = """
                            SELECT id FROM dictionary.value_type
                             WHERE value_type = $1;
                            """,
                    tupleMapper = TupleMapperEnum.StringTuple,
                    name = Constants.FIND_FIELD_ID),
            @TransactionAction(name = Constants.UPDATE_MAIN_TABLE),
            @TransactionAction(name = Constants.INSERT_INTO_RELATION),
    })
    public Uni<Optional<Long>> update(SettingTable table, String field) {
        return super.doUpdate(table, field);
    }

    @Override
    @TransactionActions({
            @TransactionAction(name = Constants.DELETE_MAIN),
            @TransactionAction(
                    value = """
                            DELETE FROM dictionary.value_type WHERE id NOT IN (
                                   SELECT DISTINCT value_type_id FROM dictionary.setting);
                            """,
                    iteratorNextMapper = IteratorNextMapperEnum.LongIdIterator,
                    name = Constants.CLEAR_JOIN_ID),
    })
    public Uni<Optional<Long>> delete(SettingTable table) {
        return super.doDelete(table);
    }

    @Override
    protected Function<RowIterator<Row>, Optional<?>> iteratorNextMapper(String actionName) {
        return switch (actionName) {
            case Constants.FIND_FIELD_ID, Constants.INSERT_INTO_RELATION -> iterator ->
                    iterator.hasNext() ? Optional.of(iterator.next().getLong(ValueTypeTable.ID)) : Optional.empty();
            case Constants.INSERT_INTO_MAIN, Constants.UPDATE_MAIN_TABLE -> iterator ->
                    iterator.hasNext() ? Optional.of(iterator.next().getLong(SettingTable.ID)) : Optional.empty();
            default -> throw new IllegalStateException("Unexpected value: " + actionName);
        };
    }

    @Override
    protected Function<Optional<?>, Optional<Long>> castOptionalJoinId() {
        return o -> o.flatMap(l -> (l instanceof Long result) ? Optional.of(result) : Optional.empty());
    }

    @Override
    protected Function<Optional<?>, Optional<Long>> castOptionalMainId() {
        return o -> o.flatMap(l -> (l instanceof Long result) ? Optional.of(result) : Optional.empty());
    }
}
