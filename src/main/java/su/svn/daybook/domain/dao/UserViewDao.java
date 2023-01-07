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
import su.svn.daybook.domain.model.UserView;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UserViewDao {

    private static final Logger LOG = Logger.getLogger(UserViewDao.class);

    @Inject
    io.vertx.mutiny.pgclient.PgPool client;

    public Multi<UserView> findAll() {
        LOG.trace("findAll");
        return UserView.findAll(client);
    }

    public Uni<Optional<UserView>> findById(UUID id) {
        LOG.tracef("findById(%s)", id);
        return UserView.findById(client, id)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<UserView>> findByUserName(String userName) {
        LOG.tracef("findByUserName(%s)", userName);
        return UserView.findByUserName(client, userName)
                .map(Optional::ofNullable);
    }

    public Multi<UserView> findRange(long offset, long limit) {
        LOG.tracef("findRange(%d, %d)", offset, limit);
        return UserView.findRange(client, offset, limit);
    }

    public Uni<Optional<Long>> count() {
        LOG.trace("count()");
        return UserView.count(client)
                .map(Optional::ofNullable);
    }
}
