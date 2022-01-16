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
        LOG.trace("findAll");
        Multi<TagLabel> result = TagLabel.findAll(client);
        LOG.tracef("findAll result: %s", result);
        return TagLabel.findAll(client);
    }

    public Uni<Optional<TagLabel>> findById(String id) {
        return TagLabel.findById(client, id)
                .map(t -> t != null ? Optional.of(t) : Optional.empty());
    }

    public Uni<Optional<String>> insert(TagLabel entry) {
        return entry.insert(client)
                .map(t -> t != null ? Optional.of(t) : Optional.empty());
    }

    public Uni<Optional<String>> update(TagLabel entry) {
        return entry.update(client)
                .map(t -> t != null ? Optional.of(t) : Optional.empty());
    }

    public Uni<Optional<String>> delete(String id) {
        return TagLabel.delete(client, id)
                .map(t -> t != null ? Optional.of(t) : Optional.empty());
    }
}
