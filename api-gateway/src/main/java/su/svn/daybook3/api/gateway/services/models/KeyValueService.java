/*
 * This file was last modified at 2024-05-14 23:08 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * KeyValueService.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.services.models;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import su.svn.daybook3.api.gateway.annotations.ExceptionBadRequestAnswer;
import su.svn.daybook3.api.gateway.annotations.ExceptionDuplicateAnswer;
import su.svn.daybook3.api.gateway.annotations.ExceptionNoSuchElementAnswer;
import su.svn.daybook3.api.gateway.annotations.PrincipalLogging;
import su.svn.daybook3.api.gateway.domain.enums.EventAddress;
import su.svn.daybook3.api.gateway.domain.messages.Answer;
import su.svn.daybook3.api.gateway.domain.messages.Request;
import su.svn.daybook3.api.gateway.models.domain.KeyValue;
import su.svn.daybook3.api.gateway.models.pagination.Page;
import su.svn.daybook3.api.gateway.models.pagination.PageRequest;
import su.svn.daybook3.api.gateway.services.cache.KeyValueCacheProvider;
import su.svn.daybook3.api.gateway.services.domain.KeyValueDataService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.UUID;

@PrincipalLogging
@ApplicationScoped
public class KeyValueService extends AbstractService<UUID, KeyValue> {

    @Inject
    KeyValueCacheProvider keyValueCacheProvider;

    @Inject
    KeyValueDataService keyValueDataService;

    /**
     * This is method a Vertx message consumer and KeyValue creater
     *
     * @param request - KeyValue
     * @return - a lazy asynchronous action (LAA) with the Answer containing the KeyValue id as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionDuplicateAnswer
    @ConsumeEvent(EventAddress.KEY_VALUE_ADD)
    public Uni<Answer> add(Request<KeyValue> request) {
        //noinspection DuplicatedCode
        return keyValueDataService
                .add(request.payload())
                .map(this::apiResponseCreatedAnswer)
                .flatMap(keyValueCacheProvider::invalidate);
    }

    /**
     * This is method a Vertx message consumer and KeyValue deleter
     *
     * @param request - id of the KeyValue
     * @return - a LAA with the Answer containing KeyValue id as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.KEY_VALUE_DEL)
    public Uni<Answer> delete(Request<UUID> request) {
        //noinspection DuplicatedCode
        return keyValueDataService
                .delete(request.payload())
                .map(this::apiResponseOkAnswer)
                .flatMap(answer -> keyValueCacheProvider.invalidateByKey(request.payload(), answer));
    }

    /**
     * This is method a Vertx message consumer and KeyValue provider by id
     *
     * @param request - id of the KeyValue
     * @return - a lazy asynchronous action with the Answer containing the KeyValue as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.KEY_VALUE_GET)
    public Uni<Answer> get(Request<UUID> request) {
        //noinspection DuplicatedCode
        return keyValueCacheProvider
                .get(request.payload())
                .map(Answer::of);
    }

    /**
     * The method provides the Answer's flow with all entries of KeyValue
     *
     * @return - the Answer's Multi-flow with all entries of KeyValue
     */
    public Multi<Answer> getAll() {
        //noinspection DuplicatedCode
        return keyValueDataService
                .getAll()
                .map(Answer::of);
    }

    @ExceptionBadRequestAnswer
    @ConsumeEvent(EventAddress.KEY_VALUE_PAGE)
    public Uni<Page<Answer>> getPage(Request<PageRequest> request) {
        //noinspection DuplicatedCode
        return keyValueCacheProvider.getPage(request.payload());
    }

    /**
     * This is method a Vertx message consumer and KeyValue updater
     *
     * @param request - KeyValue
     * @return - a LAA with the Answer containing KeyValue id as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionDuplicateAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.KEY_VALUE_PUT)
    public Uni<Answer> put(Request<KeyValue> request) {
        //noinspection DuplicatedCode
        return keyValueDataService
                .put(request.payload())
                .map(this::apiResponseAcceptedAnswer)
                .flatMap(answer -> keyValueCacheProvider.invalidateByKey(request.payload().id(), answer));
    }
}
