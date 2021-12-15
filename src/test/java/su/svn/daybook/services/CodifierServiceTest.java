package su.svn.daybook.services;

import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import su.svn.daybook.DataTest;
import su.svn.daybook.domain.dao.CodifierDao;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.Codifier;

import javax.inject.Inject;
import java.util.Optional;

@QuarkusTest
class CodifierServiceTest {

    @Inject
    CodifierService service;

    static CodifierDao mock;

    @BeforeAll
    public static void setup() {
        Uni<Optional<Codifier>> tezd = Uni.createFrom()
                .item(Optional.of(DataTest.TEZD_Codifier));
        Uni<Answer> empty = Uni.createFrom()
                .item(new Answer("empty", 1));
        Uni<Optional<Long>> tezdId = Uni.createFrom().item(Optional.of(0L));
        Multi<Codifier> tezds = Multi.createFrom()
                .item(DataTest.TEZD_Codifier);

        mock = Mockito.mock(CodifierDao.class);
        Mockito.when(mock.findById(0L)).thenReturn(tezd);
        Mockito.when(mock.insert(DataTest.TEZD_Codifier)).thenReturn(tezdId);
        Mockito.when(mock.findAll()).thenReturn(tezds);
        QuarkusMock.installMockForType(mock, CodifierDao.class);
    }

    @Test
    void tagGet() {
        service.codeGet("0")
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.of(Optional.of(DataTest.TEZD_Codifier)), actual))
                .await()
                .indefinitely();
    }

    @Test
    void testNullTagGet() {
        service.codeGet(null)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely();
    }

    @Test
    void tagAdd() {
        service.codeAdd(DataTest.TEZD_Codifier)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.of(0L), actual))
                .await()
                .indefinitely();
    }

    @Test
    void getAll() {
        service.getAll()
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.of(DataTest.TEZD_Codifier), actual))
                .toUni()
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_getAll_return_null() {
        Mockito.when(mock.findAll()).thenReturn(Multi.createFrom().empty());
        QuarkusMock.installMockForType(mock, CodifierDao.class);
        service.getAll()
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.of(DataTest.TEZD_Codifier), actual))
                .toUni()
                .await()
                .indefinitely();
    }
}