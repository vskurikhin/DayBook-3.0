/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * SessionDao.java
 * $Id$
 */

package su.svn.daybook.domain.dao;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.model.SessionTable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class SessionDao {

    private static final Logger LOG = Logger.getLogger(SessionDao.class);

    @Inject
    io.vertx.mutiny.pgclient.PgPool client;

    public Multi<SessionTable> findAll() {
        LOG.trace("findAll()");
        return SessionTable.findAll(client);
    }

    public Uni<Optional<SessionTable>> findById(UUID id) {
        LOG.tracef("findById(%s)", id);
        return SessionTable.findById(client, id)
                .map(Optional::ofNullable);
    }

    public Multi<SessionTable> findRange(long offset, long limit) {
        LOG.tracef("findRange(%d, %d)", offset, limit);
        return SessionTable.findRange(client, offset, limit);
    }

    public Uni<Optional<UUID>> insert(SessionTable entry) {
        LOG.tracef("insert(%s)", entry);
        return entry.insert(client)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<UUID>> update(SessionTable entry) {
        LOG.tracef("update(%s)", entry);
        return entry.update(client)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<UUID>> delete(UUID id) {
        LOG.tracef("delete(%s)", id);
        return SessionTable.delete(client, id)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<Long>> count() {
        LOG.trace("count()");
        return SessionTable.count(client)
                .map(Optional::ofNullable);
    }
}
