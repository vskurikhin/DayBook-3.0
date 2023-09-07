/*
 * This file was last modified at 2023.09.07 14:07 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * AbstractManyToManyJob.java
 * $Id$
 */

package su.svn.daybook.domain.transact.many_to_many;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.Pool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowIterator;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.model.CasesOfId;

import jakarta.annotation.Nonnull;
import su.svn.daybook.domain.transact.ActionJob;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;

public abstract class AbstractManyToManyJob<
        MainId extends Comparable<? extends Serializable>,
        MainTable extends CasesOfId<MainId>,
        SubId extends Comparable<? extends Serializable>,
        Subsidiary extends CasesOfId<SubId>,
        MainField extends Comparable<? extends Serializable>,
        SubField extends Comparable<? extends Serializable>> extends ActionJob {

    private final Function<MainTable, MainField> getMainField;
    private final ManyToManyHelperFactory<MainId, MainTable, SubId, Subsidiary, MainField, SubField> helperFactory;
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

    protected abstract Function<Optional<?>, Optional<SubId>> castOptionalSubId();

    public abstract Uni<Optional<MainId>> insert(MainTable table, Collection<SubField> collection);

    public abstract Uni<Optional<MainId>> update(MainTable table, Collection<SubField> collection);

    public abstract Uni<Optional<MainId>> delete(MainTable table);

    protected Uni<Optional<MainId>> doInsert(MainTable table, Collection<SubField> collection) {
        log.tracef("doInsert(%s, %s)", table, collection);
        var helper = helperFactory.createInsertHelper(table, getMainField.apply(table), collection);
        return pool.withTransaction(helper);
    }

    protected Uni<Optional<MainId>> doUpdate(MainTable table, Collection<SubField> collection) {
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