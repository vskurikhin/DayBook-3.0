/*
 * This file was last modified at 2022.01.12 17:31 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * WordServiceTest.java
 * $Id$
 */

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
import su.svn.daybook.domain.dao.WordDao;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.messages.ApiResponse;
import su.svn.daybook.domain.model.Word;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        Assertions.assertThrows(CompositeException.class, () -> service.put(TestData.WORD.OBJECT_0)
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