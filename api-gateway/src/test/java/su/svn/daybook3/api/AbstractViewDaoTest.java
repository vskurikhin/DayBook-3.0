/*
 * This file was last modified at 2024-05-14 23:10 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * AbstractViewDaoTest.java
 * $Id$
 */

package su.svn.daybook3.api;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Assertions;
import su.svn.daybook3.api.domain.dao.DaoIface;
import su.svn.daybook3.api.domain.dao.DaoViewIface;
import su.svn.daybook3.domain.model.CasesOfId;
import su.svn.daybook3.models.Identification;
import su.svn.daybook3.models.TimeUpdated;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

class AbstractViewDaoTest
        <I extends Comparable<? extends Serializable>, E extends CasesOfId<I>, V extends Identification<I>> {

    DaoIface<I, E> dao;
    DaoViewIface<I, V> viewDao;
    I id1;
    I id2;

    void setUp(DaoIface<I, E> dao, DaoViewIface<I, V> viewDao, E entry1, E entry2) {
        this.dao = dao;
        this.viewDao = viewDao;
        Assertions.assertDoesNotThrow(() -> {
            id1 = uniOptionalHelper(dao.insert(entry1));
            id2 = uniOptionalHelper(dao.insert(entry2));
        });
    }

    void tearDown() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id2, uniOptionalHelper(dao.delete(id2))));
        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id1, uniOptionalHelper(dao.delete(id1))));
        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(0, uniOptionalHelper(dao.count())));
    }

    void checkCount2() {
        Assertions.assertDoesNotThrow(() -> {
            Assertions.assertEquals(2, uniOptionalHelper(viewDao.count()));
        });
    }

    void checkCount3() {
        Assertions.assertDoesNotThrow(() -> {
            Assertions.assertEquals(3, uniOptionalHelper(viewDao.count()));
        });
    }

    void whenFindById1ThenEntry(BiFunction<I, V, V> toExpected) {
        whenFindByIdThenEntry(id1, toExpected);
    }

    void whenFindById2ThenEntry(BiFunction<I, V, V> toExpected) {
        whenFindByIdThenEntry(id2, toExpected);
    }

    void whenFindByIdThenEntry(I id, BiFunction<I, V, V> toExpected) {
        Assertions.assertDoesNotThrow(() -> {
            var test = uniOptionalHelper(viewDao.findById(id));
            Assertions.assertEquals(toExpected.apply(id, test), test);
            if (test instanceof TimeUpdated timeUpdated) {
                Assertions.assertNotNull(timeUpdated.createTime());
                Assertions.assertNull(timeUpdated.updateTime());
            }
        });
    }

    void whenSupplierThenEntry(Supplier<Uni<Optional<V>>> supplier, Function<V, V> toExpected) {
        Assertions.assertDoesNotThrow(() -> {
            var test = uniOptionalHelper(supplier.get());
            Assertions.assertEquals(toExpected.apply(test), test);
            if (test instanceof TimeUpdated timeUpdated) {
                Assertions.assertNotNull(timeUpdated.createTime());
                Assertions.assertNull(timeUpdated.updateTime());
            }
        });
    }

    void whenSupplierThenList(Supplier<Multi<V>> supplier, Function<List<V>, List<V>> toExpected) {
        Assertions.assertDoesNotThrow(() -> {
            var test = multiAsListHelper(supplier.get());
            Assertions.assertEquals(toExpected.apply(test), test);
            if (test instanceof TimeUpdated timeUpdated) {
                Assertions.assertNotNull(timeUpdated.createTime());
                Assertions.assertNull(timeUpdated.updateTime());
            }
        });
    }

    void whenFindAllThenMultiWithOneItem() {
        Assertions.assertDoesNotThrow(() -> {
            var test = multiAsListHelper(viewDao.findAll());
            Assertions.assertNotNull(test);
            Assertions.assertFalse(test.isEmpty());
            Assertions.assertEquals(2, test.size());
        });
    }

    void whenFindRangeZeroThenEmptiestMulti() {
        Assertions.assertDoesNotThrow(() -> {
            var test = multiAsListHelper(viewDao.findRange(0, 0));
            Assertions.assertNotNull(test);
            Assertions.assertTrue(test.isEmpty());
        });
    }

    void whenFindRangeFromZeroLimitOneThenMultiWithOneItem(BiFunction<I, V, V> toExpected) {
        Assertions.assertDoesNotThrow(() -> {
            var test = multiAsListHelper(viewDao.findRange(0, 1));
            Assertions.assertNotNull(test);
            Assertions.assertFalse(test.isEmpty());
            Assertions.assertEquals(1, test.size());
            Assertions.assertEquals(toExpected.apply(id1, test.get(0)), test.get(0));
        });
    }

    void whenFindRangeFromOneLimitOneMultiWithOneItem(BiFunction<I, V, V> toExpected) {
        Assertions.assertDoesNotThrow(() -> {
            var test = multiAsListHelper(viewDao.findRange(1, 1));
            Assertions.assertNotNull(test);
            Assertions.assertFalse(test.isEmpty());
            Assertions.assertEquals(1, test.size());
            Assertions.assertEquals(toExpected.apply(id2, test.get(0)), test.get(0));
        });
    }

    void whenFindRangeFromZeroToMaxValueThenMultiWithTwoItems() {
        Assertions.assertDoesNotThrow(() -> {
            var test = multiAsListHelper(viewDao.findRange(0, Long.MAX_VALUE));
            Assertions.assertNotNull(test);
            Assertions.assertFalse(test.isEmpty());
            Assertions.assertEquals(2, test.size());
        });
    }

    public static <T> T uniOptionalHelper(Uni<Optional<T>> uni) throws Exception {
        var result = uni.subscribeAsCompletionStage();
        Assertions.assertNotNull(result);
        return result.get().orElse(null);
    }

    public static <T> List<T> multiAsListHelper(Multi<T> multi) throws Exception {
        return multi.collect().asList().subscribeAsCompletionStage().get();
    }
}
