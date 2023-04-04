/*
 * This file was last modified at 2023.01.06 15:00 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * ManyToManyHelper.java
 * $Id$
 */

package su.svn.daybook.domain.transact;

import com.google.common.collect.Lists;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.PreparedQuery;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.SqlConnection;
import io.vertx.mutiny.sqlclient.Tuple;
import su.svn.daybook.domain.model.CasesOfId;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

class ManyToManyHelper<
        K extends Comparable<? extends Serializable>,
        D extends CasesOfId<K>,
        E extends Comparable<E>,
        T extends Comparable<T>> {

    public static final String COUNT = "count";
    private final AbstractManyToMayJob<K, D, E, T> job;
    private final Collection<T> collection;
    private final D table;
    private final E entry;
    private volatile SqlConnection connection;

    ManyToManyHelper(
            @Nonnull AbstractManyToMayJob<K, D, E, T> job,
            @Nonnull Collection<T> collection,
            @Nonnull D table,
            @Nonnull E entry) {
        this.job = job;
        this.collection = collection;
        this.table = table;
        this.entry = entry;
    }

    Uni<Optional<K>> checkAndInsert(@Nonnull final SqlConnection connection) {
        this.connection = connection;
        return countNotExists()
                .flatMap(this::check)
                .flatMap(o -> insert());
    }

    Uni<Optional<K>> checkAndUpdate(@Nonnull final SqlConnection connection) {
        this.connection = connection;
        return countNotExists()
                .flatMap(this::check)
                .flatMap(o -> update());
    }

    protected Uni<Optional<K>> insert() {
        return connection
                .preparedQuery(job.caseInsertSql(table))
                .execute(table.caseInsertTuple())
                .map(RowSet::iterator)
                .map(job::getKeyByIteratorNext)
                .flatMap(id -> this.updateHasRelationForTable()
                        .flatMap(c1 -> clearIfHasRelationNotForEntry())
                        .map(c2 -> Optional.ofNullable(id))
                );
    }

    protected Uni<Optional<K>> update() {
        return connection
                .preparedQuery(job.sqlUpdateTable())
                .execute(table.updateTuple())
                .map(RowSet::iterator)
                .map(job::getKeyByIteratorNext)
                .onItem()
                .transformToUni(this::checkIdIsNull)
                .flatMap(id -> this.updateHasRelationForTable()
                        .flatMap(c1 -> clearIfHasRelationNotForEntry())
                        .map(c2 -> Optional.ofNullable(id))
                );
    }

    protected Uni<Optional<K>> delete(@Nonnull final SqlConnection connection) {
        this.connection = connection;
        return deleteAllFromHasRelationByEntry()
                .flatMap(c -> connection
                        .preparedQuery(job.sqlDeleteFromTable())
                        .execute(Tuple.of(table.id()))
                        .map(RowSet::iterator)
                        .map(job::getKeyByIteratorNext))
                .map(Optional::of);
    }

    protected Uni<Object> check(long count) {
        if (count < 1) {
            return Uni.createFrom().item(count);
        }
        return Uni
                .createFrom()
                .failure(new IllegalArgumentException("can't find elements from relation"));
    }

    protected Uni<K> checkIdIsNull(K k) {
        if (k != null) {
            return Uni.createFrom().item(k);
        }
        return Uni.createFrom().failure(new IllegalArgumentException("Can't update! id is invalid"));
    }

    protected Uni<Long> countNotExists() {
        if (this.collection.isEmpty()) {
            return Uni.createFrom().item(0L);
        }
        return connection
                .preparedQuery(job.sqlCountOfNotExisted())
                .execute(Tuple.of(collection.toArray()))
                .onItem()
                .transform(pgRowSet -> pgRowSet.iterator().next().getLong(COUNT));
    }

    protected Uni<Long> updateHasRelationForTable() {

        if (collection.isEmpty()) {
            return deleteAllFromHasRelationByEntry();
        }
        if ((collection.size() % 2) != 0) {
            var substitution = collection
                    .stream()
                    .map(relation -> Tuple.of(entry, relation))
                    .toList();
            return resultRowCount(substitution, job.sqlUpdateHasRelation2());
        }
        var substitution = Lists
                .partition(new ArrayList<>(collection), 2)
                .stream()
                .map(pair -> Tuple.of(entry, pair.get(0), entry, pair.get(1)))
                .toList();
        return resultRowCount(substitution, job.sqlUpdateHasRelation4());
    }

    protected Uni<Long> clearIfHasRelationNotForEntry() {

        if (collection.isEmpty()) {
            return deleteAllFromHasRelationByEntry();
        }
        return connection
                .preparedQuery(job.sqlClearHasRelation())
                .execute(Tuple.of(entry, collection.toArray()))
                .onItem()
                .transform(pgRowSet -> (long) pgRowSet.rowCount());
    }

    protected Uni<Long> deleteAllFromHasRelationByEntry() {
        return connection
                .preparedQuery(job.sqlDeleteFromHasRelationByEntry())
                .execute(Tuple.of(entry))
                .onItem()
                .transform(pgRowSet -> (long) pgRowSet.rowCount());
    }

    protected Uni<Long> resultRowCount(List<Tuple> substitution, String sql) {

        PreparedQuery<RowSet<Row>> preparedQuery = connection.preparedQuery(sql);
        Uni<RowSet<Row>> rowSet = preparedQuery.executeBatch(substitution);

        return rowSet.onItem().transform(res -> {
            long total = 0;
            do {
                total += res.rowCount();
            } while ((res = res.next()) != null);
            return total;
        });
    }
}
