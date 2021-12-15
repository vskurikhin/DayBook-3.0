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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import su.svn.daybook.DataTest;
import su.svn.daybook.domain.dao.I18nDao;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.I18n;

import javax.inject.Inject;
import java.util.Optional;

@QuarkusTest
class I18nServiceTest {

    @Inject
    I18nService service;

    static I18nDao mock;

    @BeforeAll
    public static void setup() {
        Uni<Optional<I18n>> tezd = Uni.createFrom()
                .item(Optional.of(DataTest.TEZD_I18n));
        Uni<Answer> empty = Uni.createFrom()
                .item(new Answer("empty", 1));
        Uni<Optional<Long>> tezdId = Uni.createFrom().item(Optional.of(0L));
        Multi<I18n> tezds = Multi.createFrom()
                .item(DataTest.TEZD_I18n);

        mock = Mockito.mock(I18nDao.class);
        Mockito.when(mock.findById(0L)).thenReturn(tezd);
        Mockito.when(mock.insert(DataTest.TEZD_I18n)).thenReturn(tezdId);
        Mockito.when(mock.findAll()).thenReturn(tezds);
        QuarkusMock.installMockForType(mock, I18nDao.class);
    }

    @Test
    void tagGet() {
        service.i18nGet("0")
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.of(Optional.of(DataTest.TEZD_I18n)), actual))
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
        service.i18nAdd(DataTest.TEZD_I18n)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.of(0L), actual))
                .await()
                .indefinitely();
    }

    @Test
    void getAll() {
        service.getAll()
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.of(DataTest.TEZD_I18n), actual))
                .toUni()
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_getAll_return_null() {
        Mockito.when(mock.findAll()).thenReturn(Multi.createFrom().empty());
        QuarkusMock.installMockForType(mock, I18nDao.class);
        service.getAll()
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.of(DataTest.TEZD_I18n), actual))
                .toUni()
                .await()
                .indefinitely();
    }
}