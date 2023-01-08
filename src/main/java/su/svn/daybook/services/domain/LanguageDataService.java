/*
 * This file was last modified at 2023.01.07 11:52 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * LanguageDataService.java
 * $Id$
 */

package su.svn.daybook.services.domain;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.dao.LanguageDao;
import su.svn.daybook.domain.model.LanguageTable;
import su.svn.daybook.models.domain.Language;
import su.svn.daybook.services.mappers.LanguageMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class LanguageDataService implements DataService<Long, LanguageTable, Language> {

    private static final Logger LOG = Logger.getLogger(LanguageDataService.class);

    @Inject
    LanguageDao languageDao;

    @Inject
    LanguageMapper languageMapper;

    public Uni<Long> add(Language o) {
        LOG.tracef("add(%s)", o);
        return addEntry(languageMapper.convertToDomain(o));
    }

    private Uni<Long> addEntry(LanguageTable entry) {
        LOG.infof("addUserAndLanguages(%s)", entry);
        return languageDao
                .insert(entry)
                .map(o -> lookup(o, entry));
    }

    public Uni<Long> count() {
        return languageDao
                .count()
                .map(o -> lookupLong(o, "count for LanguageTable"));
    }

    public Multi<Language> findRange(long offset, long limit) {
        LOG.tracef("findRange(%d, %d)", offset, limit);
        return languageDao
                .findRange(offset, limit)
                .map(languageMapper::convertToModel);
    }

    public Uni<Language> get(Long id) {
        LOG.tracef("get(%s)", id);
        return languageDao
                .findById(id)
                .map(o -> lookup(o, id))
                .map(languageMapper::convertToModel);
    }

    public Multi<Language> getAll() {
        LOG.tracef("getAll()");
        return languageDao
                .count()
                .onItem()
                .transformToMulti(count -> getAllIfNotOverSize(count, languageDao::findAll))
                .map(languageMapper::convertToModel);
    }

    public Uni<Long> put(Language o) {
        LOG.tracef("put(%s)", o);
        return putEntry(languageMapper.convertToDomain(o));
    }

    private Uni<Long> putEntry(LanguageTable entry) {
        return languageDao
                .update(entry)
                .map(o -> lookup(o, entry));
    }

    public Uni<Long> delete(Long id) {
        LOG.infof("delete(%s)", id);
        return deleteEntry(id);
    }

    private Uni<Long> deleteEntry(Long id) {
        return languageDao
                .delete(id)
                .map(o -> lookupId(o, id));
    }
}
