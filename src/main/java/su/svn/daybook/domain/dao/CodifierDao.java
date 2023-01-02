/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * CodifierDao.java
 * $Id$
 */

package su.svn.daybook.domain.dao;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.model.CodifierTable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class CodifierDao {

    private static final Logger LOG = Logger.getLogger(CodifierDao.class);

    @Inject
    io.vertx.mutiny.pgclient.PgPool client;

    public Multi<CodifierTable> findAll() {
        LOG.trace("findAll()");
        return CodifierTable.findAll(client);
    }

    public Uni<Optional<CodifierTable>> findById(String id) {
        LOG.tracef("findById(%s)", id);
        return CodifierTable.findById(client, id)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<CodifierTable>> findByCode(String code) {
        return findById(code);
    }

    public Multi<CodifierTable> findRange(long offset, long limit) {
        LOG.tracef("findRange(%d, %d)", offset, limit);
        return CodifierTable.findRange(client, offset, limit);
    }

    public Uni<Optional<String>> insert(CodifierTable entry) {
        LOG.tracef("insert(%s)", entry);
        return entry.insert(client)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<String>> update(CodifierTable entry) {
        LOG.tracef("update(%s)", entry);
        return entry.update(client)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<String>> delete(String id) {
        LOG.tracef("delete(%s)", id);
        return CodifierTable.delete(client, id)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<Long>> count() {
        LOG.trace("count()");
        return CodifierTable.count(client)
                .map(Optional::ofNullable);
    }
}
