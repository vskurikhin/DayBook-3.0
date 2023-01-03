/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * VocabularyCacheProvider.java
 * $Id$
 */

package su.svn.daybook.services.cache;

import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheManager;
import io.quarkus.cache.CacheResult;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.dao.VocabularyDao;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.VocabularyTable;
import su.svn.daybook.models.domain.Vocabulary;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.PageService;
import su.svn.daybook.services.mappers.VocabularyMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class VocabularyCacheProvider extends AbstractCacheProvider<Long> {

    private static final Logger LOG = Logger.getLogger(VocabularyCacheProvider.class);

    @Inject
    CacheManager cacheManager;

    @Inject
    PageService pageService;

    @Inject
    VocabularyDao vocabularyDao;

    @Inject
    VocabularyMapper vocabularyMapper;

    public VocabularyCacheProvider() {
        super(EventAddress.VOCABULARY_GET, EventAddress.VOCABULARY_PAGE, LOG);
    }

    @CacheResult(cacheName = EventAddress.VOCABULARY_GET)
    public Uni<Vocabulary> get(@CacheKey Long id) {
        LOG.tracef("get(%s)", id);
        return vocabularyDao
                .findById(id)
                .map(Optional::get)
                .map(vocabularyMapper::convertToModel);
    }

    @CacheResult(cacheName = EventAddress.VOCABULARY_PAGE)
    public Uni<Page<Answer>> getPage(@CacheKey PageRequest pageRequest) {
        LOG.tracef("getPage(%s)", pageRequest);
        return pageService.getPage(pageRequest, vocabularyDao::count, vocabularyDao::findRange, this::answerOfModel);
    }

    @Override
    public Uni<Answer> invalidate(Answer answer) {
        return invalidateAllPagesCache(answer);
    }

    @Override
    public Uni<Answer> invalidateById(Long id, Answer answer) {
        return invalidateCacheById(id).map(l -> answer);
    }

    @Override
    protected CacheManager getCacheManager() {
        return cacheManager;
    }

    private Answer answerOfModel(VocabularyTable table) {
        return Answer.of(vocabularyMapper.convertToModel(table));
    }
}
