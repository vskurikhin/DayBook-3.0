/*
 * This file was last modified at 2024-10-30 09:48 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * CodifierService.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.services.models;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import su.svn.daybook3.api.gateway.annotations.ExceptionBadRequestAnswer;
import su.svn.daybook3.api.gateway.annotations.ExceptionDuplicateAnswer;
import su.svn.daybook3.api.gateway.annotations.ExceptionNoSuchElementAnswer;
import su.svn.daybook3.api.gateway.annotations.PrincipalLogging;
import su.svn.daybook3.api.gateway.domain.enums.EventAddress;
import su.svn.daybook3.api.gateway.models.domain.Codifier;
import su.svn.daybook3.api.gateway.models.pagination.Page;
import su.svn.daybook3.api.gateway.models.pagination.PageRequest;
import su.svn.daybook3.api.gateway.services.cache.CodifierCacheProvider;
import su.svn.daybook3.api.gateway.services.domain.CodifierDataService;
import su.svn.daybook3.domain.messages.Answer;
import su.svn.daybook3.domain.messages.Request;

@PrincipalLogging
@ApplicationScoped
public class CodifierService
        extends AbstractService<String, Codifier>
        implements MultiAnswerAllService {

    @Inject
    CodifierCacheProvider codifierCacheProvider;

    @Inject
    CodifierDataService codifierDataService;

    /**
     * This is method a Vertx message consumer and Codifier creater
     *
     * @param request - Codifier
     * @return - a lazy asynchronous action (LAA) with the Answer containing the Codifier code as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionDuplicateAnswer
    @ConsumeEvent(EventAddress.CODIFIER_ADD)
    public Uni<Answer> add(Request<Codifier> request) {
        //noinspection DuplicatedCode
        return codifierDataService
                .add(request.payload())
                .map(this::apiResponseCreatedAnswer)
                .flatMap(codifierCacheProvider::invalidate);
    }

    /**
     * This is method a Vertx message consumer and Codifier deleter
     *
     * @param request - code of the Codifier
     * @return - a LAA with the Answer containing Codifier code as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.CODIFIER_DEL)
    public Uni<Answer> delete(Request<String> request) {
        //noinspection DuplicatedCode
        return codifierDataService
                .delete(request.payload())
                .map(this::apiResponseOkAnswer)
                .flatMap(answer -> codifierCacheProvider.invalidateByKey(request.payload(), answer));
    }

    /**
     * This is method a Vertx message consumer and Codifier provider by code
     *
     * @param request - code of the Codifier
     * @return - a lazy asynchronous action with the Answer containing the Codifier as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.CODIFIER_GET)
    public Uni<Answer> get(Request<String> request) {
        //noinspection DuplicatedCode
        return codifierCacheProvider
                .get(request.payload())
                .map(Answer::of);
    }

    /**
     * The method provides the Answer's flow with all entries of Codifier
     *
     * @return - the Answer's Multi-flow with all entries of Codifier
     */
    @Override
    public Multi<Answer> getAll() {
        //noinspection DuplicatedCode
        return codifierDataService
                .getAll()
                .map(Answer::of);
    }

    @ExceptionBadRequestAnswer
    @ConsumeEvent(EventAddress.CODIFIER_PAGE)
    public Uni<Page<Answer>> getPage(Request<PageRequest> request) {
        //noinspection DuplicatedCode
        return codifierCacheProvider.getPage(request.payload());
    }

    /**
     * This is method a Vertx message consumer and Codifier updater
     *
     * @param request - Codifier
     * @return - a LAA with the Answer containing Codifier code as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionDuplicateAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.CODIFIER_PUT)
    public Uni<Answer> put(Request<Codifier> request) {
        //noinspection DuplicatedCode
        return codifierDataService
                .put(request.payload())
                .map(this::apiResponseAcceptedAnswer)
                .flatMap(answer -> codifierCacheProvider.invalidateByKey(request.payload().id(), answer));
    }
}
