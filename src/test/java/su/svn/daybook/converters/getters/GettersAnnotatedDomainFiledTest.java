package su.svn.daybook.converters.getters;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import su.svn.daybook.models.domain.TestModel;

import java.util.ArrayList;

class GettersAnnotatedDomainFiledTest {

    GettersAnnotatedDomainFiled<?> getters;

    @BeforeEach
    void setUp() {
        Assertions.assertDoesNotThrow(() -> {
            getters = new GettersAnnotatedDomainFiled<>(TestModel.class);
        });
    }

    @Test
    void test() {
        var expected = new ArrayList<String>() {{
            add("id");
            add("test");
        }};
        Assertions.assertDoesNotThrow(() -> {
            getters.forEach((name, f) -> Assertions.assertTrue(expected.contains(name)));
        });
    }

    @Test
    void testGetters(){
        Assertions.assertDoesNotThrow(getters::getGetters);
        Assertions.assertDoesNotThrow(getters::getPClass);
    }
}