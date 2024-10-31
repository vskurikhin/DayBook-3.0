/*
 * This file was last modified at 2024-10-30 19:24 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * SettingDataService.java
 * $Id$
 */

package su.svn.daybook3.api.services.domain;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import su.svn.daybook3.api.domain.dao.SettingDao;
import su.svn.daybook3.api.domain.dao.SettingViewDao;
import su.svn.daybook3.api.domain.model.SettingTable;
import su.svn.daybook3.api.domain.model.SettingView;
import su.svn.daybook3.api.domain.transact.SettingTransactionalJob;
import su.svn.daybook3.api.models.domain.Setting;
import su.svn.daybook3.api.services.mappers.SettingMapper;

@ApplicationScoped
public class SettingDataService implements DataService<Long, SettingView, Setting> {

    private static final Logger LOG = Logger.getLogger(SettingDataService.class);

    @Inject
    SettingTransactionalJob settingTransactionalJob;

    @Inject
    SettingDao settingDao;

    @Inject
    SettingViewDao settingViewDao;

    @Inject
    SettingMapper settingMapper;

    public Uni<Long> add(Setting o) {
        LOG.tracef("add(%s)", o);
        var table = settingMapper.convertToSettingTable(o);
        return settingTransactionalJob
                .insert(table, o.valueType())
                .map(op -> lookup(op, table));
    }

    public Uni<Long> count() {
        return settingDao
                .count()
                .map(o -> lookupLong(o, "count for SettingTable"));
    }

    public Multi<Setting> findRange(long offset, long limit) {
        LOG.tracef("findRange(%d, %d)", offset, limit);
        return settingViewDao
                .findRange(offset, limit)
                .map(settingMapper::convertToModel);
    }

    public Uni<Setting> get(Long id) {
        LOG.tracef("get(%s)", id);
        return settingViewDao
                .findById(id)
                .map(o -> lookup(o, id))
                .map(settingMapper::convertToModel);
    }

    public Multi<Setting> getAll() {
        LOG.tracef("getAll()");
        return settingViewDao
                .count()
                .onItem()
                .transformToMulti(count -> getAllIfNotOverSize(count, settingViewDao::findAll))
                .map(settingMapper::convertToModel);
    }

    public Uni<Long> put(Setting o) {
        LOG.tracef("put(%s)", o);
        return putEntry(settingMapper.convertToSettingTable(o), o.valueType());
    }

    private Uni<Long> putEntry(SettingTable entry, String valueType) {
        return settingTransactionalJob
                .update(entry, valueType)
                .map(o -> lookup(o, entry));
    }

    public Uni<Long> delete(Long id) {
        LOG.tracef("delete(%s)", id);
        return deleteEntry(id);
    }

    private Uni<Long> deleteEntry(Long id) {
        return settingDao
                .delete(id)
                .map(o -> lookupId(o, id));
    }
}
