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
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import su.svn.daybook.DataTest;
import su.svn.daybook.domain.dao.WordDao;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.messages.DictionaryResponse;
import su.svn.daybook.domain.model.Word;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@QuarkusTest
class WordServiceTest {

    @Inject
    WordService service;

    static WordDao mock;

    static Uni<Optional<Word>> optionalUniTest = Uni.createFrom().item(Optional.of(DataTest.OBJECT_Word_0));

    static Uni<Optional<String>> optionalUniId = Uni.createFrom().item(Optional.of("word"));

    static Uni<Optional<String>> optionalUniEmptyId = Uni.createFrom().item(Optional.empty());

    static Multi<Word> multiTest = Multi.createFrom().item(DataTest.OBJECT_Word_0);

    static Multi<Word> multiEmpties = Multi.createFrom().empty();

    static Multi<Word> multiWithNull = Multi.createFrom().item(() -> null);

    @BeforeEach
    void setUp() {
        mock = Mockito.mock(WordDao.class);
        Mockito.when(mock.findByWord("word")).thenReturn(optionalUniTest);
        QuarkusMock.installMockForType(mock, WordDao.class);
    }

    @Test
    void testMethod_getAll() {
        Mockito.when(mock.findAll()).thenReturn(multiTest);
        List<Answer> result = service.getAll()
                .subscribe()
                .asStream()
                .peek(actual -> Assertions.assertEquals(Answer.of(DataTest.OBJECT_Word_0), actual))
                .toList();
        Assertions.assertTrue(result.size() > 0);
    }

    @Test
    void testMethod_getAll_whithEmptyResult() {
        Mockito.when(mock.findAll()).thenReturn(multiEmpties);
        List<Answer> result = service.getAll()
                .subscribe()
                .asStream()
                .toList();
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void testMethod_getAll_whithNullResult() {
        Mockito.when(mock.findAll()).thenReturn(multiWithNull);
        List<Answer> result = service.getAll()
                .subscribe()
                .asStream()
                .toList();
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void testMethod_wordGet() {
        service.wordGet("word")
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.of(DataTest.OBJECT_Word_0), actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_wordGet_whenNullParameter() {
        service.wordGet(null)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_wordAdd() {
        var expected = Answer.of(DictionaryResponse.word("word"));
        Mockito.when(mock.insert(DataTest.OBJECT_Word_0)).thenReturn(optionalUniId);
        service.wordAdd(DataTest.OBJECT_Word_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_wordAdd_whithEmptyResult() {
        Mockito.when(mock.insert(DataTest.OBJECT_Word_0)).thenReturn(optionalUniEmptyId);
        service.wordAdd(DataTest.OBJECT_Word_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_wordPut() {
        Mockito.when(mock.update(DataTest.OBJECT_Word_0)).thenReturn(optionalUniId);
        var expected = Answer.of(DictionaryResponse.word("word"));
        service.wordPut(DataTest.OBJECT_Word_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_codePut_whithEmptyResult() {
        Mockito.when(mock.update(DataTest.OBJECT_Word_0)).thenReturn(optionalUniEmptyId);
        service.wordPut(DataTest.OBJECT_Word_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_wordDelete() {
        Mockito.when(mock.delete("word")).thenReturn(optionalUniId);
        var expected = Answer.of(DictionaryResponse.word("word"));
        service.wordDelete("word")
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_wordDelete_whenNullParameter() {
        service.wordDelete(null)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely();
    }
}