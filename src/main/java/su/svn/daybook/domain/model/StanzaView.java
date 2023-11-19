/*
 * This file was last modified at 2023.09.12 22:05 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * StanzaView.java
 * $Id$
 */

package su.svn.daybook.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.vertx.mutiny.sqlclient.Row;
import jakarta.annotation.Nonnull;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.Nullable;
import su.svn.daybook.annotations.ModelField;
import su.svn.daybook.models.LongIdentification;
import su.svn.daybook.models.Marked;
import su.svn.daybook.models.Owned;
import su.svn.daybook.models.TimeUpdated;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record StanzaView(
        @ModelField Long id,
        @ModelField(nullable = false) @Nonnull String name,
        @ModelField String description,
        @ModelField(nullable = false) @Nonnull StanzaTable parent,
        @ModelField(nullable = false) @Nonnull Collection<SettingView> settings,
        String userName,
        LocalDateTime createTime,
        LocalDateTime updateTime,
        boolean enabled,
        @ModelField boolean visible,
        @ModelField int flags)
        implements LongIdentification, Marked, Owned, TimeUpdated, Serializable {

    public static final String ID = "id";
    public static final String NONE = "00000000-0000-0000-0000-000000000000";
    public static final StanzaView ROOT = new StanzaView(0L, NONE, null, StanzaTable.ROOT, Collections.emptySet(), null, LocalDateTime.MIN, null, true, true, 0);
    @Language("SQL")
    public static final String COUNT_DICTIONARY_STANZA_VIEW = """
            SELECT count(*) FROM dictionary.stanza_view WHERE enabled""";
    @Language("SQL")
    public static final String SELECT_ALL_FROM_DICTIONARY_STANZA_VIEW_ORDER_BY_S = """
            SELECT id, name, description, parent_id, user_name, create_time, update_time, enabled, visible, flags,
                   parent_name, parent_description, parent_parent_id, parent_user_name,
                   parent_create_time, parent_update_time, parent_enabled, parent_visible, parent_flags,
                   settings_json
              FROM dictionary.stanza_view
             WHERE enabled
             ORDER BY %s
            """;
    @Language("SQL")
    public static final String SELECT_ALL_FROM_DICTIONARY_STANZA_VIEW_ORDER_BY_S_OFFSET_$1_LIMIT_$2 = """
            SELECT id, name, description, parent_id, user_name, create_time, update_time, enabled, visible, flags,
                   parent_name, parent_description, parent_parent_id, parent_user_name,
                   parent_create_time, parent_update_time, parent_enabled, parent_visible, parent_flags,
                   settings_json
              FROM dictionary.stanza_view
             WHERE enabled
             ORDER BY %s OFFSET $1 LIMIT $2
            """;
    @Language("SQL")
    public static final String SELECT_FROM_DICTIONARY_STANZA_VIEW_WHERE_ID_$1 = """
            SELECT id, name, description, parent_id, user_name, create_time, update_time, enabled, visible, flags,
                   parent_name, parent_description, parent_parent_id, parent_user_name,
                   parent_create_time, parent_update_time, parent_enabled, parent_visible, parent_flags,
                   settings_json
              FROM dictionary.stanza_view
             WHERE id = $1 AND enabled
            """;
    @Language("SQL")
    public static final String SELECT_FROM_DICTIONARY_STANZA_VIEW_WHERE_NAME_$1 = """
            SELECT id, name, description, parent_id, user_name, create_time, update_time, enabled, visible, flags,
                   parent_name, parent_description, parent_parent_id, parent_user_name,
                   parent_create_time, parent_update_time, parent_enabled, parent_visible, parent_flags,
                   settings_json
              FROM dictionary.stanza_view
             WHERE name = $1 AND enabled
            """;
    @Language("SQL")
    public static final String SELECT_FROM_DICTIONARY_STANZA_VIEW_WHERE_PARENT_ID_$1 = """
            SELECT id, name, description, parent_id, user_name, create_time, update_time, enabled, visible, flags,
                   parent_name, parent_description, parent_parent_id, parent_user_name,
                   parent_create_time, parent_update_time, parent_enabled, parent_visible, parent_flags,
                   settings_json
              FROM dictionary.stanza_view
             WHERE parent_id = $1 AND enabled
            """;

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return builder()
                .id(this.id)
                .name(this.name)
                .description(this.description)
                .parent(this.parent)
                .settings(this.settings)
                .userName(this.userName)
                .createTime(this.createTime)
                .updateTime(this.updateTime)
                .enabled(this.enabled)
                .visible(this.visible)
                .flags(this.flags);
    }

    public static StanzaView from(Row row) {
        var settingsJson = row.getJsonArray("settings_json");
        return new StanzaView(
                row.getLong(ID),
                row.getString("name"),
                row.getString("description"),
                new StanzaTable(
                        row.getLong("parent_id"),
                        row.getString("parent_name"),
                        row.getString("parent_description"),
                        row.getLong("parent_parent_id"),
                        row.getString("parent_user_name"),
                        row.getLocalDateTime("parent_create_time"),
                        row.getLocalDateTime("parent_update_time"),
                        row.getBoolean("parent_enabled"),
                        row.getBoolean("parent_visible"),
                        row.getInteger("parent_flags")
                ),
                settingsJson.stream()
                        .filter(Objects::nonNull)
                        .map(StanzaView::convToSettingView)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toUnmodifiableSet()),
                row.getString("user_name"),
                row.getLocalDateTime("create_time"),
                row.getLocalDateTime("update_time"),
                row.getBoolean("enabled"),
                row.getBoolean("visible"),
                row.getInteger("flags")
        );
    }

    @Nullable
    private static SettingView convToSettingView(Object o) {
        if (o instanceof io.vertx.core.json.JsonObject jsonObject) {
            var ct = jsonObject.getString("create_time");
            var ut = jsonObject.getString("update_time");
            var createTime = ct != null ? LocalDateTime.parse(ct) : null;
            var updateTime = ut != null ? LocalDateTime.parse(ut) : null;
            return jsonObject.mapTo(SettingView.class)
                    .toBuilder()
                    .valueType(jsonObject.getString("value_type"))
                    .stanzaId(jsonObject.getLong("stanza_id"))
                    .userName(jsonObject.getString("user_name"))
                    .createTime(createTime)
                    .updateTime(updateTime)
                    .build();
        }
        return null;
    }

    public static final class Builder {
        private @ModelField Long id;
        private @ModelField
        @Nonnull String name;
        private @ModelField String description;
        private @ModelField StanzaTable parent;
        private @ModelField
        @Nonnull Collection<SettingView> settings;
        private String userName;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
        private boolean enabled;
        private @ModelField boolean visible;
        private @ModelField int flags;

        private Builder() {
            this.name = NONE;
            this.parent = StanzaTable.ROOT;
            this.enabled = true;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(@Nonnull String name) {
            this.name = name;
            return this;
        }

        public Builder description(@Nonnull String description) {
            this.description = description;
            return this;
        }


        public Builder parent(StanzaTable parent) {
            this.parent = parent;
            return this;
        }

        public Builder settings(@Nonnull Collection<SettingView> settings) {
            this.settings = settings;
            return this;
        }

        public Builder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder createTime(LocalDateTime createTime) {
            this.createTime = createTime;
            return this;
        }

        public Builder updateTime(LocalDateTime updateTime) {
            this.updateTime = updateTime;
            return this;
        }

        public Builder enabled(boolean enabled) {
            this.enabled = enabled;
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

        public StanzaView build() {
            return new StanzaView(
                    id, name, description, parent, settings, userName, createTime, updateTime, enabled, visible, flags
            );
        }
    }
}