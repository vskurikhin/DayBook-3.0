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
import su.svn.daybook.domain.model.Codifier;
import su.svn.daybook.domain.model.UserName;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UserNameDao {

    private static final Logger LOG = Logger.getLogger(UserNameDao.class);

    @Inject
    io.vertx.mutiny.pgclient.PgPool client;

    public Multi<UserName> findAll() {
        LOG.trace("findAll");
        return UserName.findAll(client);
    }

    public Uni<Optional<UserName>> findById(UUID id) {
        return UserName.findById(client, id)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<UUID>> insert(UserName entry) {
        return entry.insert(client)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<UUID>> update(UserName entry) {
        return entry.update(client)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<UUID>> delete(UUID id) {
        return UserName.delete(client, id)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<Long>> count() {
        return UserName.count(client)
                .map(Optional::ofNullable);
    }
}
