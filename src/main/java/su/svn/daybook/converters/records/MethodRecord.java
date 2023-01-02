package su.svn.daybook.converters.records;

import java.lang.reflect.Method;

public record MethodRecord(
        Method method,
        boolean nullable,
        String buildPartPrefix,
        String getterPrefix) {
}
