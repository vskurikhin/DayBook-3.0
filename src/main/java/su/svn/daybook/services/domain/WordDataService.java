/*
 * This file was last modified at 2023.01.07 11:52 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * UserDataService.java
 * $Id$
 */

package su.svn.daybook.services.domain;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.dao.WordDao;
import su.svn.daybook.domain.model.WordTable;
import su.svn.daybook.models.domain.Word;
import su.svn.daybook.services.mappers.WordMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.NoSuchElementException;

@ApplicationScoped
public class WordDataService implements DataService<String, WordTable, Word> {

    private static final Logger LOG = Logger.getLogger(WordDataService.class);

    @Inject
    WordDao wordDao;

    @Inject
    WordMapper wordMapper;

    public Uni<String> add(Word o) {
        LOG.tracef("add(%s)", o);
        return addEntry(wordMapper.convertToDomain(o));
    }

    private Uni<String> addEntry(WordTable entry) {
        LOG.infof("addUserAndRoles(%s)", entry);
        return wordDao
                .insert(entry)
                .map(o -> lookup(o, entry));
    }

    public Uni<Long> count() {
        return wordDao
                .count()
                .map(o -> lookupLong(o, "count for WordTable"));
    }

    public Multi<Word> findRange(long offset, long limit) {
        LOG.tracef("findRange(%d, %d)", offset, limit);
        return wordDao
                .findRange(offset, limit)
                .map(wordMapper::convertToModel);
    }

    public Uni<Word> get(String id) {
        LOG.tracef("get(%s)", id);
        return wordDao
                .findById(id)
                .map(o -> lookup(o, id))
                .map(wordMapper::convertToModel);
    }

    public Multi<Word> getAll() {
        LOG.tracef("getAll()");
        return wordDao
                .count()
                .onItem()
                .transformToMulti(count -> getAllIfNotOverSize(count, wordDao::findAll))
                .map(wordMapper::convertToModel);
    }

    public Uni<String> put(Word o) {
        LOG.tracef("put(%s)", o);
        return putEntry(wordMapper.convertToDomain(o));
    }

    private Uni<String> putEntry(WordTable entry) {
        return wordDao
                .update(entry)
                .map(o -> lookup(o, entry));
    }

    public Uni<String> delete(String id) {
        LOG.infof("delete(%s)", id);
        return deleteEntry(id);
    }

    private Uni<String> deleteEntry(String id) {
        return wordDao
                .delete(id)
                .map(o -> lookupId(o, id));
    }
}
