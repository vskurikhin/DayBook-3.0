/*
 * This file was last modified at 2024-05-14 21:25 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * BaseRecord.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.domain.entities;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.Nonnull;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Entity
@Getter
@Setter
@Builder
@ToString
@Cacheable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(fluent = true, chain = false)
@Table(schema = "db", name = "base_record")
@SuppressWarnings("LombokGetterMayBeUsed")
public class BaseRecord extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @CreationTimestamp
    @Column(name = "create_time")
    public ZonedDateTime createTime;

    @UpdateTimestamp
    @Column(name = "update_time")
    public ZonedDateTime updateTime;

    public Uni<Void> delete() {
        return Panache.withTransaction(() -> deleteById(this.id))
                .replaceWith(Uni.createFrom().voidItem());
    }

    public Uni<Object> update() {
        return Panache
                .withTransaction(() -> findByBaseRecordId(this.id)
                        .onItem()
                        .ifNotNull()
                        .transform(entity -> {
                            entity.description = this.description;
                            entity.title = this.title;
                            return entity;
                        })
                        .map((Function<BaseRecord, Object>) baseRecord -> baseRecord)
                        .onFailure()
                        .recoverWithNull());
    }

    public static Uni<BaseRecord> findByBaseRecordId(UUID id) {
        return findById(id);
    }

    public static Uni<BaseRecord> updateBaseRecord(UUID id, BaseRecord baseRecord) {
        return Panache
                .withTransaction(() -> findByBaseRecordId(id)
                        .onItem()
                        .ifNotNull()
                        .transform(entity -> {
                            entity.description = baseRecord.description;
                            entity.title = baseRecord.title;
                            return entity;
                        })
                        .onFailure()
                        .recoverWithNull());
    }

    public static Uni<BaseRecord> addBaseRecord(@Nonnull BaseRecord baseRecord) {
        return Panache
                .withTransaction(baseRecord::persist)
                .replaceWith(baseRecord)
                .ifNoItem()
                .after(Duration.ofMillis(10000))
                .fail()
                .onFailure()
                .transform(IllegalStateException::new);
    }

    public static Uni<List<BaseRecord>> getAllBaseRecord() {
        //noinspection unchecked
        return BaseRecord
                .listAll()
                .ifNoItem()
                .after(Duration.ofMillis(10000))
                .fail()
                .onFailure()
                .recoverWithUni(Uni.createFrom().<List<BaseRecord>>item(Collections.EMPTY_LIST));
    }

    public static Uni<Boolean> deleteBaseRecord(UUID id) {
        return Panache.withTransaction(() -> deleteById(id));
    }
}
