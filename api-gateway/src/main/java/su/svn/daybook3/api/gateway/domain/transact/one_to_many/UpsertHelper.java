/*
 * This file was last modified at 2024-05-14 21:25 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * UpsertHelper.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.domain.transact.one_to_many;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.SqlConnection;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.tuple.Pair;
import org.jboss.logging.Logger;
import su.svn.daybook3.api.gateway.domain.model.CasesOfId;
import su.svn.daybook3.api.gateway.domain.transact.Action;
import su.svn.daybook3.api.gateway.domain.transact.Helper;
import su.svn.daybook3.api.gateway.models.Constants;
import su.svn.daybook3.api.gateway.models.Identification;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

class UpsertHelper<
        MainId extends Comparable<? extends Serializable>,
        MainTable extends CasesOfId<MainId>,
        SubId extends Comparable<? extends Serializable>,
        Subsidiary extends CasesOfId<SubId>>
        extends AbstractListHelper<MainId, MainTable, SubId, Subsidiary> {

    private static final Logger LOG = Logger.getLogger(UpsertHelper.class);

    private final Collection<Pair<MainTable, Collection<Subsidiary>>> collection;

    protected UpsertHelper(
            @Nonnull AbstractOneToManyJob<MainId, MainTable, SubId, Subsidiary> job,
            @Nonnull Map<String, Map<String, Action>> map,
            @Nonnull Collection<Pair<MainTable, Collection<Subsidiary>>> collection) {
        super(job, map.get(Constants.UPSERT));
        this.collection = collection;
    }

    @Override
    public Uni<List<MainId>> apply(@Nonnull final SqlConnection sqlConnection) {
        super.setConnection(sqlConnection);
        return checkCountInJoinTableAndThenInsert();
    }

    private Uni<List<MainId>> checkCountInJoinTableAndThenInsert() {
        LOG.tracef("checkCountInJoinTableAndThenInsert");
        return Uni
                .join()
                .all(this.doEachMainTable())
                .andCollectFailures()
                .map(this::listOfOptionalToList);
    }

    @Nonnull
    private List<Uni<Optional<MainId>>> doEachMainTable() {
        return collection
                .stream()
                .map(this::updateRelation)
                .toList();
    }

    @Nonnull
    private List<MainId> listOfOptionalToList(List<Optional<MainId>> list) {
        return list
                .stream()
                .map(o -> o.orElse(null))
                .filter(Objects::nonNull)
                .toList();
    }

    private Uni<Optional<MainId>> upsertMain(Pair<MainTable, Collection<Subsidiary>> pair) {
        var table = pair.getKey();
        var subsidiaries = pair.getValue();
        var helper = new UpsertMainHelper<>(super.job, super.map, table, subsidiaries);
        helper.setConnection(super.connection);
        return null;
    }

    static class UpsertMainHelper<
            MainId extends Comparable<? extends Serializable>,
            MainTable extends CasesOfId<MainId>,
            SubId extends Comparable<? extends Serializable>,
            Subsidiary extends CasesOfId<SubId>>
            extends AbstractHelper<MainId, MainTable, SubId, Subsidiary> {

        private final Action action;
        private final MainTable table;
        private final Collection<Subsidiary> subsidiaries;

        UpsertMainHelper(
                AbstractOneToManyJob<MainId, MainTable, SubId, Subsidiary> job,
                Map<String, Action> map,
                MainTable table,
                Collection<Subsidiary> subsidiaries) {
            super(job, map);
            this.action = super.map.get(Constants.UPSERT_MAIN_TABLE);
            this.table = table;
            this.subsidiaries = subsidiaries;
        }

        @Override
        public Uni<Optional<MainId>> apply(@Nonnull final SqlConnection sqlConnection) {
            return connection
                    .preparedQuery(this.action.sql())
                    .execute(Helper.updateTuple(action, table))
                    .map(RowSet::iterator)
                    .map(iteratorNextMapper(action, Constants.UPSERT_MAIN_TABLE))
                    .map(job.castOptionalMainId());
        }
    }

    private Uni<Optional<MainId>> updateRelation(Pair<MainTable, Collection<Subsidiary>> pair) {
        var action = super.map.get(Constants.UPDATE_RELATION);
        var table = pair.getKey();
        var subsidiaries = pair.getValue();
        var array = subsidiaries
                .stream()
                .map(Identification::id)
                .<SubId>toArray();
        return connection
                .preparedQuery(action.sql())
                .execute(Tuple.of(table.id(), array))
                .map(RowSet::iterator)
                .flatMap(r -> rowIteratorUniFunction1(table, array));
    }

    private Uni<Optional<MainId>> rowIteratorUniFunction1(MainTable table, Object[] array) {
        var action = super.map.get(Constants.CLEAR_RELATION);
        return connection
                .preparedQuery(action.sql())
                .execute(Tuple.of(table.id(), array))
                .map(RowSet::iterator)
                .flatMap(r -> rowIteratorUniFunction2(table));
    }

    private Uni<Optional<MainId>> rowIteratorUniFunction2(MainTable table) {
        var action = super.map.get(Constants.UPDATE_MAIN_TABLE);
        return connection
                .preparedQuery(action.sql())
                .execute(Helper.updateTuple(action, table))
                .map(RowSet::iterator)
                .map(iteratorNextMapper(action, Constants.UPDATE_MAIN_TABLE))
                .map(job.castOptionalMainId());
    }
}
