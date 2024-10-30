/*
 * This file was last modified at 2024-10-30 09:54 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * TagLabelCacheProvider.java
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
import su.svn.daybook3.api.gateway.models.domain.TagLabel;
import su.svn.daybook3.api.gateway.models.pagination.Page;
import su.svn.daybook3.api.gateway.models.pagination.PageRequest;
import su.svn.daybook3.api.gateway.services.PageService;
import su.svn.daybook3.api.gateway.services.domain.TagLabelDataService;
import su.svn.daybook3.domain.messages.Answer;

@ApplicationScoped
public class TagLabelCacheProvider extends AbstractCacheProvider<String, TagLabel> {

    private static final Logger LOG = Logger.getLogger(TagLabelCacheProvider.class);

    @Inject
    PageService pageService;

    @Inject
    TagLabelDataService tagLabelDataService;

    public TagLabelCacheProvider() {
        super(EventAddress.TAG_LABEL_GET, EventAddress.TAG_LABEL_PAGE, LOG);
    }

    @Counted
    @PrincipalLogging
    @CacheResult(cacheName = EventAddress.TAG_LABEL_GET)
    public Uni<TagLabel> get(@CacheKey String id) {
        return tagLabelDataService.get(id);
    }

    @Counted
    @PrincipalLogging
    @CacheResult(cacheName = EventAddress.TAG_LABEL_PAGE)
    public Uni<Page<Answer>> getPage(@CacheKey PageRequest pageRequest) {
        return pageService.getPage(pageRequest, tagLabelDataService::count, tagLabelDataService::findRange, Answer::of);
    }

    @Override
    public Uni<Answer> invalidate(Answer answer) {
        return invalidateAllPagesCache(answer);
    }

    @Override
    public Uni<Answer> invalidateByKey(String id, Answer answer) {
        return invalidateCacheByKey(id).map(l -> answer);
    }
}
