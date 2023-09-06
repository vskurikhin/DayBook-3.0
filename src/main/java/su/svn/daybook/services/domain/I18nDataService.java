/*
 * This file was last modified at 2023.09.06 17:04 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * I18nDataService.java
 * $Id$
 */

package su.svn.daybook.services.domain;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.dao.I18nDao;
import su.svn.daybook.domain.dao.I18nViewDao;
import su.svn.daybook.domain.model.I18nTable;
import su.svn.daybook.domain.model.I18nView;
import su.svn.daybook.domain.transact.I18nTransactionalJob;
import su.svn.daybook.models.domain.I18n;
import su.svn.daybook.services.mappers.I18nMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class I18nDataService implements DataService<Long, I18nView, I18n> {

    private static final Logger LOG = Logger.getLogger(I18nDataService.class);

    @Inject
    I18nDao i18nDao;

    @Inject
    I18nViewDao i18nViewDao;

    @Inject
    I18nMapper i18nMapper;

    @Inject
    I18nTransactionalJob i18nTransactionalJob;

    public Uni<Long> add(I18n o) {
        LOG.tracef("add(%s)", o);
        var table = i18nMapper.convertToI18nTable(o);
        return i18nTransactionalJob.insert(table, o.language()).map(op -> lookup(op, table));
    }

    public Uni<Long> count() {
        return i18nDao
                .count()
                .map(o -> lookupLong(o, "count for I18nTable"));
    }

    public Multi<I18n> findRange(long offset, long limit) {
        LOG.tracef("findRange(%d, %d)", offset, limit);
        return i18nViewDao
                .findRange(offset, limit)
                .map(i18nMapper::convertToModel);
    }

    public Uni<I18n> get(Long id) {
        LOG.tracef("get(%s)", id);
        return i18nViewDao
                .findById(id)
                .map(o -> lookup(o, id))
                .map(i18nMapper::convertToModel);
    }

    public Multi<I18n> getAll() {
        LOG.tracef("getAll()");
        return i18nViewDao
                .count()
                .onItem()
                .transformToMulti(count -> getAllIfNotOverSize(count, i18nViewDao::findAll))
                .map(i18nMapper::convertToModel);
    }

    public Uni<Long> put(I18n o) {
        LOG.tracef("put(%s)", o);
        return putEntry(i18nMapper.convertToI18nTable(o));
    }

    private Uni<Long> putEntry(I18nTable entry) {
        return i18nDao
                .update(entry)
                .map(o -> lookup(o, entry));
    }

    public Uni<Long> delete(Long id) {
        LOG.tracef("delete(%s)", id);
        return deleteEntry(id);
    }

    private Uni<Long> deleteEntry(Long id) {
        return i18nDao
                .delete(id)
                .map(o -> lookupId(o, id));
    }
}
