/*
 * This file was last modified at 2024-05-14 21:36 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * BuildPartsAnnotatedModelFiled.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.converters.buildparts;

import su.svn.daybook3.api.gateway.converters.CollectorAnnotatedModelFiled;
import su.svn.daybook3.api.gateway.converters.records.MethodRecord;
import su.svn.daybook3.api.gateway.models.Identification;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class BuildPartsAnnotatedModelFiled<P extends Identification<? extends Comparable<? extends Serializable>>>
        extends AbstractBuildParts<P>
        implements CollectorAnnotatedModelFiled<P> {

    public BuildPartsAnnotatedModelFiled(Class<P> pClass, Supplier<?> builderFactory) {
        super(pClass, builderFactory);
    }

    @Override
    protected Stream<Field> streamMethods(Class<P> pClass) {
        return streamModelMethods(pClass);
    }

    @Override
    protected MethodRecord extractMethodRecord(Field field, Method method) {
        return extractModelMethodRecord(field, method);
    }
}
