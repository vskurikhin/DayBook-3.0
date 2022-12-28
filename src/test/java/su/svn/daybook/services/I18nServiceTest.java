/*
 * This file was last modified at 2021.12.15 13:12 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * I18nServiceTest.java
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
import su.svn.daybook.domain.dao.I18nDao;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.messages.ApiResponse;
import su.svn.daybook.domain.model.I18n;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@QuarkusTest
class I18nServiceTest {

    @Inject
    I18nService service;

    static I18nDao mock;

    static Uni<Optional<I18n>> optionalUniTest = Uni.createFrom().item(Optional.of(DataTest.OBJECT_I18n_0));

    static Uni<Optional<Long>> optionalUniId = Uni.createFrom().item(Optional.of(0L));

    static Uni<Optional<Long>> optionalUniEmptyId = Uni.createFrom().item(Optional.empty());

    static Multi<I18n> multiTest = Multi.createFrom().item(DataTest.OBJECT_I18n_0);

    static Multi<I18n> multiEmpties = Multi.createFrom().empty();

    static Multi<I18n> multiWithNull = Multi.createFrom().item(() -> null);

    @BeforeEach
    void setUp() {
        mock = Mockito.mock(I18nDao.class);
        Mockito.when(mock.findById(0L)).thenReturn(optionalUniTest);
        QuarkusMock.installMockForType(mock, I18nDao.class);
    }

    @Test
    void testMethod_getAll() {
        Mockito.when(mock.findAll()).thenReturn(multiTest);
        List<Answer> result = service.getAll()
                .subscribe()
                .asStream()
                .peek(actual -> Assertions.assertEquals(Answer.of(DataTest.OBJECT_I18n_0), actual))
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
    void testMethod_i18nGet() {
        service.i18nGet("0")
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.of(Optional.of(DataTest.OBJECT_I18n_0)), actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_i18nGet_whenNoNumberParameter() {
        service.i18nGet("noNumber")
                .onItem()
                .invoke(actual -> Assertions.assertEquals(DataTest.ANSWER_ERROR_NoNumber, actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_i18nGet_whenNullParameter() {
        service.i18nGet(null)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_i18nAdd() {
        var expected = Answer.of(new ApiResponse<>(0L));
        Mockito.when(mock.insert(DataTest.OBJECT_I18n_0)).thenReturn(optionalUniId);
        service.i18nAdd(DataTest.OBJECT_I18n_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_i18nAdd_whithEmptyResult() {
        Mockito.when(mock.insert(DataTest.OBJECT_I18n_0)).thenReturn(optionalUniEmptyId);
        service.i18nAdd(DataTest.OBJECT_I18n_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_i18nPut() {
        Mockito.when(mock.update(DataTest.OBJECT_I18n_0)).thenReturn(optionalUniId);
        var expected = Answer.of(new ApiResponse<>(0L));
        service.i18nPut(DataTest.OBJECT_I18n_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_i18nPut_whithEmptyResult() {
        Mockito.when(mock.update(DataTest.OBJECT_I18n_0)).thenReturn(optionalUniEmptyId);
        service.i18nPut(DataTest.OBJECT_I18n_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_i18nDelete() {
        Mockito.when(mock.delete(0L)).thenReturn(optionalUniId);
        var expected = Answer.of(new ApiResponse<>(0L));
        service.i18nDelete("0")
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_i18nDelete_whenNoNumberParameter() {
        service.i18nDelete("noNumber")
                .onItem()
                .invoke(actual -> Assertions.assertEquals(DataTest.ANSWER_ERROR_NoNumber, actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_i18nDelete_whenNullParameter() {
        service.i18nDelete(null)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely();
    }
/*
    @Inject
    I18nService service;

    static I18nDao mock;

    static Uni<Optional<I18n>> optionalUniTest = Uni.createFrom().item(Optional.of(DataTest.OBJECT_I18n_0));

    static Uni<Answer> empty = Uni.createFrom().item(new Answer("empty", 1));

    static Uni<Optional<Long>> optionalUniId = Uni.createFrom().item(Optional.of(0L));

    static Multi<I18n> multiTest = Multi.createFrom().item(DataTest.OBJECT_I18n_0);

    static Multi<I18n> multiEmpties = Multi.createFrom().empty();

    @BeforeAll
    public static void setup() {

        mock = Mockito.mock(I18nDao.class);
        Mockito.when(mock.findById(0L)).thenReturn(optionalUniTest);
        Mockito.when(mock.insert(DataTest.OBJECT_I18n_0)).thenReturn(optionalUniId);
        Mockito.when(mock.findAll()).thenReturn(multiTest);
        QuarkusMock.installMockForType(mock, I18nDao.class);
    }

    @Test
    void testMethod_getAll() {
        service.getAll()
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.of(DataTest.OBJECT_I18n_0), actual))
                .toUni()
                .await()
                .indefinitely();
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
    void testMethod_i18nGet() {
        service.i18nGet("0")
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.of(Optional.of(DataTest.OBJECT_I18n_0)), actual))
                .await()
                .indefinitely();
    }

    @Test
    void testNullTagGet() {
        service.i18nGet(null)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely();
    }

    @Test
    void tagAdd() {
        service.i18nAdd(DataTest.OBJECT_I18n_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.of(0L), actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_getAll_return_null() {
        Mockito.when(mock.findAll()).thenReturn(Multi.createFrom().empty());
        QuarkusMock.installMockForType(mock, I18nDao.class);
        service.getAll()
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.of(DataTest.OBJECT_I18n_0), actual))
                .toUni()
                .await()
                .indefinitely();
    }
*/
}