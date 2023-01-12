/*
 * This file was last modified at 2023.01.13 19:25 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * CacheInvalidator.java
 * $Id$
 */

package su.svn.daybook.services.cache;

import io.smallrye.mutiny.Multi;

import java.util.function.Function;

public interface CacheCollector<K extends Comparable<?>, T> {
    <O> Multi<T> flowByOther(O o, Function<T, O> fOther);
}
