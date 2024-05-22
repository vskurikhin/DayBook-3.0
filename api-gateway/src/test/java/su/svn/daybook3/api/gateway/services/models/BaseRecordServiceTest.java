/*
 * This file was last modified at 2024-05-14 23:10 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * BaseRecordServiceTest.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.services.models;

import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.vertx.VertxContextSupport;
import io.smallrye.mutiny.TimeoutException;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import su.svn.daybook3.api.gateway.TestData;
import su.svn.daybook3.api.gateway.domain.messages.Answer;
import su.svn.daybook3.api.gateway.domain.messages.ApiResponse;
import su.svn.daybook3.api.gateway.domain.messages.Request;
import su.svn.daybook3.api.gateway.models.pagination.Page;
import su.svn.daybook3.api.gateway.models.pagination.PageRequest;
import su.svn.daybook3.api.gateway.services.domain.BaseRecordDataService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
class BaseRecordServiceTest {

    @Inject
    BaseRecordService service;

    static BaseRecordDataService mock;

    @BeforeEach
    void setUp() {
        mock = Mockito.mock(BaseRecordDataService.class);
        QuarkusMock.installMockForType(mock, BaseRecordDataService.class);
    }

    @Test
    void testWhenAddThenId() {
        var expected = Answer.builder()
                .message(Answer.DEFAULT_MESSAGE)
                .error(201)
                .payload(new ApiResponse<>(TestData.uuid.ZERO, 201))
                .build();
        var request = new Request<>(TestData.BASE_RECORD.MODEL_0, null);

        Mockito.when(mock.add(any())).thenReturn(TestData.uuid.UNI_ZERO);

        Assertions.assertDoesNotThrow(() -> service.add(request)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenAddThenEmpty() {
        var expected = Answer.builder()
                .message("bad request")
                .error(400)
                .build();
        var request = new Request<>(TestData.BASE_RECORD.MODEL_0, null);

        Mockito.when(mock.add(any())).thenReturn(Uni.createFrom().failure(new RuntimeException()));

        Assertions.assertThrows(RuntimeException.class, () -> service.add(request)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenDeleteThenId() {
        Mockito.when(mock.delete(any())).thenReturn(TestData.uuid.UNI_ZERO);

        var expected = Answer.of(new ApiResponse<>(TestData.uuid.ZERO, 200));

        Assertions.assertDoesNotThrow(() -> service.delete(new Request<>(TestData.uuid.ZERO, null))
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenDeleteThenNullParameter() {
        Mockito.when(mock.delete(any())).thenReturn(Uni.createFrom().failure(new RuntimeException()));
        Assertions.assertThrows(RuntimeException.class, () -> service.delete(null)
                .await()
                .indefinitely());
    }

    @Test
    @Disabled
    void testWhenGetThenEntry() throws Throwable {

        var test = TestData.BASE_RECORD.MODEL_0;
        Mockito.when(mock.get(TestData.uuid.ZERO)).thenReturn(Uni.createFrom().item(test));

        VertxContextSupport.subscribeAndAwait(() ->
                service.get(new Request<>(TestData.uuid.ZERO, null))
                        .onItem()
                        .invoke(actual -> Assertions.assertEquals(Answer.of(test), actual)));
    }

    @Test
    void testWhenGetPageThenSingletonList() {

        var pageRequest = new PageRequest(0L, (short) (Short.MAX_VALUE - 1));
        var expected = Page.<Answer>builder()
                .page(0)
                .totalPages(1L)
                .totalRecords(1)
                .rows((short) 1)
                .prevPage(false)
                .nextPage(false)
                .content(Collections.singletonList(Answer.of(TestData.BASE_RECORD.MODEL_0)))
                .build();
        Mockito.when(mock.getPage(pageRequest)).thenReturn(Uni.createFrom().item(expected));

        Assertions.assertDoesNotThrow(() -> service.getPage(new Request<>(pageRequest, null))
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely()
        );
    }


    @Test
    void testWhenGetPageThenNull() {

        var pageRequest = new PageRequest(0L, (short) (Short.MAX_VALUE - 2));
        var expected = Page.<Answer>builder()
                .page(0)
                .totalPages(1L)
                .totalRecords(1)
                .rows((short) 1)
                .prevPage(false)
                .nextPage(false)
                .content(Collections.singletonList(Answer.of(TestData.BASE_RECORD.MODEL_0)))
                .build();
        Mockito.when(mock.getPage(pageRequest)).thenReturn(Uni.createFrom().item(() -> null));

        Assertions.assertThrows(ClassCastException.class, () -> service.getPage(new Request<>(pageRequest, null))
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely()
        );
    }

    @Test
    void testWhenGetPageThenNothing() {

        PageRequest pageRequest = new PageRequest(0L, (short) (Short.MAX_VALUE - 3));
        var expected = Page.<Answer>builder()
                .totalPages(0L)
                .totalRecords(0)
                .rows((short) 0)
                .prevPage(false)
                .nextPage(false)
                .content(Collections.emptyList())
                .build();
        Mockito.when(mock.getPage(pageRequest)).thenReturn(Uni.createFrom().nothing());

        Assertions.assertThrows(TimeoutException.class, () -> service.getPage(new Request<>(pageRequest, null))
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .ifNoItem()
                .after(TestData.DURATION.TIMEOUT_DURATION)
                .fail()
                .await()
                .indefinitely()
        );
    }

    @Test
    void testWhenGetPageThenRuntimeException() {

        PageRequest pageRequest = new PageRequest(0L, (short) (Short.MAX_VALUE - 4));
        var expected = Page.<Answer>builder()
                .totalPages(0L)
                .totalRecords(0)
                .rows((short) 0)
                .prevPage(false)
                .nextPage(false)
                .content(Collections.emptyList())
                .build();
        Mockito.when(mock.getPage(pageRequest)).thenReturn(Uni.createFrom().failure(new RuntimeException()));

        Assertions.assertThrows(RuntimeException.class, () -> service.getPage(new Request<>(pageRequest, null))
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely()
        );
    }

    @Test
    void testWhenPutThenId() {
        var expected = Answer.builder()
                .message(Answer.DEFAULT_MESSAGE)
                .error(202)
                .payload(new ApiResponse<>(TestData.uuid.ZERO, 202))
                .build();
        Mockito.when(mock.put(TestData.BASE_RECORD.MODEL_0)).thenReturn(TestData.uuid.UNI_ZERO);
        Assertions.assertDoesNotThrow(() -> service.put(new Request<>(TestData.BASE_RECORD.MODEL_0, TestData.PRINCIPAL.EMPTY_SET))
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenPutThenNothing() {
        Mockito.when(mock.put(TestData.BASE_RECORD.MODEL_0)).thenReturn(Uni.createFrom().nothing());
        Assertions.assertThrows(TimeoutException.class, () -> service.put(new Request<>(TestData.BASE_RECORD.MODEL_0, TestData.PRINCIPAL.EMPTY_SET))
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .ifNoItem()
                .after(TestData.DURATION.TIMEOUT_DURATION)
                .fail()
                .await()
                .indefinitely());
    }
}