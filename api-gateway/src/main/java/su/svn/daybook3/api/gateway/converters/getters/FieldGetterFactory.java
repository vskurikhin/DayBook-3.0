/*
 * This file was last modified at 2024-05-14 21:36 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * FieldGetterFactory.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.converters.getters;

import su.svn.daybook3.api.gateway.converters.records.FieldGetter;
import su.svn.daybook3.api.gateway.converters.records.FieldRecord;
import su.svn.daybook3.api.gateway.utils.AccessorsUtil;

import java.lang.reflect.Method;
import java.util.Map;

class FieldGetterFactory {
    private final Map.Entry<String, FieldRecord> field;
    private final Map<String, Method> methods;
    private final String prefix;
    private String methodName;
    private Method method;

    FieldGetterFactory(Map.Entry<String, FieldRecord> field, Map<String, Method> methods, String prefix) {
        this.field = field;
        this.methods = methods;
        this.prefix = prefix;
    }

    FieldGetter create() {
        if (null == method && !isMethodExists()) {
            return null;
        }
        return new FieldGetter(
                field.getValue().field(),
                methods.get(methodName),
                AccessorsUtil.getterFunction(methods.get(methodName)),
                field.getValue().nullable(),
                prefix
        );
    }

    boolean isMethodExists() {
        methodName = constructMethodName(field.getKey(), prefix);
        method = methods.get(methodName);
        return method != null;
    }

    private String constructMethodName(String fieldName, String getterPrefix) {
        if (0 == getterPrefix.length()) {
            return fieldName;
        }
        return getterPrefix + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }
}
