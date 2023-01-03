package su.svn.daybook.services.domain;

import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
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
import su.svn.daybook.domain.model.CodifierTable;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionException;

@QuarkusTest
class CodifierServiceTest {

    @Inject
    CodifierService service;

    static CodifierDao mock;

    static final Uni<Optional<String>> UNI_OPTIONAL_NONE_STRING = Uni.createFrom().item(Optional.of(CodifierTable.NONE));

    static final Uni<Optional<CodifierTable>> UNI_OPTIONAL_TEST = Uni.createFrom().item(Optional.of(TestData.CODIFIER.TABLE_0));

    static final Multi<CodifierTable> MULTI_TEST = Multi.createFrom().item(TestData.CODIFIER.TABLE_0);

    static final Multi<CodifierTable> MULTI_WITH_NULL = TestData.createMultiWithNull(CodifierTable.class);

    static final Multi<CodifierTable> MULTI_EMPTIES = TestData.createMultiEmpties(CodifierTable.class);

    @BeforeEach
    void setUp() {
        mock = Mockito.mock(CodifierDao.class);
        Mockito.when(mock.findById(CodifierTable.NONE)).thenReturn(UNI_OPTIONAL_TEST);
        Mockito.when(mock.findByCode(CodifierTable.NONE)).thenReturn(UNI_OPTIONAL_TEST);
        QuarkusMock.installMockForType(mock, CodifierDao.class);
    }

    @Test
    void testWhenGetAllThenSingletonList() {
        Mockito.when(mock.findAll()).thenReturn(MULTI_TEST);
        List<Answer> result = new ArrayList<>();
        Assertions.assertDoesNotThrow(() -> result.addAll(service.getAll()
                .subscribe()
                .asStream()
                .peek(actual -> Assertions.assertEquals(Answer.of(TestData.CODIFIER.TABLE_0), actual)).toList()));
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
    void testWhenGetPageThenSingletonList() {

        Mockito.when(mock.findRange(0L, Short.MAX_VALUE - 1)).thenReturn(MULTI_TEST);
        Mockito.when(mock.count()).thenReturn(TestData.UNI_OPTIONAL_ONE_LONG);

        PageRequest pageRequest = new PageRequest(0L, (short) (Short.MAX_VALUE - 1));
        var expected = Page.<Answer>builder()
                .totalPages(1L)
                .totalElements(1)
                .pageSize((short) 1)
                .prevPage(false)
                .nextPage(false)
                .content(Collections.singletonList(Answer.of(TestData.CODIFIER.TABLE_0)))
                .build();

        List<Page<Answer>> result = new ArrayList<>();
        Assertions.assertDoesNotThrow(() -> service.getPage(pageRequest)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely()
        );
    }

    @Test
    void testWhenGetPageThenEmpty() {

        Mockito.when(mock.findRange(0L, Short.MAX_VALUE - 2)).thenReturn(MULTI_EMPTIES);
        Mockito.when(mock.count()).thenReturn(TestData.UNI_OPTIONAL_ZERO_LONG);

        PageRequest pageRequest = new PageRequest(0L, (short) (Short.MAX_VALUE - 2));
        var expected = Page.<Answer>builder()
                .totalPages(0L)
                .totalElements(0)
                .pageSize((short) 0)
                .prevPage(false)
                .nextPage(false)
                .content(Collections.emptyList())
                .build();

        List<Page<Answer>> result = new ArrayList<>();
        Assertions.assertDoesNotThrow(() -> service.getPage(pageRequest)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely()
        );
    }

    @Test
    void testWhenGetPageThenZeroPage() {

        Mockito.when(mock.findRange(0L, 0)).thenReturn(MULTI_EMPTIES);
        Mockito.when(mock.count()).thenReturn(TestData.UNI_OPTIONAL_ONE_LONG);

        PageRequest pageRequest = new PageRequest(0, (short) 0);
        var expected = Page.<Answer>builder()
                .totalPages(0L)
                .totalElements(1)
                .pageSize((short) 0)
                .prevPage(false)
                .nextPage(false)
                .content(Collections.emptyList())
                .build();

        List<Page<Answer>> result = new ArrayList<>();
        Assertions.assertDoesNotThrow(() -> service.getPage(pageRequest)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely()
        );
    }

    @Test
    void testWhenGetThenEntry() {
        Assertions.assertDoesNotThrow(() -> service.get(CodifierTable.NONE)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.of(TestData.CODIFIER.TABLE_0), actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenAddThenId() {
        var expected = Answer.builder()
                .error(201)
                .payload(new ApiResponse<>(CodifierTable.NONE))
                .build();
        Mockito.when(mock.insert(TestData.CODIFIER.TABLE_0)).thenReturn(UNI_OPTIONAL_NONE_STRING);
        Assertions.assertDoesNotThrow(() -> service.add(TestData.CODIFIER.TABLE_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenAddThenEmpty() {
        Mockito.when(mock.insert(TestData.CODIFIER.TABLE_0)).thenReturn(TestData.UNI_OPTIONAL_EMPTY_STRING);
        Assertions.assertDoesNotThrow(() -> service.add(TestData.CODIFIER.TABLE_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenPutThenId() {
        var expected = Answer.builder()
                .error(202)
                .payload(new ApiResponse<>(CodifierTable.NONE))
                .build();
        Mockito.when(mock.update(TestData.CODIFIER.TABLE_0)).thenReturn(UNI_OPTIONAL_NONE_STRING);
        Assertions.assertDoesNotThrow(() -> service.put(TestData.CODIFIER.TABLE_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenPutThenEmpty() {
        Mockito.when(mock.update(TestData.CODIFIER.TABLE_0)).thenReturn(TestData.UNI_OPTIONAL_EMPTY_STRING);
        Assertions.assertThrows(CompletionException.class, () -> service.put(TestData.CODIFIER.TABLE_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenDeleteThenId() {
        Mockito.when(mock.delete(CodifierTable.NONE)).thenReturn(UNI_OPTIONAL_NONE_STRING);
        var expected = Answer.of(new ApiResponse<>(CodifierTable.NONE));
        Assertions.assertDoesNotThrow(() -> service.delete(CodifierTable.NONE)
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