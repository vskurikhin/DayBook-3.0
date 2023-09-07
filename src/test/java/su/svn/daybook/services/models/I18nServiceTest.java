/*
 * This file was last modified at 2023.09.07 14:07 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * I18nServiceTest.java
 * $Id$
 */

package su.svn.daybook.services.models;

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
import su.svn.daybook.TestData;
import su.svn.daybook.TestUtils;
import su.svn.daybook.domain.dao.I18nDao;
import su.svn.daybook.domain.dao.I18nViewDao;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.messages.ApiResponse;
import su.svn.daybook.domain.messages.Request;
import su.svn.daybook.domain.model.I18nTable;
import su.svn.daybook.domain.model.I18nView;
import su.svn.daybook.domain.transact.I18nTransactionalJob;
import su.svn.daybook.models.domain.I18n;
import su.svn.daybook.models.domain.Language;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.cache.I18nCacheProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@QuarkusTest
class I18nServiceTest {

    static final Uni<Optional<I18nTable>> UNI_OPTIONAL_TEST = Uni.createFrom().item(Optional.of(TestData.I18N.TABLE_0));
    static final Uni<Optional<I18nView>> UNI_OPTIONAL_VIEW = Uni.createFrom().item(Optional.of(TestData.I18N.VIEW_0));
    static final Uni<I18n> UNI_I18N_NULL = Uni.createFrom().nullItem();
    static final Uni<Optional<I18nView>> UNI_OPTIONAL_VIEW_EMPTY = Uni.createFrom().item(Optional.empty());
    static final Multi<I18nView> MULTI_VIEW_TEST = Multi.createFrom().item(TestData.I18N.VIEW_0);
    static final Multi<I18nTable> MULTI_EMPTIES = TestUtils.createMultiEmpties(I18nTable.class);
    static final Multi<I18nView> MULTI_VIEW_EMPTIES = TestUtils.createMultiEmpties(I18nView.class);
    static I18nDao i18nDaoMock;
    static I18nViewDao i18nViewDaoMock;
    static I18nCacheProvider i18nCacheProviderMock;
    static I18nTransactionalJob i18nTransactionalJobMock;
    @Inject
    I18nService service;

    @BeforeEach
    void setUp() {
        i18nDaoMock = Mockito.mock(I18nDao.class);
        i18nViewDaoMock = Mockito.mock(I18nViewDao.class);
        i18nCacheProviderMock = Mockito.mock(I18nCacheProvider.class);
        i18nTransactionalJobMock = Mockito.mock(I18nTransactionalJob.class);
        Mockito.when(i18nDaoMock.findById(0L)).thenReturn(UNI_OPTIONAL_TEST);
        QuarkusMock.installMockForType(i18nDaoMock, I18nDao.class);
        QuarkusMock.installMockForType(i18nViewDaoMock, I18nViewDao.class);
        QuarkusMock.installMockForType(i18nTransactionalJobMock, I18nTransactionalJob.class);
    }

    @Test
    void testWhenGetAllThenSingletonList() {
        Mockito.when(i18nViewDaoMock.count()).thenReturn(TestData.UNI_OPTIONAL_ONE_LONG);
        Mockito.when(i18nViewDaoMock.findAll()).thenReturn(MULTI_VIEW_TEST);
        List<Answer> result = new ArrayList<>();
        Assertions.assertDoesNotThrow(() -> result.addAll(service.getAll()
                .subscribe()
                .asStream()
                .peek(actual -> Assertions.assertEquals(Answer.of(TestData.I18N.MODEL_0), actual)).toList()));
        Assertions.assertTrue(result.size() > 0);
    }

    @Test
    @ActivateRequestContext
    void testWhenGetAllThenCountMinusOne() {
        Mockito.when(i18nDaoMock.count()).thenReturn(TestData.UNI_OPTIONAL_MINUS_ONE_LONG);
        Mockito.when(i18nDaoMock.findAll()).thenReturn(MULTI_EMPTIES);
        List<Answer> result = new ArrayList<>();
        Assertions.assertThrows(
                IndexOutOfBoundsException.class,
                () -> result.addAll(service.getAll()
                        .subscribe()
                        .asStream()
                        .toList()));
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void testWhenGetAllThenEmpty() {
        Mockito.when(i18nViewDaoMock.count()).thenReturn(TestData.UNI_OPTIONAL_ZERO_LONG);
        Mockito.when(i18nViewDaoMock.findAll()).thenReturn(MULTI_VIEW_EMPTIES);
        List<Answer> result = new ArrayList<>();
        Assertions.assertDoesNotThrow(() -> result.addAll(service.getAll()
                .subscribe()
                .asStream()
                .toList()));
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void testWhenGetAllThenNull() {
        QuarkusMock.installMockForType(i18nCacheProviderMock, I18nCacheProvider.class);
        Mockito.when(i18nViewDaoMock.count()).thenReturn(TestData.lng.UNI_OPTIONAL_ZERO);
        Mockito.when(i18nViewDaoMock.findById(0L)).thenReturn(UNI_OPTIONAL_VIEW_EMPTY);
        Mockito.when(i18nCacheProviderMock.get(0L)).thenReturn(UNI_I18N_NULL);
        List<Answer> result = new ArrayList<>();
        Assertions.assertDoesNotThrow(() -> result.addAll(service.getAll()
                .subscribe()
                .asStream()
                .toList()));
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void testWhenGetPageThenSingletonList() {

        Mockito.when(i18nViewDaoMock.findRange(0L, Short.MAX_VALUE - 1)).thenReturn(MULTI_VIEW_TEST);
        Mockito.when(i18nDaoMock.count()).thenReturn(TestData.UNI_OPTIONAL_ONE_LONG);

        PageRequest pageRequest = new PageRequest(0L, (short) (Short.MAX_VALUE - 1));
        var expected = Page.<Answer>builder()
                .page(0)
                .totalPages(1L)
                .totalRecords(1)
                .rows((short) 1)
                .prevPage(false)
                .nextPage(false)
                .content(Collections.singletonList(Answer.of(TestData.I18N.MODEL_0)))
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
        QuarkusMock.installMockForType(i18nCacheProviderMock, I18nCacheProvider.class);

        Mockito.when(i18nDaoMock.findRange(0L, Short.MAX_VALUE - 2)).thenReturn(MULTI_EMPTIES);
        Mockito.when(i18nDaoMock.count()).thenReturn(TestData.UNI_OPTIONAL_ZERO_LONG);

        PageRequest pageRequest = new PageRequest(0L, (short) (Short.MAX_VALUE - 2));
        var expected = Page.<Answer>builder()
                .totalPages(0L)
                .totalRecords(0)
                .rows((short) 0)
                .prevPage(false)
                .nextPage(false)
                .content(Collections.emptyList())
                .build();
        Mockito.when(i18nCacheProviderMock.getPage(pageRequest)).thenReturn(TestData.UNI_PAGE_ANSWER_EMPTY);

        Assertions.assertDoesNotThrow(() -> service.getPage(new Request<>(pageRequest, null))
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely()
        );
    }

    @Test
    void testWhenGetPageThenZeroPage() {

        Mockito.when(i18nDaoMock.findRange(0L, 0)).thenReturn(MULTI_EMPTIES);
        Mockito.when(i18nDaoMock.count()).thenReturn(TestData.UNI_OPTIONAL_ONE_LONG);

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
        Mockito.when(i18nViewDaoMock.findById(0L)).thenReturn(UNI_OPTIONAL_VIEW);
        Assertions.assertDoesNotThrow(() -> service.get(new Request<>(0L, null))
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.of(TestData.I18N.MODEL_0), actual))
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
        Mockito.when(i18nTransactionalJobMock.insert(TestData.I18N.TABLE_0, Language.NONE))
                .thenReturn(TestData.lng.UNI_OPTIONAL_ZERO);
        Assertions.assertDoesNotThrow(() -> service.add(new Request<>(TestData.I18N.MODEL_0, null))
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
                .payload("No value present for entry: " + TestData.I18N.TABLE_0)
                .build();
        Mockito.when(i18nTransactionalJobMock.insert(TestData.I18N.TABLE_0, Language.NONE))
                .thenReturn(TestData.lng.UNI_OPTIONAL_EMPTY);
        Assertions.assertDoesNotThrow(() -> service.add(new Request<>(TestData.I18N.MODEL_0, null))
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
        Mockito.when(i18nDaoMock.update(TestData.I18N.TABLE_0)).thenReturn(TestData.lng.UNI_OPTIONAL_ZERO);
        Assertions.assertDoesNotThrow(() -> service.put(new Request<>(TestData.I18N.MODEL_0, null))
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenPutThenEmpty() {
        Mockito.when(i18nDaoMock.update(TestData.I18N.TABLE_0)).thenReturn(TestData.lng.UNI_OPTIONAL_ZERO);
        Assertions.assertThrows(RuntimeException.class, () -> service.put(new Request<>(TestData.I18N.MODEL_0, null))
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenDeleteThenId() {
        Mockito.when(i18nDaoMock.delete(0L)).thenReturn(TestData.lng.UNI_OPTIONAL_ZERO);
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