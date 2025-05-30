/*
 * This file was last modified at 2024-10-30 17:27 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * VocabularyCacheProvider.java
 * $Id$
 */

package su.svn.daybook3.api.services.cache;

import io.micrometer.core.annotation.Counted;
import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import su.svn.daybook3.api.annotations.PrincipalLogging;
import su.svn.daybook3.api.domain.enums.EventAddress;
import su.svn.daybook3.api.models.domain.Vocabulary;
import su.svn.daybook3.api.models.pagination.Page;
import su.svn.daybook3.api.models.pagination.PageRequest;
import su.svn.daybook3.api.services.PageService;
import su.svn.daybook3.api.services.domain.VocabularyDataService;
import su.svn.daybook3.domain.messages.Answer;

@ApplicationScoped
public class VocabularyCacheProvider extends AbstractCacheProvider<Long, Vocabulary> {

    private static final Logger LOG = Logger.getLogger(VocabularyCacheProvider.class);

    @Inject
    PageService pageService;

    @Inject
    VocabularyDataService vocabularyDataService;

    public VocabularyCacheProvider() {
        super(EventAddress.VOCABULARY_GET, EventAddress.VOCABULARY_PAGE, LOG);
    }

    @Counted
    @PrincipalLogging
    @CacheResult(cacheName = EventAddress.VOCABULARY_GET)
    public Uni<Vocabulary> get(@CacheKey Long id) {
        return vocabularyDataService.get(id);
    }

    @Counted
    @PrincipalLogging
    @CacheResult(cacheName = EventAddress.VOCABULARY_PAGE)
    public Uni<Page<Answer>> getPage(@CacheKey PageRequest pageRequest) {
        return pageService.getPage(pageRequest, vocabularyDataService::count, vocabularyDataService::findRange, Answer::of);
    }

    @Override
    public Uni<Answer> invalidate(Answer answer) {
        return invalidateAllPagesCache(answer);
    }

    @Override
    public Uni<Answer> invalidateByKey(Long id, Answer answer) {
        return invalidateCacheByKey(id).map(l -> answer);
    }
}
