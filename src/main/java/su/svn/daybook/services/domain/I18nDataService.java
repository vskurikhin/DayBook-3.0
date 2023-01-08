/*
 * This file was last modified at 2023.01.07 11:52 by Victor N. Skurikhin.
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
import su.svn.daybook.domain.model.I18nTable;
import su.svn.daybook.models.domain.I18n;
import su.svn.daybook.services.mappers.I18nMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class I18nDataService implements DataService<Long, I18nTable, I18n> {

    private static final Logger LOG = Logger.getLogger(I18nDataService.class);

    @Inject
    I18nDao i18nDao;

    @Inject
    I18nMapper i18nMapper;

    public Uni<Long> add(I18n o) {
        LOG.tracef("add(%s)", o);
        return addEntry(i18nMapper.convertToDomain(o));
    }

    private Uni<Long> addEntry(I18nTable entry) {
        return i18nDao
                .insert(entry)
                .map(o -> lookup(o, entry));
    }

    public Uni<Long> count() {
        return i18nDao
                .count()
                .map(o -> lookupLong(o, "count for I18nTable"));
    }

    public Multi<I18n> findRange(long offset, long limit) {
        LOG.tracef("findRange(%d, %d)", offset, limit);
        return i18nDao
                .findRange(offset, limit)
                .map(i18nMapper::convertToModel);
    }

    public Uni<I18n> get(Long id) {
        LOG.tracef("get(%s)", id);
        return i18nDao
                .findById(id)
                .map(o -> lookup(o, id))
                .map(i18nMapper::convertToModel);
    }

    public Multi<I18n> getAll() {
        LOG.tracef("getAll()");
        return i18nDao
                .count()
                .onItem()
                .transformToMulti(count -> getAllIfNotOverSize(count, i18nDao::findAll))
                .map(i18nMapper::convertToModel);
    }

    public Uni<Long> put(I18n o) {
        LOG.tracef("put(%s)", o);
        return putEntry(i18nMapper.convertToDomain(o));
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
