/*
 * This file was last modified at 2023.01.06 11:20 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * CasableById.java
 * $Id$
 */

package su.svn.daybook.domain.model;

import io.vertx.mutiny.sqlclient.Tuple;
import su.svn.daybook.models.Identification;

import java.io.Serializable;

public interface CasesOfId<K  extends Comparable<? extends Serializable>> extends Identification<K> {
    String caseInsertSql();
    Tuple caseInsertTuple();
    Tuple updateTuple();
}
