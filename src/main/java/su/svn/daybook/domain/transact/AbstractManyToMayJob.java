/*
 * This file was last modified at 2023.01.06 11:06 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * AbstractManyToMayJob.java
 * $Id$
 */

package su.svn.daybook.domain.transact;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.Pool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowIterator;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.model.CasesOfId;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;

abstract class AbstractManyToMayJob<
        K extends Comparable<? extends Serializable>,
        D extends CasesOfId<K>,
        E extends Comparable<E>,
        T extends Comparable<T>> {

    private final Logger log;
    private final Pool pool;

    private final Function<D, E> getEntry;

    AbstractManyToMayJob(@Nonnull Pool pool, @Nonnull Function<D, E> getEntry, @Nonnull Logger log) {
        this.log = log;
        this.pool = pool;
        this.getEntry = getEntry;
    }

    protected abstract @Nonnull
    String caseInsertSql(D table);

    protected abstract K getKeyByIteratorNext(RowIterator<Row> iterator);

    protected abstract @Nonnull
    String sqlCountOfNotExisted();

    protected abstract @Nonnull
    String sqlClearHasRelation();

    protected abstract @Nonnull
    String sqlDeleteFromTable();

    protected abstract @Nonnull
    String sqlDeleteFromHasRelationByEntry();

    protected abstract @Nonnull
    String sqlUpdateTable();

    protected abstract @Nonnull
    String sqlUpdateHasRelation2();

    protected abstract @Nonnull
    String sqlUpdateHasRelation4();

    public Uni<Optional<K>> insert(D table, Collection<T> collection) {
        log.tracef("insert(%s, %s, D -> E)", table, collection);
        var helper = new ManyToManyHelper<>(this, collection, table, this.getEntry.apply(table));
        return pool.withTransaction(helper::checkAndInsert);
    }

    public Uni<Optional<K>> update(D table, Collection<T> collection) {
        log.tracef("update(%s, %s, D -> E)", table, collection);
        var helper = new ManyToManyHelper<>(this, collection, table, this.getEntry.apply(table));
        return pool.withTransaction(helper::checkAndUpdate);
    }

    public Uni<Optional<K>> delete(D table) {
        log.tracef("delete(%s, D -> E)", table);
        var helper = new ManyToManyHelper<>(this, Collections.emptySet(), table, this.getEntry.apply(table));
        return pool.withTransaction(helper::delete);
    }
}
