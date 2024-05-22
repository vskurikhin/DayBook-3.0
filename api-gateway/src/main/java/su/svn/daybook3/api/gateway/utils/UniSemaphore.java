/*
 * This file was last modified at 2024-05-20 14:31 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * UniSemaphore.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.utils;

import io.smallrye.mutiny.Uni;

import java.util.function.Supplier;

public interface UniSemaphore {
    <T> Uni<T> protect(Supplier<Uni<T>> inner);
}
