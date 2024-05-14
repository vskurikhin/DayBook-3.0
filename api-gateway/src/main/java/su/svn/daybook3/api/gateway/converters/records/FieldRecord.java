/*
 * This file was last modified at 2024-05-14 21:36 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * FieldRecord.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.converters.records;

import java.lang.reflect.Field;

public record FieldRecord(
        Field field,
        boolean nullable,
        String buildPartPrefix,
        String getterPrefix) {
}
