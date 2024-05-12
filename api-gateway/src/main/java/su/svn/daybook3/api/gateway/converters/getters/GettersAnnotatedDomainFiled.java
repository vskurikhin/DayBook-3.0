/*
 * This file was last modified at 2024-05-14 21:00 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * GettersAnnotatedDomainFiled.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.converters.getters;

import su.svn.daybook3.api.gateway.converters.CollectorAnnotatedDomainFiled;
import su.svn.daybook3.api.gateway.converters.records.FieldRecord;
import su.svn.daybook3.api.gateway.models.Identification;

import java.io.Serializable;
import java.util.Map;

public class GettersAnnotatedDomainFiled<P extends Identification<? extends Comparable<? extends Serializable>>>
        extends AbstractGetters<P>
        implements CollectorAnnotatedDomainFiled<P> {

    public GettersAnnotatedDomainFiled(Class<P> pClass) {
        super(pClass);
    }

    @Override
    public Map<String, FieldRecord> collectFields(Class<P> pClass) {
        return collectDomainFields(pClass);
    }
}
