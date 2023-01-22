package su.svn.daybook.domain.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.@KType@;

class @Name@TableTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new @Name@Table(
                null, @KType@.ZERO, null, null, null, null, true, true, 0
        ));
    }
    @Test
    void testGetters(){
        var entry = @Name@Table.builder().build();
        Assertions.assertDoesNotThrow(entry::id);
        Assertions.assertDoesNotThrow(entry::@key@);
        Assertions.assertDoesNotThrow(entry::@value@);
        Assertions.assertDoesNotThrow(entry::userName);
        Assertions.assertDoesNotThrow(entry::createTime);
        Assertions.assertDoesNotThrow(entry::updateTime);
        Assertions.assertDoesNotThrow(entry::enabled);
        Assertions.assertDoesNotThrow(entry::visible);
        Assertions.assertDoesNotThrow(entry::flags);
    }

    @Test
    void testEqualsVerifier() {
        EqualsVerifier.forClass(@Name@Table.class).verify();
    }

    @Test
    void testToString() {
        var entry = @Name@Table.builder().build();
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(entry.toString()));
    }

    @Test
    void testBuilder() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(@Name@Table.builder()
                .id(null)
                .@key@(@KType@.ZERO)
                .@value@(null)
                .userName(null)
                .createTime(null)
                .updateTime(null)
                .enabled(true)
                .visible(true)
                .flags(0)
                .build()));
    }
}