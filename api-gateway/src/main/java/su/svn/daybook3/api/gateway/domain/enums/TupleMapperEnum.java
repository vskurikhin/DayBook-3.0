/*
 * This file was last modified at 2024-05-14 20:56 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * TupleMapperEnum.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.domain.enums;

import io.vertx.mutiny.sqlclient.Tuple;
import su.svn.daybook3.api.gateway.domain.model.CasesOfId;

import java.util.function.Function;

public enum TupleMapperEnum {
    Null(null),
    StringTuple((Function<String, Tuple>) Tuple::of),
    CasesOfIdInsertTuple((Function<CasesOfId<?>, Tuple>) CasesOfId::caseInsertTuple),
    CasesOfIdUpdateTuple((Function<CasesOfId<?>, Tuple>) CasesOfId::caseInsertTuple);

    private final Function<?, Tuple> mapper;

    TupleMapperEnum(Function<?, Tuple> mapper) {
        this.mapper = mapper;
    }

    public Function<?, Tuple> getMapper() {
        return mapper;
    }
}
