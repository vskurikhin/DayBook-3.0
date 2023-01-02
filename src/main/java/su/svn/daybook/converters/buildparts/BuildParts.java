package su.svn.daybook.converters.buildparts;

import su.svn.daybook.converters.records.MethodRecord;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface BuildParts {
    Class<?> getPClass();

    Map<String, MethodRecord> getBuildParts();

    Supplier<?> getBuilderFactory();

    void forEach(@Nonnull Consumer<Map.Entry<String, MethodRecord>> action);
}
