/*
 * This file was last modified at 2023.11.19 16:20 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * CollectorAnnotatedModelFiled.java
 * $Id$
 */

package su.svn.daybook.converters;

import su.svn.daybook.annotations.ModelField;
import su.svn.daybook.converters.records.FieldRecord;
import su.svn.daybook.converters.records.MethodRecord;
import su.svn.daybook.models.Identification;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface CollectorAnnotatedModelFiled<P extends Identification<? extends Comparable<? extends Serializable>>> {

    default Map<String, FieldRecord> collectModelFields(Class<P> pClass) {
        return Arrays
                .stream(pClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(ModelField.class))
                .filter(this::notOnlyBuildPart)
                .collect(Collectors.toMap(Field::getName, this::extractModelFieldRecord));
    }

    private boolean notOnlyBuildPart(Field field) {
        ModelField modelField = field.getAnnotation(ModelField.class);
        return !modelField.onlyBuildPart();
    }

    default Stream<Field> streamModelMethods(Class<P> eClass) {
        return Arrays
                .stream(eClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(ModelField.class));
    }

    private FieldRecord extractModelFieldRecord(Field field) {
        var f = field.getAnnotation(ModelField.class);
        return new FieldRecord(field, f.nullable(), f.buildPartPrefix(), f.getterPrefix());
    }

    default MethodRecord extractModelMethodRecord(Field field, Method method) {
        var f = field.getAnnotation(ModelField.class);
        return new MethodRecord(method, f.nullable(), f.buildPartPrefix(), f.getterPrefix());
    }
}
