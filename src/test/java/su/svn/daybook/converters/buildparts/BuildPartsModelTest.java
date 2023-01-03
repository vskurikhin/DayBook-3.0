package su.svn.daybook.converters.buildparts;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import su.svn.daybook.domain.model.TestTable;

import java.util.ArrayList;

class BuildPartsModelTest {

    BuildPartsAnnotatedModelFiled<?> buildParts;

    @BeforeEach
    void setUp() {
        Assertions.assertDoesNotThrow(() -> {
            buildParts = new BuildPartsAnnotatedModelFiled<>(TestTable.class, TestTable::builder);
        });
    }

    @Test
    void test() {
        var expected = new ArrayList<String>() {{
            add("id");
            add("test");
            add("flag");
            add("prefix");
        }};
        Assertions.assertDoesNotThrow(() -> {
            buildParts.forEach(entry -> Assertions.assertTrue(expected.contains(entry.getKey())));
        });
    }

    @Test
    void testGetters() {
        Assertions.assertDoesNotThrow(buildParts::getBuildParts);
        Assertions.assertDoesNotThrow(buildParts::getBuilderFactory);
        Assertions.assertDoesNotThrow(buildParts::getPClass);
    }
}