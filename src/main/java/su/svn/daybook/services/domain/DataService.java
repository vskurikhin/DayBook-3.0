/*
 * This file was last modified at 2023.09.06 17:04 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * DataService.java
 * $Id$
 */

package su.svn.daybook.services.domain;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import su.svn.daybook.models.Identification;

import jakarta.annotation.Nonnull;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Supplier;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
interface DataService
        <I extends Comparable<? extends Serializable>, D extends Identification<I>, M extends Identification<I>> {

    Uni<I> add(M model);

    Uni<Long> count();

    Uni<I> delete(I id);

    Multi<M> findRange(long offset, long limit);

    Uni<M> get(I id);

    Multi<M> getAll();

    Uni<I> put(M model);

    default <K extends Comparable<? extends Serializable>, X extends Identification<K>>
    K lookup(@Nonnull Optional<K> o, X e) {
        if (o.isPresent()) {
            return o.get();
        }
        throw new NoSuchElementException("No value present for entry: " + e);
    }

    default <K extends Comparable<? extends Serializable>, X extends Identification<K>>
    X lookup(@Nonnull Optional<X> o, K id) {
        if (o.isPresent()) {
            return o.get();
        }
        throw new NoSuchElementException("No value present for id: " + id);
    }

    default <K extends Comparable<? extends Serializable>, X extends Identification<K>>
    K lookupId(@Nonnull Optional<K> o, K id) {
        if (o.isPresent()) {
            return o.get();
        }
        throw new NoSuchElementException("No value present for id: " + id);
    }

    default Long lookupLong(@Nonnull Optional<Long> o, String method) {
        if (o.isPresent()) {
            return o.get();
        }
        throw new NoSuchElementException("No value present in method: " + method);
    }

    default Multi<D> getAllIfNotOverSize(Optional<Long> count, Supplier<Multi<D>> supplier) {
        if (count.isPresent()) {
            if (-1 < count.get() && count.get() < (Short.MAX_VALUE / 2)) {
                return supplier.get();
            }
            throw new IndexOutOfBoundsException("content too long");
        }
        throw new IndexOutOfBoundsException("content too long");
    }
}