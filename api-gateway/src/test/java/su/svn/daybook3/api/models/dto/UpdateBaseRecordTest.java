package su.svn.daybook3.api.models.dto;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UpdateBaseRecordTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new UpdateBaseRecord(null, false, 0));
    }

    @Test
    void testGetters() {
        var entry = new UpdateBaseRecord(null, false, 0);
        Assertions.assertDoesNotThrow(entry::id);
        Assertions.assertDoesNotThrow(entry::visible);
        Assertions.assertDoesNotThrow(entry::flags);
    }

    @Test
    void testEqualsVerifier() {
        EqualsVerifier.forClass(UpdateBaseRecord.class)
                .suppress(Warning.NO_EXAMPLE_FOR_CACHED_HASHCODE)
                .verify();
    }

    @Test
    void testToString() {
        var entry = new UpdateBaseRecord(null, false, 0);
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(entry.toString()));
    }

    @Test
    void testBuilder() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(UpdateBaseRecord.builder()
                .id(null)
                .visible(false)
                .flags(0)
                .build()));
    }
}