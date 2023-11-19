/*
 * This file was last modified at 2023.11.19 11:15 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * OneToOneHelperFactory.java
 * $Id$
 */

package su.svn.daybook.domain.transact.one_to_one;

import su.svn.daybook.domain.model.CasesOfId;
import su.svn.daybook.domain.transact.Action;
import su.svn.daybook.domain.transact.OptionalHelper;

import jakarta.annotation.Nonnull;

import java.io.Serializable;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

record OneToOneHelperFactory<
        MainId extends Comparable<? extends Serializable>,
        MainTable extends CasesOfId<MainId>,
        JoinId extends Comparable<? extends Serializable>,
        JoinTable extends CasesOfId<JoinId>,
        Field extends Comparable<? extends Serializable>>(
        @Nonnull AbstractOneToOneJob<MainId, MainTable, JoinId, JoinTable, Field> job,
        @Nonnull Map<String, Map<String, Action>> mapJob,
        @Nonnull BiFunction<MainTable, JoinId, MainTable> tableBuilder,
        @Nonnull Function<Field, JoinTable> joinFieldBuilder) {

    OneToOneHelperFactory(
            @Nonnull AbstractOneToOneJob<MainId, MainTable, JoinId, JoinTable, Field> job,
            @Nonnull Map<String, Map<String, Action>> mapJob,
            @Nonnull BiFunction<MainTable, JoinId, MainTable> tableBuilder,
            @Nonnull Function<Field, JoinTable> joinFieldBuilder) {
        this.tableBuilder = tableBuilder;
        this.joinFieldBuilder = joinFieldBuilder;
        this.mapJob = mapJob;
        this.job = job;
    }

    public OptionalHelper<MainId> createInsertHelper(MainTable table, Field field) {
        return new InsertHelper<>(this.job, this.mapJob, this.tableBuilder, this.joinFieldBuilder, table, field);
    }

    public OptionalHelper<MainId> createUpdateHelper(MainTable table, Field field) {
        return new UpdateHelper<>(this.job, this.mapJob, this.tableBuilder, this.joinFieldBuilder, table, field);
    }

    public OptionalHelper<MainId> createDeleteHelper(MainTable table) {
        return new DeleteHelper<>(this.job, this.mapJob, table);
    }

}