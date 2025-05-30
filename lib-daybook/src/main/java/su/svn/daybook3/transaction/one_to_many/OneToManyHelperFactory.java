/*
 * This file was last modified at 2024-10-30 19:24 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * OneToManyHelperFactory.java
 * $Id$
 */

package su.svn.daybook3.transaction.one_to_many;

import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.tuple.Pair;
import su.svn.daybook3.domain.model.CasesOfId;
import su.svn.daybook3.transaction.Action;
import su.svn.daybook3.transaction.ListHelper;
import su.svn.daybook3.transaction.OptionalHelper;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

record OneToManyHelperFactory<
        MainId extends Comparable<? extends Serializable>,
        MainTable extends CasesOfId<MainId>,
        SubId extends Comparable<? extends Serializable>,
        Subsidiary extends CasesOfId<SubId>>(
        @Nonnull AbstractOneToManyJob<MainId, MainTable, SubId, Subsidiary> job,
        @Nonnull Map<String, Map<String, Action>> mapJob) {

    OneToManyHelperFactory(
            @Nonnull AbstractOneToManyJob<MainId, MainTable, SubId, Subsidiary> job,
            @Nonnull Map<String, Map<String, Action>> mapJob) {
        this.job = job;
        this.mapJob = mapJob;
    }

    public ListHelper<MainId> createUpsertHelper(@Nonnull Collection<Pair<MainTable, Collection<Subsidiary>>> collection) {
        return new UpsertHelper<>(this.job, this.mapJob, collection);
    }

    public OptionalHelper<MainId> createDeleteHelper(@Nonnull MainTable table) {
        return new DeleteHelper<>(this.job, this.mapJob, table);
    }
}
