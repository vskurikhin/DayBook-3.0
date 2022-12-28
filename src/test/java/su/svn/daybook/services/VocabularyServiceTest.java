/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * VocabularyServiceTest.java
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
import su.svn.daybook.domain.dao.VocabularyDao;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.messages.ApiResponse;
import su.svn.daybook.domain.model.Vocabulary;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@QuarkusTest
class VocabularyServiceTest {

    @Inject
    VocabularyService service;

    static VocabularyDao mock;

    static Uni<Optional<Vocabulary>> optionalUniTest = Uni.createFrom().item(Optional.of(DataTest.OBJECT_Vocabulary_0));

    static Uni<Optional<Long>> optionalUniId = Uni.createFrom().item(Optional.of(0L));

    static Uni<Optional<Long>> optionalUniEmptyId = Uni.createFrom().item(Optional.empty());

    static Multi<Vocabulary> multiTest = Multi.createFrom().item(DataTest.OBJECT_Vocabulary_0);

    static Multi<Vocabulary> multiEmpties = Multi.createFrom().empty();

    static Multi<Vocabulary> multiWithNull = Multi.createFrom().item(() -> null);

    @BeforeEach
    void setUp() {
        mock = Mockito.mock(VocabularyDao.class);
        Mockito.when(mock.findById(0L)).thenReturn(optionalUniTest);
        QuarkusMock.installMockForType(mock, VocabularyDao.class);
    }

    @Test
    void testMethod_getAll() {
        Mockito.when(mock.findAll()).thenReturn(multiTest);
        List<Answer> result = service.getAll()
                .subscribe()
                .asStream()
                .peek(actual -> Assertions.assertEquals(Answer.of(DataTest.OBJECT_Vocabulary_0), actual))
                .collect(Collectors.toList());
        Assertions.assertTrue(result.size() > 0);
    }

    @Test
    void testMethod_getAll_whithEmptyResult() {
        Mockito.when(mock.findAll()).thenReturn(multiEmpties);
        List<Answer> result = service.getAll()
                .subscribe()
                .asStream()
                .collect(Collectors.toList());
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void testMethod_getAll_whithNullResult() {
        Mockito.when(mock.findAll()).thenReturn(multiWithNull);
        List<Answer> result = service.getAll()
                .subscribe()
                .asStream()
                .collect(Collectors.toList());
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void testMethod_vocabularyGet() {
        service.vocabularyGet("0")
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.of(Optional.of(DataTest.OBJECT_Vocabulary_0)), actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_codeGet_whenNoNumberParameter() {
        service.vocabularyGet("noNumber")
                .onItem()
                .invoke(actual -> Assertions.assertEquals(DataTest.ANSWER_ERROR_NoNumber, actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_codeGet_whenNullParameter() {
        service.vocabularyGet(null)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_vocabularyAdd() {
        var expected = Answer.of(new ApiResponse<>(0L));
        Mockito.when(mock.insert(DataTest.OBJECT_Vocabulary_0)).thenReturn(optionalUniId);
        service.vocabularyAdd(DataTest.OBJECT_Vocabulary_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_codeAdd_whithEmptyResult() {
        Mockito.when(mock.insert(DataTest.OBJECT_Vocabulary_0)).thenReturn(optionalUniEmptyId);
        service.vocabularyAdd(DataTest.OBJECT_Vocabulary_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_vocabularyPut() {
        Mockito.when(mock.update(DataTest.OBJECT_Vocabulary_0)).thenReturn(optionalUniId);
        var expected = Answer.of(new ApiResponse<>(0L));
        service.vocabularyPut(DataTest.OBJECT_Vocabulary_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_codePut_whithEmptyResult() {
        Mockito.when(mock.update(DataTest.OBJECT_Vocabulary_0)).thenReturn(optionalUniEmptyId);
        service.vocabularyPut(DataTest.OBJECT_Vocabulary_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_vocabularyDelete() {
        Mockito.when(mock.delete(0L)).thenReturn(optionalUniId);
        var expected = Answer.of(new ApiResponse<>(0L));
        service.vocabularyDelete("0")
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_codeDelete_whenNoNumberParameter() {
        service.vocabularyDelete("noNumber")
                .onItem()
                .invoke(actual -> Assertions.assertEquals(DataTest.ANSWER_ERROR_NoNumber, actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_codeDelete_whenNullParameter() {
        service.vocabularyDelete(null)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely();
    }
}