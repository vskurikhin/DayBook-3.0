/*
 * This file was last modified at 2024-05-14 23:10 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * TagLabelServiceTest.java
 * $Id$
 */

package su.svn.daybook3.api.services.models;

import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import su.svn.daybook3.api.TestData;
import su.svn.daybook3.api.TestUtils;
import su.svn.daybook3.api.domain.dao.TagLabelDao;
import su.svn.daybook3.api.domain.model.TagLabelTable;
import su.svn.daybook3.api.models.pagination.Page;
import su.svn.daybook3.api.models.pagination.PageRequest;
import su.svn.daybook3.domain.messages.Answer;
import su.svn.daybook3.domain.messages.ApiResponse;
import su.svn.daybook3.domain.messages.Request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@QuarkusTest
class TagLabelServiceTest {

    @Inject
    TagLabelService service;

    static TagLabelDao mock;

    static final Uni<Optional<TagLabelTable>> UNI_OPTIONAL_TEST = Uni.createFrom().item(Optional.of(TestData.TAG_LABEL.TABLE_0));

    static final Multi<TagLabelTable> MULTI_TEST = Multi.createFrom().item(TestData.TAG_LABEL.TABLE_0);

    static final Multi<TagLabelTable> MULTI_WITH_NULL = TestUtils.createMultiWithNull(TagLabelTable.class);

    static final Multi<TagLabelTable> MULTI_EMPTIES = TestUtils.createMultiEmpties(TagLabelTable.class);

    @BeforeEach
    void setUp() {
        mock = Mockito.mock(TagLabelDao.class);
        Mockito.when(mock.findById(TestData.uuid.STRING_ZERO)).thenReturn(UNI_OPTIONAL_TEST);
        QuarkusMock.installMockForType(mock, TagLabelDao.class);
    }

    @Test
    void testWhenGetAllThenSingletonList() {
        Mockito.when(mock.count()).thenReturn(TestData.UNI_OPTIONAL_ONE_LONG);
        Mockito.when(mock.findAll()).thenReturn(MULTI_TEST);
        List<Answer> result = new ArrayList<>();
        Assertions.assertDoesNotThrow(() -> result.addAll(service.getAll()
                .subscribe()
                .asStream()
                .peek(actual -> Assertions.assertEquals(Answer.of(TestData.TAG_LABEL.MODEL_0), actual)).toList()));
        Assertions.assertTrue(result.size() > 0);
    }

    @Test
    @ActivateRequestContext
    void testWhenGetAllThenCountMinusOne() {
        Mockito.when(mock.count()).thenReturn(TestData.UNI_OPTIONAL_MINUS_ONE_LONG);
        Mockito.when(mock.findAll()).thenReturn(MULTI_EMPTIES);
        List<Answer> result = new ArrayList<>();
        Assertions.assertThrows(
                java.lang.IndexOutOfBoundsException.class,
                () -> result.addAll(service.getAll()
                        .subscribe()
                        .asStream()
                        .toList()));
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void testWhenGetAllThenEmpty() {
        Mockito.when(mock.count()).thenReturn(TestData.UNI_OPTIONAL_ZERO_LONG);
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
        Mockito.when(mock.count()).thenReturn(TestData.UNI_OPTIONAL_ZERO_LONG);
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
                .page(0)
                .totalPages(1L)
                .totalRecords(1)
                .rows((short) 1)
                .prevPage(false)
                .nextPage(false)
                .content(Collections.singletonList(Answer.of(TestData.TAG_LABEL.MODEL_0)))
                .build();

        Assertions.assertDoesNotThrow(() -> service.getPage(new Request<>(pageRequest, null))
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
                .totalRecords(0)
                .rows((short) 0)
                .prevPage(false)
                .nextPage(false)
                .content(Collections.emptyList())
                .build();

        Assertions.assertDoesNotThrow(() -> service.getPage(new Request<>(pageRequest, null))
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
                .totalRecords(1)
                .rows((short) 0)
                .prevPage(false)
                .nextPage(false)
                .content(Collections.emptyList())
                .build();

        Assertions.assertDoesNotThrow(() -> service.getPage(new Request<>(pageRequest, null))
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely()
        );
    }

    @Test
    void testWhenGetThenEntry() {
        Assertions.assertDoesNotThrow(() -> service.get(new Request<>(TestData.uuid.STRING_ZERO, null))
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.of(TestData.TAG_LABEL.MODEL_0), actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenAddThenId() {
        var expected = Answer.builder()
                .message(Answer.DEFAULT_MESSAGE)
                .error(201)
                .payload(new ApiResponse<>(TestData.TAG_LABEL.ID, 201))
                .build();
        Mockito.when(mock.insert(TestData.TAG_LABEL.TABLE_0)).thenReturn(TestData.TAG_LABEL.UNI_OPTIONAL_ID);
        Assertions.assertDoesNotThrow(() -> service.add(new Request<>(TestData.TAG_LABEL.MODEL_0, null))
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
                .payload("No value present for entry: " + TestData.TAG_LABEL.TABLE_0)
                .build();
        Mockito.when(mock.insert(TestData.TAG_LABEL.TABLE_0)).thenReturn(TestData.UNI_OPTIONAL_EMPTY_STRING);
        Assertions.assertDoesNotThrow(() -> service.add(new Request<>(TestData.TAG_LABEL.MODEL_0, null))
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenPutThenId() {
        var expected = Answer.builder()
                .message(Answer.DEFAULT_MESSAGE)
                .error(202)
                .payload(new ApiResponse<>(TestData.TAG_LABEL.ID, 202))
                .build();
        Mockito.when(mock.update(TestData.TAG_LABEL.TABLE_0)).thenReturn(TestData.TAG_LABEL.UNI_OPTIONAL_ID);
        Assertions.assertDoesNotThrow(() -> service.put(new Request<>(TestData.TAG_LABEL.MODEL_0, null))
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenPutThenEmpty() {
        Mockito.when(mock.update(TestData.TAG_LABEL.TABLE_0)).thenReturn(TestData.UNI_OPTIONAL_EMPTY_STRING);
        Assertions.assertThrows(RuntimeException.class, () -> service.put(new Request<>(TestData.TAG_LABEL.MODEL_0, null))
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenDeleteThenId() {
        Mockito.when(mock.delete(TestData.uuid.STRING_ZERO)).thenReturn(TestData.TAG_LABEL.UNI_OPTIONAL_ID);
        var expected = Answer.of(new ApiResponse<>(TestData.TAG_LABEL.ID, 200));
        Assertions.assertDoesNotThrow(() -> service.delete(new Request<>(TestData.uuid.STRING_ZERO, null))
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenDeleteThenNullParameter() {
        var expected = Answer.builder()
                .message(Answer.NO_SUCH_ELEMENT)
                .error(404)
                .payload("No value present for id: null")
                .build();
        Assertions.assertThrows(NullPointerException.class, () -> service.delete(null)
                .await()
                .indefinitely());
    }
}