package su.svn.daybook.converters.domain;

import su.svn.daybook.models.Identification;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.function.Supplier;

public abstract class AbstractConverter
        <K extends Comparable<? extends Serializable>, D extends Identification<K>, M extends Identification<K>> {

    public abstract D convertToDomain(M model);

    public abstract M convertToModel(D domain);

    protected AbstractConverter(
            @Nonnull Class<D> dClass,
            @Nonnull Supplier<?> domainBuildFactory,
            @Nonnull Class<M> mClass,
            @Nonnull Supplier<?> modelBuildFactory) {

    }
}
