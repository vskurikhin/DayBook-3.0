/*
 * This file was last modified at 2024-05-14 21:25 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * DeleteHelper.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.domain.transact.one_to_many;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.SqlConnection;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.annotation.Nonnull;
import org.jboss.logging.Logger;
import su.svn.daybook3.api.gateway.domain.model.CasesOfId;
import su.svn.daybook3.api.gateway.domain.transact.Action;
import su.svn.daybook3.api.gateway.domain.transact.Helper;
import su.svn.daybook3.api.gateway.models.Constants;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

class DeleteHelper<
        MainId extends Comparable<? extends Serializable>,
        MainTable extends CasesOfId<MainId>,
        SubId extends Comparable<? extends Serializable>,
        Subsidiary extends CasesOfId<SubId>>
        extends AbstractHelper<MainId, MainTable, SubId, Subsidiary> {

    private static final Logger LOG = Logger.getLogger(DeleteHelper.class);

    protected final MainTable table;

    protected DeleteHelper(
            @Nonnull AbstractOneToManyJob<MainId, MainTable, SubId, Subsidiary> job,
            @Nonnull Map<String, Map<String, Action>> map,
            @Nonnull MainTable table) {
        super(job, map.get(Constants.DELETE));
        this.table = table;
    }

    @Override
    public Uni<Optional<MainId>> apply(@Nonnull final SqlConnection sqlConnection) {
        super.setConnection(sqlConnection);
        return delete();
    }

    private Uni<Optional<MainId>> delete() {
        LOG.tracef("checkCountInJoinTableAndThenUpdate");
        return deleteAllRelation()
                .flatMap(this::deleteEntry);
    }

    private Uni<Optional<MainId>> deleteEntry(Long aLong) {
        Action action = super.map.get(Constants.DELETE_MAIN);
        return super.connection
                .preparedQuery(String.format(Helper.deleteSql(action, table), Constants.ID))
                .execute(Tuple.of(table.id()))
                .map(RowSet::iterator)
                .map(iteratorNextMapper(action, Constants.DELETE_MAIN))
                .map(job.castOptionalMainId())
                .log();
    }

    private Uni<Long> deleteAllRelation() {
        Action action = super.map.get(Constants.CLEAR_ALL_HAS_RELATION_BY_ID);
        return this.deleteAllFromHasRelationByEntry(action);
    }

    private Uni<Long> deleteAllFromHasRelationByEntry(Action action) {
        return super.deleteAllFromHasRelationByEntry(action, this.table.id());
    }
}
