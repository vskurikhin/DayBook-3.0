/*
 * This file was last modified at 2024-10-29 23:33 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * SQLMapperEnum.java
 * $Id$
 */

package su.svn.daybook3.enums;

import su.svn.daybook3.domain.model.CasesOfId;

import java.io.Serializable;
import java.util.function.Function;

public enum SQLMapperEnum {
    Null(null),
    CasesOfIdInsertSQL(CasesOfId::caseInsertSql);

    private final Function<CasesOfId<? extends Comparable<? extends Serializable>>, String> mapper;

    SQLMapperEnum(Function<CasesOfId<? extends Comparable<? extends Serializable>>, String> mapper) {
        this.mapper = mapper;
    }

    public Function<CasesOfId<? extends Comparable<? extends Serializable>>, String> getMapper() {
        return mapper;
    }
}
