package su.svn.daybook.services.cache;

import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheManager;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.messages.Answer;

import java.io.Serializable;
import java.util.List;

abstract class AbstractCacheProvider<K extends Comparable<? extends Serializable>> {

    private final Logger log;
    private final String getCacheName;
    private final String pageCacheName;

    protected AbstractCacheProvider(String getCacheName, String pageCacheName, Logger log) {
        this.getCacheName = getCacheName;
        this.pageCacheName = pageCacheName;
        this.log = log;
    }

    public abstract Uni<Answer> invalidate(Answer answer);

    public abstract Uni<Answer> invalidateById(K id, Answer answer);

    protected abstract CacheManager getCacheManager();

    protected Uni<Answer> invalidateAllPagesCache(Answer answer) {
        log.tracef("invalidatedPagesCacheByName(%s) cacheName: %s", answer, pageCacheName);
        return invalidatedPagesCacheByName(pageCacheName)
                .map(u -> answer)
                .onFailure()
                .recoverWithUni(t -> Uni.createFrom().item(answer));
    }

    protected Uni<List<Void>> invalidateCacheById(K id) {
        log.tracef("invalidateCacheById(%s) cacheName: %s", id, getCacheName);

        var getVoid = getCacheManager()
                .getCache(getCacheName)
                .map(cache -> cache.invalidate(id))
                .orElse(Uni.createFrom().voidItem());
        var pageVoid = invalidatedPagesCacheByName(pageCacheName);

        return joinCollectFailures(getVoid, pageVoid)
                .onItem()
                .invoke(voids -> log.tracef("invalidate of %d caches", voids.size()));
    }

    private Uni<Void> invalidatedPagesCacheByName(String cacheName) {
        log.tracef("invalidatedPagesCacheByName(%s)", cacheName);
        return getCacheManager()
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