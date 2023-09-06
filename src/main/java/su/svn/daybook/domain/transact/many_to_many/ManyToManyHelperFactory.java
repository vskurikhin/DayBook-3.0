/*
 * This file was last modified at 2023.09.06 19:32 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * ManyToManyHelperFactory.java
 * $Id$
 */

package su.svn.daybook.domain.transact.many_to_many;

import su.svn.daybook.domain.model.CasesOfId;
import su.svn.daybook.domain.transact.Action;
import su.svn.daybook.domain.transact.Helper;

import jakarta.annotation.Nonnull;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

record ManyToManyHelperFactory<
        MainId extends Comparable<? extends Serializable>,
        MainTable extends CasesOfId<MainId>,
        SubId extends Comparable<? extends Serializable>,
        Subsidiary extends CasesOfId<SubId>,
        MainField extends Comparable<? extends Serializable>,
        SubField extends Comparable<? extends Serializable>>(
        @Nonnull AbstractManyToManyJob<MainId, MainTable, SubId, Subsidiary, MainField, SubField> job,
        @Nonnull Map<String, Map<String, Action>> mapJob) {

    ManyToManyHelperFactory(
            @Nonnull AbstractManyToManyJob<MainId, MainTable, SubId, Subsidiary, MainField, SubField> job,
            @Nonnull Map<String, Map<String, Action>> mapJob) {
        this.job = job;
        this.mapJob = mapJob;
    }

    public Helper<MainId, MainTable, SubId, Subsidiary, SubField> createInsertHelper(
            @Nonnull MainTable table,
            MainField field,
            @Nonnull Collection<SubField> collection) {
        return new InsertHelper<>(this.job, this.mapJob, table, field, collection);
    }

    public Helper<MainId, MainTable, SubId, Subsidiary, SubField> createUpdateHelper(
            @Nonnull MainTable table,
            MainField field,
            @Nonnull Collection<SubField> collection) {
        return new UpdateHelper<>(this.job, this.mapJob, table, field, collection);
    }

    public Helper<MainId, MainTable, SubId, Subsidiary, SubField> createDeleteHelper(
            @Nonnull MainTable table,
            MainField field) {
        return new DeleteHelper<>(this.job, this.mapJob, table, field);
    }

}
