/*
 * This file was last modified at 2023.11.19 11:15 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * ListHelper.java
 * $Id$
 */

package su.svn.daybook.domain.transact;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

public interface ListHelper<Identifier extends Comparable<? extends Serializable>>
        extends Function<SqlConnection, Uni<List<Identifier>>> {
}
