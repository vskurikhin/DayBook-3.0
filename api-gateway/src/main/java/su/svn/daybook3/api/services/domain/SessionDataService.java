/*
 * This file was last modified at 2024-10-31 13:16 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * SessionDataService.java
 * $Id$
 */

package su.svn.daybook3.api.services.domain;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import su.svn.daybook3.api.domain.dao.SessionDao;
import su.svn.daybook3.api.domain.model.SessionTable;
import su.svn.daybook3.api.models.domain.Session;
import su.svn.daybook3.api.services.mappers.SessionMapper;

import java.util.UUID;

@ApplicationScoped
public class SessionDataService implements DataService<UUID, SessionTable, Session> {

    private static final Logger LOG = Logger.getLogger(SessionDataService.class);

    @Inject
    SessionDao sessionDao;

    @Inject
    SessionMapper sessionMapper;

    public Uni<UUID> add(Session o) {
        LOG.tracef("add(%s)", o);
        return addEntry(sessionMapper.convertToDomain(o));
    }

    private Uni<UUID> addEntry(SessionTable entry) {
        return sessionDao
                .insert(entry)
                .map(o -> lookup(o, entry));
    }

    public Uni<Long> count() {
        return sessionDao
                .count()
                .map(o -> lookupLong(o, "count for SessionTable"));
    }

    public Multi<Session> findByName(String userName) {
        LOG.tracef("findByName(%s)", userName);
        return sessionDao
                .findByValue(userName)
                .map(sessionMapper::convertToModel);
    }

    public Multi<Session> findRange(long offset, long limit) {
        LOG.tracef("findRange(%d, %d)", offset, limit);
        return sessionDao
                .findRange(offset, limit)
                .map(sessionMapper::convertToModel);
    }

    public Uni<Session> get(UUID id) {
        LOG.tracef("get(%s)", id);
        return sessionDao
                .findById(id)
                .map(o -> lookup(o, id))
                .map(sessionMapper::convertToModel);
    }

    public Multi<Session> getAll() {
        LOG.tracef("getAll()");
        return sessionDao
                .count()
                .onItem()
                .transformToMulti(count -> getAllIfNotOverSize(count, sessionDao::findAll))
                .map(sessionMapper::convertToModel);
    }

    public Uni<UUID> put(Session o) {
        LOG.tracef("put(%s)", o);
        return putEntry(sessionMapper.convertToDomain(o));
    }

    private Uni<UUID> putEntry(SessionTable entry) {
        return sessionDao
                .update(entry)
                .map(o -> lookup(o, entry));
    }

    public Uni<UUID> delete(UUID id) {
        LOG.tracef("delete(%s)", id);
        return deleteEntry(id);
    }

    private Uni<UUID> deleteEntry(UUID id) {
        return sessionDao
                .delete(id)
                .map(o -> lookupId(o, id));
    }
}
