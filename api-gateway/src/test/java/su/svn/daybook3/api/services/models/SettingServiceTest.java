/*
 * This file was last modified at 2024-05-14 23:10 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * SettingServiceTest.java
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
import su.svn.daybook3.api.domain.dao.SettingDao;
import su.svn.daybook3.api.domain.dao.SettingViewDao;
import su.svn.daybook3.api.domain.model.SettingTable;
import su.svn.daybook3.api.domain.model.SettingView;
import su.svn.daybook3.api.domain.transact.SettingTransactionalJob;
import su.svn.daybook3.api.models.pagination.Page;
import su.svn.daybook3.api.models.pagination.PageRequest;
import su.svn.daybook3.api.services.cache.SettingCacheProvider;
import su.svn.daybook3.domain.messages.Answer;
import su.svn.daybook3.domain.messages.ApiResponse;
import su.svn.daybook3.domain.messages.Request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@QuarkusTest
class SettingServiceTest {

    @Inject
    SettingService service;

    static SettingCacheProvider settingCacheProviderMock;
    static SettingDao settingDaoMock;
    static SettingTransactionalJob settingTransactionalJobMock;
    static SettingViewDao settingViewDaoMock;
    static final Uni<Optional<SettingTable>> UNI_OPTIONAL_TEST = Uni.createFrom().item(Optional.of(TestData.SETTING.TABLE_0));
    static final Uni<Optional<SettingView>> UNI_OPTIONAL_VIEW = Uni.createFrom().item(Optional.of(TestData.SETTING.VIEW_0));
    static final Multi<SettingTable> MULTI_TEST = Multi.createFrom().item(TestData.SETTING.TABLE_0);
    static final Multi<SettingView> MULTI_VIEW_TEST = Multi.createFrom().item(TestData.SETTING.VIEW_0);
    static final Multi<SettingTable> MULTI_WITH_NULL = TestUtils.createMultiWithNull(SettingTable.class);
    static final Multi<SettingTable> MULTI_EMPTIES = TestUtils.createMultiEmpties(SettingTable.class);
    static final Multi<SettingView> MULTI_VIEW_EMPTIES = TestUtils.createMultiEmpties(SettingView.class);

    @BeforeEach
    void setUp() {
        settingCacheProviderMock = Mockito.mock(SettingCacheProvider.class);
        settingDaoMock = Mockito.mock(SettingDao.class);
        settingTransactionalJobMock = Mockito.mock(SettingTransactionalJob.class);
        settingViewDaoMock = Mockito.mock(SettingViewDao.class);
        Mockito.when(settingDaoMock.findById(0L)).thenReturn(UNI_OPTIONAL_TEST);
        Mockito.when(settingViewDaoMock.findById(0L)).thenReturn(UNI_OPTIONAL_VIEW);
        QuarkusMock.installMockForType(settingDaoMock, SettingDao.class);
        QuarkusMock.installMockForType(settingTransactionalJobMock, SettingTransactionalJob.class);
        QuarkusMock.installMockForType(settingViewDaoMock, SettingViewDao.class);
    }

    @Test
    void testWhenGetAllThenSingletonList() {
        Mockito.when(settingViewDaoMock.count()).thenReturn(TestData.UNI_OPTIONAL_ONE_LONG);
        Mockito.when(settingViewDaoMock.findAll()).thenReturn(MULTI_VIEW_TEST);
        List<Answer> result = new ArrayList<>();
        Assertions.assertDoesNotThrow(() -> result.addAll(service.getAll()
                .subscribe()
                .asStream()
                .peek(actual -> Assertions.assertEquals(Answer.of(TestData.SETTING.MODEL_0), actual)).toList()));
        Assertions.assertTrue(result.size() > 0);
    }

    @Test
    @ActivateRequestContext
    void testWhenGetAllThenCountMinusOne() {
        Mockito.when(settingViewDaoMock.count()).thenReturn(TestData.UNI_OPTIONAL_MINUS_ONE_LONG);
        Mockito.when(settingViewDaoMock.findAll()).thenReturn(MULTI_VIEW_EMPTIES);
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
        Mockito.when(settingViewDaoMock.count()).thenReturn(TestData.UNI_OPTIONAL_ZERO_LONG);
        Mockito.when(settingViewDaoMock.findAll()).thenReturn(MULTI_VIEW_EMPTIES);
        List<Answer> result = new ArrayList<>();
        Assertions.assertDoesNotThrow(() -> result.addAll(service.getAll()
                .subscribe()
                .asStream()
                .toList()));
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void testWhenGetAllThenNull() {
        Mockito.when(settingViewDaoMock.count()).thenReturn(TestData.UNI_OPTIONAL_ZERO_LONG);
        Mockito.when(settingViewDaoMock.findAll()).thenReturn(MULTI_VIEW_EMPTIES);
        List<Answer> result = new ArrayList<>();
        Assertions.assertDoesNotThrow(() -> result.addAll(service.getAll()
                .subscribe()
                .asStream()
                .toList()));
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void testWhenGetPageThenSingletonList() {

        Mockito.when(settingViewDaoMock.findRange(0L, Short.MAX_VALUE - 1)).thenReturn(MULTI_VIEW_TEST);
        Mockito.when(settingDaoMock.count()).thenReturn(TestData.UNI_OPTIONAL_ONE_LONG);

        PageRequest pageRequest = new PageRequest(0L, (short) (Short.MAX_VALUE - 1));
        var expected = Page.<Answer>builder()
                .page(0)
                .totalPages(1L)
                .totalRecords(1)
                .rows((short) 1)
                .prevPage(false)
                .nextPage(false)
                .content(Collections.singletonList(Answer.of(TestData.SETTING.MODEL_0)))
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

        QuarkusMock.installMockForType(settingCacheProviderMock, SettingCacheProvider.class);

        Mockito.when(settingDaoMock.findRange(0L, Short.MAX_VALUE - 2)).thenReturn(MULTI_EMPTIES);
        Mockito.when(settingDaoMock.count()).thenReturn(TestData.UNI_OPTIONAL_ZERO_LONG);

        PageRequest pageRequest = new PageRequest(0L, (short) (Short.MAX_VALUE - 2));
        var expected = Page.<Answer>builder()
                .totalPages(0L)
                .totalRecords(0)
                .rows((short) 0)
                .prevPage(false)
                .nextPage(false)
                .content(Collections.emptyList())
                .build();

        Mockito.when(settingCacheProviderMock.getPage(pageRequest)).thenReturn(TestData.UNI_PAGE_ANSWER_EMPTY);

        Assertions.assertDoesNotThrow(() -> service.getPage(new Request<>(pageRequest, null))
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely()
        );
    }

    @Test
    void testWhenGetPageThenZeroPage() {

        Mockito.when(settingDaoMock.findRange(0L, 0)).thenReturn(MULTI_EMPTIES);
        Mockito.when(settingDaoMock.count()).thenReturn(TestData.UNI_OPTIONAL_ZERO_LONG);

        PageRequest pageRequest = new PageRequest(0, (short) 0);
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
    void testWhenGetThenEntry() {
        Assertions.assertDoesNotThrow(() -> service.get(new Request<>(0L, null))
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.of(TestData.SETTING.MODEL_0), actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenAddThenId() {
        var expected = Answer.builder()
                .message(Answer.DEFAULT_MESSAGE)
                .error(201)
                .payload(new ApiResponse<>(Long.valueOf(0), 201))
                .build();
        var table = new SettingTable(
                0L, SettingTable.NONE, null, null, 0L, null, null, null, true, true, true, 0
        );
        Mockito.when(settingTransactionalJobMock.insert(table, SettingTable.DEFAULT_TYPE))
                .thenReturn(TestData.lng.UNI_OPTIONAL_ZERO);
        Assertions.assertDoesNotThrow(() -> service.add(new Request<>(TestData.SETTING.MODEL_0, null))
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenAddThenEmpty() {
        var table = new SettingTable(
                0L, SettingTable.NONE, null, null, 0L, null, null, null, true, true, true, 0
        );
        var expected = Answer.builder()
                .message("bad request")
                .error(400)
                .payload("No value present for entry: " + table)
                .build();
        Mockito.when(settingTransactionalJobMock.insert(table, SettingTable.DEFAULT_TYPE))
                .thenReturn(TestData.lng.UNI_OPTIONAL_EMPTY);
        Assertions.assertDoesNotThrow(() -> service.add(new Request<>(TestData.SETTING.MODEL_0, null))
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
                .payload(new ApiResponse<>(Long.valueOf(0), 202))
                .build();
        var table = new SettingTable(
                0L, SettingTable.NONE, null, null, 0L, null, null, null, true, true, true, 0
        );
        Mockito.when(settingTransactionalJobMock.update(table, SettingTable.DEFAULT_TYPE))
                .thenReturn(TestData.lng.UNI_OPTIONAL_ZERO);
        Assertions.assertDoesNotThrow(() -> service.put(new Request<>(TestData.SETTING.MODEL_0, null))
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenPutThenEmpty() {
        Mockito.when(settingTransactionalJobMock.update(
                        TestData.SETTING.TABLE_0, SettingTable.DEFAULT_TYPE))
                .thenReturn(TestData.lng.UNI_OPTIONAL_EMPTY);
        Assertions.assertThrows(RuntimeException.class, () -> service.put(new Request<>(TestData.SETTING.MODEL_0, null))
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenDeleteThenId() {
        Mockito.when(settingDaoMock.delete(0L)).thenReturn(TestData.lng.UNI_OPTIONAL_ZERO);
        var expected = Answer.of(new ApiResponse<>(Long.valueOf(0), 200));
        Assertions.assertDoesNotThrow(() -> service.delete(new Request<>(0L, null))
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