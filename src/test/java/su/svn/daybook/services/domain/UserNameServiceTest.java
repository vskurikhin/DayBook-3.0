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
import su.svn.daybook.domain.dao.UserNameDao;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.messages.ApiResponse;
import su.svn.daybook.domain.model.UserName;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionException;

@QuarkusTest
class UserNameServiceTest {

    @Inject
    UserNameService service;

    static UserNameDao mock;

    static final Uni<Optional<UserName>> UNI_OPTIONAL_TEST = Uni.createFrom().item(Optional.of(TestData.USERNAME.OBJECT_0));

    static final Multi<UserName> MULTI_TEST = Multi.createFrom().item(TestData.USERNAME.OBJECT_0);

    static final Multi<UserName> MULTI_EMPTIES = Multi.createFrom().empty();

    @BeforeEach
    void setUp() {
        mock = Mockito.mock(UserNameDao.class);
        Mockito.when(mock.findById(TestData.ZERO_UUID)).thenReturn(UNI_OPTIONAL_TEST);
        QuarkusMock.installMockForType(mock, UserNameDao.class);
    }

    @Test
    void testWhenGetAllThenSingletonList() {
        Mockito.when(mock.findAll()).thenReturn(MULTI_TEST);
        List<Answer> result = service.getAll()
                .subscribe()
                .asStream()
                .peek(actual -> Assertions.assertEquals(Answer.of(TestData.USERNAME.OBJECT_0), actual))
                .toList();
        Assertions.assertTrue(result.size() > 0);
    }

    @Test
    void testWhenGetAllThenEmpty() {
        Mockito.when(mock.findAll()).thenReturn(MULTI_EMPTIES);
        List<Answer> result = service.getAll()
                .subscribe()
                .asStream()
                .toList();
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
                .content(Collections.singletonList(Answer.of(TestData.USERNAME.OBJECT_0)))
                .build();

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

        Assertions.assertDoesNotThrow(() -> service.getPage(pageRequest)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely()
        );
    }

    @Test
    void testWhenGetThenEntry() {
        service.get(TestData.ZERO_UUID)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.of(TestData.USERNAME.OBJECT_0), actual))
                .await()
                .indefinitely();
    }

    @Test
    void testWhenGetWithStringThenEntry() {
        service.get(TestData.STRING_ZERO_UUID)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.of(TestData.USERNAME.OBJECT_0), actual))
                .await()
                .indefinitely();
    }

    @Test
    void testWhenAddThenId() {
        var expected = Answer.builder()
                .error(201)
                .payload(new ApiResponse<>(TestData.ZERO_UUID))
                .build();
        Mockito.when(mock.insert(TestData.USERNAME.OBJECT_0)).thenReturn(TestData.UNI_OPTIONAL_ZERO_UUID);
        service.add(TestData.USERNAME.OBJECT_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely();
    }

    @Test
    void testWhenAddThenEmpty() {
        Mockito.when(mock.insert(TestData.USERNAME.OBJECT_0)).thenReturn(TestData.UNI_OPTIONAL_EMPTY_UUID);
        service.add(TestData.USERNAME.OBJECT_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely();
    }


    @Test
    void testWhenPutThenId() {
        var expected = Answer.builder()
                .error(202)
                .payload(new ApiResponse<>(TestData.ZERO_UUID))
                .build();
        Mockito.when(mock.update(TestData.USERNAME.OBJECT_0)).thenReturn(TestData.UNI_OPTIONAL_ZERO_UUID);
        service.put(TestData.USERNAME.OBJECT_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely();
    }

    @Test
    void testWhenPutThenEmpty() {
        Mockito.when(mock.update(TestData.USERNAME.OBJECT_0)).thenReturn(TestData.UNI_OPTIONAL_EMPTY_UUID);
        Mockito.when(mock.findById(TestData.ZERO_UUID)).thenReturn(UNI_OPTIONAL_TEST);
        Assertions.assertThrows(CompletionException.class, () -> service.put(TestData.USERNAME.OBJECT_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenDeleteThenId() {
        var expected = Answer.of(new ApiResponse<>(TestData.ZERO_UUID));
        Mockito.when(mock.delete(TestData.ZERO_UUID)).thenReturn(TestData.UNI_OPTIONAL_ZERO_UUID);
        service.delete(TestData.ZERO_UUID)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely();
    }

    @Test
    void testWhenDeleteWithNullIdThenEmpty() {
        service.delete(null)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely();
    }
}