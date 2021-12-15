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
import su.svn.daybook.domain.model.I18n;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class I18nDao {

    private static final Logger LOG = Logger.getLogger(I18nDao.class);

    @Inject
    io.vertx.mutiny.pgclient.PgPool client;

    public Multi<I18n> findAll() {
        LOG.trace("findAll");
        return I18n.findAll(client);
    }

    public Uni<Optional<I18n>> findById(Long id) {
        LOG.tracef("findById(%d)", id);
        return I18n.findById(client, id)
                .map(t -> t != null ? Optional.of(t) : Optional.empty());
    }

    public Uni<Optional<Long>> insert(I18n entry) {
        LOG.tracef("insert(%s)", entry);
        return entry.insert(client)
                .map(t -> t != null ? Optional.of(t) : Optional.empty());
    }

    public Uni<Optional<Long>> update(I18n entry) {
        LOG.tracef("update(%s)", entry);
        return entry.update(client)
                .map(t -> t != null ? Optional.of(t) : Optional.empty());
    }

    public Uni<Optional<Long>> delete(I18n entry) {
        LOG.tracef("delete(%s)", entry);
        return entry.delete(client)
                .map(t -> t != null ? Optional.of(t) : Optional.empty());
    }
}
