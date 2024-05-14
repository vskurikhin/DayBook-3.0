/*
 * This file was last modified at 2024-05-14 21:36 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * AbstractManyToManyJob.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.domain.transact.many_to_many;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowIterator;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import su.svn.daybook3.api.gateway.domain.model.CasesOfId;

import jakarta.annotation.Nonnull;
import su.svn.daybook3.api.gateway.domain.transact.ActionJob;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;

public abstract class AbstractManyToManyJob<
        MainId extends Comparable<? extends Serializable>,
        MainTable extends CasesOfId<MainId>,
        RelId extends Comparable<? extends Serializable>,
        Relative extends CasesOfId<RelId>,
        MainField extends Comparable<? extends Serializable>,
        RelField extends Comparable<? extends Serializable>> extends ActionJob {

    private final Function<MainTable, MainField> getMainField;
    private final ManyToManyHelperFactory<MainId, MainTable, RelId, Relative, MainField, RelField> helperFactory;
    private final Logger log;

    @Inject
    io.vertx.mutiny.pgclient.PgPool pool;

    public AbstractManyToManyJob(@Nonnull Function<MainTable, MainField> getMainField, @Nonnull Logger log) {
        this.log = log;
        this.getMainField = getMainField;
        var map = Collections.unmodifiableMap(super.getActionsOfMethods());
        this.helperFactory = new ManyToManyHelperFactory<>(this, map);
    }

    protected abstract Function<RowIterator<Row>, Optional<?>> iteratorNextMapper(String actionName);

    protected abstract Function<Optional<?>, Optional<MainId>> castOptionalMainId();

    protected abstract Function<Optional<?>, Optional<RelId>> castOptionalRelativeId();

    public abstract Uni<Optional<MainId>> insert(MainTable table, Collection<RelField> collection);

    public abstract Uni<Optional<MainId>> update(MainTable table, Collection<RelField> collection);

    public abstract Uni<Optional<MainId>> delete(MainTable table);

    protected Uni<Optional<MainId>> doInsert(MainTable table, Collection<RelField> collection) {
        log.tracef("doInsert(%s, %s)", table, collection);
        var helper = helperFactory.createInsertHelper(table, getMainField.apply(table), collection);
        return pool.withTransaction(helper);
    }

    protected Uni<Optional<MainId>> doUpdate(MainTable table, Collection<RelField> collection) {
        log.tracef("doUpdate(%s, %s)", table, collection);
        var helper = helperFactory.createUpdateHelper(table, getMainField.apply(table), collection);
        return pool.withTransaction(helper);
    }

    protected Uni<Optional<MainId>> doDelete(MainTable table) {
        log.tracef("doDelete(%s)", table);
        var helper = helperFactory.createDeleteHelper(table, getMainField.apply(table));
        return pool.withTransaction(helper);
    }
}