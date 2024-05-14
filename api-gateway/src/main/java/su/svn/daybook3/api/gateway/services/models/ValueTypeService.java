/*
 * This file was last modified at 2024-05-14 23:10 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * ValueTypeService.java
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
import su.svn.daybook3.api.gateway.models.domain.ValueType;
import su.svn.daybook3.api.gateway.models.pagination.Page;
import su.svn.daybook3.api.gateway.models.pagination.PageRequest;
import su.svn.daybook3.api.gateway.services.cache.ValueTypeCacheProvider;
import su.svn.daybook3.api.gateway.services.domain.ValueTypeDataService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@PrincipalLogging
@ApplicationScoped
public class ValueTypeService extends AbstractService<Long, ValueType> {

    @Inject
    ValueTypeCacheProvider valueTypeCacheProvider;

    @Inject
    ValueTypeDataService valueTypeDataService;

    /**
     * This is method a Vertx message consumer and ValueType creater
     *
     * @param request - ValueType
     * @return - a lazy asynchronous action (LAA) with the Answer containing the ValueType id as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionDuplicateAnswer
    @ConsumeEvent(EventAddress.VALUE_TYPE_ADD)
    public Uni<Answer> add(Request<ValueType> request) {
        //noinspection DuplicatedCode
        return valueTypeDataService
                .add(request.payload())
                .map(this::apiResponseCreatedAnswer)
                .flatMap(valueTypeCacheProvider::invalidate);
    }

    /**
     * This is method a Vertx message consumer and ValueType deleter
     *
     * @param request - id of the ValueType
     * @return - a LAA with the Answer containing ValueType id as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.VALUE_TYPE_DEL)
    public Uni<Answer> delete(Request<Long> request) {
        //noinspection DuplicatedCode
        return valueTypeDataService
                .delete(request.payload())
                .map(this::apiResponseOkAnswer)
                .flatMap(answer -> valueTypeCacheProvider.invalidateByKey(request.payload(), answer));
    }

    /**
     * This is method a Vertx message consumer and ValueType provider by id
     *
     * @param request - id of the ValueType
     * @return - a lazy asynchronous action with the Answer containing the ValueType as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.VALUE_TYPE_GET)
    public Uni<Answer> get(Request<Long> request) {
        //noinspection DuplicatedCode
        return valueTypeCacheProvider
                .get(request.payload())
                .map(Answer::of);
    }

    /**
     * The method provides the Answer's flow with all entries of ValueType
     *
     * @return - the Answer's Multi-flow with all entries of ValueType
     */
    public Multi<Answer> getAll() {
        //noinspection DuplicatedCode
        return valueTypeDataService
                .getAll()
                .map(Answer::of);
    }

    @ExceptionBadRequestAnswer
    @ConsumeEvent(EventAddress.VALUE_TYPE_PAGE)
    public Uni<Page<Answer>> getPage(Request<PageRequest> request) {
        //noinspection DuplicatedCode
        return valueTypeCacheProvider.getPage(request.payload());
    }

    /**
     * This is method a Vertx message consumer and ValueType updater
     *
     * @param request - ValueType
     * @return - a LAA with the Answer containing ValueType id as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionDuplicateAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.VALUE_TYPE_PUT)
    public Uni<Answer> put(Request<ValueType> request) {
        //noinspection DuplicatedCode
        return valueTypeDataService
                .put(request.payload())
                .map(this::apiResponseAcceptedAnswer)
                .flatMap(answer -> valueTypeCacheProvider.invalidateByKey(request.payload().id(), answer));
    }
}
