/*
 * This file was last modified at 2021.12.06 18:10 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * TagLabelDao.java
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
        LOG.trace("findAll");
        Multi<Codifier> result = Codifier.findAll(client);
        LOG.tracef("findAll result: %s", result);
        return Codifier.findAll(client);
    }

    public Uni<Optional<Codifier>> findByCode(String code) {
        return Codifier.findByCode(client, code)
                .map(t -> t != null ? Optional.of(t) : Optional.empty());
    }

    public Uni<Optional<String>> insert(Codifier entry) {
        return entry.insert(client)
                .map(t -> t != null ? Optional.of(t) : Optional.empty());
    }

    public Uni<Optional<String>> update(Codifier entry) {
        return entry.update(client)
                .map(t -> t != null ? Optional.of(t) : Optional.empty());
    }

    public Uni<Optional<String>> delete(String code) {
        return Codifier.delete(client, code)
                .map(t -> t != null ? Optional.of(t) : Optional.empty());
    }
}
