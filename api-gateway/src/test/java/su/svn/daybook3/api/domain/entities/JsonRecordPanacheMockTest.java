package su.svn.daybook3.api.domain.entities;

import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

@QuarkusTest
public class JsonRecordPanacheMockTest {

    @Test
    public void testPanacheMocking() throws ExecutionException, InterruptedException {
        PanacheMock.mock(JsonRecord.class);
        Assertions.assertEquals(0, JsonRecord.count().subscribe().asCompletionStage().get());
        Assertions.assertDoesNotThrow(() -> JsonRecord.addJsonRecord(new BaseRecord(), new JsonRecord()));
    }
}
