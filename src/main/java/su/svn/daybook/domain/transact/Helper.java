/*
 * This file was last modified at 2023.11.19 11:15 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * Helper.java
 * $Id$
 */

package su.svn.daybook.domain.transact;

import io.vertx.mutiny.sqlclient.Tuple;
import su.svn.daybook.domain.model.CasesOfId;

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
