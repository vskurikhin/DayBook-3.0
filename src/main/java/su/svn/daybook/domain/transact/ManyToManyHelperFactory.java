/*
 * This file was last modified at 2023.09.06 17:04 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * ManyToManyHelperFactory.java
 * $Id$
 */

package su.svn.daybook.domain.transact;

import com.google.common.collect.Lists;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.PreparedQuery;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowIterator;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.SqlConnection;
import io.vertx.mutiny.sqlclient.Tuple;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.model.CasesOfId;
import su.svn.daybook.models.Constants;

import jakarta.annotation.Nonnull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

class ManyToManyHelperFactory<
        I extends Comparable<? extends Serializable>,
        M extends CasesOfId<I>,
        J extends Comparable<? extends Serializable>,
        S extends CasesOfId<J>,
        F extends Comparable<? extends Serializable>,
        G extends Comparable<? extends Serializable>> {

    private final AbstractManyToManyJob<I, M, J, S, F, G> job;
    private final Map<String, Map<String, Action>> mapJob;

    ManyToManyHelperFactory(
            @Nonnull AbstractManyToManyJob<I, M, J, S, F, G> job,
            @Nonnull Map<String, Map<String, Action>> mapJob) {
        this.job = job;
        this.mapJob = mapJob;
    }

    public Helper<I, M, J, S, G> createInsertHelper(@Nonnull M table, F field, @Nonnull Collection<G> collection) {
        return new InsertHelper<>(this.job, this.mapJob, table, field, collection);
    }

    public Helper<I, M, J, S, G> createUpdateHelper(@Nonnull M table, F field, @Nonnull Collection<G> collection) {
        return new UpdateHelper<>(this.job, this.mapJob, table, field, collection);
    }

    public Helper<I, M, J, S, G> createDeleteHelper(@Nonnull M table, F field) {
        return new DeleteHelper<>(this.job, this.mapJob, table, field);
    }

    private abstract static class AbstractHelper<
            I extends Comparable<? extends Serializable>,
            M extends CasesOfId<I>,
            J extends Comparable<? extends Serializable>,
            S extends CasesOfId<J>,
            F extends Comparable<? extends Serializable>,
            G extends Comparable<? extends Serializable>>
            implements Helper<I, M, J, S, G> {

        protected final AbstractManyToManyJob<I, M, J, S, F, G> job;
        protected volatile SqlConnection connection;
        protected final M table;

        AbstractHelper(AbstractManyToManyJob<I, M, J, S, F, G> job, M table) {
            this.job = job;
            this.table = table;
        }

        protected void setConnection(SqlConnection connection) {
            if (this.connection == null) {
                synchronized (this) {
                    this.connection = connection;
                }
            }
        }

        protected Function<? super RowIterator<Row>, ? extends Optional<?>> iteratorNextMapper(
                Action action, String actName) {
            return (action.iteratorNextMapper() == null)
                    ? job.iteratorNextMapper(actName)
                    : action.iteratorNextMapper();
        }

        protected Uni<Long> countInJoinTable(@Nonnull Action action, @Nonnull Collection<G> collection) {
            if (collection.isEmpty()) {
                return Uni.createFrom().item(0L);
            }
            return this.connection
                    .preparedQuery(action.sql())
                    .execute(Tuple.of(collection.toArray()))
                    .onItem()
                    .transform(pgRowSet -> pgRowSet.iterator().next().getLong(Constants.COUNT));
        }

        protected Uni<Object> checkCountInJoin(long count) {
            if (count < 1) {
                return Uni.createFrom().item(count);
            }
            return Uni
                    .createFrom()
                    .failure(new IllegalArgumentException("can't find elements from relation"));
        }

        protected Uni<Long> insertThenOddSizeCollection(Action action, Collection<G> collection, F field) {
            var substitution = collection
                    .stream()
                    .map(relation -> Tuple.of(field, relation))
                    .toList();
            return resultRowCount(substitution, action.sql());
        }

        protected Uni<Long> insertThenEvenSizeOfCollection(Action action, Collection<G> collection, F field) {
            var substitution = Lists
                    .partition(new ArrayList<>(collection), 2)
                    .stream()
                    .map(pair -> Tuple.of(field, pair.get(0), field, pair.get(1)))
                    .toList();
            return resultRowCount(substitution, action.sql());
        }

        protected Uni<Long> resultRowCount(List<Tuple> substitution, String sql) {
            var preparedQuery = this.connection.preparedQuery(sql);
            return resultRowCount(substitution, preparedQuery);
        }

        protected Uni<Long> resultRowCount(List<Tuple> substitution, PreparedQuery<RowSet<Row>> preparedQuery) {
            Uni<RowSet<Row>> rowSet = preparedQuery.executeBatch(substitution);

            return rowSet.onItem().transform(res -> {
                long total = 0;
                do {
                    total += res.rowCount();
                } while ((res = res.next()) != null);
                return total;
            });
        }

        protected Uni<Long> clearIfHasRelationNotForEntry(Action action, F field, Object[] array) {
            return this.connection
                    .preparedQuery(action.sql())
                    .execute(Tuple.of(field, array))
                    .onItem()
                    .transform(pgRowSet -> (long) pgRowSet.rowCount());
        }

        private Uni<Long> deleteAllFromHasRelationByEntry(Action action, F field) {
            return this.connection
                    .preparedQuery(action.sql())
                    .execute(Tuple.of(field))
                    .onItem()
                    .transform(pgRowSet -> (long) pgRowSet.rowCount());
        }
    }

    static class InsertHelper<
            I extends Comparable<? extends Serializable>,
            M extends CasesOfId<I>,
            J extends Comparable<? extends Serializable>,
            S extends CasesOfId<J>,
            F extends Comparable<? extends Serializable>,
            G extends Comparable<? extends Serializable>>
            extends AbstractHelper<I, M, J, S, F, G> {

        private static final Logger LOG = Logger.getLogger(InsertHelper.class);

        private final F field;
        private final Collection<G> collection;
        private final Map<String, Action> map;

        protected InsertHelper(
                @Nonnull AbstractManyToManyJob<I, M, J, S, F, G> job,
                @Nonnull Map<String, Map<String, Action>> map,
                @Nonnull M table,
                F field,
                @Nonnull Collection<G> collection) {
            super(job, table);
            this.collection = collection;
            this.field = field;
            this.map = map.get(Constants.INSERT);
        }

        @Override
        public Uni<Optional<I>> apply(@Nonnull final SqlConnection connection) {
            super.setConnection(connection);
            return checkCountInJoinTableAndThenInsert();
        }

        private Uni<Optional<I>> checkCountInJoinTableAndThenInsert() {
            LOG.tracef("checkCountInJoinTableAndThenInsert");
            return countInJoinTable()
                    .flatMap(this::checkCountInJoin)
                    .flatMap(o -> insert());
        }

        private Uni<Long> countInJoinTable() {
            Action action = map.get(Constants.COUNT_NOT_EXISTS);
            return super.countInJoinTable(action, collection);
        }

        private Uni<Optional<I>> insert() {
            var action = map.get(Constants.INSERT_MAIN);
            return super.connection
                    .preparedQuery(String.format(Helper.insertSql(action, table), Constants.ID))
                    .execute(table.caseInsertTuple())
                    .map(RowSet::iterator)
                    .map(iteratorNextMapper(action, Constants.INSERT_MAIN))
                    .flatMap(id -> this.insertIntoHasRelation()
                            .flatMap(c1 -> clearIfHasRelationNotForEntry())
                            .map(c2 -> id)
                    ).map(job.castOptionalMainId());
        }

        private Uni<Long> insertIntoHasRelation() {
            if (collection.isEmpty()) {
                return deleteAllFromHasRelationByEntry();
            }
            if ((collection.size() % 2) != 0) {
                var action = map.get(Constants.INSERT_JOIN2);
                return super.insertThenOddSizeCollection(action, collection, field);
            }
            var action = map.get(Constants.INSERT_JOIN4);
            return super.insertThenEvenSizeOfCollection(action, collection, field);
        }

        private Uni<Long> clearIfHasRelationNotForEntry() {
            if (collection.isEmpty()) {
                return deleteAllFromHasRelationByEntry();
            }
            var action = map.get(Constants.CLEAR_HAS_RELATION);
            return super.clearIfHasRelationNotForEntry(action, this.field, collection.toArray());
        }

        private Uni<Long> deleteAllFromHasRelationByEntry() {
            var action = map.get(Constants.CLEAR_ALL_HAS_RELATION_BY_FIELD);
            return super.deleteAllFromHasRelationByEntry(action, this.field);
        }
    }

    static class UpdateHelper<
            I extends Comparable<? extends Serializable>,
            M extends CasesOfId<I>,
            J extends Comparable<? extends Serializable>,
            S extends CasesOfId<J>,
            F extends Comparable<? extends Serializable>,
            G extends Comparable<? extends Serializable>>
            extends AbstractHelper<I, M, J, S, F, G> {

        private static final Logger LOG = Logger.getLogger(UpdateHelper.class);

        private final Map<String, Action> map;
        private final F field;
        private final Collection<G> collection;

        protected UpdateHelper(
                @Nonnull AbstractManyToManyJob<I, M, J, S, F, G> job,
                @Nonnull Map<String, Map<String, Action>> map,
                @Nonnull M table,
                F field,
                @Nonnull Collection<G> collection) {
            super(job, table);
            this.collection = collection;
            this.map = map.get(Constants.UPDATE);
            this.field = field;
        }

        @Override
        public Uni<Optional<I>> apply(@Nonnull final SqlConnection connection) {
            super.setConnection(connection);
            return checkCountInJoinTableAndThenUpdate();
        }

        private Uni<Optional<I>> checkCountInJoinTableAndThenUpdate() {
            LOG.tracef("checkCountInJoinTableAndThenUpdate");
            return countInJoinTable()
                    .flatMap(this::checkCountInJoin)
                    .flatMap(o -> update());
        }

        private Uni<Long> countInJoinTable() {
            Action action = map.get(Constants.COUNT_NOT_EXISTS);
            return super.countInJoinTable(action, collection);
        }

        private Uni<Optional<I>> update() {
            var action = map.get(Constants.UPDATE_MAIN);
            return super.connection
                    .preparedQuery(String.format(Helper.updateSql(action, table), Constants.ID))
                    .execute(table.updateTuple())
                    .map(RowSet::iterator)
                    .map(iteratorNextMapper(action, Constants.UPDATE_MAIN))
                    .flatMap(id -> this.insertIntoHasRelation()
                            .flatMap(c1 -> clearIfHasRelationNotForEntry())
                            .map(c2 -> id)
                    ).map(job.castOptionalMainId());
        }

        private Uni<Long> insertIntoHasRelation() {
            if (collection.isEmpty()) {
                return deleteAllFromHasRelationByEntry();
            }
            if ((collection.size() % 2) != 0) {
                var action = map.get(Constants.INSERT_JOIN2);
                return super.insertThenOddSizeCollection(action, collection, field);
            }
            var action = map.get(Constants.INSERT_JOIN4);
            return super.insertThenEvenSizeOfCollection(action, collection, field);
        }

        private Uni<Long> clearIfHasRelationNotForEntry() {
            if (collection.isEmpty()) {
                return deleteAllFromHasRelationByEntry();
            }
            var action = map.get(Constants.CLEAR_HAS_RELATION);
            return super.clearIfHasRelationNotForEntry(action, this.field, collection.toArray());
        }

        private Uni<Long> deleteAllFromHasRelationByEntry() {
            var action = map.get(Constants.CLEAR_ALL_HAS_RELATION_BY_FIELD);
            return super.deleteAllFromHasRelationByEntry(action, this.field);
        }
    }

    static class DeleteHelper<
            I extends Comparable<? extends Serializable>,
            M extends CasesOfId<I>,
            J extends Comparable<? extends Serializable>,
            S extends CasesOfId<J>,
            F extends Comparable<? extends Serializable>,
            G extends Comparable<? extends Serializable>>
            extends AbstractHelper<I, M, J, S, F, G> {

        private static final Logger LOG = Logger.getLogger(DeleteHelper.class);

        private final Map<String, Action> map;
        private final F field;

        protected DeleteHelper(
                @Nonnull AbstractManyToManyJob<I, M, J, S, F, G> job,
                @Nonnull Map<String, Map<String, Action>> map,
                @Nonnull M table,
                F field) {
            super(job, table);
            this.map = map.get(Constants.DELETE);
            this.field = field;
        }

        @Override
        public Uni<Optional<I>> apply(@Nonnull final SqlConnection connection) {
            super.setConnection(connection);
            return delete();
        }

        private Uni<Optional<I>> delete() {
            LOG.tracef("checkCountInJoinTableAndThenUpdate");
            return deleteAllRelation()
                    .flatMap(this::deleteEntry);
        }

        private Uni<Optional<I>> deleteEntry(Long aLong) {
            Action action = map.get(Constants.DELETE_MAIN);
            return super.connection
                    .preparedQuery(String.format(Helper.deleteSql(action, table), Constants.ID))
                    .execute(Tuple.of(table.id()))
                    .map(RowSet::iterator)
                    .map(iteratorNextMapper(action, Constants.DELETE_MAIN))
                    .map(job.castOptionalMainId());
        }

        private Uni<Long> deleteAllRelation() {
            Action action = map.get(Constants.CLEAR_ALL_HAS_RELATION_BY_FIELD);
            return this.deleteAllFromHasRelationByEntry(action);
        }

        private Uni<Long> deleteAllFromHasRelationByEntry(Action action) {
            return super.deleteAllFromHasRelationByEntry(action, this.field);
        }

        protected Uni<Long> countInJoinTable(@Nonnull Action action) {
            return this.connection
                    .preparedQuery(action.sql())
                    .execute()
                    .onItem()
                    .transform(pgRowSet -> pgRowSet.iterator().next().getLong(Constants.COUNT));
        }
    }
}
