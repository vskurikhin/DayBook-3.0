/*
 * This file was last modified at 2023.09.06 19:32 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * InsertHelper.java
 * $Id$
 */

package su.svn.daybook.domain.transact.many_to_many;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.SqlConnection;
import jakarta.annotation.Nonnull;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.model.CasesOfId;
import su.svn.daybook.domain.transact.Action;
import su.svn.daybook.domain.transact.Helper;
import su.svn.daybook.models.Constants;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

class InsertHelper<
        MainId extends Comparable<? extends Serializable>,
        MainTable extends CasesOfId<MainId>,
        SubId extends Comparable<? extends Serializable>,
        Subsidiary extends CasesOfId<SubId>,
        MainField extends Comparable<? extends Serializable>,
        SubField extends Comparable<? extends Serializable>>
        extends AbstractHelper<MainId, MainTable, SubId, Subsidiary, MainField, SubField> {

    private static final Logger LOG = Logger.getLogger(InsertHelper.class);

    private final Collection<SubField> collection;

    protected InsertHelper(
            @Nonnull AbstractManyToManyJob<MainId, MainTable, SubId, Subsidiary, MainField, SubField> job,
            @Nonnull Map<String, Map<String, Action>> map,
            @Nonnull MainTable table,
            MainField field,
            @Nonnull Collection<SubField> collection) {
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
        return countInJoinTable()
                .flatMap(this::checkCountInJoin)
                .flatMap(o -> insert());
    }

    private Uni<Long> countInJoinTable() {
        Action action = super.map.get(Constants.COUNT_NOT_EXISTS);
        return super.countInJoinTable(action, collection);
    }

    private Uni<Optional<MainId>> insert() {
        var action = super.map.get(Constants.INSERT_MAIN);
        return super.connection
                .preparedQuery(String.format(Helper.insertSql(action, table), Constants.ID))
                .execute(table.caseInsertTuple())
                .map(RowSet::iterator)
                .map(iteratorNextMapper(action, Constants.INSERT_MAIN))
                .flatMap(id -> this.insertIntoHasRelation()
                        .flatMap(c1 -> clearIfHasRelationNotForEntry())
                        .map(c2 -> id)
                ).map(job.castOptionalMainId());
    }

    private Uni<Long> insertIntoHasRelation() {
        return super.insertIntoHasRelation(this.collection);
    }

    private Uni<Long> clearIfHasRelationNotForEntry() {
        return super.clearIfHasRelationNotForEntry(this.collection);
    }
}
