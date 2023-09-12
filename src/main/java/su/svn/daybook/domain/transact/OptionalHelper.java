/*
 * This file was last modified at 2023.11.19 11:15 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * OptionalHelper.java
 * $Id$
 */

package su.svn.daybook.domain.transact;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;
import su.svn.daybook.domain.enums.IteratorNextMapperEnum;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Function;

public interface OptionalHelper<Identifier extends Comparable<? extends Serializable>>
        extends Function<SqlConnection, Uni<Optional<Identifier>>>, Helper {
    default boolean isNullIteratorNextMapper(Action action) {
        return action.iteratorNextMapper() == null
                || IteratorNextMapperEnum.Null.getMapper().equals(action.iteratorNextMapper());
    }
}
