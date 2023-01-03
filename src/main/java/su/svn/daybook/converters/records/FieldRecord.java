package su.svn.daybook.converters.records;

import java.lang.reflect.Field;

public record FieldRecord(
        Field field,
        boolean nullable,
        String buildPartPrefix,
        String getterPrefix) {
}
