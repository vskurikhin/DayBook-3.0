/*
 * This file was last modified at 2024-10-31 16:52 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * I18nTransactionalJob.java
 * $Id$
 */

package su.svn.daybook3.api.domain.transact;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowIterator;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;
import su.svn.daybook3.annotations.TransactionAction;
import su.svn.daybook3.annotations.TransactionActions;
import su.svn.daybook3.api.domain.model.I18nTable;
import su.svn.daybook3.api.domain.model.LanguageTable;
import su.svn.daybook3.api.models.Constants;
import su.svn.daybook3.enums.IteratorNextMapperEnum;
import su.svn.daybook3.enums.TupleMapperEnum;
import su.svn.daybook3.transaction.one_to_one.AbstractOneToOneJob;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

@ApplicationScoped
public class I18nTransactionalJob extends AbstractOneToOneJob<Long, I18nTable, Long, LanguageTable, String> {

    private static final Logger LOG = Logger.getLogger(I18nTransactionalJob.class);

    private static final BiFunction<I18nTable, Long, I18nTable> I18N_TABLE_BUILDER
            = (t, id) -> t.toBuilder().languageId(id).build();

    private static final Function<String, LanguageTable> LANGUAGE_TABLE_BUILDER
            = s -> LanguageTable.builder().language(s).userName("root").build(); // todo

    public I18nTransactionalJob() {
        super(I18N_TABLE_BUILDER, LANGUAGE_TABLE_BUILDER, LOG);
    }

    @Override
    @TransactionActions({
            @TransactionAction(
                    value = """
                            SELECT id FROM dictionary.language
                             WHERE language = $1;
                            """,
                    tupleMapper = TupleMapperEnum.StringTuple,
                    name = Constants.FIND_FIELD_ID),
            @TransactionAction(name = Constants.INSERT_INTO_MAIN),
            @TransactionAction(name = Constants.INSERT_INTO_RELATION),
    })
    public Uni<Optional<Long>> insert(I18nTable table, String field) {
        return super.doInsert(table, field);
    }

    @Override
    @TransactionActions({
            @TransactionAction(
                    value = """
                            SELECT id FROM dictionary.language
                             WHERE language = $1;
                            """,
                    tupleMapper = TupleMapperEnum.StringTuple,
                    name = Constants.FIND_FIELD_ID),
            @TransactionAction(name = Constants.UPDATE_MAIN_TABLE),
            @TransactionAction(name = Constants.INSERT_INTO_RELATION),
    })
    public Uni<Optional<Long>> update(I18nTable table, String field) {
        return super.doUpdate(table, field);
    }

    @Override
    @TransactionActions({
            @TransactionAction(name = Constants.DELETE_MAIN),
            @TransactionAction(
                    value = """
                            DELETE FROM dictionary.language WHERE id NOT IN (
                                   SELECT DISTINCT language_id FROM dictionary.i18n);
                            """,
                    iteratorNextMapper = IteratorNextMapperEnum.LongIdIterator,
                    name = Constants.CLEAR_JOIN_ID),
    })
    public Uni<Optional<Long>> delete(I18nTable table) {
        return super.doDelete(table);
    }

    @Override
    protected Function<RowIterator<Row>, Optional<?>> iteratorNextMapper(String actionName) {
        return switch (actionName) {
            case Constants.FIND_FIELD_ID, Constants.INSERT_INTO_RELATION -> iterator ->
                    iterator.hasNext() ? Optional.of(iterator.next().getLong(LanguageTable.ID)) : Optional.empty();
            case Constants.INSERT_INTO_MAIN, Constants.UPDATE_MAIN_TABLE -> iterator ->
                    iterator.hasNext() ? Optional.of(iterator.next().getLong(I18nTable.ID)) : Optional.empty();
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
