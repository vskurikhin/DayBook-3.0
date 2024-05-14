/*
 * This file was last modified at 2024-05-14 20:56 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * CacheUtil.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.utils;

import io.quarkus.cache.CacheManager;
import org.junit.jupiter.api.Assertions;
import su.svn.daybook3.api.gateway.domain.enums.EventAddress;

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
