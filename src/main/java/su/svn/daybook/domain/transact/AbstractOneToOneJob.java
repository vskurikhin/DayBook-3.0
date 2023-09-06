/*
 * This file was last modified at 2023.09.06 17:04 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * AbstractOneToOneJob.java
 * $Id$
 */

package su.svn.daybook.domain.transact;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.Pool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowIterator;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.model.CasesOfId;

import jakarta.annotation.Nonnull;

import java.io.Serializable;
import java.util.Collections;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

abstract class AbstractOneToOneJob<
        MainId extends Comparable<? extends Serializable>,
        Main extends CasesOfId<MainId>,
        JoinId extends Comparable<? extends Serializable>,
        Join extends CasesOfId<JoinId>,
        Field extends Comparable<? extends Serializable>> extends ActionJob {

    public abstract Uni<Optional<MainId>> insert(Main table, Field field);

    public abstract Uni<Optional<MainId>> update(Main table, Field field);

    public abstract Uni<Optional<MainId>> delete(Main table);

    protected abstract Function<RowIterator<Row>, Optional<?>> iteratorNextMapper(String actionName);

    protected abstract Function<Optional<?>, Optional<JoinId>> castOptionalJoinId();

    protected abstract Function<Optional<?>, Optional<MainId>> castOptionalMainId();

    private final Logger log;
    private final Pool pool;
    private final OneToOneHelperFactory<MainId, Main, JoinId, Join, Field> helperFactory;

    AbstractOneToOneJob(
            @Nonnull Pool pool,
            @Nonnull BiFunction<Main, JoinId, Main> tableBuilder,
            @Nonnull Function<Field, Join> joinFieldBuilder,
            @Nonnull Logger log) {
        this.log = log;
        this.pool = pool;
        var map = Collections.unmodifiableMap(super.getActionsOfMethods());
        this.helperFactory = new OneToOneHelperFactory<>(this, map, tableBuilder, joinFieldBuilder);
    }

    protected Uni<Optional<MainId>> doInsert(Main table, Field field) {
        log.tracef("doInsert(%s, %s)", table, field);
        var helper = helperFactory.createInsertHelper(table, field);
        return pool.withTransaction(helper);
    }

    protected Uni<Optional<MainId>> doUpdate(Main table, Field field) {
        log.tracef("doUpdate(%s, %s)", table, field);
        var helper = helperFactory.createUpdateHelper(table, field);
        return pool.withTransaction(helper);
    }

    protected Uni<Optional<MainId>> doDelete(Main table) {
        log.tracef("doDelete(%s)", table);
        var helper = helperFactory.createDeleteHelper(table);
        return pool.withTransaction(helper);
    }
}
