/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * ValueTypeDao.java
 * $Id$
 */

package su.svn.daybook.domain.dao;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.model.ValueTypeTable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class ValueTypeDao {

    private static final Logger LOG = Logger.getLogger(ValueTypeDao.class);

    @Inject
    io.vertx.mutiny.pgclient.PgPool client;

    public Multi<ValueTypeTable> findAll() {
        LOG.trace("findAll()");
        return ValueTypeTable.findAll(client);
    }

    public Uni<Optional<ValueTypeTable>> findById(Long id) {
        LOG.tracef("findById(%s)", id);
        return ValueTypeTable.findById(client, id)
                .map(Optional::ofNullable);
    }

    public Multi<ValueTypeTable> findRange(long offset, long limit) {
        LOG.tracef("findRange(%d, %d)", offset, limit);
        return ValueTypeTable.findRange(client, offset, limit);
    }

    public Uni<Optional<Long>> insert(ValueTypeTable entry) {
        LOG.tracef("insert(%s)", entry);
        return entry.insert(client)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<Long>> update(ValueTypeTable entry) {
        LOG.tracef("update(%s)", entry);
        return entry.update(client)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<Long>> delete(Long id) {
        LOG.tracef("delete(%s)", id);
        return ValueTypeTable.delete(client, id)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<Long>> count() {
        LOG.trace("count()");
        return ValueTypeTable.count(client)
                .map(Optional::ofNullable);
    }
}
