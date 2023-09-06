/*
 * This file was last modified at 2023.09.06 17:04 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * QuarkusCaffeineCacheCollectorTest.java
 * $Id$
 */

package su.svn.daybook.services.cache;

import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheManager;
import io.quarkus.cache.CacheResult;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@QuarkusTest
class QuarkusCaffeineCacheCollectorTest {

    @Inject
    CacheManager cacheManager;

    private CacheCollector<String, KeyPair> cacheCollector;

    static PublicKey publicKey1 = new PublicKey() {
        public String getAlgorithm() { return null; }
        public String getFormat() { return null;}
        public byte[] getEncoded() { return new byte[0]; }
    };
    static PrivateKey privateKey1 = new PrivateKey() {
        public String getAlgorithm() { return null; }
        public String getFormat() { return null; }
        public byte[] getEncoded() { return new byte[0]; }
    };

    static KeyPair keyPair1 = new KeyPair(publicKey1, privateKey1);

    @Inject
    TestClass testClass;

    @BeforeEach
    void setUp() {
        cacheCollector = new QuarkusCaffeineCacheCollector<>(cacheManager, "test", String.class, KeyPair.class);
    }

    @Test
    void flowByOther() {
        testClass.get("test");
        Assertions.assertDoesNotThrow(() -> {
            var test = cacheCollector.flowByOther(publicKey1, KeyPair::getPublic)
                    .collect()
                    .asList()
                    .subscribeAsCompletionStage()
                    .get();
            Assertions.assertNotNull(test);
            Assertions.assertFalse(test.isEmpty());
            Assertions.assertEquals(keyPair1, test.get(0));
        });
    }


    @Test
    void testThenReturnNull() {
        var cacheManager = Mockito.mock(CacheManager.class);
        Mockito.when(cacheManager.getCache(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(new Cache() {
                    public String getName() {
                        return null;
                    }

                    public Object getDefaultKey() {
                        return null;
                    }

                    public <K, V> Uni<V> get(K key, Function<K, V> valueLoader) {
                        return null;
                    }

                    public <K, V> Uni<V> getAsync(K key, Function<K, Uni<V>> valueLoader) {
                        return null;
                    }

                    public Uni<Void> invalidate(Object key) {
                        return null;
                    }

                    public Uni<Void> invalidateAll() {
                        return null;
                    }

                    public Uni<Void> invalidateIf(Predicate<Object> predicate) {
                        return null;
                    }

                    public <T extends Cache> T as(Class<T> type) {
                        return null;
                    }
                }));
        var cacheCollector = new QuarkusCaffeineCacheCollector<>(cacheManager, "test", String.class, KeyPair.class);
        Assertions.assertNotNull(cacheCollector);
        Assertions.assertDoesNotThrow(() -> {
            var test = cacheCollector.flowByOther(publicKey1, KeyPair::getPublic)
                    .collect()
                    .asList()
                    .subscribeAsCompletionStage()
                    .get();
            Assertions.assertNotNull(test);
            Assertions.assertTrue(test.isEmpty());
        });
    }

    @ApplicationScoped
    static class TestClass {
        @CacheResult(cacheName = "test")
        KeyPair get(@CacheKey String key) {
            return keyPair1;
        }
    }
}