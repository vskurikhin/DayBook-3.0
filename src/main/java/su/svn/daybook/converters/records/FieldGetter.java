/*
 * This file was last modified at 2023.11.19 16:20 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * FieldGetter.java
 * $Id$
 */

package su.svn.daybook.converters.records;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.Function;

public record FieldGetter(
        Field field,
        Method method,
        Function<Object, Object> getter,
        boolean nullable,
        String getterPrefix) {
}
