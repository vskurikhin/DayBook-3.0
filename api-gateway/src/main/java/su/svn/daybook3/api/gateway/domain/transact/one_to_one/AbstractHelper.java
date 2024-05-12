/*
 * This file was last modified at 2024-05-14 21:25 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * AbstractHelper.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.domain.transact.one_to_one;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowIterator;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.SqlConnection;
import jakarta.annotation.Nonnull;
import su.svn.daybook3.api.gateway.domain.model.CasesOfId;
import su.svn.daybook3.api.gateway.domain.transact.Action;
import su.svn.daybook3.api.gateway.domain.transact.Helper;
import su.svn.daybook3.api.gateway.domain.transact.OptionalHelper;
import su.svn.daybook3.api.gateway.models.Constants;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

abstract class AbstractHelper<
        MainId extends Comparable<? extends Serializable>,
        MainTable extends CasesOfId<MainId>,
        JoinId extends Comparable<? extends Serializable>,
        JoinTable extends CasesOfId<JoinId>,
        Field extends Comparable<? extends Serializable>>
        implements OptionalHelper<MainId> {

    protected final AbstractOneToOneJob<MainId, MainTable, JoinId, JoinTable, Field> job;
    protected final Map<String, Map<String, Action>> mapJob;
    protected final MainTable table;
    protected volatile SqlConnection connection;

    AbstractHelper(
            @Nonnull AbstractOneToOneJob<MainId, MainTable, JoinId, JoinTable, Field> job,
            @Nonnull Map<String, Map<String, Action>> map,
            @Nonnull MainTable table) {
        this.job = job;
        this.mapJob = map;
        this.table = table;
    }

    protected void setConnection(@Nonnull final SqlConnection connection) {
        if (this.connection == null) {
            synchronized (this) {
                this.connection = connection;
            }
        }
    }

    protected Uni<Optional<JoinId>> findFieldId(Action action, Field field) {
        return this.connection
                .preparedQuery(action.sql())
                .execute(Helper.tuple(action, field))
                .map(RowSet::iterator)
                .map(iteratorNextMapper(action, Constants.FIND_FIELD_ID))
                .map(job.castOptionalJoinId())
                .log();
    }

    protected Uni<Optional<JoinId>> insertJoin(Action action, JoinTable join) {
        return this.connection
                .preparedQuery(String.format(Helper.insertSql(action, join), Constants.ID))
                .execute(Helper.insertTuple(action, join))
                .map(RowSet::iterator)
                .map(iteratorNextMapper(action, Constants.INSERT_INTO_RELATION))
                .map(job.castOptionalJoinId())
                .log();
    }

    protected Function<? super RowIterator<Row>, ? extends Optional<?>> iteratorNextMapper(
            Action action, String actName) {
        return (isNullIteratorNextMapper(action))
                ? job.iteratorNextMapper(actName)
                : action.iteratorNextMapper();
    }

    protected Uni<Optional<MainId>> getFailure() {
        return Uni.createFrom().failure(new RuntimeException()); // TODO Custom Exception
    }
}
