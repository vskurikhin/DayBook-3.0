/*
 * This file was last modified at 2022.01.24 09:53 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * LanguageServiceTest.java
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
import su.svn.daybook.domain.dao.LanguageDao;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.messages.ApiResponse;
import su.svn.daybook.domain.model.Language;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@QuarkusTest
class LanguageServiceTest {

    @Inject
    LanguageService service;

    static LanguageDao mock;

    static Uni<Optional<Language>> optionalUniTest = Uni.createFrom().item(Optional.of(DataTest.OBJECT_Language_0));

    static Uni<Optional<Long>> optionalUniId = Uni.createFrom().item(Optional.of(0L));

    static Uni<Optional<Long>> optionalUniEmptyId = Uni.createFrom().item(Optional.empty());

    static Multi<Language> multiTest = Multi.createFrom().item(DataTest.OBJECT_Language_0);

    static Multi<Language> multiEmpties = Multi.createFrom().empty();

    static Multi<Language> multiWithNull = Multi.createFrom().item(() -> null);

    @BeforeEach
    void setUp() {
        mock = Mockito.mock(LanguageDao.class);
        Mockito.when(mock.findById(0L)).thenReturn(optionalUniTest);
        QuarkusMock.installMockForType(mock, LanguageDao.class);
    }

    @Test
    void testMethod_getAll() {
        Mockito.when(mock.findAll()).thenReturn(multiTest);
        List<Answer> result = service.getAll()
                .subscribe()
                .asStream()
                .peek(actual -> Assertions.assertEquals(Answer.of(DataTest.OBJECT_Language_0), actual))
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
    void testMethod_languageGet() {
        service.languageGet("0")
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.of(Optional.of(DataTest.OBJECT_Language_0)), actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_languageGet_whenNoNumberParameter() {
        service.languageGet("noNumber")
                .onItem()
                .invoke(actual -> Assertions.assertEquals(DataTest.errorNoNumber, actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_languageGet_whenNullParameter() {
        service.languageGet(null)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_languageAdd() {
        var expected = Answer.of(new ApiResponse<>(0L));
        Mockito.when(mock.insert(DataTest.OBJECT_Language_0)).thenReturn(optionalUniId);
        service.languageAdd(DataTest.OBJECT_Language_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_languagePut() {
        Mockito.when(mock.update(DataTest.OBJECT_Language_0)).thenReturn(optionalUniId);
        var expected = Answer.of(new ApiResponse<>(0L));
        service.languagePut(DataTest.OBJECT_Language_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_languageDelete() {
        Mockito.when(mock.delete(0L)).thenReturn(optionalUniId);
        var expected = Answer.of(new ApiResponse<>(0L));
        service.languageDelete("0")
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_languageDelete_whenNoNumberParameter() {
        service.languageDelete("noNumber")
                .onItem()
                .invoke(actual -> Assertions.assertEquals(DataTest.errorNoNumber, actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_languageDelete_whenNullParameter() {
        service.languageDelete(null)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely();
    }
}