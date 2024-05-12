/*
 * This file was last modified at 2024-05-14 21:25 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * InsertHelper.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.domain.transact.many_to_many;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.SqlConnection;
import jakarta.annotation.Nonnull;
import org.jboss.logging.Logger;
import su.svn.daybook3.api.gateway.domain.model.CasesOfId;
import su.svn.daybook3.api.gateway.domain.transact.Action;
import su.svn.daybook3.api.gateway.domain.transact.Helper;
import su.svn.daybook3.api.gateway.models.Constants;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

class InsertHelper<
        MainId extends Comparable<? extends Serializable>,
        MainTable extends CasesOfId<MainId>,
        RelId extends Comparable<? extends Serializable>,
        Relative extends CasesOfId<RelId>,
        MainField extends Comparable<? extends Serializable>,
        RelField extends Comparable<? extends Serializable>>
        extends AbstractHelper<MainId, MainTable, RelId, Relative, MainField, RelField> {

    private static final Logger LOG = Logger.getLogger(InsertHelper.class);

    private final Collection<RelField> collection;

    protected InsertHelper(
            @Nonnull AbstractManyToManyJob<MainId, MainTable, RelId, Relative, MainField, RelField> job,
            @Nonnull Map<String, Map<String, Action>> map,
            @Nonnull MainTable table,
            MainField field,
            @Nonnull Collection<RelField> collection) {
        super(job, map.get(Constants.INSERT), table, field);
        this.collection = collection;
    }

    @Override
    public Uni<Optional<MainId>> apply(@Nonnull final SqlConnection sqlConnection) {
        super.setConnection(sqlConnection);
        return checkCountInJoinTableAndThenInsert();
    }

    private Uni<Optional<MainId>> checkCountInJoinTableAndThenInsert() {
        LOG.tracef("checkCountInJoinTableAndThenInsert");
        return checkNotInRelationTable()
                .flatMap(this::checkCountInJoin)
                .flatMap(o -> insert());
    }

    private Uni<Long> checkNotInRelationTable() {
        Action action = super.map.get(Constants.CHECK_NOT_IN_RELATIVE);
        return super.executeIfNotInRelationTable(action, collection);
    }

    private Uni<Optional<MainId>> insert() {
        var action = super.map.get(Constants.INSERT_INTO_MAIN);
        return super.connection
                .preparedQuery(String.format(Helper.insertSql(action, table), Constants.ID))
                .execute(table.caseInsertTuple())
                .map(RowSet::iterator)
                .map(iteratorNextMapper(action, Constants.INSERT_INTO_MAIN))
                .flatMap( // TODO refactoring!
                        id -> this.insertIntoHasRelation()
                                .flatMap(c1 -> clearIfHasRelationNotForEntry())
                                .map(c2 -> id)
                )
                .map(job.castOptionalMainId())
                .log();
    }

    // TODO refactoring!
    @Nonnull
    private Function<? extends Optional<?>, Uni<? extends Optional<?>>> getUniFunction() {
        return id -> {
            return this.insertIntoHasRelation()
                    .flatMap(c1 -> clearIfHasRelationNotForEntry())
                    .map(c2 -> id);
        };
    }

    private Uni<Long> insertIntoHasRelation() {
        return super.insertIntoHasRelation(this.collection);
    }

    private Uni<Long> clearIfHasRelationNotForEntry() {
        return super.clearIfHasRelationNotForEntry(this.collection);
    }
}
