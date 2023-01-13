package su.svn.daybook.utils;

import io.quarkus.cache.CacheManager;
import org.junit.jupiter.api.Assertions;
import su.svn.daybook.domain.enums.EventAddress;

public enum CacheUtil {
    Null;

    public static void invalidateAll(CacheManager cacheManager, String cacheName) {
        cacheManager
                .getCache(EventAddress.LOGIN_REQUEST)
                .ifPresent(
                        cache -> Assertions.assertDoesNotThrow(
                                () -> cache.invalidateAll().subscribeAsCompletionStage().get()
                        ));
    }
}
