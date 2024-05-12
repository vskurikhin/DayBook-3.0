/*
 * This file was last modified at 2024-05-14 21:00 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * GettersAnnotatedModelFiled.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.converters.getters;

import su.svn.daybook3.api.gateway.converters.CollectorAnnotatedModelFiled;
import su.svn.daybook3.api.gateway.converters.records.FieldRecord;
import su.svn.daybook3.api.gateway.models.Identification;

import java.io.Serializable;
import java.util.Map;

public class GettersAnnotatedModelFiled<P extends Identification<? extends Comparable<? extends Serializable>>>
        extends AbstractGetters<P>
        implements CollectorAnnotatedModelFiled<P> {

    public GettersAnnotatedModelFiled(Class<P> pClass) {
        super(pClass);
    }

    @Override
    public Map<String, FieldRecord> collectFields(Class<P> pClass) {
        return collectModelFields(pClass);
    }
}
