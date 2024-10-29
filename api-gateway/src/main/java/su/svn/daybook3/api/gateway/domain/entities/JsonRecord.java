/*
 * This file was last modified at 2024-10-29 00:40 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * JsonRecord.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.domain.entities;

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
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;
import su.svn.daybook3.api.gateway.models.Marked;
import su.svn.daybook3.api.gateway.models.Owned;
import su.svn.daybook3.api.gateway.models.TimeUpdated;
import su.svn.daybook3.api.gateway.models.UUIDIdentification;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

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
                query = "From JsonRecord j LEFT JOIN FETCH j.baseRecord WHERE j.enabled = :enabled"
        )
})
public class JsonRecord
        extends PanacheEntityBase
        implements UUIDIdentification, Marked, Owned, TimeUpdated, Serializable {

    public static final Duration TIMEOUT_DURATION = Duration.ofMillis(10100);
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

    @Column(name = "values")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, String> values;

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
        return Panache.withTransaction(() -> deleteById(id));
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
                            entity.values = jsonRecord.values;
                            entity.userName = jsonRecord.userName;
                            entity.enabled = jsonRecord.enabled;
                            entity.visible = jsonRecord.visible;
                            entity.flags = jsonRecord.flags;
                            return entity;
                        })
                        .onFailure()
                        .recoverWithNull()
                ) /*.replaceWith(jsonRecord) */
                .ifNoItem()
                .after(TIMEOUT_DURATION)
                .fail()
                .onFailure()
                .transform(IllegalStateException::new);
    }
}
