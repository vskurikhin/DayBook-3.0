/*
 * This file was last modified at 2024-10-30 18:38 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * Helper.java
 * $Id$
 */

package su.svn.daybook3.transaction;

import io.vertx.mutiny.sqlclient.Tuple;
import su.svn.daybook3.domain.model.CasesOfId;

public interface Helper {

    static <X extends CasesOfId<?>> String deleteSql(Action action, X table) {
        return (action.sqlMapper() != null)
                ? action.sqlMapper().apply(table)
                : table.deleteSql();
    }

    static <X extends CasesOfId<?>> String insertSql(Action action, X table) {
        return (action.sqlMapper() != null)
                ? action.sqlMapper().apply(table)
                : table.caseInsertSql();
    }

    static <X extends CasesOfId<?>> String updateSql(Action action, X table) {
        return (action.sqlMapper() != null)
                ? action.sqlMapper().apply(table)
                : table.updateSql();
    }

    static <X extends CasesOfId<?>> Tuple insertTuple(Action action, X table) {
        return (action.tupleMapper() != null)
                ? action.tupleMapper().apply(table)
                : table.caseInsertTuple();
    }

    static <X extends CasesOfId<?>> Tuple updateTuple(Action action, X table) {
        return (action.tupleMapper() != null)
                ? action.tupleMapper().apply(table)
                : table.updateTuple();
    }

    static Tuple tuple(Action action, Object o) {
        return (action.tupleMapper() != null)
                ? action.tupleMapper().apply(o)
                : Tuple.tuple();
    }
}
