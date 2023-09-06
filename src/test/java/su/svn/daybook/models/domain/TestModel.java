/*
 * This file was last modified at 2023.09.06 17:04 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * TestModel.java
 * $Id$
 */

package su.svn.daybook.models.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import su.svn.daybook.annotations.DomainField;
import su.svn.daybook.models.LongIdentification;

import jakarta.annotation.Nonnull;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class TestModel implements LongIdentification, Serializable {

    public static final String NONE = "d94d93d9-d44c-403c-97b1-d071b6974d80";
    @Serial
    private static final long serialVersionUID = 3421670798382710094L;
    @DomainField
    private final Long id;
    @DomainField(nullable = false)
    private final String test;

    public TestModel() {
        this.id = null;
        this.test = NONE;
    }

    public TestModel(
            Long id,
            @Nonnull String test) {
        this.id = id;
        this.test = test;
    }

    public static TestModel.Builder builder() {
        return new TestModel.Builder();
    }

    public Long id() {
        return id;
    }

    public String getTest() {
        return test;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var that = (TestModel) o;
        return Objects.equals(id, that.id)
                && Objects.equals(test, that.test);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, test);
    }

    @Override
    public String toString() {
        return "TestModel{" +
                "id=" + id +
                ", key='" + test + '\'' +
                '}';
    }

    public static final class Builder {
        private Long id;
        private String test;

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

        public TestModel build() {
            return new TestModel(id, test);
        }
    }
}
