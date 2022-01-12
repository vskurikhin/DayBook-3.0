/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * VocabularyDao.java
 * $Id$
 */

package su.svn.daybook.domain.dao;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.model.Vocabulary;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class VocabularyDao {

    private static final Logger LOG = Logger.getLogger(VocabularyDao.class);

    @Inject
    io.vertx.mutiny.pgclient.PgPool client;

    public Multi<Vocabulary> findAll() {
        LOG.trace("findAll");
        Multi<Vocabulary> result = Vocabulary.findAll(client);
        LOG.tracef("findAll result: %s", result);
        return Vocabulary.findAll(client);
    }

    public Uni<Optional<Vocabulary>> findById(Long id) {
        return Vocabulary.findById(client, id)
                .map(t -> t != null ? Optional.of(t) : Optional.empty());
    }

    public Uni<Optional<Long>> insert(Vocabulary entry) {
        return entry.insert(client)
                .map(t -> t != null ? Optional.of(t) : Optional.empty());
    }

    public Uni<Optional<Long>> update(Vocabulary entry) {
        return entry.update(client)
                .map(t -> t != null ? Optional.of(t) : Optional.empty());
    }

    public Uni<Optional<Long>> delete(Long id) {
        return Vocabulary.delete(client, id)
                .map(t -> t != null ? Optional.of(t) : Optional.empty());
    }
}
