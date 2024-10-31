/*
 * This file was last modified at 2024-10-31 15:25 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * StanzaCacheProvider.java
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
import su.svn.daybook3.api.models.domain.Stanza;
import su.svn.daybook3.api.models.pagination.Page;
import su.svn.daybook3.api.models.pagination.PageRequest;
import su.svn.daybook3.api.services.PageService;
import su.svn.daybook3.api.services.domain.StanzaDataService;
import su.svn.daybook3.domain.messages.Answer;

@ApplicationScoped
public class StanzaCacheProvider extends AbstractCacheProvider<Long, Stanza> {

    private static final Logger LOG = Logger.getLogger(StanzaCacheProvider.class);

    @Inject
    PageService pageService;

    @Inject
    StanzaDataService stanzaDataService;

    public StanzaCacheProvider() {
        super(EventAddress.STANZA_GET, EventAddress.STANZA_PAGE, LOG);
    }

    @Counted
    @PrincipalLogging
    @CacheResult(cacheName = EventAddress.STANZA_GET)
    public Uni<Stanza> get(@CacheKey Long id) {
        return stanzaDataService.get(id);
    }

    @Counted
    @PrincipalLogging
    @CacheResult(cacheName = EventAddress.STANZA_PAGE)
    public Uni<Page<Answer>> getPage(@CacheKey PageRequest pageRequest) {
        return pageService.getPage(pageRequest, stanzaDataService::count, stanzaDataService::findRange, Answer::of)
                .log("StanzaCacheProvider.getPage"); // todo remove
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
