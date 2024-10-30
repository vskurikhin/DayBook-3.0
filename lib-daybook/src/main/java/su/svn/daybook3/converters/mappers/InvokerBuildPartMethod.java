/*
 * This file was last modified at 2024-10-30 09:48 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * InvokerBuildPartMethod.java
 * $Id$
 */

package su.svn.daybook3.converters.mappers;

import org.jboss.logging.Logger;
import su.svn.daybook3.converters.getters.AbstractGetters;
import su.svn.daybook3.converters.records.FieldGetter;
import su.svn.daybook3.converters.records.MethodRecord;
import su.svn.daybook3.models.Identification;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;

class InvokerBuildPartMethod<K extends Comparable<? extends Serializable>, X extends Identification<K>> {

    private final Object builder;

    private final AbstractGetters<X> essenceGetters;

    private final Logger log;

    InvokerBuildPartMethod(Object builder, AbstractGetters<X> essenceGetters, Logger log) {
        this.builder = builder;
        this.essenceGetters = essenceGetters;
        this.log = log;
    }

    void invokeBuilderFor(Map.Entry<String, MethodRecord> entry, X essence) {
        var buildPart = entry.getValue();
        if (buildPart != null) {
            var fieldName = entry.getKey();
            var getters = essenceGetters.getGetters();
            var fieldGetter = getters.get(fieldName);
            var invoker = new Helper<>(buildPart, entry, fieldGetter, essence);
            invoker.invoke();
        } else {
            log.tracef("invokeBuilderFor(%s, %s): buildPart is null", entry, essence);
        }
    }

    class Helper<K extends Comparable<? extends Serializable>, X extends Identification<K>> {

        private final Map.Entry<String, MethodRecord> entry;
        private final MethodRecord buildPart;
        private final FieldGetter fieldGetter;
        private final String fieldName;
        private final X essence;

        Helper(MethodRecord buildPart, Map.Entry<String, MethodRecord> entry, FieldGetter fieldGetter, X essence) {
            this.entry = entry;
            this.fieldName = entry.getKey();
            this.buildPart = buildPart;
            this.fieldGetter = fieldGetter;
            this.essence = essence;
        }

        public void invoke() {
            if (fieldGetter != null) {
                var getter = fieldGetter.getter();
                var value = getter.apply(essence);
                var buildPartMethod = buildPart.method();
                invoker(buildPartMethod, value);
            } else {
                var format = "invokeBuilderFor(%s, %s): for field: %s fieldGetter is null";
                log.tracef(format, entry, essence, fieldName);
            }
        }

        private void invoker(Method buildPartMethod, Object value) {
            if (buildPartMethod != null) {
                try {
                    buildPartMethod.invoke(builder, value);
                } catch (ReflectiveOperationException e) {
                    log.error("build part method invoke ", e);
                }
            } else {
                var format = "invokeBuilderFor(%s, %s): for field: %s buildPartMethod is null";
                log.tracef(format, entry, essence, fieldName);
            }
        }
    }
}
