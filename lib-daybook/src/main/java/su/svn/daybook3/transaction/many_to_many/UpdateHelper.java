/*
 * This file was last modified at 2024-10-31 15:15 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * UpdateHelper.java
 * $Id$
 */

package su.svn.daybook3.transaction.many_to_many;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.SqlConnection;
import jakarta.annotation.Nonnull;
import org.jboss.logging.Logger;
import su.svn.daybook3.domain.model.CasesOfId;
import su.svn.daybook3.transaction.Action;
import su.svn.daybook3.transaction.Constants;
import su.svn.daybook3.transaction.Helper;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

class UpdateHelper<
        MainId extends Comparable<? extends Serializable>,
        MainTable extends CasesOfId<MainId>,
        RelId extends Comparable<? extends Serializable>,
        Relative extends CasesOfId<RelId>,
        MainField extends Comparable<? extends Serializable>,
        RelField extends Comparable<? extends Serializable>>
        extends AbstractHelper<MainId, MainTable, RelId, Relative, MainField, RelField> {

    private static final Logger LOG = Logger.getLogger(UpdateHelper.class);

    private final Collection<RelField> collection;

    protected UpdateHelper(
            @Nonnull AbstractManyToManyJob<MainId, MainTable, RelId, Relative, MainField, RelField> job,
            @Nonnull Map<String, Map<String, Action>> map,
            @Nonnull MainTable table,
            MainField field,
            @Nonnull Collection<RelField> collection) {
        super(job, map.get(Constants.UPDATE), table, field);
        this.collection = collection;
    }

    @Override
    public Uni<Optional<MainId>> apply(@Nonnull final SqlConnection sqlConnection) {
        super.setConnection(sqlConnection);
        return checkCountInJoinTableAndThenUpdate();
    }

    private Uni<Optional<MainId>> checkCountInJoinTableAndThenUpdate() {
        LOG.tracef("checkCountInJoinTableAndThenUpdate");
        return countInJoinTable()
                .flatMap(this::checkCountInJoin)
                .flatMap(o -> update());
    }

    private Uni<Long> countInJoinTable() {
        Action action = super.map.get(Constants.CHECK_NOT_IN_RELATIVE);
        return super.executeIfNotInRelationTable(action, collection);
    }

    private Uni<Optional<MainId>> update() {
        var action = super.map.get(Constants.UPDATE_MAIN_TABLE);
        return super.connection
                .preparedQuery(String.format(Helper.updateSql(action, table), Constants.ID))
                .execute(table.updateTuple())
                .map(RowSet::iterator)
                .map(iteratorNextMapper(action, Constants.UPDATE_MAIN_TABLE))
                .flatMap(id -> this.insertIntoHasRelation()
                        .flatMap(c1 -> clearIfHasRelationNotForEntry())
                        .map(c2 -> id)
                ).map(job.castOptionalMainId())
                .log("UpdateHelper.update"); // todo remove
    }

    private Uni<Long> insertIntoHasRelation() {
        return super.insertIntoHasRelation(this.collection);
    }

    private Uni<Long> clearIfHasRelationNotForEntry() {
        return super.clearIfHasRelationNotForEntry(this.collection);
    }
}
