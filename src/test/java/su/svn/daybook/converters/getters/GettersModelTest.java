package su.svn.daybook.converters.getters;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import su.svn.daybook.domain.model.TestTable;

import java.util.ArrayList;

class GettersModelTest {

    GettersModel<?> getters;

    @BeforeEach
    void setUp() {
        Assertions.assertDoesNotThrow(() -> {
            getters = new GettersModel<>(TestTable.class);
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
            getters.forEach((name, f) -> Assertions.assertTrue(expected.contains(name)));
        });
    }

    @Test
    void testGetters() {
        Assertions.assertDoesNotThrow(getters::getGetters);
        Assertions.assertDoesNotThrow(getters::getPClass);
    }
}