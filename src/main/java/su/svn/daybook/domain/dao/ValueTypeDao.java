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
import su.svn.daybook.domain.model.ValueType;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class ValueTypeDao {

    private static final Logger LOG = Logger.getLogger(ValueTypeDao.class);

    @Inject
    io.vertx.mutiny.pgclient.PgPool client;

    public Multi<ValueType> findAll() {
        LOG.trace("findAll()");
        return ValueType.findAll(client);
    }

    public Uni<Optional<ValueType>> findById(Long id) {
        LOG.tracef("findById(%s)", id);
        return ValueType.findById(client, id)
                .map(Optional::ofNullable);
    }

    public Multi<ValueType> findRange(long offset, long limit) {
        LOG.tracef("findRange(%d, %d)", offset, limit);
        return ValueType.findRange(client, offset, limit);
    }

    public Uni<Optional<Long>> insert(ValueType entry) {
        LOG.tracef("insert(%s)", entry);
        return entry.insert(client)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<Long>> update(ValueType entry) {
        LOG.tracef("update(%s)", entry);
        return entry.update(client)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<Long>> delete(Long id) {
        LOG.tracef("delete(%s)", id);
        return ValueType.delete(client, id)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<Long>> count() {
        LOG.trace("count()");
        return ValueType.count(client)
                .map(Optional::ofNullable);
    }
}
