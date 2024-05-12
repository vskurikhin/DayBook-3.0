/*
 * This file was last modified at 2024-05-14 21:25 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * CasesOfId.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.domain.model;

import io.vertx.mutiny.sqlclient.Tuple;
import su.svn.daybook3.api.gateway.models.Identification;

import java.io.Serializable;

public interface CasesOfId<K  extends Comparable<? extends Serializable>> extends Identification<K> {
    String caseInsertSql();
    Tuple caseInsertTuple();
    String deleteSql();
    String updateSql();
    Tuple updateTuple();
}
