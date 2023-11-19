/*
 * This file was last modified at 2023.11.20 00:10 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * StanzaDataService.java
 * $Id$
 */

package su.svn.daybook.services.domain;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.commons.lang3.tuple.Pair;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.dao.StanzaDao;
import su.svn.daybook.domain.dao.StanzaViewDao;
import su.svn.daybook.domain.model.SettingTable;
import su.svn.daybook.domain.model.StanzaTable;
import su.svn.daybook.domain.model.StanzaView;
import su.svn.daybook.domain.transact.StanzaTransactionalJob;
import su.svn.daybook.models.domain.Stanza;
import su.svn.daybook.services.mappers.SettingMapper;
import su.svn.daybook.services.mappers.StanzaMapper;

import java.util.Collection;
import java.util.Collections;

@ApplicationScoped
public class StanzaDataService implements DataService<Long, StanzaView, Stanza> {

    private static final Logger LOG = Logger.getLogger(StanzaDataService.class);

    @Inject
    StanzaTransactionalJob stanzaTransactionalJob;

    @Inject
    StanzaDao stanzaDao;

    @Inject
    StanzaViewDao stanzaViewDao;

    @Inject
    SettingMapper settingMapper;

    @Inject
    StanzaMapper stanzaMapper;

    public Uni<Long> add(Stanza o) {
        LOG.tracef("add(%s)", o);
        return upsert(o);
    }

    public Uni<Long> count() {
        return stanzaDao
                .count()
                .map(o -> lookupLong(o, "count for StanzaTable"));
    }

    public Multi<Stanza> findRange(long offset, long limit) {
        LOG.tracef("findRange(%d, %d)", offset, limit);
        return stanzaViewDao
                .findRange(offset, limit)
                .map(stanzaMapper::convertToModel);
    }

    public Uni<Stanza> get(Long id) {
        LOG.tracef("get(%s)", id);
        return stanzaViewDao // TODO to stanzaViewDao
                .findById(id)
                .map(o -> lookup(o, id))
                .map(stanzaMapper::convertToModel);
    }

    public Multi<Stanza> getAll() {
        LOG.tracef("getAll()");
        return stanzaViewDao
                .count()
                .onItem()
                .transformToMulti(count -> getAllIfNotOverSize(count, stanzaViewDao::findAll))
                .map(stanzaMapper::convertToModel);
    }

    public Uni<Long> put(Stanza o) {
        LOG.tracef("put(%s)", o);
        return upsert(o);
    }

    private Uni<Long> upsert(Stanza o) {
        var table = stanzaMapper.convertToSettingTable(o);
        Collection<SettingTable> settings = o
                .settings()
                .stream()
                .map(setting -> settingMapper.convertToSettingTable(setting))
                .toList();
        var pair = Pair.of(table, settings);
        return stanzaTransactionalJob
                .upsert(Collections.singleton(pair))
                .map(l -> l.isEmpty() ? Long.MIN_VALUE : l.iterator().next());
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
