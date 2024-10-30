/*
 * This file was last modified at 2024-10-30 18:38 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * ActionJob.java
 * $Id$
 */

package su.svn.daybook3.transaction;

import jakarta.annotation.Nonnull;
import su.svn.daybook3.annotations.TransactionAction;
import su.svn.daybook3.annotations.TransactionActions;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ActionJob {

    protected Map<String, Map<String, Action>> getActionsOfMethods() {
        return Arrays.stream(this.getClass().getDeclaredMethods())
                .filter(this::testAnnotationTransactionActions)
                .map(this::createActionsEntry)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Nonnull
    private Map.Entry<String, Map<String, Action>> createActionsEntry(Method method) {
        return new AbstractMap.SimpleEntry<>(method.getName(), createActionEntries(method));
    }

    @Nonnull
    private Map<String, Action> createActionEntries(Method method) {
        TransactionActions transactionActions = method.getAnnotation(TransactionActions.class);
        var result = Stream
                .of(transactionActions.value())
                .map(this::createActionEntry)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return Collections.unmodifiableMap(result);
    }

    @Nonnull
    private Map.Entry<String, Action> createActionEntry(TransactionAction transactionAction) {
        return new AbstractMap.SimpleEntry<>(transactionAction.name(), Action.of(transactionAction));
    }

    private boolean testAnnotationTransactionActions(Method method) {
        return method.isAnnotationPresent(TransactionActions.class)
                && Modifier.isPublic(method.getModifiers())
                && !method.isBridge();
    }
}
