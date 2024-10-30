/*
 * This file was last modified at 2024-05-14 20:56 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * AbstractDaoTest.java
 * $Id$
 */

package su.svn.daybook3.api.gateway;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Assertions;
import su.svn.daybook3.api.gateway.domain.dao.DaoIface;
import su.svn.daybook3.domain.model.CasesOfId;
import su.svn.daybook3.models.TimeUpdated;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

class AbstractDaoTest<I extends Comparable<? extends Serializable>, E extends CasesOfId<I>> {

    DaoIface<I, E> dao;
    I id;
    I customId;

    void setUp(DaoIface<I, E> dao, E entry, I customId) {
        this.dao = dao;
        this.customId = customId;
        Assertions.assertDoesNotThrow(() -> {
            id = uniOptionalHelper(dao.insert(entry));
        });
    }

    void tearDown() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id, uniOptionalHelper(dao.delete(id))));
        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(0, uniOptionalHelper(dao.count())));
    }

    void whenFindByIdThenEntry(BiFunction<I, E, E> toExpected) {
        Assertions.assertDoesNotThrow(() -> {
            var test = uniOptionalHelper(dao.findById(id));
            Assertions.assertEquals(toExpected.apply(id, test), test);
            if (test instanceof TimeUpdated timeUpdated) {
                Assertions.assertNotNull(timeUpdated.createTime());
                Assertions.assertNull(timeUpdated.updateTime());
            }
        });
    }

    void whenUpdateAndFindByIdThenEntry(BiFunction<I, E, E> toExpected, E update) {
        Assertions.assertDoesNotThrow(() -> {
            var test = uniOptionalHelper(dao.updateEntry(update));
            Assertions.assertEquals(toExpected.apply(id, test), test);
        });
        updateAndFindByIdThenEntry(toExpected, id);
    }

    void whenUpdateCustomAndFindByIdThenEntry(BiFunction<I, E, E> toExpected, E update) {
        Assertions.assertDoesNotThrow(
                () -> Assertions.assertEquals(customId, uniOptionalHelper(dao.update(update)))
        );
        updateAndFindByIdThenEntry(toExpected, customId);
    }

    private void updateAndFindByIdThenEntry(BiFunction<I, E, E> toExpected, I lid) {
        Assertions.assertDoesNotThrow(() -> {
            var test = uniOptionalHelper(dao.findById(lid));
            Assertions.assertEquals(toExpected.apply(lid, test), test);
            if (test instanceof TimeUpdated timeUpdated) {
                Assertions.assertNotNull(timeUpdated.createTime());
                Assertions.assertNotNull(timeUpdated.updateTime());
            }
        });
    }

    void whenFindAllThenMultiWithOneItem() {
        Assertions.assertDoesNotThrow(() -> {
            var test = multiAsListHelper((dao.findAll()));
            Assertions.assertNotNull(test);
            Assertions.assertFalse(test.isEmpty());
            Assertions.assertEquals(1, test.size());
        });
    }

    void whenFindRangeZeroThenEmptiestMulti() {
        Assertions.assertDoesNotThrow(() -> {
            var test = multiAsListHelper(dao.findRange(0, 0));
            Assertions.assertNotNull(test);
            Assertions.assertTrue(test.isEmpty());
        });
    }

    void whenFindRangeFromZeroLimitOneThenMultiWithOneItem() {
        Assertions.assertDoesNotThrow(() -> {
            var test = multiAsListHelper(dao.findRange(0, 1));
            Assertions.assertNotNull(test);
            Assertions.assertFalse(test.isEmpty());
            Assertions.assertEquals(1, test.size());
        });
    }

    void whenInsertCustomThenEntry(BiFunction<I, E, E> toExpected, E custom) {
        Assertions.assertDoesNotThrow(() -> {
            var test = uniOptionalHelper(dao.insertEntry(custom));
            Assertions.assertEquals(toExpected.apply(customId, test), test);
        });
    }

    void whenDeleteCustomThenOk() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(customId, uniOptionalHelper(dao.delete(customId))));
    }

    void whenFindRangeFromZeroToOneThenMultiWithOneItem() {
        Assertions.assertDoesNotThrow(() -> {
            var test = multiAsListHelper(dao.findRange(0, 1));
            Assertions.assertNotNull(test);
            Assertions.assertFalse(test.isEmpty());
            Assertions.assertEquals(1, test.size());
        });
    }

    void whenFindRangeFromZeroToOneThenMultiWithOneItemCustom(BiFunction<I, E, E> toExpected) {
        Assertions.assertDoesNotThrow(() -> {
            var test = multiAsListHelper(dao.findRange(0, 1));
            Assertions.assertNotNull(test);
            Assertions.assertFalse(test.isEmpty());
            Assertions.assertEquals(1, test.size());
            Assertions.assertEquals(toExpected.apply(customId, test.get(0)), test.get(0));
        });
    }

    void whenFindRangeFromZeroToMaxValueThenMultiWithTwoItems() {
        Assertions.assertDoesNotThrow(() -> {
            var test = multiAsListHelper(dao.findRange(0, Long.MAX_VALUE));
            Assertions.assertNotNull(test);
            Assertions.assertFalse(test.isEmpty());
            Assertions.assertEquals(2, test.size());
        });
    }

    void whenFindRangeFromOneLimitOneMultiWithOneItem() {
        Assertions.assertDoesNotThrow(() -> {
            var test = multiAsListHelper(dao.findRange(1, 1));
            Assertions.assertNotNull(test);
            Assertions.assertFalse(test.isEmpty());
            Assertions.assertEquals(1, test.size());
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
