package su.svn.daybook3.api.gateway.models.dto;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NewBaseRecordTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new NewBaseRecord(false, 0));
    }

    @Test
    void testGetters() {
        var entry = new NewBaseRecord(false, 0);
        Assertions.assertDoesNotThrow(entry::visible);
        Assertions.assertDoesNotThrow(entry::flags);
    }

    @Test
    void testEqualsVerifier() {
        EqualsVerifier.forClass(NewBaseRecord.class)
                .suppress(Warning.NO_EXAMPLE_FOR_CACHED_HASHCODE)
                .verify();
    }

    @Test
    void testToString() {
        var entry = new NewBaseRecord(false, 0);
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(entry.toString()));
    }

    @Test
    void testBuilder() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(NewBaseRecord.builder()
                .visible(true)
                .flags(0)
                .build()));
    }
}