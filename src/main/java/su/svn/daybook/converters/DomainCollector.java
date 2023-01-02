package su.svn.daybook.converters;

import su.svn.daybook.annotations.DomainField;
import su.svn.daybook.converters.records.FieldRecord;
import su.svn.daybook.converters.records.MethodRecord;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface DomainCollector<P extends Serializable> {

    default Map<String, FieldRecord> collectDomainFields(Class<P> eClass) {
        return Arrays
                .stream(eClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(DomainField.class))
                .collect(Collectors.toMap(Field::getName, this::extractDomainFieldRecord));
    }

    default Stream<Field> streamDomainMethods(Class<P> eClass) {
        return Arrays
                .stream(eClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(DomainField.class));
    }

    private FieldRecord extractDomainFieldRecord(Field field) {
        var f = field.getAnnotation(DomainField.class);
        return new FieldRecord(field, f.nullable(), f.buildPartPrefix(), f.getterPrefix());
    }

    default MethodRecord extractDomainMethodRecord(Field field, Method method) {
        var f = field.getAnnotation(DomainField.class);
        return new MethodRecord(method, f.nullable(), f.buildPartPrefix(), f.getterPrefix());
    }
}
