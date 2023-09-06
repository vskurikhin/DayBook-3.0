/*
 * This file was last modified at 2023.09.06 17:04 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * TestTable.java
 * $Id$
 */

package su.svn.daybook.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import su.svn.daybook.annotations.ModelField;
import su.svn.daybook.models.LongIdentification;

import jakarta.annotation.Nonnull;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class TestTable implements LongIdentification, Serializable {

    public static final String NONE = "b19bba7c-d53a-4174-9475-6ae9d7b9bbee";

    @Serial
    private static final long serialVersionUID = 3377791800667728148L;
    @ModelField
    private final Long id;
    @ModelField(nullable = false)
    private final String test;

    @ModelField
    private final Boolean flag;

    @ModelField(getterPrefix = "prefix")
    private final String prefix;

    public TestTable() {
        this.id = null;
        this.test = NONE;
        this.flag = false;
        this.prefix = null;
    }

    public TestTable(
            Long id,
            @Nonnull String test,
            Boolean flag,
            String prefix) {
        this.id = id;
        this.test = test;
        this.flag = flag;
        this.prefix = prefix;
    }

    public static TestTable.Builder builder() {
        return new TestTable.Builder();
    }

    public Long id() {
        return id;
    }

    public String getTest() {
        return test;
    }

    public Boolean isFlag() {
        return flag;
    }

    public String getPrefix() {
        return prefix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestTable testTable = (TestTable) o;
        return Objects.equals(id, testTable.id)
                && Objects.equals(test, testTable.test)
                && Objects.equals(flag, testTable.flag)
                && Objects.equals(prefix, testTable.prefix);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, test, flag, prefix);
    }

    @Override
    public String toString() {
        return "TestTable{" +
                "id=" + id +
                ", test='" + test + '\'' +
                ", flag=" + flag +
                ", prefix='" + prefix + '\'' +
                '}';
    }

    public static final class Builder {
        private Long id;
        private String test;

        private Boolean flag;

        private String prefix;

        private Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder test(@Nonnull String test) {
            this.test = test;
            return this;
        }

        public Builder flag(Boolean flag) {
            this.flag = flag;
            return this;
        }

        public Builder prefix(String noGetterPrefix) {
            this.prefix = noGetterPrefix;
            return this;
        }

        public TestTable build() {
            return new TestTable(id, test, flag, prefix);
        }
    }
}
