package su.svn.daybook.converters.buildparts;

import su.svn.daybook.converters.DomainCollector;
import su.svn.daybook.converters.records.MethodRecord;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class BuildPartsDomain<P extends Serializable> extends AbstractBuildParts<P> implements DomainCollector<P> {

    public BuildPartsDomain(Class<P> pClass, Supplier<?> builderFactory) {
        super(pClass, builderFactory);
    }

    @Override
    protected Stream<Field> streamMethods(Class<P> pClass) {
        return streamDomainMethods(pClass);
    }

    @Override
    protected MethodRecord extractMethodRecord(Field field, Method method) {
        return extractDomainMethodRecord(field, method);
    }
}
