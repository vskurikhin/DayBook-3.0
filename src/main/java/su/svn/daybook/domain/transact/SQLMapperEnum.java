package su.svn.daybook.domain.transact;

import su.svn.daybook.domain.model.CasesOfId;

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
