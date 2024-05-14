/*
 * This file was last modified at 2024-05-14 21:36 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * AbstractOneToManyJob.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.domain.transact.one_to_many;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowIterator;
import jakarta.annotation.Nonnull;
import jakarta.inject.Inject;
import org.apache.commons.lang3.tuple.Pair;
import org.jboss.logging.Logger;
import su.svn.daybook3.api.gateway.domain.model.CasesOfId;
import su.svn.daybook3.api.gateway.domain.transact.ActionJob;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public abstract class AbstractOneToManyJob<
        MainId extends Comparable<? extends Serializable>,
        MainTable extends CasesOfId<MainId>,
        SubId extends Comparable<? extends Serializable>,
        Subsidiary extends CasesOfId<SubId>> extends ActionJob {

    private final OneToManyHelperFactory<MainId, MainTable, SubId, Subsidiary> helperFactory;
    private final Logger log;

    @Inject
    io.vertx.mutiny.pgclient.PgPool pool;

    public AbstractOneToManyJob(@Nonnull Logger log) {
        this.log = log;
        var map = Collections.unmodifiableMap(super.getActionsOfMethods());
        this.helperFactory = new OneToManyHelperFactory<>(this, map);
    }

    protected abstract Function<RowIterator<Row>, Optional<?>> iteratorNextMapper(String actionName);

    protected abstract Function<Optional<?>, Optional<MainId>> castOptionalMainId();

    protected abstract Function<Optional<?>, Optional<SubId>> castOptionalSubId();

    public abstract Uni<List<MainId>> upsert(Collection<Pair<MainTable, Collection<Subsidiary>>> collection);

    public abstract Uni<Optional<MainId>> delete(MainTable table);

    protected Uni<List<MainId>> doUpsert(Collection<Pair<MainTable, Collection<Subsidiary>>> collection) {
        log.tracef("doUpsert(%s)", collection);
        var helper = helperFactory.createUpsertHelper(collection);
        return pool.withTransaction(helper);
    }

    protected Uni<Optional<MainId>> doDelete(MainTable table) {
        log.tracef("doDelete(%s)", table);
        var helper = helperFactory.createDeleteHelper(table);
        return pool.withTransaction(helper);
    }
}