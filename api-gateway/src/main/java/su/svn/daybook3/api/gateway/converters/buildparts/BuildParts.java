/*
 * This file was last modified at 2024-05-14 21:36 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * BuildParts.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.converters.buildparts;

import su.svn.daybook3.api.gateway.converters.records.MethodRecord;

import jakarta.annotation.Nonnull;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface BuildParts {
    Class<?> getPClass();

    Map<String, MethodRecord> getBuildParts();

    Supplier<?> getBuilderFactory();

    void forEach(@Nonnull Consumer<Map.Entry<String, MethodRecord>> action);
}
