package su.svn.daybook.converters.buildparts;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import su.svn.daybook.models.domain.TestModel;

import java.util.ArrayList;

class BuildPartsDomainTest {

    BuildPartsAnnotatedDomainFiled<?> buildParts;

    @BeforeEach
    void setUp() {
        Assertions.assertDoesNotThrow(() -> {
            buildParts = new BuildPartsAnnotatedDomainFiled<>(TestModel.class, TestModel::builder);
        });
    }

    @Test
    void test() {
        var expected = new ArrayList<String>() {{
            add("id");
            add("test");
        }};
        Assertions.assertDoesNotThrow(() -> {
            buildParts.forEach(entry -> Assertions.assertTrue(expected.contains(entry.getKey())));
        });
    }

    @Test
    void testGetters(){
        Assertions.assertDoesNotThrow(buildParts::getBuildParts);
        Assertions.assertDoesNotThrow(buildParts::getBuilderFactory);
        Assertions.assertDoesNotThrow(buildParts::getPClass);
    }
}