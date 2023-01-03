package su.svn.daybook.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import su.svn.daybook.converters.getters.GettersAnnotatedDomainFiled;
import su.svn.daybook.models.domain.Codifier;

class GettersAnnotatedDomainFiledFieldTest {

    GettersAnnotatedDomainFiled<?> getters;

    @BeforeEach
    void setUp() {
        getters = new GettersAnnotatedDomainFiled<>(Codifier.class);
    }

    @Test
    void getMap() {
        var result = getters.getGetters();
        for (var getter : getters.getGetters().values()) {
            System.out.println("getter = " + getter);
        }
        System.out.println("result = " + result);
    }
}