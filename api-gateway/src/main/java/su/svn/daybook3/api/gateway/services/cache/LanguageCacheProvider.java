/*
 * This file was last modified at 2024-05-14 23:10 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * LanguageCacheProvider.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.services.cache;

import io.micrometer.core.annotation.Counted;
import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import su.svn.daybook3.api.gateway.annotations.PrincipalLogging;
import su.svn.daybook3.api.gateway.domain.enums.EventAddress;
import su.svn.daybook3.api.gateway.domain.messages.Answer;
import su.svn.daybook3.api.gateway.models.domain.Language;
import su.svn.daybook3.api.gateway.models.pagination.Page;
import su.svn.daybook3.api.gateway.models.pagination.PageRequest;
import su.svn.daybook3.api.gateway.services.domain.LanguageDataService;
import su.svn.daybook3.api.gateway.services.PageService;

@ApplicationScoped
public class LanguageCacheProvider extends AbstractCacheProvider<Long, Language> {

    private static final Logger LOG = Logger.getLogger(LanguageCacheProvider.class);

    @Inject
    PageService pageService;

    @Inject
    LanguageDataService languageDataService;

    public LanguageCacheProvider() {
        super(EventAddress.LANGUAGE_GET, EventAddress.LANGUAGE_PAGE, LOG);
    }

    @Counted
    @PrincipalLogging
    @CacheResult(cacheName = EventAddress.LANGUAGE_GET)
    public Uni<Language> get(@CacheKey Long id) {
        return languageDataService.get(id);
    }

    @Counted
    @PrincipalLogging
    @CacheResult(cacheName = EventAddress.LANGUAGE_PAGE)
    public Uni<Page<Answer>> getPage(@CacheKey PageRequest pageRequest) {
        return pageService.getPage(pageRequest, languageDataService::count, languageDataService::findRange, Answer::of);
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
