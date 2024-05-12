/*
 * This file was last modified at 2024-05-14 23:08 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * UserServiceTest.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.services.models;

import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import su.svn.daybook3.api.gateway.TestData;
import su.svn.daybook3.api.gateway.domain.dao.UserNameDao;
import su.svn.daybook3.api.gateway.domain.dao.UserViewDao;
import su.svn.daybook3.api.gateway.domain.messages.Answer;
import su.svn.daybook3.api.gateway.domain.messages.Request;
import su.svn.daybook3.api.gateway.domain.model.UserView;
import su.svn.daybook3.api.gateway.models.pagination.Page;
import su.svn.daybook3.api.gateway.models.pagination.PageRequest;

import jakarta.inject.Inject;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@QuarkusTest
public class UserServiceTest {

    @Inject
    UserService service;

    static UserNameDao userNameDaoMock;
    static UserViewDao userViewDaoMock;


    static final Uni<Optional<UserView>> UNI_OPTIONAL_TEST
            = Uni.createFrom().item(Optional.of(TestData.USER.VIEW.TABLE_0));

    static final Multi<UserView> MULTI_TEST = Multi.createFrom().item(TestData.USER.VIEW.TABLE_0);

    static final Multi<UserView> MULTI_EMPTIES = Multi.createFrom().empty();

    @BeforeEach
    void setUp() {
        userNameDaoMock = Mockito.mock(UserNameDao.class);
        userViewDaoMock = Mockito.mock(UserViewDao.class);
        QuarkusMock.installMockForType(userNameDaoMock, UserNameDao.class);
        QuarkusMock.installMockForType(userViewDaoMock, UserViewDao.class);
    }


    @Test
    void testWhenGetAllThenSingletonList() {
        Mockito.when(userViewDaoMock.count()).thenReturn(TestData.UNI_OPTIONAL_ONE_LONG);
        Mockito.when(userViewDaoMock.findAll()).thenReturn(MULTI_TEST);
        List<Answer> result = service.getAll()
                .subscribe()
                .asStream()
                .peek(actual -> Assertions.assertEquals(Answer.of(TestData.USER.MODEL_0), actual))
                .toList();
        Assertions.assertTrue(result.size() > 0);
    }


    @Test
    void testWhenGetAllThenEmpty() {
        Mockito.when(userViewDaoMock.count()).thenReturn(TestData.UNI_OPTIONAL_ZERO_LONG);
        Mockito.when(userViewDaoMock.findAll()).thenReturn(MULTI_EMPTIES);
        List<Answer> result = service.getAll()
                .subscribe()
                .asStream()
                .toList();
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void testWhenGetPageThenSingletonList() {

        Mockito.when(userViewDaoMock.findRange(0L, Short.MAX_VALUE - 1)).thenReturn(MULTI_TEST);
        Mockito.when(userViewDaoMock.count()).thenReturn(TestData.UNI_OPTIONAL_ONE_LONG);

        PageRequest pageRequest = new PageRequest(0L, (short) (Short.MAX_VALUE - 1));
        var expected = Page.<Answer>builder()
                .page(0)
                .totalPages(1L)
                .totalRecords(1)
                .rows((short) 1)
                .prevPage(false)
                .nextPage(false)
                .content(Collections.singletonList(Answer.of(TestData.USER.MODEL_0)))
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

        Mockito.when(userViewDaoMock.findRange(0L, Short.MAX_VALUE - 2)).thenReturn(MULTI_EMPTIES);
        Mockito.when(userViewDaoMock.count()).thenReturn(TestData.UNI_OPTIONAL_ZERO_LONG);

        PageRequest pageRequest = new PageRequest(0L, (short) (Short.MAX_VALUE - 2));
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
    void testWhenGetPageThenZeroPage() {

        Mockito.when(userViewDaoMock.findRange(0L, 0)).thenReturn(MULTI_EMPTIES);
        Mockito.when(userViewDaoMock.count()).thenReturn(TestData.UNI_OPTIONAL_ONE_LONG);

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
        Mockito.when(userViewDaoMock.findById(TestData.uuid.ZERO)).thenReturn(UNI_OPTIONAL_TEST);
        service.get(new Request<>(TestData.uuid.ZERO, null))
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.of(TestData.USER.MODEL_0), actual))
                .await()
                .indefinitely();
    }
}
