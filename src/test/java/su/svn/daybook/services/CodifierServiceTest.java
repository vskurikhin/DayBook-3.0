package su.svn.daybook.services;

import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.CompositeException;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import su.svn.daybook.TestData;
import su.svn.daybook.domain.dao.CodifierDao;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.messages.ApiResponse;
import su.svn.daybook.domain.model.Codifier;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@QuarkusTest
class CodifierServiceTest {

    @Inject
    CodifierService service;

    static CodifierDao mock;

    static final Uni<Optional<String>> UNI_OPTIONAL_NONE_STRING = Uni.createFrom().item(Optional.of(Codifier.NONE));

    static final Uni<Optional<Codifier>> UNI_OPTIONAL_TEST = Uni.createFrom().item(Optional.of(TestData.CODIFIER.OBJECT_0));

    static final Multi<Codifier> MULTI_TEST = Multi.createFrom().item(TestData.CODIFIER.OBJECT_0);

    static final Multi<Codifier> MULTI_WITH_NULL = TestData.createMultiWithNull(Codifier.class);

    static final Multi<Codifier> MULTI_EMPTIES = TestData.createMultiEmpties(Codifier.class);

    @BeforeEach
    void setUp() {
        mock = Mockito.mock(CodifierDao.class);
        Mockito.when(mock.findById(Codifier.NONE)).thenReturn(UNI_OPTIONAL_TEST);
        Mockito.when(mock.findByCode(Codifier.NONE)).thenReturn(UNI_OPTIONAL_TEST);
        QuarkusMock.installMockForType(mock, CodifierDao.class);
    }

    @Test
    void testWhenGetAllThenSingletonList() {
        Mockito.when(mock.findAll()).thenReturn(MULTI_TEST);
        List<Answer> result = new ArrayList<>();
        Assertions.assertDoesNotThrow(() -> result.addAll(service.getAll()
                .subscribe()
                .asStream()
                .peek(actual -> Assertions.assertEquals(Answer.of(TestData.CODIFIER.OBJECT_0), actual)).toList()));
        Assertions.assertTrue(result.size() > 0);
    }

    @Test
    void testWhenGetAllThenEmpty() {
        Mockito.when(mock.findAll()).thenReturn(MULTI_EMPTIES);
        List<Answer> result = new ArrayList<>();
        Assertions.assertDoesNotThrow(() -> result.addAll(service.getAll()
                .subscribe()
                .asStream()
                .toList()));
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void testWhenGetAllThenNull() {
        Mockito.when(mock.findAll()).thenReturn(MULTI_WITH_NULL);
        List<Answer> result = new ArrayList<>();
        Assertions.assertDoesNotThrow(() -> result.addAll(service.getAll()
                .subscribe()
                .asStream()
                .toList()));
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void testWhenGetThenEntry() {
        Assertions.assertDoesNotThrow(() -> service.get(Codifier.NONE)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.of(TestData.CODIFIER.OBJECT_0), actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenGetThenNullParameter() {
        Assertions.assertDoesNotThrow(() -> service.get(null)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenAddThenId() {
        var expected = Answer.builder()
                .error(201)
                .payload(new ApiResponse<>(Codifier.NONE))
                .build();
        Mockito.when(mock.insert(TestData.CODIFIER.OBJECT_0)).thenReturn(UNI_OPTIONAL_NONE_STRING);
        Assertions.assertDoesNotThrow(() -> service.add(TestData.CODIFIER.OBJECT_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenAddThenEmpty() {
        Mockito.when(mock.insert(TestData.CODIFIER.OBJECT_0)).thenReturn(TestData.UNI_OPTIONAL_EMPTY_STRING);
        Assertions.assertDoesNotThrow(() -> service.add(TestData.CODIFIER.OBJECT_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenPutThenId() {
        var expected = Answer.builder()
                .error(202)
                .payload(new ApiResponse<>(Codifier.NONE))
                .build();
        Mockito.when(mock.update(TestData.CODIFIER.OBJECT_0)).thenReturn(UNI_OPTIONAL_NONE_STRING);
        Assertions.assertDoesNotThrow(() -> service.put(TestData.CODIFIER.OBJECT_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenPutThenEmpty() {
        Mockito.when(mock.update(TestData.CODIFIER.OBJECT_0)).thenReturn(TestData.UNI_OPTIONAL_EMPTY_STRING);
        Assertions.assertThrows(CompositeException.class, () -> service.put(TestData.CODIFIER.OBJECT_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenDeleteThenId() {
        Mockito.when(mock.delete(Codifier.NONE)).thenReturn(UNI_OPTIONAL_NONE_STRING);
        var expected = Answer.of(new ApiResponse<>(Codifier.NONE));
        Assertions.assertDoesNotThrow(() -> service.delete(Codifier.NONE)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenDeleteThenNullParameter() {
        Assertions.assertDoesNotThrow(() -> service.delete(null)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely());
    }
}