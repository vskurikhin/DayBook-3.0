/*
 * This file was last modified at 2024-10-30 09:48 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * JsonRecordService.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.services.models;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import su.svn.daybook3.api.gateway.annotations.ExceptionBadRequestAnswer;
import su.svn.daybook3.api.gateway.annotations.ExceptionDuplicateAnswer;
import su.svn.daybook3.api.gateway.annotations.ExceptionNoSuchElementAnswer;
import su.svn.daybook3.api.gateway.annotations.PrincipalLogging;
import su.svn.daybook3.api.gateway.models.dto.ResourceJsonRecord;
import su.svn.daybook3.api.gateway.models.pagination.Page;
import su.svn.daybook3.api.gateway.models.pagination.PageRequest;
import su.svn.daybook3.api.gateway.services.cache.JsonRecordCacheProvider;
import su.svn.daybook3.api.gateway.services.domain.JsonRecordDataService;
import su.svn.daybook3.domain.messages.Answer;
import su.svn.daybook3.domain.messages.Request;

import java.util.UUID;

@PrincipalLogging
@ApplicationScoped
public class JsonRecordService
        extends AbstractService<UUID, ResourceJsonRecord> {

    @Inject
    JsonRecordCacheProvider jsonRecordCacheProvider;

    @Inject
    JsonRecordDataService jsonRecordDataService;

    /**
     * This is method a Panache got ResourceJsonRecord creater
     *
     * @param request - ResponseJsonRecord
     * @return - a lazy asynchronous action (LAA) with the Answer containing the ResponseJsonRecord id as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionDuplicateAnswer
    public Uni<Answer> add(Request<ResourceJsonRecord> request) {
        //noinspection DuplicatedCode
        return jsonRecordDataService
                .add(request.payload())
                .map(this::apiResponseCreatedAnswer)
                .flatMap(jsonRecordCacheProvider::invalidate);
    }

    /**
     * This is method a Panache got ResourceJsonRecord deleter
     *
     * @param request - id of the ResourceJsonRecord
     * @return - a LAA with the Answer containing ResponseJsonRecord id as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionNoSuchElementAnswer
    public Uni<Answer> delete(Request<UUID> request) {
        return jsonRecordDataService
                .delete(request.payload())
                .map(this::apiResponseOkAnswer)
                .flatMap(answer1 -> jsonRecordCacheProvider
                        .invalidateByKey(request.payload(), answer1)
                        .flatMap(answer2 -> jsonRecordCacheProvider.invalidate(answer2))
                );
    }

    /**
     * This is method a Panache got ResourceJsonRecord provider by id
     *
     * @param request - id of the ResourceJsonRecord
     * @return - a lazy asynchronous action with the Answer containing the ResponseJsonRecord as payload or empty payload
     */
    @WithSession
    @ExceptionBadRequestAnswer
    @ExceptionNoSuchElementAnswer
    public Uni<Answer> get(Request<UUID> request) {
        //noinspection DuplicatedCode
        return jsonRecordCacheProvider
                .get(request.payload())
                .map(Answer::of);
    }

    @ExceptionBadRequestAnswer
    public Uni<Page<Answer>> getPage(Request<PageRequest> request) {
        return jsonRecordCacheProvider.getPage(request.payload());
    }

    /**
     * This is method a Panache got ResourceJsonRecord updater
     *
     * @param request - ResponseJsonRecord
     * @return - a LAA with the Answer containing ResourceJsonRecord id as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionDuplicateAnswer
    @ExceptionNoSuchElementAnswer
    public Uni<Answer> put(Request<ResourceJsonRecord> request) {
        return jsonRecordDataService
                .put(request.payload())
                .map(this::apiResponsePutAcceptedOrNotFoundAnswer)
                .flatMap(answer1 -> jsonRecordCacheProvider
                        .invalidateByKey(request.payload().id(), answer1)
                        .flatMap(answer2 -> jsonRecordCacheProvider.invalidate(answer2))
                );
    }
}
