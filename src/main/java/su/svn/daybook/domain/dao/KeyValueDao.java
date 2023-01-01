/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * KeyValueDao.java
 * $Id$
 */

package su.svn.daybook.domain.dao;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.model.KeyValue;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class KeyValueDao {

    private static final Logger LOG = Logger.getLogger(KeyValueDao.class);

    @Inject
    io.vertx.mutiny.pgclient.PgPool client;

    public Multi<KeyValue> findAll() {
        LOG.trace("findAll()");
        return KeyValue.findAll(client);
    }

    public Uni<Optional<KeyValue>> findById(Long id) {
        LOG.tracef("findById(%s)", id);
        return KeyValue.findById(client, id)
                .map(Optional::ofNullable);
    }

    public Multi<KeyValue> findRange(long offset, long limit) {
        LOG.tracef("findRange(%d, %d)", offset, limit);
        return KeyValue.findRange(client, offset, limit);
    }

    public Uni<Optional<Long>> insert(KeyValue entry) {
        LOG.tracef("insert(%s)", entry);
        return entry.insert(client)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<Long>> update(KeyValue entry) {
        LOG.tracef("update(%s)", entry);
        return entry.update(client)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<Long>> delete(Long id) {
        LOG.tracef("delete(%s)", id);
        return KeyValue.delete(client, id)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<Long>> count() {
        LOG.trace("count()");
        return KeyValue.count(client)
                .map(Optional::ofNullable);
    }
}
