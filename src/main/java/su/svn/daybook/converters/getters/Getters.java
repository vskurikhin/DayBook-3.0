/*
 * This file was last modified at 2023.09.06 17:04 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * Getters.java
 * $Id$
 */

package su.svn.daybook.converters.getters;

import su.svn.daybook.converters.records.FieldGetter;

import jakarta.annotation.Nonnull;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public interface Getters {
    Class<?> getPClass();

    Map<String, FieldGetter> getGetters();

    void forEach(@Nonnull BiConsumer<String, Function<Object, Object>> action);
}
