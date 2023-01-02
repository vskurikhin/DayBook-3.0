/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * Setting.java
 * $Id$
 */

package su.svn.daybook.models.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import su.svn.daybook.domain.model.LongIdentification;
import su.svn.daybook.domain.model.Marked;
import su.svn.daybook.domain.model.Owned;
import su.svn.daybook.domain.model.TimeUpdated;

import javax.annotation.Nonnull;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Setting implements LongIdentification, Serializable {

    public static final String NONE = "f98f1078-373e-479e-8f81-874839306720";
    @Serial
    private static final long serialVersionUID = 3276899182029163317L;
    public static final String ID = "id";
    private final Long id;
    private final String key;
    private final String value;
    private final Long valueTypeId;
    private final boolean visible;
    private final int flags;

    @JsonIgnore
    private transient volatile int hash;

    @JsonIgnore
    private transient volatile boolean hashIsZero;

    public Setting() {
        this.id = null;
        this.key = NONE;
        this.value = null;
        this.valueTypeId = 0L;
        this.visible = true;
        this.flags = 0;
    }

    public Setting(
            Long id,
            @Nonnull String key,
            String value,
            long valueTypeId,
            boolean visible,
            int flags) {
        this.id = id;
        this.key = key;
        this.value = value;
        this.valueTypeId = valueTypeId;
        this.visible = visible;
        this.flags = flags;
    }

    public static Setting.Builder builder() {
        return new Setting.Builder();
    }

    public Long getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public Long getValueTypeId() {
        return valueTypeId;
    }

    public boolean getVisible() {
        return visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public int getFlags() {
        return flags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var that = (Setting) o;
        return visible == that.visible
                && flags == that.flags
                && Objects.equals(id, that.id)
                && Objects.equals(key, that.key)
                && Objects.equals(value, that.value)
                && Objects.equals(valueTypeId, that.valueTypeId);
    }

    @Override
    public int hashCode() {
        int h = hash;
        if (h == 0 && !hashIsZero) {
            h = calculateHashCode();
            if (h == 0) {
                hashIsZero = true;
            } else {
                hash = h;
            }
        }
        return h;
    }

    private int calculateHashCode() {
        return Objects.hash(id, key, value, valueTypeId, visible, flags);
    }

    @Override
    public String toString() {
        return "Setting{" +
                "id=" + id +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", valueTypeId='" + valueTypeId + '\'' +
                ", visible=" + visible +
                ", flags=" + flags +
                '}';
    }

    public static final class Builder {
        private Long id;
        private String key;
        private String value;
        private long valueTypeId;
        private boolean visible;
        private int flags;

        private Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder key(@Nonnull String key) {
            this.key = key;
            return this;
        }

        public Builder value(String value) {
            this.value = value;
            return this;
        }

        public Builder valueTypeId(long valueTypeId) {
            this.valueTypeId = valueTypeId;
            return this;
        }

        public Builder visible(boolean visible) {
            this.visible = visible;
            return this;
        }

        public Builder flags(int flags) {
            this.flags = flags;
            return this;
        }

        public Setting build() {
            return new Setting(id, key, value, valueTypeId, visible, flags);
        }
    }
}
