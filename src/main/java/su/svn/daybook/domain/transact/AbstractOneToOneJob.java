/*
 * This file was last modified at 2023.01.06 11:06 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * AbstractOneToOneJob.java
 * $Id$
 */

package su.svn.daybook.domain.transact;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.Pool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowIterator;
import org.jboss.logging.Logger;
import org.jetbrains.annotations.NotNull;
import su.svn.daybook.domain.model.CasesOfId;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

abstract class AbstractOneToOneJob<
        I extends Comparable<? extends Serializable>,
        D extends CasesOfId<I>,
        G extends Comparable<? extends Serializable>,
        J extends CasesOfId<G>,
        F extends Comparable<? extends Serializable>> {

    private final Logger log;
    private final Pool pool;
    private final BiFunction<D, G, D> tableBuilder;
    private final Function<F, J> joinFieldBuilder;

    private final Map<String, Map<String, Action>> map;

    public abstract Uni<Optional<I>> insert(D table, F field);

    public abstract Uni<Optional<I>> update(D table, F field);

    protected abstract Function<RowIterator<Row>, Optional<?>> iteratorNextFunctionMapper(String actionName);

    protected abstract Function<Optional<?>, Optional<G>> oToG();

    protected abstract Function<Optional<?>, Optional<I>> oToI();

    AbstractOneToOneJob(
            @Nonnull Pool pool,
            @Nonnull BiFunction<D, G, D> tableBuilder,
            @Nonnull Function<F, J> joinFieldBuilder,
            @Nonnull Logger log) {
        this.log = log;
        this.pool = pool;
        this.tableBuilder = tableBuilder;
        this.joinFieldBuilder = joinFieldBuilder;
        this.map = Collections.unmodifiableMap(getActionsOfMethods());
    }

    protected Map<String, Map<String, Action>> getActionsOfMethods() {
        return Arrays.stream(this.getClass().getDeclaredMethods())
                .filter(this::testAnnotationTransactionActions)
                .map(this::createActionsEntry)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @NotNull
    private Map.Entry<String, Map<String, Action>> createActionsEntry(Method method) {
        return new AbstractMap.SimpleEntry<>(method.getName(), createActionEntries(method));
    }

    @NotNull
    private Map<String, Action> createActionEntries(Method method) {
        TransactionActions transactionActions = method.getAnnotation(TransactionActions.class);
        var result = Stream
                .of(transactionActions.value())
                .map(this::createActionEntry)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return Collections.unmodifiableMap(result);
    }

    @NotNull
    private Map.Entry<String, Action> createActionEntry(TransactionAction transactionAction) {
        return new AbstractMap.SimpleEntry<>(transactionAction.name(), Action.of(transactionAction));
    }

    private boolean testAnnotationTransactionActions(Method method) {
        return method.isAnnotationPresent(TransactionActions.class)
                && Modifier.isPublic(method.getModifiers())
                && !method.isBridge();
    }

    public Uni<Optional<I>> doInsert(D table, F field) {
        log.tracef("doInsert(%s, %s)", table, field);
        var helper = new OneToOneHelper<>(this, map, table, field, this.tableBuilder, this.joinFieldBuilder);
        return pool.withTransaction(helper::findingOrThenInsert);
    }

    public Uni<Optional<I>> doUpdate(D table, F field) {
        log.tracef("doUpdate(%s, %s)", table, field);
        var helper = new OneToOneHelper<>(this, map, table, field, this.tableBuilder, this.joinFieldBuilder);
        return pool.withTransaction(helper::findingOrThenUpdate);
    }
}
