/*
 * This file was last modified at 2024-10-30 17:27 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * KeyValueService.java
 * $Id$
 */

package su.svn.daybook3.api.services.models;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import su.svn.daybook3.api.annotations.ExceptionBadRequestAnswer;
import su.svn.daybook3.api.annotations.ExceptionDuplicateAnswer;
import su.svn.daybook3.api.annotations.ExceptionNoSuchElementAnswer;
import su.svn.daybook3.api.annotations.PrincipalLogging;
import su.svn.daybook3.api.domain.enums.EventAddress;
import su.svn.daybook3.api.models.domain.KeyValue;
import su.svn.daybook3.api.models.pagination.Page;
import su.svn.daybook3.api.models.pagination.PageRequest;
import su.svn.daybook3.api.services.cache.KeyValueCacheProvider;
import su.svn.daybook3.api.services.domain.KeyValueDataService;
import su.svn.daybook3.domain.messages.Answer;
import su.svn.daybook3.domain.messages.Request;

import java.util.UUID;

@PrincipalLogging
@ApplicationScoped
public class KeyValueService
        extends AbstractService<UUID, KeyValue>
        implements MultiAnswerAllService {

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
    @Override
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
