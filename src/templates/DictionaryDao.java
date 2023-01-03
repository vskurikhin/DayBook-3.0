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
import su.svn.daybook.domain.model.@Name@Table;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class @Name@Dao {

    private static final Logger LOG = Logger.getLogger(@Name@Dao.class);

    @Inject
    io.vertx.mutiny.pgclient.PgPool client;

    public Multi<@Name@Table> findAll() {
        LOG.trace("findAll()");
        return @Name@Table.findAll(client);
    }

    public Uni<Optional<@Name@Table>> findById(@IdType@ id) {
        LOG.tracef("findById(%s)", id);
        return @Name@Table.findById(client, id)
                .map(Optional::ofNullable);
    }

    public Multi<@Name@Table> findRange(long offset, long limit) {
        LOG.tracef("findRange(%d, %d)", offset, limit);
        return @Name@Table.findRange(client, offset, limit);
    }

    public Uni<Optional<@IdType@>> insert(@Name@Table entry) {
        LOG.tracef("insert(%s)", entry);
        return entry.insert(client)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<@IdType@>> update(@Name@Table entry) {
        LOG.tracef("update(%s)", entry);
        return entry.update(client)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<@IdType@>> delete(@IdType@ id) {
        LOG.tracef("delete(%s)", id);
        return @Name@Table.delete(client, id)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<Long>> count() {
        LOG.trace("count()");
        return @Name@Table.count(client)
                .map(Optional::ofNullable);
    }
}
