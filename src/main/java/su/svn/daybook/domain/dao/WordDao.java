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
import su.svn.daybook.domain.model.Word;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class WordDao {

    private static final Logger LOG = Logger.getLogger(WordDao.class);

    @Inject
    io.vertx.mutiny.pgclient.PgPool client;

    public Multi<Word> findAll() {
        LOG.trace("findAll");
        Multi<Word> result = Word.findAll(client);
        LOG.tracef("findAll result: %s", result);
        return Word.findAll(client);
    }

    public Uni<Optional<Word>> findByWord(String word) {
        return Word.findByWord(client, word)
                .map(t -> t != null ? Optional.of(t) : Optional.empty());
    }

    public Uni<Optional<String>> insert(Word entry) {
        return entry.insert(client)
                .map(t -> t != null ? Optional.of(t) : Optional.empty());
    }

    public Uni<Optional<String>> update(Word entry) {
        return entry.update(client)
                .map(t -> t != null ? Optional.of(t) : Optional.empty());
    }

    public Uni<Optional<String>> delete(String id) {
        return Word.delete(client, id)
                .map(t -> t != null ? Optional.of(t) : Optional.empty());
    }
}
