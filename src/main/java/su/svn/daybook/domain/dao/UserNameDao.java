/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * UserNameDao.java
 * $Id$
 */

package su.svn.daybook.domain.dao;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.model.UserNameTable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UserNameDao {

    private static final Logger LOG = Logger.getLogger(UserNameDao.class);

    @Inject
    io.vertx.mutiny.pgclient.PgPool client;

    public Multi<UserNameTable> findAll() {
        LOG.trace("findAll");
        return UserNameTable.findAll(client);
    }

    public Uni<Optional<UserNameTable>> findById(UUID id) {
        LOG.tracef("findById(%s)", id);
        return UserNameTable.findById(client, id)
                .map(Optional::ofNullable);
    }

    public Multi<UserNameTable> findRange(long offset, long limit) {
        LOG.tracef("findRange(%d, %d)", offset, limit);
        return UserNameTable.findRange(client, offset, limit);
    }

    public Uni<Optional<UUID>> insert(UserNameTable entry) {
        LOG.tracef("insert(%s)", entry);
        return entry.insert(client)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<UUID>> update(UserNameTable entry) {
        LOG.tracef("update(%s)", entry);
        return entry.update(client)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<UUID>> delete(UUID id) {
        LOG.tracef("delete(%s)", id);
        return UserNameTable.delete(client, id)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<Long>> count() {
        LOG.trace("count()");
        return UserNameTable.count(client)
                .map(Optional::ofNullable);
    }
}
