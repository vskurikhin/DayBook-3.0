/*
 * This file was last modified at 2024-10-30 09:54 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * UpsertHelper.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.domain.transact.one_to_many;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.SqlConnection;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.tuple.Pair;
import org.jboss.logging.Logger;
import su.svn.daybook3.api.gateway.domain.transact.Action;
import su.svn.daybook3.api.gateway.domain.transact.Helper;
import su.svn.daybook3.api.gateway.models.Constants;
import su.svn.daybook3.domain.model.CasesOfId;
import su.svn.daybook3.models.Identification;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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
        return Uni.combine()
                .all()
                .unis(this.doEachMainTable())
                .with(objects -> {
                    System.out.println("objects = " + objects);
                    return objects.stream().map(o -> {
                                if (o instanceof Optional<?> opt) {
                                    return job.castOptionalMainId().apply(opt);
                                } else {
                                    return Optional.<MainId>empty();
                                }
                            }).filter(Optional::isPresent)
                            .map(Optional::get)
                            .toList();
                });
    }

    @Nonnull
    private List<Uni<Optional<MainId>>> doEachMainTable() {
        return collection
                .stream()
                .map(this::updateRelation)
                .toList();
    }

    private Uni<Optional<MainId>> updateRelation(Pair<MainTable, Collection<Subsidiary>> pair) {
        var action = super.map.get(Constants.UPSERT_MAIN_TABLE);
        var table = pair.getKey();
        var subsidiaries = pair.getValue();
        var array = subsidiaries
                .stream()
                .map(Identification::id)
                .<SubId>toArray();
        return connection
                .preparedQuery(action.sql())
                .execute(Helper.updateTuple(action, table))
                .map(RowSet::iterator)
                .map(iteratorNextMapper(action, Constants.UPSERT_MAIN_TABLE))
                .map(job.castOptionalMainId())
                .flatMap(
                        mainId -> rowIteratorUniFunction1(table, array)
                                .replaceWith(Uni.createFrom().item(mainId))
                )
                .flatMap(
                        mainId -> rowIteratorUniFunction2(table, array)
                                .replaceWith(Uni.createFrom().item(mainId))
                );
    }

    private Uni<Optional<Row>> rowIteratorUniFunction1(MainTable table, Object[] array) {
        var action = super.map.get(Constants.CLEAR_RELATION);
        return connection
                .preparedQuery(action.sql())
                .execute(Tuple.of(table.id(), array))
                .map(RowSet::iterator)
                .map(r -> r.hasNext() ? Optional.of(r.next()) : Optional.empty());
    }

    private Uni<Optional<Row>> rowIteratorUniFunction2(MainTable table, Object[] array) {
        var action = super.map.get(Constants.UPDATE_RELATION);
        return connection
                .preparedQuery(action.sql())
                .execute(Tuple.of(table.id(), array))
                .map(RowSet::iterator)
                .map(r -> r.hasNext() ? Optional.of(r.next()) : Optional.empty());
    }
}
