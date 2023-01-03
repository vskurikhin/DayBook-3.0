package su.svn.daybook.converters.mappers;

import org.jboss.logging.Logger;
import su.svn.daybook.converters.getters.AbstractGetters;
import su.svn.daybook.converters.records.MethodRecord;
import su.svn.daybook.models.Identification;

import java.io.Serializable;
import java.util.Map;

class InvokerBuildPartMethod
        <K extends Comparable<? extends Serializable>, X extends Identification<K>> {

    private final Object builder;

    private final AbstractGetters<X> essenceGetters;

    private final Logger log;

    InvokerBuildPartMethod(Object builder, AbstractGetters<X> essenceGetters, Logger log) {
        this.builder = builder;
        this.essenceGetters = essenceGetters;
        this.log = log;
    }

    void invokeBuilderFor(Map.Entry<String, MethodRecord> entry, X essence) {
        log.tracef("invokeBuilderFor(%s, %s)", entry, essence);
        var buildPart = entry.getValue();
        if (buildPart != null) {
            var fieldName = entry.getKey();
            var getters = essenceGetters.getGetters();
            var fieldGetter = getters.get(fieldName);
            if (fieldGetter != null) {
                var getter = fieldGetter.getter();
                var value = getter.apply(essence);
                var buildPartMethod = buildPart.method();
                if (buildPartMethod != null) {
                    try {
                        log.tracef("invokeBuilderFor(%s, %s): set value: %s", entry, essence, String.valueOf(value));
                        buildPartMethod.invoke(builder, value);
                    } catch (ReflectiveOperationException e) {
                        log.error("build part method invoke ", e);
                    }
                } else {
                    var format = "invokeBuilderFor(%s, %s): for field: %s buildPartMethod is null";
                    log.warnf(format, entry, essence, fieldName);
                }
            } else {
                var format = "invokeBuilderFor(%s, %s): for field: %s fieldGetter is null";
                log.warnf(format, entry, essence, fieldName);
            }
        } else {
            log.warnf("invokeBuilderFor(%s, %s): buildPart is null", entry, essence);
        }
    }
}
