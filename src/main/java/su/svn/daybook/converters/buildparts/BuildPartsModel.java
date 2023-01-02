package su.svn.daybook.converters.buildparts;

import su.svn.daybook.converters.ModelCollector;
import su.svn.daybook.converters.records.MethodRecord;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class BuildPartsModel<P extends Serializable> extends AbstractBuildParts<P> implements ModelCollector<P> {

    public BuildPartsModel(Class<P> pClass, Supplier<?> builderFactory) {
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
