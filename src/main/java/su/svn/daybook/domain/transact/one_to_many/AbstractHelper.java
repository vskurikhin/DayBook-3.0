/*
 * This file was last modified at 2023.11.19 16:20 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * AbstractHelper.java
 * $Id$
 */

package su.svn.daybook.domain.transact.one_to_many;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowIterator;
import io.vertx.mutiny.sqlclient.SqlConnection;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.annotation.Nonnull;
import su.svn.daybook.domain.model.CasesOfId;
import su.svn.daybook.domain.transact.Action;
import su.svn.daybook.domain.transact.OptionalHelper;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

abstract class AbstractHelper<
        MainId extends Comparable<? extends Serializable>,
        MainTable extends CasesOfId<MainId>,
        SubId extends Comparable<? extends Serializable>,
        Subsidiary extends CasesOfId<SubId>>
        implements OptionalHelper<MainId> {

    protected final AbstractOneToManyJob<MainId, MainTable, SubId, Subsidiary> job;
    protected final Map<String, Action> map;
    protected volatile SqlConnection connection;

    AbstractHelper(
            @Nonnull AbstractOneToManyJob<MainId, MainTable, SubId, Subsidiary> job,
            @Nonnull Map<String, Action> map) {
        this.job = job;
        this.map = map;
    }

    protected void setConnection(@Nonnull final SqlConnection connection) {
        if (this.connection == null) {
            synchronized (this) {
                this.connection = connection;
            }
        }
    }

    protected Function<? super RowIterator<Row>, ? extends Optional<?>> iteratorNextMapper(Action action, String aName) {
        return (isNullIteratorNextMapper(action))
                ? job.iteratorNextMapper(aName)
                : action.iteratorNextMapper();
    }

    protected Uni<Long> deleteAllFromHasRelationByEntry(Action action, MainId id) {
        return this.connection
                .preparedQuery(action.sql())
                .execute(Tuple.of(id))
                .onItem()
                .transform(pgRowSet -> (long) pgRowSet.rowCount())
                .log();
    }
}
