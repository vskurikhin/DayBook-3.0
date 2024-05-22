/*
 * This file was last modified at 2024-05-20 16:36 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * UniSemaphoreImpl.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.utils;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.subscription.UniEmitter;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Supplier;

/**
 * {@link UniSemaphoreImpl <a href="https://blog.lunatech.com/posts/2023-05-19-hibernate-reactive-gotchas">UniSemaphore</a>}
 */
public class UniSemaphoreImpl implements UniSemaphore {

    private int permits;
    private final Queue<UniEmitter<Void>> queue;

    public UniSemaphoreImpl(int permits) {
        assert (permits > 0);
        this.permits = permits;
        queue = new LinkedBlockingDeque<>();
    }

    @Override
    public <T> Uni<T> protect(Supplier<Uni<T>> inner) {
        return acquire().replaceWith(inner.get())
                .eventually(this::release);
    }

    private Uni<Void> release() {
        return Uni.createFrom()
                .item(() -> {
                    synchronized (this) {
                        UniEmitter<Void> next = queue.poll();
                        if (next == null) {
                            permits++;
                        } else {
                            next.complete(null);
                        }
                        return null;
                    }
                });
    }

    private Uni<Void> acquire() {
        return Uni.createFrom()
                .deferred(() -> {
                    synchronized (this) {
                        if (permits >= 1) {
                            permits--;
                            return Uni.createFrom().voidItem();
                        } else {
                            //noinspection unchecked
                            return Uni.createFrom()
                                    .emitter(emitter -> queue.add((UniEmitter<Void>) emitter));
                        }
                    }
                });
    }
}
