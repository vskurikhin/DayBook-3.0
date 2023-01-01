/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * TagLabelDao.java
 * $Id$
 */

package su.svn.daybook.domain.dao;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.model.TagLabel;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class TagLabelDao {

    private static final Logger LOG = Logger.getLogger(TagLabelDao.class);

    @Inject
    io.vertx.mutiny.pgclient.PgPool client;

    public Multi<TagLabel> findAll() {
        LOG.trace("findAll()");
        return TagLabel.findAll(client);
    }

    public Uni<Optional<TagLabel>> findById(String id) {
        LOG.tracef("findById(%s)", id);
        return TagLabel.findById(client, id)
                .map(Optional::ofNullable);
    }

    public Multi<TagLabel> findRange(long offset, long limit) {
        LOG.tracef("findRange(%d, %d)", offset, limit);
        return TagLabel.findRange(client, offset, limit);
    }

    public Uni<Optional<String>> insert(TagLabel entry) {
        LOG.tracef("insert(%s)", entry);
        return entry.insert(client)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<String>> update(TagLabel entry) {
        LOG.tracef("update(%s)", entry);
        return entry.update(client)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<String>> delete(String id) {
        LOG.tracef("delete(%s)", id);
        return TagLabel.delete(client, id)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<Long>> count() {
        LOG.trace("count()");
        return TagLabel.count(client)
                .map(Optional::ofNullable);
    }
}
