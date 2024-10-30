/*
 * This file was last modified at 2024-10-30 09:48 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * I18nService.java
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
import su.svn.daybook3.api.gateway.models.domain.I18n;
import su.svn.daybook3.api.gateway.models.pagination.Page;
import su.svn.daybook3.api.gateway.models.pagination.PageRequest;
import su.svn.daybook3.api.gateway.services.cache.I18nCacheProvider;
import su.svn.daybook3.api.gateway.services.domain.I18nDataService;
import su.svn.daybook3.domain.messages.Answer;
import su.svn.daybook3.domain.messages.Request;

@PrincipalLogging
@ApplicationScoped
public class I18nService
        extends AbstractService<Long, I18n>
        implements MultiAnswerAllService {

    @Inject
    I18nCacheProvider i18nCacheProvider;

    @Inject
    I18nDataService i18nDataService;

    /**
     * This is method a Vertx message consumer and I18n creater
     *
     * @param request - I18n
     * @return - a lazy asynchronous action (LAA) with the Answer containing the I18n id as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionDuplicateAnswer
    @ConsumeEvent(EventAddress.I18N_ADD)
    public Uni<Answer> add(Request<I18n> request) {
        //noinspection DuplicatedCode
        return i18nDataService
                .add(request.payload())
                .map(this::apiResponseCreatedAnswer)
                .flatMap(i18nCacheProvider::invalidate);
    }

    /**
     * This is method a Vertx message consumer and I18n deleter
     *
     * @param request - id of the I18n
     * @return - a LAA with the Answer containing I18n id as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.I18N_DEL)
    public Uni<Answer> delete(Request<Long> request) {
        //noinspection DuplicatedCode
        return i18nDataService
                .delete(request.payload())
                .map(this::apiResponseOkAnswer)
                .flatMap(answer -> i18nCacheProvider.invalidateByKey(request.payload(), answer));
    }

    /**
     * This is method a Vertx message consumer and I18n provider by id
     *
     * @param request - id of the I18n
     * @return - a lazy asynchronous action with the Answer containing the I18n as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.I18N_GET)
    public Uni<Answer> get(Request<Long> request) {
        //noinspection DuplicatedCode
        return i18nCacheProvider
                .get(request.payload())
                .map(Answer::of);
    }

    /**
     * The method provides the Answer's flow with all entries of I18n
     *
     * @return - the Answer's Multi-flow with all entries of I18n
     */
    @Override
    public Multi<Answer> getAll() {
        //noinspection DuplicatedCode
        return i18nDataService
                .getAll()
                .map(Answer::of);
    }

    @ExceptionBadRequestAnswer
    @ConsumeEvent(EventAddress.I18N_PAGE)
    public Uni<Page<Answer>> getPage(Request<PageRequest> request) {
        //noinspection DuplicatedCode
        return i18nCacheProvider.getPage(request.payload());
    }

    /**
     * This is method a Vertx message consumer and I18n updater
     *
     * @param request - I18n
     * @return - a LAA with the Answer containing I18n id as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionDuplicateAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.I18N_PUT)
    public Uni<Answer> put(Request<I18n> request) {
        //noinspection DuplicatedCode
        return i18nDataService
                .put(request.payload())
                .map(this::apiResponseAcceptedAnswer)
                .flatMap(answer -> i18nCacheProvider.invalidateByKey(request.payload().id(), answer));
    }
}
