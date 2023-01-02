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
import su.svn.daybook.domain.model.TagLabelTable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class TagLabelDao {

    private static final Logger LOG = Logger.getLogger(TagLabelDao.class);

    @Inject
    io.vertx.mutiny.pgclient.PgPool client;

    public Multi<TagLabelTable> findAll() {
        LOG.trace("findAll()");
        return TagLabelTable.findAll(client);
    }

    public Uni<Optional<TagLabelTable>> findById(String id) {
        LOG.tracef("findById(%s)", id);
        return TagLabelTable.findById(client, id)
                .map(Optional::ofNullable);
    }

    public Multi<TagLabelTable> findRange(long offset, long limit) {
        LOG.tracef("findRange(%d, %d)", offset, limit);
        return TagLabelTable.findRange(client, offset, limit);
    }

    public Uni<Optional<String>> insert(TagLabelTable entry) {
        LOG.tracef("insert(%s)", entry);
        return entry.insert(client)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<String>> update(TagLabelTable entry) {
        LOG.tracef("update(%s)", entry);
        return entry.update(client)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<String>> delete(String id) {
        LOG.tracef("delete(%s)", id);
        return TagLabelTable.delete(client, id)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<Long>> count() {
        LOG.trace("count()");
        return TagLabelTable.count(client)
                .map(Optional::ofNullable);
    }
}
