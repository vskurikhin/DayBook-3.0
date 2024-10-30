/*
 * This file was last modified at 2024-10-30 08:52 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * AbstractCacheProvider.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.services.cache;

import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheManager;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import su.svn.daybook3.domain.messages.Answer;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

abstract class AbstractCacheProvider<K extends Comparable<? extends Serializable>, T> {

    public abstract Uni<Answer> invalidate(Answer answer);

    public abstract Uni<Answer> invalidateByKey(K id, Answer answer);

    private final Logger log;
    private final String getCacheName;
    private final String pageCacheName;

    private CacheCollector<K, T> cacheCollector;

    @Inject
    CacheManager cacheManager;

    AbstractCacheProvider(String getCacheName, String pageCacheName, Logger log) {
        this.getCacheName = getCacheName;
        this.pageCacheName = pageCacheName;
        this.log = log;
    }

    protected void setup(Class<K> kClass, Class<T> tClass) {
        this.cacheCollector = new QuarkusCaffeineCacheCollector<>(cacheManager, getCacheName, kClass, tClass);
    }

    protected Uni<Answer> invalidateAllPagesCache(Answer answer) {
        log.tracef("invalidatedPagesCacheByName(%s) cacheName: %s", answer, pageCacheName);
        return invalidatedPagesCacheByName(pageCacheName)
                .map(u -> answer)
                .onFailure()
                .recoverWithUni(t -> Uni.createFrom().item(answer));
    }

    protected Uni<List<Void>> invalidateCacheByKey(K id) {
        log.tracef("invalidateCacheById(%s) cacheName: %s", id, getCacheName);

        var getVoid = cacheManager
                .getCache(getCacheName)
                .map(cache -> cache.invalidate(id))
                .orElse(Uni.createFrom().voidItem());
        var pageVoid = invalidatedPagesCacheByName(pageCacheName);

        return joinCollectFailures(getVoid, pageVoid)
                .onItem()
                .invoke(voids -> log.tracef("invalidate of %d caches", voids.size()));
    }

    protected <O> Uni<Answer> invalidateByOther(O o, Answer answer, Function<T, O> fOther, Function<T, K> fKey) {
        if (cacheCollector != null) {
            return cacheCollector.flowByOther(o, fOther)
                    .onItem()
                    .transformToUni(e -> invalidateByKey(fKey.apply(e), answer))
                    .concatenate()
                    .collect()
                    .asList()
                    .map(l -> answer);
        }
        return Uni.createFrom().item(answer);
    }

    private Uni<Void> invalidatedPagesCacheByName(String cacheName) {
        log.tracef("invalidatedPagesCacheByName(%s)", cacheName);
        return cacheManager
                .getCache(cacheName)
                .map(Cache::invalidateAll)
                .orElse(Uni.createFrom().voidItem());
    }

    private Uni<List<Void>> joinCollectFailures(Uni<Void> void1, Uni<Void> void2) {
        return Uni.join()
                .all(void1, void2)
                .andCollectFailures();
    }
}