package su.svn.daybook.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import su.svn.daybook.converters.getters.GettersDomain;
import su.svn.daybook.models.domain.Codifier;

class GettersDomainFieldTest {

    GettersDomain<?> getters;

    @BeforeEach
    void setUp() {
        getters = new GettersDomain<>(Codifier.class);
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