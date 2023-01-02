package su.svn.daybook.converters.getters;

import su.svn.daybook.converters.records.FieldGetter;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public interface Getters {
    Class<?> getPClass();

    Map<String, FieldGetter> getGetters();

    void forEach(@Nonnull BiConsumer<String, Function<Object, Object>> action);
}
