/*
 * This file was last modified at 2022.01.12 17:31 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * WordServiceTest.java
 * $Id$
 */

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
import su.svn.daybook.domain.dao.WordDao;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.messages.ApiResponse;
import su.svn.daybook.domain.model.Word;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionException;

@QuarkusTest
class WordServiceTest {

    @Inject
    WordService service;

    static WordDao mock;

    static final Uni<Optional<String>> UNI_OPTIONAL_NONE_STRING = Uni.createFrom().item(Optional.of(Word.NONE));

    static final Uni<Optional<Word>> UNI_OPTIONAL_TEST = Uni.createFrom().item(Optional.of(TestData.WORD.OBJECT_0));

    static final Multi<Word> MULTI_TEST = Multi.createFrom().item(TestData.WORD.OBJECT_0);

    static final Multi<Word> MULTI_WITH_NULL = TestData.createMultiWithNull(Word.class);

    static final Multi<Word> MULTI_EMPTIES = TestData.createMultiEmpties(Word.class);

    @BeforeEach
    void setUp() {
        mock = Mockito.mock(WordDao.class);
        Mockito.when(mock.findById(Word.NONE)).thenReturn(UNI_OPTIONAL_TEST);
        Mockito.when(mock.findByWord(Word.NONE)).thenReturn(UNI_OPTIONAL_TEST);
        QuarkusMock.installMockForType(mock, WordDao.class);
    }

    @Test
    void testWhenGetAllThenSingletonList() {
        Mockito.when(mock.count()).thenReturn(TestData.UNI_OPTIONAL_ONE_LONG);
        Mockito.when(mock.findAll()).thenReturn(MULTI_TEST);
        List<Answer> result = new ArrayList<>();
        Assertions.assertDoesNotThrow(() -> result.addAll(service.getAll()
                .subscribe()
                .asStream()
                .peek(actual -> Assertions.assertEquals(Answer.of(TestData.WORD.OBJECT_0), actual)).toList()));
        Assertions.assertTrue(result.size() > 0);
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
        Mockito.when(mock.count()).thenReturn(TestData.UNI_OPTIONAL_ONE_LONG);
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

        Mockito.when(mock.findRange(0L, Short.MAX_VALUE)).thenReturn(MULTI_TEST);
        Mockito.when(mock.count()).thenReturn(TestData.UNI_OPTIONAL_ONE_LONG);

        PageRequest pageRequest = new PageRequest(0, Short.MAX_VALUE);
        var expected = Page.<Answer>builder()
                .totalPages(1L)
                .totalElements(1)
                .pageSize((short) 1)
                .prevPage(false)
                .nextPage(false)
                .content(Collections.singletonList(Answer.of(TestData.WORD.OBJECT_0)))
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

        Mockito.when(mock.findRange(0L, Short.MAX_VALUE)).thenReturn(MULTI_EMPTIES);
        Mockito.when(mock.count()).thenReturn(TestData.UNI_OPTIONAL_ZERO_LONG);

        PageRequest pageRequest = new PageRequest(0, Short.MAX_VALUE);
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
        Assertions.assertDoesNotThrow(() -> service.get(Word.NONE)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.of(TestData.WORD.OBJECT_0), actual))
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
                .payload(new ApiResponse<>(Word.NONE))
                .build();
        Mockito.when(mock.insert(TestData.WORD.OBJECT_0)).thenReturn(UNI_OPTIONAL_NONE_STRING);
        Assertions.assertDoesNotThrow(() -> service.add(TestData.WORD.OBJECT_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenAddThenEmpty() {
        Mockito.when(mock.insert(TestData.WORD.OBJECT_0)).thenReturn(TestData.UNI_OPTIONAL_EMPTY_STRING);
        Assertions.assertDoesNotThrow(() -> service.add(TestData.WORD.OBJECT_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenPutThenId() {
        var expected = Answer.builder()
                .error(202)
                .payload(new ApiResponse<>(Word.NONE))
                .build();
        Mockito.when(mock.update(TestData.WORD.OBJECT_0)).thenReturn(UNI_OPTIONAL_NONE_STRING);
        Assertions.assertDoesNotThrow(() -> service.put(TestData.WORD.OBJECT_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenPutThenEmpty() {
        Mockito.when(mock.update(TestData.WORD.OBJECT_0)).thenReturn(TestData.UNI_OPTIONAL_EMPTY_STRING);
        Assertions.assertThrows(CompletionException.class, () -> service.put(TestData.WORD.OBJECT_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenDeleteThenId() {
        Mockito.when(mock.delete(Word.NONE)).thenReturn(UNI_OPTIONAL_NONE_STRING);
        var expected = Answer.of(new ApiResponse<>(Word.NONE));
        Assertions.assertDoesNotThrow(() -> service.delete(Word.NONE)
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