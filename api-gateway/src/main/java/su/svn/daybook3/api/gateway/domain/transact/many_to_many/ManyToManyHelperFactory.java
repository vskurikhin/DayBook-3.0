/*
 * This file was last modified at 2024-10-30 09:48 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * ManyToManyHelperFactory.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.domain.transact.many_to_many;

import jakarta.annotation.Nonnull;
import su.svn.daybook3.api.gateway.domain.transact.Action;
import su.svn.daybook3.api.gateway.domain.transact.OptionalHelper;
import su.svn.daybook3.domain.model.CasesOfId;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

record ManyToManyHelperFactory<
        MainId extends Comparable<? extends Serializable>,
        MainTable extends CasesOfId<MainId>,
        RelId extends Comparable<? extends Serializable>,
        Relative extends CasesOfId<RelId>,
        MainField extends Comparable<? extends Serializable>,
        RelField extends Comparable<? extends Serializable>>(
        @Nonnull AbstractManyToManyJob<MainId, MainTable, RelId, Relative, MainField, RelField> job,
        @Nonnull Map<String, Map<String, Action>> mapJob) {

    ManyToManyHelperFactory(
            @Nonnull AbstractManyToManyJob<MainId, MainTable, RelId, Relative, MainField, RelField> job,
            @Nonnull Map<String, Map<String, Action>> mapJob) {
        this.job = job;
        this.mapJob = mapJob;
    }

    public OptionalHelper<MainId> createInsertHelper(
            @Nonnull MainTable table,
            MainField field,
            @Nonnull Collection<RelField> collection) {
        return new InsertHelper<>(this.job, this.mapJob, table, field, collection);
    }

    public OptionalHelper<MainId> createUpdateHelper(
            @Nonnull MainTable table,
            MainField field,
            @Nonnull Collection<RelField> collection) {
        return new UpdateHelper<>(this.job, this.mapJob, table, field, collection);
    }

    public OptionalHelper<MainId> createDeleteHelper(@Nonnull MainTable table, MainField field) {
        return new DeleteHelper<>(this.job, this.mapJob, table, field);
    }
}
