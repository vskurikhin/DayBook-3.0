/*
 * This file was last modified at 2024-10-30 09:48 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * Getters.java
 * $Id$
 */

package su.svn.daybook3.converters.getters;

import jakarta.annotation.Nonnull;
import su.svn.daybook3.converters.records.FieldGetter;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public interface Getters {
    Class<?> getPClass();

    Map<String, FieldGetter> getGetters();

    void forEach(@Nonnull BiConsumer<String, Function<Object, Object>> action);
}
