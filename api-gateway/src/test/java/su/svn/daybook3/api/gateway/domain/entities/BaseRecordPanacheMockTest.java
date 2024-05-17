package su.svn.daybook3.api.gateway.domain.entities;

import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

@QuarkusTest
class BaseRecordPanacheMockTest {

    @Test
    public void testPanacheMocking() throws ExecutionException, InterruptedException {
        PanacheMock.mock(BaseRecord.class);
        Assertions.assertEquals(0, BaseRecord.count().subscribe().asCompletionStage().get());
        Assertions.assertDoesNotThrow(() -> BaseRecord.addBaseRecord(new BaseRecord()));
    }
}