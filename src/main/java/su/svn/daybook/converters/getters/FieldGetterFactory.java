package su.svn.daybook.converters.getters;

import su.svn.daybook.converters.records.FieldGetter;
import su.svn.daybook.converters.records.FieldRecord;
import su.svn.daybook.utils.AccessorsUtil;

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
