/*
 * This file was last modified at 2024-10-29 23:31 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * MethodRecord.java
 * $Id$
 */

package su.svn.daybook3.converters.records;

import java.lang.reflect.Method;

public record MethodRecord(
        Method method,
        boolean nullable,
        String buildPartPrefix,
        String getterPrefix) {
}
