/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * LanguageDao.java
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
        LOG.trace("findAll()");
        return Language.findAll(client);
    }

    public Uni<Optional<Language>> findById(Long id) {
        LOG.tracef("findById(%s)", id);
        return Language.findById(client, id)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<Long>> insert(Language entry) {
        LOG.tracef("insert(%s)", entry);
        return entry.insert(client)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<Long>> update(Language entry) {
        LOG.tracef("update(%s)", entry);
        return entry.update(client)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<Long>> delete(Long id) {
        LOG.tracef("delete(%s)", id);
        return Language.delete(client, id)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<Long>> count() {
        LOG.trace("count()");
        return Language.count(client)
                .map(Optional::ofNullable);
    }
}
