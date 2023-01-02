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
import su.svn.daybook.domain.model.VocabularyTable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class VocabularyDao {

    private static final Logger LOG = Logger.getLogger(VocabularyDao.class);

    @Inject
    io.vertx.mutiny.pgclient.PgPool client;

    public Multi<VocabularyTable> findAll() {
        LOG.trace("findAll()");
        return VocabularyTable.findAll(client);
    }

    public Uni<Optional<VocabularyTable>> findById(Long id) {
        LOG.tracef("findById(%s)", id);
        return VocabularyTable.findById(client, id)
                .map(Optional::ofNullable);
    }

    public Multi<VocabularyTable> findRange(long offset, long limit) {
        LOG.tracef("findRange(%d, %d)", offset, limit);
        return VocabularyTable.findRange(client, offset, limit);
    }

    public Uni<Optional<Long>> insert(VocabularyTable entry) {
        LOG.tracef("insert(%s)", entry);
        return entry.insert(client)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<Long>> update(VocabularyTable entry) {
        LOG.tracef("update(%s)", entry);
        return entry.update(client)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<Long>> delete(Long id) {
        LOG.tracef("delete(%s)", id);
        return VocabularyTable.delete(client, id)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<Long>> count() {
        LOG.trace("count()");
        return VocabularyTable.count(client)
                .map(Optional::ofNullable);
    }
}
