/*
 * This file was last modified at 2023.11.19 16:20 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * AbstractHelper.java
 * $Id$
 */

package su.svn.daybook.domain.transact.many_to_many;

import com.google.common.collect.Lists;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.PreparedQuery;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowIterator;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.SqlConnection;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.annotation.Nonnull;
import su.svn.daybook.domain.model.CasesOfId;
import su.svn.daybook.domain.transact.Action;
import su.svn.daybook.domain.transact.OptionalHelper;
import su.svn.daybook.models.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

abstract class AbstractHelper<
        MainId extends Comparable<? extends Serializable>,
        MainTable extends CasesOfId<MainId>,
        RelId extends Comparable<? extends Serializable>,
        Relative extends CasesOfId<RelId>,
        MainField extends Comparable<? extends Serializable>,
        RelField extends Comparable<? extends Serializable>>
        implements OptionalHelper<MainId> {

    protected final AbstractManyToManyJob<MainId, MainTable, RelId, Relative, MainField, RelField> job;
    protected final Map<String, Action> map;
    protected final MainTable table;
    protected final MainField field;
    protected volatile SqlConnection connection;

    AbstractHelper(
            @Nonnull AbstractManyToManyJob<MainId, MainTable, RelId, Relative, MainField, RelField> job,
            @Nonnull Map<String, Action> map,
            @Nonnull MainTable table,
            MainField field) {
        this.job = job;
        this.map = map;
        this.table = table;
        this.field = field;
    }

    protected void setConnection(@Nonnull final SqlConnection connection) {
        if (this.connection == null) {
            synchronized (this) {
                this.connection = connection;
            }
        }
    }

    protected Function<? super RowIterator<Row>, ? extends Optional<?>> iteratorNextMapper(Action action, String aName) {
        return isNullIteratorNextMapper(action)
                ? job.iteratorNextMapper(aName)
                : action.iteratorNextMapper();
    }

    protected Uni<Long> executeIfNotInRelationTable(@Nonnull Action action, @Nonnull Collection<RelField> collection) {
        if (collection.isEmpty()) {
            return Uni.createFrom().item(0L);
        }
        return this.connection
                .preparedQuery(action.sql())
                .execute(Tuple.of(collection.toArray()))
                .onItem()
                .transform(pgRowSet -> pgRowSet.iterator().next().getLong(Constants.COUNT))
                .log();
    }

    protected Uni<Object> checkCountInJoin(long count) {
        if (count < 1) {
            return Uni.createFrom().item(count);
        }
        return Uni
                .createFrom()
                .failure(new IllegalArgumentException("can't find elements from relation"));
    }

    protected Uni<Long> insertIntoHasRelation(Collection<RelField> collection) {
        if (collection.isEmpty()) {
            return deleteAllFromHasRelationByEntry();
        }
        if ((collection.size() % 2) != 0) {
            var action = this.map.get(Constants.INSERT_INTO_RELATION_BY_2_VALUES);
            return this.insertThenOddSizeCollection(action, collection, this.field);
        }
        var action = this.map.get(Constants.INSERT_INTO_RELATION_BY_4_VALUES);
        return this.insertThenEvenSizeOfCollection(action, collection, this.field);
    }

    protected Uni<Long> clearIfHasRelationNotForEntry(Collection<RelField> collection) {
        if (collection.isEmpty()) {
            return this.deleteAllFromHasRelationByEntry();
        }
        var action = this.map.get(Constants.CLEAR_HAS_RELATION);
        return this.clearIfHasRelationNotForEntry(action, this.field, collection.toArray());
    }

    private Uni<Long> deleteAllFromHasRelationByEntry() {
        var action = this.map.get(Constants.CLEAR_ALL_HAS_RELATION_BY_FIELD);
        return this.deleteAllFromHasRelationByEntry(action, this.field);
    }

    private Uni<Long> insertThenOddSizeCollection(Action action, Collection<RelField> collection, MainField field) {
        var substitution = collection
                .stream()
                .map(relation -> Tuple.of(field, relation))
                .toList();
        return resultRowCount(substitution, action.sql());
    }

    private Uni<Long> insertThenEvenSizeOfCollection(Action action, Collection<RelField> collection, MainField field) {
        var substitution = Lists
                .partition(new ArrayList<>(collection), 2)
                .stream()
                .map(pair -> Tuple.of(field, pair.get(0), field, pair.get(1)))
                .toList();
        return resultRowCount(substitution, action.sql());
    }

    private Uni<Long> resultRowCount(List<Tuple> substitution, String sql) {
        var preparedQuery = this.connection.preparedQuery(sql);
        return resultRowCount(substitution, preparedQuery);
    }

    private Uni<Long> resultRowCount(List<Tuple> substitution, PreparedQuery<RowSet<Row>> preparedQuery) {
        Uni<RowSet<Row>> rowSet = preparedQuery.executeBatch(substitution);

        return rowSet.onItem().transform(res -> {
            long total = 0;
            do {
                total += res.rowCount();
            } while ((res = res.next()) != null);
            return total;
        });
    }

    private Uni<Long> clearIfHasRelationNotForEntry(Action action, MainField field, Object[] array) {
        return this.connection
                .preparedQuery(action.sql())
                .execute(Tuple.of(field, array))
                .onItem()
                .transform(pgRowSet -> (long) pgRowSet.rowCount())
                .log();
    }

    protected Uni<Long> deleteAllFromHasRelationByEntry(Action action, MainField field) {
        return this.connection
                .preparedQuery(action.sql())
                .execute(Tuple.of(field))
                .onItem()
                .transform(pgRowSet -> (long) pgRowSet.rowCount())
                .log();
    }
}
