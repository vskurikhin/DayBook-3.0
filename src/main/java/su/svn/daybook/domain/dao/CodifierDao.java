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
import su.svn.daybook.domain.model.Codifier;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class CodifierDao {

    private static final Logger LOG = Logger.getLogger(CodifierDao.class);

    @Inject
    io.vertx.mutiny.pgclient.PgPool client;

    public Multi<Codifier> findAll() {
        LOG.trace("findAll()");
        return Codifier.findAll(client);
    }

    public Uni<Optional<Codifier>> findById(String id) {
        LOG.tracef("findById(%s)", id);
        return Codifier.findById(client, id)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<Codifier>> findByCode(String code) {
        return findById(code);
    }

    public Uni<Optional<String>> insert(Codifier entry) {
        LOG.tracef("insert(%s)", entry);
        return entry.insert(client)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<String>> update(Codifier entry) {
        LOG.tracef("update(%s)", entry);
        return entry.update(client)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<String>> delete(String id) {
        LOG.tracef("delete(%s)", id);
        return Codifier.delete(client, id)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<Long>> count() {
        LOG.trace("count()");
        return Codifier.count(client)
                .map(Optional::ofNullable);
    }
}
