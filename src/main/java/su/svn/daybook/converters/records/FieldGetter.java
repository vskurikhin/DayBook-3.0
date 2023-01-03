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
