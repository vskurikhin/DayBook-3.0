package su.svn.daybook.converters.getters;

import su.svn.daybook.converters.records.FieldGetter;
import su.svn.daybook.converters.records.FieldRecord;
import su.svn.daybook.utils.AccessorsUtil;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

abstract class AbstractGetters<P extends Serializable> implements Getters {

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
        final Map<String, FieldRecord> fields = collectFields(eClass);
        final Map<String, Method> methods = Arrays
                .stream(eClass.getDeclaredMethods())
                .filter(method -> forFieldSet(method.getName(), fields.keySet()))
                .filter(method -> isGetter(method, fields))
                .collect(Collectors.toMap(Method::getName, Function.identity()));
        final Map<String, FieldGetter> result = new HashMap<>();
        for (var field : fields.entrySet()) {
            var domainColumnGetterFactory = new FieldGetterFactory(field, methods, field.getValue().getterPrefix());
            if (domainColumnGetterFactory.isMethodExists()) {
                result.put(field.getKey(), domainColumnGetterFactory.create());
            } else {
                var getterFactory = new FieldGetterFactory(field, methods, AccessorsUtil.GETTER_PREFIX_GET);
                if (getterFactory.isMethodExists()) {
                    result.put(field.getKey(), getterFactory.create());
                } else {
                    if (AccessorsUtil.isBooleanField(field.getValue().field())) {
                        for (String prefix : AccessorsUtil.BOOLEAN_GETTER_PREFIXES) {
                            var factory = new FieldGetterFactory(field, methods, prefix);
                            if (factory.isMethodExists()) {
                                result.put(field.getKey(), factory.create());
                                break;
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    private boolean isGetter(Method method, Map<String, FieldRecord> fields) {
        final String methodName = method.getName();
        Set<String> prefixes = fields
                .entrySet()
                .stream()
                .filter(Objects::nonNull)
                .filter(entry -> AccessorsUtil.isMethodNameEndsWithFieldName(methodName, entry.getKey()))
                .filter(f -> !"".equals(f.getValue().getterPrefix()))
                .filter(f -> !"prefix".equals(f.getValue().getterPrefix()))
                .map(entry -> entry.getValue().getterPrefix())
                .collect(Collectors.toSet());
        if (prefixes.isEmpty()) {
            return AccessorsUtil.isGetter(method);
        } else {
            return prefixes.stream().anyMatch(methodName::startsWith)
                    && method.getParameterCount() == 0 && method.getReturnType() != void.class
                    && !Modifier.isVolatile(method.getModifiers());
        }
    }

    private boolean forFieldSet(String method, Set<String> keySet) {
        for (String field : keySet) {
            if (AccessorsUtil.isCorrectFieldAndMethodName(field, method)
                    && AccessorsUtil.isMethodNameEndsWithFieldName(method, field)) {
                return true;
            }
        }
        return false;
    }
}
