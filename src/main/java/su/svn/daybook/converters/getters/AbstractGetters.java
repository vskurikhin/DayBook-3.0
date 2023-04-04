/*
 * This file was last modified at 2023.02.19 10:06 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * AbstractGetters.java
 * $Id$
 */

package su.svn.daybook.converters.getters;

import su.svn.daybook.converters.records.FieldGetter;
import su.svn.daybook.converters.records.FieldRecord;
import su.svn.daybook.models.Identification;
import su.svn.daybook.utils.AccessorsUtil;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractGetters<P extends Identification<? extends Comparable<? extends Serializable>>>
        implements Getters {

    private final Class<P> pClass;

    private final Map<String, FieldGetter> map;

    AbstractGetters(Class<P> pClass) {
        this.pClass = pClass;
        this.map = Collections.unmodifiableMap(getters(pClass));
    }

    protected abstract Map<String, FieldRecord> collectFields(Class<P> pClass);

    @Override
    public Class<P> getPClass() {
        return pClass;
    }

    @Override
    public Map<String, FieldGetter> getGetters() {
        return map;
    }

    @Override
    public void forEach(@Nonnull BiConsumer<String, Function<Object, Object>> action) {
        for (var entry : map.entrySet()) {
            action.accept(entry.getKey(), entry.getValue().getter());
        }
    }

    private Map<String, FieldGetter> getters(Class<P> eClass) {
        final Map<String, FieldGetter> result = new HashMap<>();
        var fields = collectFields(eClass);
        var methods = Arrays
                .stream(eClass.getDeclaredMethods())
                .filter(method -> forFieldSet(method.getName(), fields.keySet()))
                .filter(method -> isGetter(method, fields))
                .collect(Collectors.toMap(Method::getName, Function.identity()));
        for (var field : fields.entrySet()) {
            var factory = new FieldGetterFactory(field, methods, field.getValue().getterPrefix());
            if (factory.isMethodExists()) {
                result.put(field.getKey(), factory.create());
            } else {
                result.putAll(getterPrefixGet(methods, field));
            }
        }
        return result;
    }

    private Map<String, FieldGetter> getterPrefixGet(Map<String, Method> methods, Map.Entry<String, FieldRecord> field) {
        final Map<String, FieldGetter> result = new HashMap<>();
        var factory = new FieldGetterFactory(field, methods, AccessorsUtil.GETTER_PREFIX_GET);
        if (factory.isMethodExists()) {
            result.put(field.getKey(), factory.create());
        } else if (AccessorsUtil.isBooleanField(field.getValue().field())) {
            result.putAll(getterBoolean(methods, field));
        }
        return result;
    }

    private Map<String, FieldGetter> getterBoolean(Map<String, Method> methods, Map.Entry<String, FieldRecord> field) {
        final Map<String, FieldGetter> result = new HashMap<>();
        for (var prefix : AccessorsUtil.BOOLEAN_GETTER_PREFIXES) {
            var factory = new FieldGetterFactory(field, methods, prefix);
            if (factory.isMethodExists()) {
                result.put(field.getKey(), factory.create());
                break;
            }
        }
        return result;
    }

    private boolean isGetter(Method method, Map<String, FieldRecord> fields) {
        var methodName = method.getName();
        var prefixes = fields
                .entrySet()
                .stream()
                .filter(Objects::nonNull)
                .filter(entry -> AccessorsUtil.isMethodNameEndsWithFieldName(methodName, entry.getKey()))
                .filter(f -> !"prefix".equals(f.getValue().getterPrefix()))
                .map(entry -> entry.getValue().getterPrefix())
                .collect(Collectors.toSet());
        if (prefixes.isEmpty()) {
            return AccessorsUtil.isGetter(method);
        } else {
            return prefixes.stream().anyMatch(methodName::startsWith)
                    && AccessorsUtil.isParameterCountZeroAndReturnTypeNotVoid(method)
                    && AccessorsUtil.isNotVolatile(method);
        }
    }

    private boolean forFieldSet(String method, Set<String> keySet) {
        for (var field : keySet) {
            if (isCorrectFieldAndMethodName(field, method)) {
                return true;
            }
        }
        return false;
    }

    private boolean isCorrectFieldAndMethodName(String field, String method) {
        return AccessorsUtil.isCorrectMethodAndEndsWithFieldName(field, method);
    }
}