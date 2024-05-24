package su.svn.daybook3.api.gateway.models.dto;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ResourceBaseRecordTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new ResourceBaseRecord(null, null, null, false, 0));
    }

    @Test
    void testGetters() {
        var entry = new ResourceBaseRecord(null, null, null, false, 0);
        Assertions.assertDoesNotThrow(entry::id);
        Assertions.assertDoesNotThrow(entry::userName);
        Assertions.assertDoesNotThrow(entry::visible);
        Assertions.assertDoesNotThrow(entry::flags);
    }

    @Test
    void testEqualsVerifier() {
        EqualsVerifier.forClass(ResourceBaseRecord.class)
                .suppress(Warning.NO_EXAMPLE_FOR_CACHED_HASHCODE)
                .verify();
    }

    @Test
    void testToString() {
        var entry = new ResourceBaseRecord(null, null, null, false, 0);
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(entry.toString()));
    }

    @Test
    void testBuilder() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(ResourceBaseRecord.builder()
                .id(null)
                .userName(null)
                .visible(false)
                .flags(0)
                .build()));
    }

    @Test
    void testToBuilder() {
        var test = new ResourceBaseRecord(null, null, null, false, 0);
        var expected = new ResourceBaseRecord(null, null, null, false, 0);
        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(expected, test.toBuilder().build()));
    }
}