/*
 * This file was last modified at 2023.09.07 16:35 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * ValueTypeService.java
 * $Id$
 */

package su.svn.daybook.services.models;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import su.svn.daybook.annotations.ExceptionBadRequestAnswer;
import su.svn.daybook.annotations.ExceptionDuplicateAnswer;
import su.svn.daybook.annotations.ExceptionNoSuchElementAnswer;
import su.svn.daybook.annotations.PrincipalLogging;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.messages.Request;
import su.svn.daybook.models.domain.ValueType;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.cache.ValueTypeCacheProvider;
import su.svn.daybook.services.domain.ValueTypeDataService;

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
