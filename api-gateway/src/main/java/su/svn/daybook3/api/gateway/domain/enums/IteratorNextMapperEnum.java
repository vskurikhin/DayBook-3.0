/*
 * This file was last modified at 2024-05-22 23:14 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * IteratorNextMapperEnum.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.domain.enums;

import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowIterator;
import su.svn.daybook3.api.gateway.models.Constants;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Function;

public enum IteratorNextMapperEnum {
    Null(null),
    LongIdIterator(it -> it.hasNext() ? Optional.of(it.next().getLong(Constants.ID)) : Optional.empty()),
    StringIdIterator(it -> it.hasNext() ? Optional.of(it.next().getString(Constants.ID)) : Optional.empty()),
    UUIDIdIterator(it -> it.hasNext() ? Optional.of(it.next().getUUID(Constants.ID)) : Optional.empty());

    private final Function<RowIterator<Row>, Optional<? extends Comparable<? extends Serializable>>> mapper;

    IteratorNextMapperEnum(Function<RowIterator<Row>, Optional<? extends Comparable<? extends Serializable>>> mapper) {
        this.mapper = mapper;
    }

    public Function<RowIterator<Row>, Optional<? extends Comparable<? extends Serializable>>> getMapper() {
        return mapper;
    }
}
