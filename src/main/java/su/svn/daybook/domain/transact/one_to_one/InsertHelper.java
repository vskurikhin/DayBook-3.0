/*
 * This file was last modified at 2023.11.19 18:33 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * InsertHelper.java
 * $Id$
 */

package su.svn.daybook.domain.transact.one_to_one;

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
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

class InsertHelper<
        MainId extends Comparable<? extends Serializable>,
        MainTable extends CasesOfId<MainId>,
        JoinId extends Comparable<? extends Serializable>,
        JoinTable extends CasesOfId<JoinId>,
        Field extends Comparable<? extends Serializable>>
        extends AbstractHelper<MainId, MainTable, JoinId, JoinTable, Field> {

    private static final Logger LOG = Logger.getLogger(InsertHelper.class);

    private final BiFunction<MainTable, JoinId, MainTable> tableBuilder;
    private final Field field;
    private final Function<Field, JoinTable> joinFieldBuilder;

    InsertHelper(
            @Nonnull AbstractOneToOneJob<MainId, MainTable, JoinId, JoinTable, Field> job,
            @Nonnull Map<String, Map<String, Action>> map,
            @Nonnull BiFunction<MainTable, JoinId, MainTable> tableBuilder,
            @Nonnull Function<Field, JoinTable> joinFieldBuilder,
            @Nonnull MainTable table,
            Field field) {
        super(job, map, table);
        this.tableBuilder = tableBuilder;
        this.joinFieldBuilder = joinFieldBuilder;
        this.field = field;
    }

    @Override
    public Uni<Optional<MainId>> apply(@Nonnull final SqlConnection sqlConnection) {
        super.setConnection(sqlConnection);
        return findingOrThenInsert();
    }

    private Uni<Optional<MainId>> findingOrThenInsert() {
        LOG.tracef("findingOrThenInsert");
        var map = super.mapJob.get(Constants.INSERT);
        return findFieldId(map)
                .flatMap(o -> o.isEmpty() ? insertJoin(map) : Uni.createFrom().item(o))
                .log()
                .flatMap(o -> o.isPresent() ? insertMain(map, o.get()) : getFailure());
    }

    private Uni<Optional<JoinId>> findFieldId(Map<String, Action> map) {
        var action = map.get(Constants.FIND_FIELD_ID);
        return super.findFieldId(action, this.field);
    }

    private Uni<Optional<JoinId>> insertJoin(Map<String, Action> map) {
        var action = map.get(Constants.INSERT_INTO_RELATION);
        var join = joinFieldBuilder.apply(this.field);
        return super.insertJoin(action, join);
    }

    private Uni<Optional<MainId>> insertMain(Map<String, Action> map, JoinId joinId) {
        var action = map.get(Constants.INSERT_INTO_MAIN);
        var main = tableBuilder.apply(super.table, joinId);
        return super.connection
                .preparedQuery(String.format(Helper.insertSql(action, main), Constants.ID))
                .execute(Helper.insertTuple(action, main))
                .map(RowSet::iterator)
                .map(iteratorNextMapper(action, Constants.INSERT_INTO_MAIN))
                .map(job.castOptionalMainId())
                .log();
    }
}
