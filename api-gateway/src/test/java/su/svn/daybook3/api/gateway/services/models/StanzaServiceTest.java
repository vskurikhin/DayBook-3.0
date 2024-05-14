/*
 * This file was last modified at 2024-05-14 23:10 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * StanzaServiceTest.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.services.models;

import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import su.svn.daybook3.api.gateway.TestData;
import su.svn.daybook3.api.gateway.TestUtils;
import su.svn.daybook3.api.gateway.domain.dao.StanzaDao;
import su.svn.daybook3.api.gateway.domain.dao.StanzaViewDao;
import su.svn.daybook3.api.gateway.domain.messages.Answer;
import su.svn.daybook3.api.gateway.domain.messages.ApiResponse;
import su.svn.daybook3.api.gateway.domain.messages.Request;
import su.svn.daybook3.api.gateway.domain.model.SettingTable;
import su.svn.daybook3.api.gateway.domain.model.StanzaTable;
import su.svn.daybook3.api.gateway.domain.model.StanzaView;
import su.svn.daybook3.api.gateway.domain.transact.StanzaTransactionalJob;
import su.svn.daybook3.api.gateway.models.domain.Stanza;
import su.svn.daybook3.api.gateway.models.pagination.Page;
import su.svn.daybook3.api.gateway.models.pagination.PageRequest;
import su.svn.daybook3.api.gateway.services.cache.StanzaCacheProvider;

import java.util.*;

@QuarkusTest
class StanzaServiceTest {

    @Inject
    StanzaService service;

    static StanzaCacheProvider stanzaCacheProviderMock;
    static StanzaDao stanzaDaoMock;
    static StanzaTransactionalJob stanzaTransactionalJobMock;
    static StanzaViewDao stanzaViewDaoMock;

    static final Uni<Stanza> UNI_MODEL = Uni.createFrom().item(TestData.STANZA.MODEL_0);
    static final Uni<Optional<StanzaTable>> UNI_OPTIONAL_TEST = Uni.createFrom().item(Optional.of(TestData.STANZA.TABLE_0));
    static final Uni<Optional<StanzaView>> UNI_OPTIONAL_VIEW = Uni.createFrom().item(Optional.of(TestData.STANZA.VIEW_0));

    static final Multi<StanzaTable> MULTI_TEST = Multi.createFrom().item(TestData.STANZA.TABLE_0);
    static final Multi<StanzaView> MULTI_VIEW_TEST = Multi.createFrom().item(TestData.STANZA.VIEW_0);

    static final Multi<StanzaTable> MULTI_WITH_NULL = TestUtils.createMultiWithNull(StanzaTable.class);

    static final Multi<StanzaView> MULTI_VIEW_EMPTIES = TestUtils.createMultiEmpties(StanzaView.class);
    static final Multi<StanzaTable> MULTI_EMPTIES = TestUtils.createMultiEmpties(StanzaTable.class);

    @BeforeEach
    void setUp() {
        stanzaCacheProviderMock = Mockito.mock(StanzaCacheProvider.class);
        stanzaDaoMock = Mockito.mock(StanzaDao.class);
        stanzaTransactionalJobMock = Mockito.mock(StanzaTransactionalJob.class);
        stanzaViewDaoMock = Mockito.mock(StanzaViewDao.class);
        Mockito.when(stanzaDaoMock.findById(0L)).thenReturn(UNI_OPTIONAL_TEST);
        Mockito.when(stanzaViewDaoMock.findById(0L)).thenReturn(UNI_OPTIONAL_VIEW);
        QuarkusMock.installMockForType(stanzaCacheProviderMock, StanzaCacheProvider.class);
        QuarkusMock.installMockForType(stanzaDaoMock, StanzaDao.class);
        QuarkusMock.installMockForType(stanzaTransactionalJobMock, StanzaTransactionalJob.class);
        QuarkusMock.installMockForType(stanzaViewDaoMock, StanzaViewDao.class);
    }

    @Test
    void testWhenGetAllThenSingletonList() {
        Mockito.when(stanzaViewDaoMock.count()).thenReturn(TestData.UNI_OPTIONAL_ONE_LONG);
        Mockito.when(stanzaViewDaoMock.findAll()).thenReturn(MULTI_VIEW_TEST);
        List<Answer> result = new ArrayList<>();
        Assertions.assertDoesNotThrow(() -> result.addAll(service.getAll()
                .subscribe()
                .asStream()
                .peek(actual -> Assertions.assertEquals(Answer.of(TestData.STANZA.MODEL_0), actual)).toList()));
        Assertions.assertTrue(result.size() > 0);
    }

    @Test
    @ActivateRequestContext
    void testWhenGetAllThenCountMinusOne() {
        Mockito.when(stanzaViewDaoMock.count()).thenReturn(TestData.UNI_OPTIONAL_MINUS_ONE_LONG);
        Mockito.when(stanzaViewDaoMock.findAll()).thenReturn(Multi.createFrom().empty());
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
        Mockito.when(stanzaViewDaoMock.count()).thenReturn(TestData.UNI_OPTIONAL_ZERO_LONG);
        Mockito.when(stanzaViewDaoMock.findAll()).thenReturn(Multi.createFrom().empty());
        List<Answer> result = new ArrayList<>();
        Assertions.assertDoesNotThrow(() -> result.addAll(service.getAll()
                .subscribe()
                .asStream()
                .toList()));
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void testWhenGetPageThenSingletonList() {

        Mockito.when(stanzaViewDaoMock.findRange(0L, Short.MAX_VALUE - 1)).thenReturn(MULTI_VIEW_TEST);
        Mockito.when(stanzaDaoMock.count()).thenReturn(TestData.UNI_OPTIONAL_ONE_LONG);

        PageRequest pageRequest = new PageRequest(0L, (short) (Short.MAX_VALUE - 1));
        var expected = Page.<Answer>builder()
                .page(0)
                .totalPages(1L)
                .totalRecords(1)
                .rows((short) 1)
                .prevPage(false)
                .nextPage(false)
                .content(Collections.singletonList(Answer.of(TestData.STANZA.MODEL_0)))
                .build();
        Mockito.when(stanzaCacheProviderMock.getPage(pageRequest)).thenReturn(Uni.createFrom().item(expected));

        Assertions.assertDoesNotThrow(() -> service.getPage(new Request<>(pageRequest, null))
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely()
        );
    }

    @Test
    void testWhenGetPageThenEmpty() {

        Mockito.when(stanzaViewDaoMock.findRange(0L, Short.MAX_VALUE - 2)).thenReturn(MULTI_VIEW_EMPTIES);
        Mockito.when(stanzaDaoMock.count()).thenReturn(TestData.UNI_OPTIONAL_ZERO_LONG);

        PageRequest pageRequest = new PageRequest(0L, (short) (Short.MAX_VALUE - 2));
        var expected = Page.<Answer>builder()
                .totalPages(0L)
                .totalRecords(0)
                .rows((short) 0)
                .prevPage(false)
                .nextPage(false)
                .content(Collections.emptyList())
                .build();
        Mockito.when(stanzaCacheProviderMock.getPage(pageRequest)).thenReturn(Uni.createFrom().item(expected));

        Assertions.assertDoesNotThrow(() -> service.getPage(new Request<>(pageRequest, null))
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely()
        );
    }

    @Test
    void testWhenGetPageThenZeroPage() {

        Mockito.when(stanzaViewDaoMock.findRange(0L, 0)).thenReturn(MULTI_VIEW_EMPTIES);
        Mockito.when(stanzaDaoMock.count()).thenReturn(TestData.UNI_OPTIONAL_ZERO_LONG);

        PageRequest pageRequest = new PageRequest(0, (short) 0);
        var expected = Page.<Answer>builder()
                .totalPages(0L)
                .totalRecords(0)
                .rows((short) 0)
                .prevPage(false)
                .nextPage(false)
                .content(Collections.emptyList())
                .build();
        Mockito.when(stanzaCacheProviderMock.getPage(pageRequest)).thenReturn(Uni.createFrom().item(expected));

        Assertions.assertDoesNotThrow(() -> service.getPage(new Request<>(pageRequest, null))
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely()
        );
    }

    @Test
    void testWhenGetThenEntry() {
        Mockito.when(stanzaCacheProviderMock.get(Mockito.any(Long.class)))
                .thenReturn(UNI_MODEL);
        Assertions.assertDoesNotThrow(() -> service.get(new Request<>(0L, null))
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.of(TestData.STANZA.MODEL_0), actual))
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
        Mockito.when(stanzaDaoMock.insert(TestData.STANZA.TABLE_0)).thenReturn(TestData.lng.UNI_OPTIONAL_ZERO);
        var table = new StanzaTable(
                0L, SettingTable.NONE, null, StanzaTable.ROOT.id(), null, null, null, true, true, 0
        );
        Collection<SettingTable> collection = Collections.emptySet();
        var pair = Pair.of(table, collection);
        Collection<Pair<StanzaTable, Collection<SettingTable>>> set = Collections.singleton(pair);
        Mockito.when(stanzaTransactionalJobMock.upsert(set))
                .thenReturn(TestData.lng.UNI_SINGLETON_LIST_ZERO);
        Mockito.when(stanzaCacheProviderMock.invalidate(Mockito.any(Answer.class)))
                .thenReturn(Uni.createFrom().item(expected));
        Assertions.assertDoesNotThrow(() -> service.add(new Request<>(TestData.STANZA.MODEL_0, null))
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenAddThenEmpty() {
        var table = new StanzaTable(
                0L, SettingTable.NONE, null, StanzaTable.ROOT.id(), null, null, null, true, true, 0
        );
        Collection<SettingTable> collection = Collections.emptySet();
        var pair = Pair.of(table, collection);
        Collection<Pair<StanzaTable, Collection<SettingTable>>> set = Collections.singleton(pair);
        Mockito.when(stanzaTransactionalJobMock.upsert(set))
                .thenReturn(TestData.lng.UNI_LIST_EMPTY);
        var expected = Answer.builder()
                .message("bad request")
                .error(400)
                .payload("No value present for entry: " + TestData.STANZA.TABLE_0)
                .build();
        Mockito.when(stanzaDaoMock.insert(TestData.STANZA.TABLE_0))
                .thenReturn(TestData.lng.UNI_OPTIONAL_EMPTY);
        Mockito.when(stanzaCacheProviderMock.invalidate(Mockito.any(Answer.class)))
                .thenReturn(Uni.createFrom().item(expected));
        Assertions.assertDoesNotThrow(() -> service.add(new Request<>(TestData.STANZA.MODEL_0, null))
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenPutThenId() {
        var table = new StanzaTable(
                0L, SettingTable.NONE, null, StanzaTable.ROOT.id(), null, null, null, true, true, 0
        );
        Collection<SettingTable> collection = Collections.emptySet();
        var pair = Pair.of(table, collection);
        Collection<Pair<StanzaTable, Collection<SettingTable>>> set = Collections.singleton(pair);
        Mockito.when(stanzaTransactionalJobMock.upsert(set))
                .thenReturn(TestData.lng.UNI_LIST_EMPTY);
        var expected = Answer.builder()
                .message(Answer.DEFAULT_MESSAGE)
                .error(202)
                .payload(new ApiResponse<>(Long.valueOf(0), 202))
                .build();
        Mockito.when(stanzaDaoMock.update(TestData.STANZA.TABLE_0)).thenReturn(TestData.lng.UNI_OPTIONAL_ZERO);
        Mockito.when(stanzaCacheProviderMock.invalidateByKey(Mockito.anyLong(), Mockito.any(Answer.class)))
                .thenReturn(Uni.createFrom().item(expected));
        Assertions.assertDoesNotThrow(() -> service.put(new Request<>(TestData.STANZA.MODEL_0, null))
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenPutThenEmpty() {
        Mockito.when(stanzaDaoMock.update(TestData.STANZA.TABLE_0)).thenReturn(TestData.lng.UNI_OPTIONAL_ZERO);
        Assertions.assertThrows(RuntimeException.class, () -> service.put(new Request<>(TestData.STANZA.MODEL_0, null))
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenDeleteThenId() {
        Mockito.when(stanzaDaoMock.delete(0L)).thenReturn(TestData.lng.UNI_OPTIONAL_ZERO);
        Mockito.when(stanzaTransactionalJobMock.delete(Mockito.any(StanzaTable.class)))
                .thenReturn(TestData.lng.UNI_OPTIONAL_ZERO);
        var expected = Answer.of(new ApiResponse<>(Long.valueOf(0), 200));
        Mockito.when(stanzaCacheProviderMock.invalidateByKey(Mockito.anyLong(), Mockito.any(Answer.class)))
                .thenReturn(Uni.createFrom().item(expected));
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
                .payload("No parentId present for id: null")
                .build();
        Assertions.assertThrows(NullPointerException.class, () -> service.delete(null)
                .await()
                .indefinitely());
    }
}