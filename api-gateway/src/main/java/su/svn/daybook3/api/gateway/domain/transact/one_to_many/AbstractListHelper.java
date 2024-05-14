/*
 * This file was last modified at 2024-05-14 21:36 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * AbstractListHelper.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.domain.transact.one_to_many;

import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowIterator;
import io.vertx.mutiny.sqlclient.SqlConnection;
import jakarta.annotation.Nonnull;
import su.svn.daybook3.api.gateway.domain.model.CasesOfId;
import su.svn.daybook3.api.gateway.domain.transact.Action;
import su.svn.daybook3.api.gateway.domain.transact.ListHelper;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

abstract class AbstractListHelper<
        MainId extends Comparable<? extends Serializable>,
        MainTable extends CasesOfId<MainId>,
        SubId extends Comparable<? extends Serializable>,
        Subsidiary extends CasesOfId<SubId>>
        implements ListHelper<MainId> {

    protected final AbstractOneToManyJob<MainId, MainTable, SubId, Subsidiary> job;
    protected final Map<String, Action> map;
    protected volatile SqlConnection connection;

    AbstractListHelper(
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
        return (action.iteratorNextMapper() == null)
                ? job.iteratorNextMapper(aName)
                : action.iteratorNextMapper();
    }
}
