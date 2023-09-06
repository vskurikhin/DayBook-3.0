/*
 * This file was last modified at 2023.09.06 17:04 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * Action.java
 * $Id$
 */

package su.svn.daybook.domain.transact;

import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowIterator;
import io.vertx.mutiny.sqlclient.Tuple;
import su.svn.daybook.annotations.TransactionAction;
import su.svn.daybook.domain.model.CasesOfId;

import jakarta.annotation.Nonnull;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Function;

record Action(
        Function<RowIterator<Row>, Optional<? extends Comparable<? extends Serializable>>> iteratorNextMapper,
        Function<CasesOfId<? extends Comparable<? extends Serializable>>, String> sqlMapper,
        Function<Object, Tuple> tupleMapper,
        String sql) {
    @SuppressWarnings("unchecked")
    static Action of(@Nonnull TransactionAction ta) {
        return new Action(
                ta.iteratorNextMapper().getMapper(),
                ta.sqlMapper().getMapper(),
                (Function<Object, Tuple>) ta.tupleMapper().getMapper(),
                ta.value()
        );
    }
}
