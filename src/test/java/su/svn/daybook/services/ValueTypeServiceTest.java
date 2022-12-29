/*
 * This file was last modified at 2021.12.15 13:12 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * ValueTypeServiceTest.java
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
import su.svn.daybook.TestData;
import su.svn.daybook.domain.dao.ValueTypeDao;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.messages.ApiResponse;
import su.svn.daybook.domain.model.ValueType;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@QuarkusTest
class ValueTypeServiceTest {

    @Inject
    ValueTypeService service;

    static ValueTypeDao mock;

    static final Uni<Optional<ValueType>> UNI_OPTIONAL_TEST = Uni.createFrom().item(Optional.of(TestData.VALUE_TYPE.OBJECT_0));

    static final Multi<ValueType> MULTI_TEST = Multi.createFrom().item(TestData.VALUE_TYPE.OBJECT_0);

    static final Multi<ValueType> MULTI_WITH_NULL = TestData.createMultiWithNull(ValueType.class);

    static final Multi<ValueType> MULTI_EMPTIES = TestData.createMultiEmpties(ValueType.class);

    @BeforeEach
    void setUp() {
        mock = Mockito.mock(ValueTypeDao.class);
        Mockito.when(mock.findById(0L)).thenReturn(UNI_OPTIONAL_TEST);
        QuarkusMock.installMockForType(mock, ValueTypeDao.class);
    }

    @Test
    void testWhenGetAllThenSingletonList() {
        Mockito.when(mock.findAll()).thenReturn(MULTI_TEST);
        List<Answer> result = new ArrayList<>();
        Assertions.assertDoesNotThrow(() -> result.addAll(service.getAll()
                .subscribe()
                .asStream()
                .peek(actual -> Assertions.assertEquals(Answer.of(TestData.VALUE_TYPE.OBJECT_0), actual)).toList()));
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
        Assertions.assertDoesNotThrow(() -> service.get(0L)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.of(TestData.VALUE_TYPE.OBJECT_0), actual))
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
    void testWhenGetThenNoNumber() {
        Assertions.assertDoesNotThrow(() -> service.get("noNumber")
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.noNumber("For input string: \"noNumber\""), actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenAddThenId() {
        var expected = Answer.builder()
                .error(201)
                .payload(new ApiResponse<>(0L))
                .build();
        Mockito.when(mock.insert(TestData.VALUE_TYPE.OBJECT_0)).thenReturn(TestData.UNI_OPTIONAL_ZERO_LONG);
        Assertions.assertDoesNotThrow(() -> service.add(TestData.VALUE_TYPE.OBJECT_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenAddThenEmpty() {
        Mockito.when(mock.insert(TestData.VALUE_TYPE.OBJECT_0)).thenReturn(TestData.UNI_OPTIONAL_EMPTY_LONG);
        Assertions.assertDoesNotThrow(() -> service.add(TestData.VALUE_TYPE.OBJECT_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenPutThenId() {
        var expected = Answer.builder()
                .error(202)
                .payload(new ApiResponse<>(0L))
                .build();
        Mockito.when(mock.update(TestData.VALUE_TYPE.OBJECT_0)).thenReturn(TestData.UNI_OPTIONAL_ZERO_LONG);
        Assertions.assertDoesNotThrow(() -> service.put(TestData.VALUE_TYPE.OBJECT_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenPutThenEmpty() {
        Mockito.when(mock.update(TestData.VALUE_TYPE.OBJECT_0)).thenReturn(TestData.UNI_OPTIONAL_ZERO_LONG);
        Assertions.assertThrows(RuntimeException.class, () -> service.put(TestData.VALUE_TYPE.OBJECT_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenDeleteThenId() {
        Mockito.when(mock.delete(0L)).thenReturn(TestData.UNI_OPTIONAL_ZERO_LONG);
        var expected = Answer.of(new ApiResponse<>(0L));
        Assertions.assertDoesNotThrow(() -> service.delete(0L)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenDeleteThenNoNumber() {
        Assertions.assertDoesNotThrow(() -> service.delete("noNumber")
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.noNumber("For input string: \"noNumber\""), actual))
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