/*
 * This file was last modified at 2025-01-19 21:46 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * BaseRecord.java
 * $Id$
 */

package su.svn.daybook3.api.domain.entities;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.quarkus.panache.common.Parameters;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
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
import org.hibernate.annotations.UpdateTimestamp;
import su.svn.daybook3.models.Marked;
import su.svn.daybook3.models.Owned;
import su.svn.daybook3.models.TimeUpdated;
import su.svn.daybook3.models.UUIDIdentification;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@ToString(exclude = "parent")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = {"id", "parent"})
@Accessors(fluent = true, chain = false)
@Table(schema = "db", name = "base_records")
@NamedQueries({
        @NamedQuery(
                name = BaseRecord.LIST_ENABLED_WITH_TYPE,
                query = "From BaseRecord WHERE enabled = :enabled AND type=:type"
        )
})
public class BaseRecord
        extends PanacheEntityBase
        implements UUIDIdentification, Marked, Owned, TimeUpdated, Serializable {

    public static final Duration TIMEOUT_DURATION = Duration.ofMillis(10000);
    public static final String LIST_ENABLED_WITH_TYPE = "BaseRecord.ListEnabledWithType";
    public static final Parameters ENABLED_BASE_TYPE = Parameters
            .with("enabled", Boolean.TRUE)
            .and("type", RecordType.Base);

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "parent_id", nullable = false)
    private UUID parentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "parent_id",
            referencedColumnName = "id",
            insertable = false,
            nullable = false,
            updatable = false)
    private BaseRecord parent;

    @Builder.Default
    @Column(name = "type")
    private RecordType type = RecordType.Base;

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

    public Uni<BaseRecord> update() {
        return updateBaseRecord(this);
    }

    public static Uni<BaseRecord> addBaseRecord(@Nonnull BaseRecord baseRecord) {
        if (baseRecord.parentId == null) {
            baseRecord.parentId(baseRecord.id());
            baseRecord.parent(baseRecord);
        }
        baseRecord.flags &= (Integer.MAX_VALUE - 1);
        return Panache
                .withTransaction(baseRecord::persistAndFlush)
                .replaceWith(baseRecord)
                .ifNoItem()
                .after(TIMEOUT_DURATION)
                .fail()
                .onFailure()
                .transform(IllegalStateException::new);
    }

    public static Uni<Boolean> deleteBaseRecordById(@Nonnull UUID id) {
        return Panache.withTransaction(() -> deleteById(id));
    }

    public static Uni<BaseRecord> findByBaseRecordById(@Nonnull UUID id) {
        return findById(id);
    }

    public static Uni<BaseRecord> updateBaseRecord(@Nonnull BaseRecord baseRecord) {
        return Panache
                .withTransaction(() -> findByBaseRecordById(baseRecord.id)
                        .onItem()
                        .ifNotNull()
                        .transform(entity -> {
                            entity.parentId = baseRecord.parentId;
                            entity.type = baseRecord.type;
                            entity.userName = baseRecord.userName;
                            entity.enabled = baseRecord.enabled;
                            entity.visible = baseRecord.visible;
                            entity.flags = baseRecord.flags &= (Integer.MAX_VALUE - 1);
                            return entity;
                        })
                        .onFailure()
                        .recoverWithNull()
                ).replaceWith(baseRecord)
                .ifNoItem()
                .after(TIMEOUT_DURATION)
                .fail()
                .onFailure()
                .transform(IllegalStateException::new);
    }
}
