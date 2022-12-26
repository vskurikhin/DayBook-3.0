/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * @Name@Dao.java
 * $Id$
 */

package su.svn.daybook.domain.dao;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.model.@Name@;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class @Name@Dao {

    private static final Logger LOG = Logger.getLogger(@Name@Dao.class);

    @Inject
    io.vertx.mutiny.pgclient.PgPool client;

    public Multi<@Name@> findAll() {
        LOG.trace("findAll");
        return @Name@.findAll(client);
    }

    public Uni<Optional<@Name@>> findById(Long id) {
        return @Name@.findById(client, id)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<Long>> insert(@Name@ entry) {
        return entry.insert(client)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<Long>> update(@Name@ entry) {
        return entry.update(client)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<Long>> delete(Long id) {
        return @Name@.delete(client, id)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<Long>> count() {
        return @Name@.count(client)
                .map(Optional::ofNullable);
    }
}
