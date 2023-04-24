/*
 * This file was last modified at 2023.04.23 16:30 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * AbstractManyToManyJob.java
 * $Id$
 */

package su.svn.daybook.domain.transact;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.Pool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowIterator;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.model.CasesOfId;
import su.svn.daybook.domain.model.UserNameTable;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public abstract class AbstractManyToManyJob<
        MainId extends Comparable<? extends Serializable>,
        Main extends CasesOfId<MainId>,
        SubId extends Comparable<? extends Serializable>,
        Subsidiary extends CasesOfId<SubId>,
        MainField extends Comparable<? extends Serializable>,
        SubField extends Comparable<? extends Serializable>> extends ActionJob {

    public abstract Uni<Optional<MainId>> insert(Main table, Collection<SubField> collection);

    public abstract Uni<Optional<MainId>> update(Main table, Collection<SubField> collection);

    protected abstract Function<RowIterator<Row>, Optional<?>> iteratorNextMapper(String actionName);

    protected abstract Function<Optional<?>, Optional<MainId>> castOptionalMainId();

    protected abstract Function<Optional<?>, Optional<SubId>> castOptionalSubId();

    private final Logger log;
    private final Pool pool;
    private final Function<Main, MainField> getMainField;
    private final ManyToManyHelperFactory<MainId, Main, SubId, Subsidiary, MainField, SubField> helperFactory;

    AbstractManyToManyJob(@Nonnull Pool pool, @Nonnull Function<Main, MainField> getMainField, @Nonnull Logger log) {
        this.log = log;
        this.pool = pool;
        this.getMainField = getMainField;
        var map = Collections.unmodifiableMap(super.getActionsOfMethods());
        this.helperFactory = new ManyToManyHelperFactory<>(this, map);
    }

    protected Uni<Optional<MainId>> doInsert(Main table, Collection<SubField> collection) {
        log.tracef("doInsert(%s, %s)", table, collection);
        var helper = helperFactory.createInsertHelper(table, getMainField.apply(table), collection);
        return pool.withTransaction(helper);
    }

    protected Uni<Optional<MainId>> doUpdate(Main table, Collection<SubField> collection) {
        log.tracef("doUpdate(%s, %s)", table, collection);
        var helper = helperFactory.createUpdateHelper(table, getMainField.apply(table), collection);
        return pool.withTransaction(helper);
    }

    protected Uni<Optional<MainId>> doDelete(Main table) {
        log.tracef("doDelete(%s)", table);
        var helper = helperFactory.createDeleteHelper(table);
        return pool.withTransaction(helper);
    }
}