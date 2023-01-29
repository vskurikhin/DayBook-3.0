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

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

class OneToOneHelper<
        I extends Comparable<? extends Serializable>,
        D extends CasesOfId<I>,
        G extends Comparable<? extends Serializable>,
        J extends CasesOfId<G>,
        F extends Comparable<? extends Serializable>> {

    private static final Logger LOG = Logger.getLogger(OneToOneHelper.class);

    private final AbstractOneToOneJob<I, D, G, J, F> job;
    private final BiFunction<D, G, D> tableBuilder;
    private final D table;
    private final F field;
    private final Function<F, J> joinFieldBuilder;
    private final Map<String, Map<String, Action>> map;
    private volatile SqlConnection connection;

    OneToOneHelper(
            @Nonnull AbstractOneToOneJob<I, D, G, J, F> job,
            @Nonnull Map<String, Map<String, Action>> map,
            @Nonnull D table,
            @Nonnull F field,
            @Nonnull BiFunction<D, G, D> tableBuilder,
            @Nonnull Function<F, J> joinFieldBuilder) {
        this.tableBuilder = tableBuilder;
        this.joinFieldBuilder = joinFieldBuilder;
        this.field = field;
        this.map = map;
        this.table = table;
        this.job = job;
    }

    Uni<Optional<I>> findingOrThenInsert(@Nonnull final SqlConnection connection) {
        LOG.tracef("findingOrThenInsert");
        this.connection = connection;
        var map = this.map.get(Constants.INSERT);
        return findFieldId(map)
                .flatMap(o -> o.isEmpty() ? insertJoin(map) : Uni.createFrom().item(o))
                .flatMap(o -> o.isPresent() ? insertMain(map, o.get()) : getFailure());
    }

    Uni<Optional<I>> findingOrThenUpdate(@Nonnull final SqlConnection connection) {
        LOG.tracef("findingOrThenUpdate");
        this.connection = connection;
        var map = this.map.get(Constants.UPDATE);
        return findFieldId(map)
                .flatMap(o -> o.isEmpty() ? insertJoin(map) : Uni.createFrom().item(o))
                .flatMap(o -> o.isPresent() ? updateMain(map, o.get()) : getFailure());
    }

    private Uni<Optional<I>> getFailure() {
        return Uni.createFrom().failure(new RuntimeException());
    }

    protected Uni<Optional<G>> findFieldId(Map<String, Action> map) {
        var action = map.get(Constants.FIND_FIELD_ID);
        return connection
                .preparedQuery(action.sql())
                .execute(tuple(action, field))
                .map(RowSet::iterator)
                .map(iteratorNextMapper(action, Constants.FIND_FIELD_ID))
                .map(job.oToG());
    }

    protected Uni<Optional<G>> insertJoin(Map<String, Action> map) {
        var action = map.get(Constants.INSERT_JOIN);
        var join = joinFieldBuilder.apply(this.field);
        return connection
                .preparedQuery(String.format(insertSql(action, join), Constants.ID))
                .execute(insertTuple(action, join))
                .map(RowSet::iterator)
                .map(iteratorNextMapper(action, Constants.INSERT_JOIN))
                .map(job.oToG());
    }

    protected Uni<Optional<I>> insertMain(Map<String, Action> map, G joinId) {
        var action = map.get(Constants.INSERT_MAIN);
        var main = tableBuilder.apply(this.table, joinId);
        return connection
                .preparedQuery(String.format(insertSql(action, main), Constants.ID))
                .execute(insertTuple(action, main))
                .map(RowSet::iterator)
                .map(iteratorNextMapper(action, Constants.INSERT_MAIN))
                .map(job.oToI());
    }

    private Uni<Optional<I>> updateMain(Map<String, Action> map, G joinId) {
        var action = map.get(Constants.UPDATE_MAIN);
        var main = tableBuilder.apply(this.table, joinId);
        return connection
                .preparedQuery(String.format(updateSql(action, main), Constants.ID))
                .execute(updateTuple(action, main))
                .map(RowSet::iterator)
                .map(iteratorNextMapper(action, Constants.UPDATE_MAIN))
                .map(job.oToI());
    }

    private <X extends CasesOfId<?>> String insertSql(Action action, X table) {
        return (action.sqlMapper() != null)
                ? action.sqlMapper().apply(table)
                : table.caseInsertSql() ;
    }

    private <X extends CasesOfId<?>> String updateSql(Action action, X table) {
        return (action.sqlMapper() != null)
                ? action.sqlMapper().apply(table)
                : table.updateSql() ;
    }

    private <X extends CasesOfId<?>> Tuple insertTuple(Action action, X table) {
        return (action.tupleMapper() != null)
                ? action.tupleMapper().apply(table)
                : table.caseInsertTuple() ;
    }

    private <X extends CasesOfId<?>> Tuple updateTuple(Action action, X table) {
        return (action.tupleMapper() != null)
                ? action.tupleMapper().apply(table)
                : table.updateTuple() ;
    }

    private Tuple tuple(Action action, Object o) {
        return (action.tupleMapper() != null)
                ? action.tupleMapper().apply(o)
                : Tuple.tuple() ;
    }

    private Function<? super RowIterator<Row>, ? extends Optional<?>> iteratorNextMapper(Action action, String actName) {
        return (action.iteratorNextMapper() == null)
                ? job.iteratorNextFunctionMapper(actName)
                : action.iteratorNextMapper();
    }
}