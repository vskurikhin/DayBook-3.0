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
import su.svn.daybook.domain.model.Language;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class LanguageDao {

    private static final Logger LOG = Logger.getLogger(LanguageDao.class);

    @Inject
    io.vertx.mutiny.pgclient.PgPool client;

    public Multi<Language> findAll() {
        LOG.trace("findAll");
        Multi<Language> result = Language.findAll(client);
        LOG.tracef("findAll result: %s", result);
        return Language.findAll(client);
    }

    public Uni<Optional<Language>> findById(Long id) {
        return Language.findById(client, id)
                .map(t -> t != null ? Optional.of(t) : Optional.empty());
    }

    public Uni<Optional<Long>> insert(Language entry) {
        return entry.insert(client)
                .map(t -> t != null ? Optional.of(t) : Optional.empty());
    }

    public Uni<Optional<Long>> update(Language entry) {
        return entry.update(client)
                .map(t -> t != null ? Optional.of(t) : Optional.empty());
    }

    public Uni<Optional<Long>> delete(Long id) {
        return Language.delete(client, id)
                .map(t -> t != null ? Optional.of(t) : Optional.empty());
    }
}
