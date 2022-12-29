/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * WordDao.java
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
        LOG.trace("findAll()");
        return Word.findAll(client);
    }

    public Uni<Optional<Word>> findById(String id) {
        LOG.tracef("findById(%s)", id);
        return Word.findById(client, id)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<Word>> findByWord(String word) {
        return findById(word);
    }

    public Multi<Word> findRange(long offset, long limit) {
        LOG.tracef("findRange(%d, %d)", offset, limit);
        return Word.findRange(client, offset, limit);
    }

    public Uni<Optional<String>> insert(Word entry) {
        LOG.tracef("insert(%s)", entry);
        return entry.insert(client)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<String>> update(Word entry) {
        LOG.tracef("update(%s)", entry);
        return entry.update(client)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<String>> delete(String id) {
        LOG.tracef("delete(%s)", id);
        return Word.delete(client, id)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<Long>> count() {
        LOG.trace("count()");
        return Word.count(client)
                .map(Optional::ofNullable);
    }
}
