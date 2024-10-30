/*
 * This file was last modified at 2024-10-30 17:26 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * JsonRecord.java
 * $Id$
 */

package su.svn.daybook3.api.domain.entities;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;
import su.svn.daybook3.models.Marked;
import su.svn.daybook3.models.Owned;
import su.svn.daybook3.models.TimeUpdated;
import su.svn.daybook3.models.UUIDIdentification;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Entity
@Getter
@Setter
@Builder
@ToString(exclude = "baseRecord")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = {"id", "baseRecord"})
@Accessors(fluent = true, chain = false)
@Table(schema = "db", name = "json_records")
@NamedQueries({
        @NamedQuery(
                name = JsonRecord.LIST_ENABLED_WITH_TYPE,
                query = """
                        FROM JsonRecord j
                        LEFT JOIN FETCH j.baseRecord
                         WHERE j.enabled = :enabled
                         ORDER BY j.refreshAt DESC
                        """
        )
})
public class JsonRecord
        extends PanacheEntityBase
        implements UUIDIdentification, Marked, Owned, TimeUpdated, Serializable {

    public static final Duration TIMEOUT_DURATION = Duration.ofMillis(900);
    public static final String LIST_ENABLED_WITH_TYPE = "JsonRecord.ListEnabled";
    public static final Parameters ENABLED_JSON_TYPE = Parameters
            .with("enabled", Boolean.TRUE);

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "id",
            referencedColumnName = "id",
            insertable = false,
            nullable = false,
            updatable = false)
    private BaseRecord baseRecord;

    @Column(name = "title")
    private String title;

    @Column(name = "values")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, String> values;

    @Column(name = "post_at", nullable = false)
    private OffsetDateTime postAt;

    @Column(name = "refresh_at")
    private OffsetDateTime refreshAt;

    @Column(name = "user_name")
    private String userName;

    @CreationTimestamp
    @Column(name = "create_time", updatable = false, nullable = false)
    private LocalDateTime createTime;

    @UpdateTimestamp
    @Column(name = "update_time", updatable = false)
    private LocalDateTime updateTime;

    @Builder.Default
    @Column(name = "enabled")
    private boolean enabled = true;

    @Column(name = "visible")
    private boolean visible;

    @Column(name = "flags")
    private int flags;

    public Uni<JsonRecord> update() {
        return updateJsonRecord(this);
    }

    public static Uni<JsonRecord> addJsonRecord(@Nonnull BaseRecord baseRecord, @Nonnull JsonRecord jsonRecord) {
        return Panache
                .withTransaction(() -> baseRecord.<BaseRecord>persistAndFlush()
                        .flatMap(base -> {
                            jsonRecord.id(base.id());
                            jsonRecord.baseRecord(base);
                            if (Objects.isNull(jsonRecord.postAt)) {
                                jsonRecord.postAt = OffsetDateTime.now();
                            }
                            if (Objects.isNull(jsonRecord.refreshAt)) {
                                jsonRecord.refreshAt = jsonRecord.postAt;
                            }
                            return jsonRecord.persistAndFlush();
                        }))
                .replaceWith(jsonRecord)
                .ifNoItem()
                .after(TIMEOUT_DURATION)
                .fail()
                .onFailure()
                .transform(IllegalStateException::new);
    }

    public static Uni<Boolean> deleteJsonRecordById(@Nonnull UUID id) {
        return Panache
                .withTransaction(() -> findByJsonRecordById(id)
                        .onItem()
                        .ifNotNull()
                        .transform(entity -> {
                            entity.enabled = false;
                            return true;
                        })
                        .onFailure()
                        .recoverWithNull()
                )
                .ifNoItem()
                .after(TIMEOUT_DURATION)
                .recoverWithUni(Uni.createFrom().item(false))
                .onFailure()
                .transform(IllegalStateException::new);
    }

    public static Uni<JsonRecord> findByJsonRecordById(@Nonnull UUID id) {
        return findById(id);
    }

    public static PanacheQuery<JsonRecord> getAllEnabledPanacheQuery() {
        return find("#" + JsonRecord.LIST_ENABLED_WITH_TYPE, JsonRecord.ENABLED_JSON_TYPE);
    }

    public static Uni<JsonRecord> updateJsonRecord(@Nonnull JsonRecord jsonRecord) {
        return Panache
                .withTransaction(() -> findByJsonRecordById(jsonRecord.id)
                        .onItem()
                        .ifNotNull()
                        .transform(entity -> {
                            if (entity.enabled) {
                                entity.values = jsonRecord.values;
                                if (!Objects.isNull(jsonRecord.postAt)) {
                                    entity.postAt = jsonRecord.postAt;
                                }
                                entity.refreshAt = jsonRecord.refreshAt;
                                if (Objects.isNull(entity.refreshAt)) {
                                    entity.refreshAt = OffsetDateTime.now();
                                }
                                entity.userName = jsonRecord.userName;
                                entity.enabled = jsonRecord.enabled;
                                entity.visible = jsonRecord.visible;
                                entity.flags = jsonRecord.flags;
                            }
                            return entity;
                        })
                        .onFailure()
                        .recoverWithNull()
                )
                .ifNoItem()
                .after(TIMEOUT_DURATION)
                .fail()
                .onFailure()
                .transform(IllegalStateException::new);
    }
}
