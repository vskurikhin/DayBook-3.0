/*
 * This file was last modified at 2023.01.12 13:23 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * AbstractDao.java
 * $Id$
 */

package su.svn.daybook.domain.dao;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.Row;
import su.svn.daybook.domain.model.CasesOfId;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.function.Function;

abstract class AbstractDao<I extends Comparable<? extends Serializable>, D extends CasesOfId<I>>
        extends AbstractViewDao<I, D> {

    public static final String DELETE = "delete";
    public static final String INSERT = "insert";
    public static final String UPDATE = "update";

    AbstractDao(String id,@Nonnull Function<Row, I> idFunction, @Nonnull Function<Row, D> fromFunction) {
        super(id, idFunction, fromFunction);
    }

    protected Uni<I> deleteSQL(I id) {
        var sql = sqlMap.get(DELETE);
        if (sql != null && !"".equals(sql)) {
            return executeByIdReturnId(id, sql);
        }
        return Uni.createFrom().nullItem();
    }

    protected Uni<I> insertSQL(D entry) {
        var s = sqlMap.get(INSERT);
        var sql = (s != null && !"".equals(s)) ? s : entry.caseInsertSql();
        if (sql != null) {
            return executeByTupleReturnId(entry.caseInsertTuple(), sql);
        }
        return Uni.createFrom().nullItem();
    }

    protected Uni<D> insertSQLEntry(D entry) {
        var s = sqlMap.get(INSERT);
        var sql = (s != null && !"".equals(s)) ? s : entry.caseInsertSql();
        if (sql != null) {
            return executeByTupleReturnEntry(entry.caseInsertTuple(), sql);
        }
        return Uni.createFrom().nullItem();
    }

    protected Uni<I> updateSQL(D entry) {
        var sql = sqlMap.get(UPDATE);
        if (sql != null && !"".equals(sql)) {
            return executeByTupleReturnId(entry.updateTuple(), sql);
        }
        return Uni.createFrom().nullItem();
    }

    protected Uni<D> updateSQLEntry(D entry) {
        var sql = sqlMap.get(UPDATE);
        if (sql != null && !"".equals(sql)) {
            return executeByTupleReturnEntry(entry.updateTuple(), sql);
        }
        return Uni.createFrom().nullItem();
    }
}
