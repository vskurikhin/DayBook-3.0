/*
 * This file was last modified at 2024-10-30 19:05 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * AbstractOneToOneJob.java
 * $Id$
 */

package su.svn.daybook3.transaction.one_to_one;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowIterator;
import jakarta.annotation.Nonnull;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import su.svn.daybook3.domain.model.CasesOfId;
import su.svn.daybook3.transaction.ActionJob;

import java.io.Serializable;
import java.util.Collections;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class AbstractOneToOneJob<
        MainId extends Comparable<? extends Serializable>,
        MainTable extends CasesOfId<MainId>,
        JoinId extends Comparable<? extends Serializable>,
        JoinTable extends CasesOfId<JoinId>,
        Field extends Comparable<? extends Serializable>> extends ActionJob {

    private final OneToOneHelperFactory<MainId, MainTable, JoinId, JoinTable, Field> helperFactory;
    private final Logger log;

    @Inject
    io.vertx.mutiny.pgclient.PgPool pool;

    public AbstractOneToOneJob(
            @Nonnull BiFunction<MainTable, JoinId, MainTable> tableBuilder,
            @Nonnull Function<Field, JoinTable> joinFieldBuilder,
            @Nonnull Logger log) {
        this.log = log;
        var map = Collections.unmodifiableMap(super.getActionsOfMethods());
        this.helperFactory = new OneToOneHelperFactory<>(this, map, tableBuilder, joinFieldBuilder);
    }

    public abstract Uni<Optional<MainId>> insert(MainTable table, Field field);

    protected abstract Function<RowIterator<Row>, Optional<?>> iteratorNextMapper(String actionName);

    protected abstract Function<Optional<?>, Optional<JoinId>> castOptionalJoinId();

    protected abstract Function<Optional<?>, Optional<MainId>> castOptionalMainId();

    public abstract Uni<Optional<MainId>> update(MainTable table, Field field);

    public abstract Uni<Optional<MainId>> delete(MainTable table);

    protected Uni<Optional<MainId>> doInsert(MainTable table, Field field) {
        log.tracef("doInsert(%s, %s)", table, field);
        var helper = helperFactory.createInsertHelper(table, field);
        return pool.withTransaction(helper);
    }

    protected Uni<Optional<MainId>> doUpdate(MainTable table, Field field) {
        log.tracef("doUpdate(%s, %s)", table, field);
        var helper = helperFactory.createUpdateHelper(table, field);
        return pool.withTransaction(helper);
    }

    protected Uni<Optional<MainId>> doDelete(MainTable table) {
        log.tracef("doDelete(%s)", table);
        var helper = helperFactory.createDeleteHelper(table);
        return pool.withTransaction(helper);
    }
}
