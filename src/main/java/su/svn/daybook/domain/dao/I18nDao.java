/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * I18nDao.java
 * $Id$
 */

package su.svn.daybook.domain.dao;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.model.I18nTable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class I18nDao {

    private static final Logger LOG = Logger.getLogger(I18nDao.class);

    @Inject
    io.vertx.mutiny.pgclient.PgPool client;

    public Multi<I18nTable> findAll() {
        LOG.trace("findAll()");
        return I18nTable.findAll(client);
    }

    public Uni<Optional<I18nTable>> findById(Long id) {
        LOG.tracef("findById(%s)", id);
        return I18nTable.findById(client, id)
                .map(Optional::ofNullable);
    }

    public Multi<I18nTable> findRange(long offset, long limit) {
        LOG.tracef("findRange(%d, %d)", offset, limit);
        return I18nTable.findRange(client, offset, limit);
    }

    public Uni<Optional<Long>> insert(I18nTable entry) {
        LOG.tracef("insert(%s)", entry);
        return entry.insert(client)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<Long>> update(I18nTable entry) {
        LOG.tracef("update(%s)", entry);
        return entry.update(client)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<Long>> delete(Long id) {
        LOG.tracef("delete(%s)", id);
        return I18nTable.delete(client, id)
                .map(Optional::ofNullable);
    }

    public Uni<Optional<Long>> count() {
        LOG.trace("count()");
        return I18nTable.count(client)
                .map(Optional::ofNullable);
    }
}
