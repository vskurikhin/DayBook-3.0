/*
 * This file was last modified at 2023.01.12 13:23 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * AbstractDao.java
 * $Id$
 */

package su.svn.daybook.domain.dao;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import su.svn.daybook.annotations.SQL;
import su.svn.daybook.models.Identification;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

abstract class AbstractViewDao<I extends Comparable<? extends Serializable>, D extends Identification<I>> {

    public static final String COUNT = "count";
    public static final String FIND_ALL = "findAll";
    public static final String FIND_BY_ID = "findById";
    public static final String FIND_BY_KEY = "findByKey";
    public static final String FIND_RANGE = "findRange";

    private final Function<Row, I> idFunction;

    private final Function<Row, D> fromFunction;

    Map<String, String> sqlMap;

    @Inject
    io.vertx.mutiny.pgclient.PgPool client;

    AbstractViewDao(@Nonnull Function<Row, I> idFunction, @Nonnull Function<Row, D> fromFunction) {
        this.idFunction = idFunction;
        this.fromFunction = fromFunction;
        this.sqlMap = Collections.unmodifiableMap(getSQLMethod());
    }

    private Map<String, String> getSQLMethod() {
        return Arrays.stream(this.getClass().getSuperclass().getDeclaredMethods())
                .filter(this::testAnnotationSQL)
                .collect(Collectors.toMap(Method::getName, method -> {
                    SQL sql = method.getAnnotation(SQL.class);
                    return sql.value();
                }));
    }

    private boolean testAnnotationSQL(Method method) {
        return method.isAnnotationPresent(SQL.class);
    }

    protected Uni<Long> countSQL() {
        var sql = sqlMap.get(COUNT);
        if (sql != null && !"".equals(sql)) {
            return client
                    .preparedQuery(sql)
                    .execute()
                    .map(pgRowSet -> pgRowSet.iterator().next().getLong(COUNT));
        }
        return Uni.createFrom().nullItem();
    }

    protected Multi<D> findAllSQL() {
        var sql = sqlMap.get(FIND_ALL);
        if (sql != null && !"".equals(sql)) {
            return client
                    .query(sql)
                    .execute()
                    .onItem()
                    .transformToMulti(set -> Multi.createFrom().iterable(set))
                    .map(fromFunction);
        }
        return Multi.createFrom().empty();
    }

    protected Uni<D> findByIdSQL(I id) {
        var sql = sqlMap.get(FIND_BY_ID);
        if (sql != null && !"".equals(sql)) {
            return executeByIdReturnEntry(id, sql);
        }
        return Uni.createFrom().nullItem();
    }

    protected Uni<D> findByKeySQL(Comparable<? extends Serializable> key) {
        var sql = sqlMap.get(FIND_BY_KEY);
        if (sql != null && !"".equals(sql)) {
            return executeByKeyReturnEntry(key, sql);
        }
        return Uni.createFrom().nullItem();
    }

    protected Multi<D> findRangeSQL(long offset, long limit) {
        var sql = sqlMap.get(FIND_RANGE);
        if (sql != null && !"".equals(sql)) {
            return client
                    .preparedQuery(sql)
                    .execute(Tuple.of(offset, limit))
                    .onItem()
                    .transformToMulti(set -> Multi.createFrom().iterable(set))
                    .map(fromFunction);
        }
        return Multi.createFrom().empty();
    }

    Uni<D> executeByIdReturnEntry(I id, @Nonnull String sql) {
        return client
                .preparedQuery(sql)
                .execute(Tuple.of(id))
                .map(RowSet::iterator)
                .map(iterator -> iterator.hasNext() ? fromFunction.apply(iterator.next()) : null);
    }

    <T extends Comparable<? extends Serializable>>
    Uni<D> executeByKeyReturnEntry(T key, @Nonnull String sql) {
        return client
                .preparedQuery(sql)
                .execute(Tuple.of(key))
                .map(RowSet::iterator)
                .map(iterator -> iterator.hasNext() ? fromFunction.apply(iterator.next()) : null);
    }

    Uni<I> executeByIdReturnId(I id, @Nonnull String sql) {
        return client
                .preparedQuery(sql)
                .execute(Tuple.of(id))
                .map(RowSet::iterator)
                .map(iterator -> iterator.hasNext() ? idFunction.apply(iterator.next()) : null);
    }

    Uni<I> executeByTupleReturnId(Tuple tuple, @Nonnull String sql) {
        return client.withTransaction(
                connection -> connection.preparedQuery(sql)
                        .execute(tuple)
                        .map(RowSet::iterator)
                        .map(iterator -> iterator.hasNext() ? idFunction.apply(iterator.next()) : null));
    }
}