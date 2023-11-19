/*
 * This file was last modified at 2023.09.12 22:07 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * StanzaDataService.java
 * $Id$
 */

package su.svn.daybook.services.domain;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.dao.StanzaDao;
import su.svn.daybook.domain.model.StanzaTable;
import su.svn.daybook.models.domain.Stanza;
import su.svn.daybook.services.mappers.StanzaMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.UUID;

@ApplicationScoped
public class StanzaDataService implements DataService<Long, StanzaTable, Stanza> {

    private static final Logger LOG = Logger.getLogger(StanzaDataService.class);

    @Inject
    StanzaDao stanzaDao;

    @Inject
    StanzaMapper stanzaMapper;

    public Uni<Long> add(Stanza o) {
        LOG.tracef("add(%s)", o);
        return addEntry(stanzaMapper.convertToDomain(o));
    }

    private Uni<Long> addEntry(StanzaTable entry) {
        return stanzaDao
                .insert(entry)
                .map(o -> lookup(o, entry));
    }

    public Uni<Long> count() {
        return stanzaDao
                .count()
                .map(o -> lookupLong(o, "count for StanzaTable"));
    }

    public Multi<Stanza> findRange(long offset, long limit) {
        LOG.tracef("findRange(%d, %d)", offset, limit);
        return stanzaDao
                .findRange(offset, limit)
                .map(stanzaMapper::convertToModel);
    }

    public Uni<Stanza> get(Long id) {
        LOG.tracef("get(%s)", id);
        return stanzaDao
                .findById(id)
                .map(o -> lookup(o, id))
                .map(stanzaMapper::convertToModel);
    }

    public Multi<Stanza> getAll() {
        LOG.tracef("getAll()");
        return stanzaDao
                .count()
                .onItem()
                .transformToMulti(count -> getAllIfNotOverSize(count, stanzaDao::findAll))
                .map(stanzaMapper::convertToModel);
    }

    public Uni<Long> put(Stanza o) {
        LOG.tracef("put(%s)", o);
        return putEntry(stanzaMapper.convertToDomain(o));
    }

    private Uni<Long> putEntry(StanzaTable entry) {
        return stanzaDao
                .update(entry)
                .map(o -> lookup(o, entry));
    }

    public Uni<Long> delete(Long id) {
        LOG.tracef("delete(%s)", id);
        return deleteEntry(id);
    }

    private Uni<Long> deleteEntry(Long id) {
        return stanzaDao
                .delete(id)
                .map(o -> lookupId(o, id));
    }
}
