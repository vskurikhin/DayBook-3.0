/*
 * This file was last modified at 2024-10-30 17:27 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * CacheCollector.java
 * $Id$
 */

package su.svn.daybook3.api.services.cache;

import io.smallrye.mutiny.Multi;

import java.util.function.Function;

public interface CacheCollector<K extends Comparable<?>, T> {
    <O> Multi<T> flowByOther(O o, Function<T, O> fOther);
}
