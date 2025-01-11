/*
 * This file was last modified at 2024-10-30 14:17 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * QuarkusCaffeineCacheCollector.java
 * $Id$
 */

package su.svn.daybook3.auth.services.cache;

import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheManager;
import io.quarkus.cache.CaffeineCache;
import io.smallrye.mutiny.Multi;
import org.jboss.logging.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class QuarkusCaffeineCacheCollector<K extends Comparable<?>, T> implements CacheCollector<K, T> {

    private static final Logger LOG = Logger.getLogger(QuarkusCaffeineCacheCollector.class);

    private final long timeoutMs;
    private final CaffeineCache caffeineCache;

    private final Class<K> kClass;

    private final Class<T> tClass;

    public QuarkusCaffeineCacheCollector(CacheManager cacheManager, String name, Class<K> kClass, Class<T> tClass) {
        this.caffeineCache = cacheManager
                .getCache(name)
                .map(this::getCaffeineCache)
                .orElse(null);
        this.timeoutMs = 500;
        this.kClass = kClass;
        this.tClass = tClass;
    }

    @Nullable
    private CaffeineCache getCaffeineCache(Cache cache) {
        if (cache instanceof CaffeineCache caffeineCache) {
            return caffeineCache;
        }
        return null;
    }

    public <O> Multi<T> flowByOther(O o, Function<T, O> fOther) {
        var keys = collect();
        LOG.tracef("invalidateBy(%s, T -> O, T -> K) -> keys: %s", o, keys);
        var set = search(keys, e -> o.equals(fOther.apply(e)));
        LOG.tracef("invalidateBy(%s, T -> O, T -> K) -> set: %s", o, set);
        return Multi.createFrom().iterable(set);
    }

    private Set<K> collect() {
        if (this.caffeineCache != null) {
            return caffeineCache
                    .keySet()
                    .stream()
                    .map(o -> convert(kClass, o))
                    .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    private Set<T> search(Set<K> set, Predicate<T> predicate) {
        return set
                .stream()
                .filter(Objects::nonNull)
                .map(this::getObject)
                .filter(Objects::nonNull)
                .filter(predicate)
                .collect(Collectors.toSet());
    }

    private T getObject(K key) {
        try {
            var result = caffeineCache.getIfPresent(key).get(timeoutMs, TimeUnit.MILLISECONDS);
            return (tClass.isInstance(result)) ? (T) result : null;
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            LOG.error(e);
        }
        return null;
    }

    private <X> X convert(Class<X> tClass, Object o) {
        if (tClass.isInstance(o)) {
            return (X) o;
        }
        return null;
    }
}
