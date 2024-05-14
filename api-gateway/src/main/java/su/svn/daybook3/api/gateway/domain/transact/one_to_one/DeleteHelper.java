/*
 * This file was last modified at 2024-05-14 21:36 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * DeleteHelper.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.domain.transact.one_to_one;

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
        JoinId extends Comparable<? extends Serializable>,
        JoinTable extends CasesOfId<JoinId>,
        Field extends Comparable<? extends Serializable>>
        extends AbstractHelper<MainId, MainTable, JoinId, JoinTable, Field> {

    private static final Logger LOG = Logger.getLogger(DeleteHelper.class);

    DeleteHelper(
            @Nonnull AbstractOneToOneJob<MainId, MainTable, JoinId, JoinTable, Field> job,
            @Nonnull Map<String, Map<String, Action>> map,
            @Nonnull MainTable table) {
        super(job, map, table);
    }

    @Override
    public Uni<Optional<MainId>> apply(@Nonnull final SqlConnection sqlConnection) {
        super.setConnection(sqlConnection);
        return deleteAndClear();
    }

    private Uni<Optional<MainId>> deleteAndClear() {
        LOG.tracef("deleteAndClear");
        var map = super.mapJob.get(Constants.DELETE);
        return deleteMain(map, super.table.id())
                .flatMap(o -> o.isPresent() ? clear(map, o.get()) : getFailure());
    }

    private Uni<Optional<MainId>> clear(Map<String, Action> map, MainId id) {
        var action = map.get(Constants.CLEAR_JOIN_ID);
        return super.connection
                .preparedQuery(String.format(action.sql(), Constants.ID))
                .execute()
                .map(RowSet::iterator)
                .map(action.iteratorNextMapper())
                .map(x -> Optional.of(id))
                .log();
    }

    private Uni<Optional<MainId>> deleteMain(Map<String, Action> map, MainId id) {
        var action = map.get(Constants.DELETE_MAIN);
        return super.connection
                .preparedQuery(String.format(Helper.deleteSql(action, super.table), Constants.ID))
                .execute(Tuple.of(id))
                .map(RowSet::iterator)
                .map(iteratorNextMapper(action, Constants.UPDATE_MAIN_TABLE))
                .map(job.castOptionalMainId())
                .log();
    }
}
