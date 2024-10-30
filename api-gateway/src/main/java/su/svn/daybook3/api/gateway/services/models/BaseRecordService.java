/*
 * This file was last modified at 2024-10-30 09:48 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * BaseRecordService.java
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
import su.svn.daybook3.api.gateway.models.dto.ResourceBaseRecord;
import su.svn.daybook3.api.gateway.models.pagination.Page;
import su.svn.daybook3.api.gateway.models.pagination.PageRequest;
import su.svn.daybook3.api.gateway.services.cache.BaseRecordCacheProvider;
import su.svn.daybook3.api.gateway.services.domain.BaseRecordDataService;
import su.svn.daybook3.domain.messages.Answer;
import su.svn.daybook3.domain.messages.Request;

import java.util.UUID;

@PrincipalLogging
@ApplicationScoped
public class BaseRecordService
        extends AbstractService<UUID, ResourceBaseRecord> {

    @Inject
    BaseRecordCacheProvider baseRecordCacheProvider;

    @Inject
    BaseRecordDataService baseRecordDataService;

    /**
     * This is method a Panache got ResourceBaseRecord creater
     *
     * @param request - ResponseBaseRecord
     * @return - a lazy asynchronous action (LAA) with the Answer containing the ResponseBaseRecord id as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionDuplicateAnswer
    public Uni<Answer> add(Request<ResourceBaseRecord> request) {
        //noinspection DuplicatedCode
        return baseRecordDataService
                .add(request.payload())
                .map(this::apiResponseCreatedAnswer)
                .flatMap(baseRecordCacheProvider::invalidate);
    }

    /**
     * This is method a Panache got ResourceBaseRecord deleter
     *
     * @param request - id of the ResourceBaseRecord
     * @return - a LAA with the Answer containing ResponseBaseRecord id as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionNoSuchElementAnswer
    public Uni<Answer> delete(Request<UUID> request) {
        //noinspection DuplicatedCode
        return baseRecordDataService
                .delete(request.payload())
                .map(this::apiResponseOkAnswer)
                .flatMap(answer -> baseRecordCacheProvider.invalidateByKey(request.payload(), answer));
    }

    /**
     * This is method a Panache got ResourceBaseRecord provider by id
     *
     * @param request - id of the ResourceBaseRecord
     * @return - a lazy asynchronous action with the Answer containing the ResponseBaseRecord as payload or empty payload
     */
    @WithSession
    @ExceptionBadRequestAnswer
    @ExceptionNoSuchElementAnswer
    public Uni<Answer> get(Request<UUID> request) {
        //noinspection DuplicatedCode
        return baseRecordCacheProvider
                .get(request.payload())
                .map(Answer::of);
    }

    @ExceptionBadRequestAnswer
    public Uni<Page<Answer>> getPage(Request<PageRequest> request) {
        return baseRecordCacheProvider.getPage(request.payload());
    }

    /**
     * This is method a Panache got ResourceBaseRecord updater
     *
     * @param request - ResponseBaseRecord
     * @return - a LAA with the Answer containing ResourceBaseRecord id as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionDuplicateAnswer
    @ExceptionNoSuchElementAnswer
    public Uni<Answer> put(Request<ResourceBaseRecord> request) {
        //noinspection DuplicatedCode
        return baseRecordDataService
                .put(request.payload())
                .map(this::apiResponseAcceptedAnswer)
                .flatMap(answer -> baseRecordCacheProvider.invalidateByKey(request.payload().id(), answer));
    }
}
