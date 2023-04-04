/*
 * This file was last modified at 2023.01.06 15:00 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * OneToOneHelper.java
 * $Id$
 */

package su.svn.daybook.domain.transact;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowIterator;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.SqlConnection;
import io.vertx.mutiny.sqlclient.Tuple;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.model.CasesOfId;
import su.svn.daybook.models.Constants;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

@SuppressWarnings("CdiInjectionPointsInspection")
class OneToOneHelperFactory<
        I extends Comparable<? extends Serializable>,
        M extends CasesOfId<I>,
        K extends Comparable<? extends Serializable>,
        J extends CasesOfId<K>,
        F extends Comparable<? extends Serializable>> {

    private final AbstractOneToOneJob<I, M, K, J, F> job;
    private final BiFunction<M, K, M> tableBuilder;
    private final Function<F, J> joinFieldBuilder;
    private final Map<String, Map<String, Action>> mapJob;

    OneToOneHelperFactory(
            @Nonnull AbstractOneToOneJob<I, M, K, J, F> job,
            @Nonnull Map<String, Map<String, Action>> mapJob,
            @Nonnull BiFunction<M, K, M> tableBuilder,
            @Nonnull Function<F, J> joinFieldBuilder) {
        this.tableBuilder = tableBuilder;
        this.joinFieldBuilder = joinFieldBuilder;
        this.mapJob = mapJob;
        this.job = job;
    }

    public Helper<I, M, K, J, F> createInsertHelper(M table, F field) {
        return new InsertHelper<>(this.job, this.mapJob, this.tableBuilder, this.joinFieldBuilder, table, field);
    }

    public Helper<I, M, K, J, F> createUpdateHelper(M table, F field) {
        return new UpdateHelper<>(this.job, this.mapJob, this.tableBuilder, this.joinFieldBuilder, table, field);
    }

    public Helper<I, M, K, J, F> createDeleteHelper(M table) {
        return new DeleteHelper<>(this.job, this.mapJob, this.tableBuilder, this.joinFieldBuilder, table);
    }

    static abstract class Helper<
            I extends Comparable<? extends Serializable>,
            D extends CasesOfId<I>,
            G extends Comparable<? extends Serializable>,
            J extends CasesOfId<G>,
            F extends Comparable<? extends Serializable>>
            implements Function<SqlConnection, Uni<Optional<I>>> {

        protected final AbstractOneToOneJob<I, D, G, J, F> job;

        protected Helper(AbstractOneToOneJob<I, D, G, J, F> job) {
            this.job = job;
        }

        <X extends CasesOfId<?>> String deleteSql(Action action, X table) {
            return (action.sqlMapper() != null)
                    ? action.sqlMapper().apply(table)
                    : table.deleteSql() ;
        }

        <X extends CasesOfId<?>> String insertSql(Action action, X table) {
            return (action.sqlMapper() != null)
                    ? action.sqlMapper().apply(table)
                    : table.caseInsertSql() ;
        }

        <X extends CasesOfId<?>> String updateSql(Action action, X table) {
            return (action.sqlMapper() != null)
                    ? action.sqlMapper().apply(table)
                    : table.updateSql() ;
        }

        <X extends CasesOfId<?>> Tuple insertTuple(Action action, X table) {
            return (action.tupleMapper() != null)
                    ? action.tupleMapper().apply(table)
                    : table.caseInsertTuple() ;
        }

        <X extends CasesOfId<?>> Tuple updateTuple(Action action, X table) {
            return (action.tupleMapper() != null)
                    ? action.tupleMapper().apply(table)
                    : table.updateTuple() ;
        }

        Tuple tuple(Action action, Object o) {
            return (action.tupleMapper() != null)
                    ? action.tupleMapper().apply(o)
                    : Tuple.tuple() ;
        }

        protected Function<? super RowIterator<Row>, ? extends Optional<?>> iteratorNextMapper(
                Action action, String actName) {
            return (action.iteratorNextMapper() == null)
                    ? job.iteratorNextMapper(actName)
                    : action.iteratorNextMapper();
        }

        protected Uni<Optional<I>> getFailure() {
            return Uni.createFrom().failure(new RuntimeException());
        }
    }

    static class InsertHelper<
            I extends Comparable<? extends Serializable>,
            D extends CasesOfId<I>,
            G extends Comparable<? extends Serializable>,
            J extends CasesOfId<G>,
            F extends Comparable<? extends Serializable>>
            extends Helper<I, D, G, J, F> {

        private static final Logger LOG = Logger.getLogger(InsertHelper.class);

        private final BiFunction<D, G, D> tableBuilder;
        private final D table;
        private final F field;
        private final Function<F, J> joinFieldBuilder;
        private final Map<String, Map<String, Action>> mapJob;
        private volatile SqlConnection connection;

        InsertHelper(
                @Nonnull AbstractOneToOneJob<I, D, G, J, F> job,
                @Nonnull Map<String, Map<String, Action>> map,
                @Nonnull BiFunction<D, G, D> tableBuilder,
                @Nonnull Function<F, J> joinFieldBuilder,
                @Nonnull D table,
                F field) {
            super(job);
            this.tableBuilder = tableBuilder;
            this.joinFieldBuilder = joinFieldBuilder;
            this.field = field;
            this.mapJob = map;
            this.table = table;
        }

        @Override
        public Uni<Optional<I>> apply(SqlConnection sqlConnection) {
            return findingOrThenInsert(sqlConnection);
        }

        Uni<Optional<I>> findingOrThenInsert(@Nonnull final SqlConnection connection) {
            LOG.tracef("findingOrThenInsert");
            this.connection = connection;
            var map = this.mapJob.get(Constants.INSERT);
            return findFieldId(map)
                    .flatMap(o -> o.isEmpty() ? insertJoin(map) : Uni.createFrom().item(o))
                    .flatMap(o -> o.isPresent() ? insertMain(map, o.get()) : getFailure());
        }

        protected Uni<Optional<G>> findFieldId(Map<String, Action> map) {
            var action = map.get(Constants.FIND_FIELD_ID);
            return connection
                    .preparedQuery(action.sql())
                    .execute(tuple(action, field))
                    .map(RowSet::iterator)
                    .map(iteratorNextMapper(action, Constants.FIND_FIELD_ID))
                    .map(job.castOptionalJoinId());
        }

        protected Uni<Optional<G>> insertJoin(Map<String, Action> map) {
            var action = map.get(Constants.INSERT_JOIN);
            var join = joinFieldBuilder.apply(this.field);
            return connection
                    .preparedQuery(String.format(insertSql(action, join), Constants.ID))
                    .execute(insertTuple(action, join))
                    .map(RowSet::iterator)
                    .map(iteratorNextMapper(action, Constants.INSERT_JOIN))
                    .map(job.castOptionalJoinId());
        }

        protected Uni<Optional<I>> insertMain(Map<String, Action> map, G joinId) {
            var action = map.get(Constants.INSERT_MAIN);
            var main = tableBuilder.apply(this.table, joinId);
            return connection
                    .preparedQuery(String.format(insertSql(action, main), Constants.ID))
                    .execute(insertTuple(action, main))
                    .map(RowSet::iterator)
                    .map(iteratorNextMapper(action, Constants.INSERT_MAIN))
                    .map(job.castOptionalMainId());
        }
    }

    static class UpdateHelper<
            I extends Comparable<? extends Serializable>,
            D extends CasesOfId<I>,
            G extends Comparable<? extends Serializable>,
            J extends CasesOfId<G>,
            F extends Comparable<? extends Serializable>>
            extends Helper<I, D, G, J, F> {

        private static final Logger LOG = Logger.getLogger(UpdateHelper.class);

        private final BiFunction<D, G, D> tableBuilder;
        private final D table;
        private final F field;
        private final Function<F, J> joinFieldBuilder;
        private final Map<String, Map<String, Action>> mapJob;
        private volatile SqlConnection connection;

        UpdateHelper(
                @Nonnull AbstractOneToOneJob<I, D, G, J, F> job,
                @Nonnull Map<String, Map<String, Action>> map,
                @Nonnull BiFunction<D, G, D> tableBuilder,
                @Nonnull Function<F, J> joinFieldBuilder,
                @Nonnull D table,
                F field) {
            super(job);
            this.tableBuilder = tableBuilder;
            this.joinFieldBuilder = joinFieldBuilder;
            this.field = field;
            this.mapJob = map;
            this.table = table;
        }

        @Override
        public Uni<Optional<I>> apply(SqlConnection sqlConnection) {
            return findingOrThenUpdate(sqlConnection);
        }

        Uni<Optional<I>> findingOrThenUpdate(@Nonnull final SqlConnection connection) {
            LOG.tracef("findingOrThenUpdate");
            this.connection = connection;
            var map = this.mapJob.get(Constants.UPDATE);
            return findFieldId(map)
                    .flatMap(o -> o.isEmpty() ? insertJoin(map) : Uni.createFrom().item(o))
                    .flatMap(o -> o.isPresent() ? updateMain(map, o.get()) : getFailure());
        }

        protected Uni<Optional<G>> findFieldId(Map<String, Action> map) {
            var action = map.get(Constants.FIND_FIELD_ID);
            return connection
                    .preparedQuery(action.sql())
                    .execute(tuple(action, field))
                    .map(RowSet::iterator)
                    .map(iteratorNextMapper(action, Constants.FIND_FIELD_ID))
                    .map(job.castOptionalJoinId());
        }

        protected Uni<Optional<G>> insertJoin(Map<String, Action> map) {
            var action = map.get(Constants.INSERT_JOIN);
            var join = joinFieldBuilder.apply(this.field);
            return connection
                    .preparedQuery(String.format(insertSql(action, join), Constants.ID))
                    .execute(insertTuple(action, join))
                    .map(RowSet::iterator)
                    .map(iteratorNextMapper(action, Constants.INSERT_JOIN))
                    .map(job.castOptionalJoinId());
        }

        private Uni<Optional<I>> updateMain(Map<String, Action> map, G joinId) {
            var action = map.get(Constants.UPDATE_MAIN);
            var main = tableBuilder.apply(this.table, joinId);
            return connection
                    .preparedQuery(String.format(updateSql(action, main), Constants.ID))
                    .execute(updateTuple(action, main))
                    .map(RowSet::iterator)
                    .map(iteratorNextMapper(action, Constants.UPDATE_MAIN))
                    .map(job.castOptionalMainId());
        }
    }

    static class DeleteHelper<
            I extends Comparable<? extends Serializable>,
            D extends CasesOfId<I>,
            G extends Comparable<? extends Serializable>,
            J extends CasesOfId<G>,
            F extends Comparable<? extends Serializable>>
            extends Helper<I, D, G, J, F> {

        private static final Logger LOG = Logger.getLogger(DeleteHelper.class);

        private final BiFunction<D, G, D> tableBuilder;
        private final D table;
        private final Function<F, J> joinFieldBuilder;
        private final Map<String, Map<String, Action>> mapJob;
        private volatile SqlConnection connection;

        DeleteHelper(
                @Nonnull AbstractOneToOneJob<I, D, G, J, F> job,
                @Nonnull Map<String, Map<String, Action>> map,
                @Nonnull BiFunction<D, G, D> tableBuilder,
                @Nonnull Function<F, J> joinFieldBuilder,
                @Nonnull D table) {
            super(job);
            this.tableBuilder = tableBuilder;
            this.joinFieldBuilder = joinFieldBuilder;
            this.mapJob = map;
            this.table = table;
        }

        @Override
        public Uni<Optional<I>> apply(SqlConnection sqlConnection) {
            return deleteAndClear(sqlConnection);
        }

        private Uni<Optional<I>> deleteAndClear(SqlConnection sqlConnection) {
            LOG.tracef("deleteAndClear");
            this.connection = sqlConnection;
            var map = this.mapJob.get(Constants.DELETE);
            return deleteMain(map, table.id()).flatMap(o -> o.isPresent() ? clear(map, o.get()) :  getFailure());
        }

        private Uni<Optional<I>> clear(Map<String, Action> map, I id) {
            var action = map.get(Constants.CLEAR_JOIN_ID);
            return connection
                    .preparedQuery(String.format(action.sql(), Constants.ID))
                    .execute()
                    .map(RowSet::iterator)
                    .map(action.iteratorNextMapper())
                    .map(x -> Optional.of(id));
        }

        private Uni<Optional<I>> deleteMain(Map<String, Action> map, I id) {
            var action = map.get(Constants.DELETE_MAIN);
            return connection
                    .preparedQuery(String.format(table.deleteSql(), Constants.ID))
                    .execute(Tuple.of(id))
                    .map(RowSet::iterator)
                    .map(iteratorNextMapper(action, Constants.UPDATE_MAIN))
                    .map(job.castOptionalMainId());
        }
    }
}