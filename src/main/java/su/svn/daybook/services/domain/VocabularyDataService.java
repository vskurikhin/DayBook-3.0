/*
 * This file was last modified at 2023.01.07 11:52 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * VocabularyDataService.java
 * $Id$
 */

package su.svn.daybook.services.domain;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.dao.VocabularyDao;
import su.svn.daybook.domain.model.VocabularyTable;
import su.svn.daybook.models.domain.Vocabulary;
import su.svn.daybook.services.mappers.VocabularyMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class VocabularyDataService implements DataService<Long, VocabularyTable, Vocabulary> {

    private static final Logger LOG = Logger.getLogger(VocabularyDataService.class);

    @Inject
    VocabularyDao vocabularyDao;

    @Inject
    VocabularyMapper vocabularyMapper;

    public Uni<Long> add(Vocabulary o) {
        LOG.tracef("add(%s)", o);
        return addEntry(vocabularyMapper.convertToDomain(o));
    }

    private Uni<Long> addEntry(VocabularyTable entry) {
        return vocabularyDao
                .insert(entry)
                .map(o -> lookup(o, entry));
    }

    public Uni<Long> count() {
        return vocabularyDao
                .count()
                .map(o -> lookupLong(o, "count for VocabularyTable"));
    }

    public Multi<Vocabulary> findRange(long offset, long limit) {
        LOG.tracef("findRange(%d, %d)", offset, limit);
        return vocabularyDao
                .findRange(offset, limit)
                .map(vocabularyMapper::convertToModel);
    }

    public Uni<Vocabulary> get(Long id) {
        LOG.tracef("get(%s)", id);
        return vocabularyDao
                .findById(id)
                .map(o -> lookup(o, id))
                .map(vocabularyMapper::convertToModel);
    }

    public Multi<Vocabulary> getAll() {
        LOG.tracef("getAll()");
        return vocabularyDao
                .count()
                .onItem()
                .transformToMulti(count -> getAllIfNotOverSize(count, vocabularyDao::findAll))
                .map(vocabularyMapper::convertToModel);
    }

    public Uni<Long> put(Vocabulary o) {
        LOG.tracef("put(%s)", o);
        return putEntry(vocabularyMapper.convertToDomain(o));
    }

    private Uni<Long> putEntry(VocabularyTable entry) {
        return vocabularyDao
                .update(entry)
                .map(o -> lookup(o, entry));
    }

    public Uni<Long> delete(Long id) {
        LOG.tracef("delete(%s)", id);
        return deleteEntry(id);
    }

    private Uni<Long> deleteEntry(Long id) {
        return vocabularyDao
                .delete(id)
                .map(o -> lookupId(o, id));
    }
}
