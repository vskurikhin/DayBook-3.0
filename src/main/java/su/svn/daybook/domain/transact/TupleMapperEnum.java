package su.svn.daybook.domain.transact;

import io.vertx.mutiny.sqlclient.Tuple;
import su.svn.daybook.domain.model.CasesOfId;

import java.io.Serializable;
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
