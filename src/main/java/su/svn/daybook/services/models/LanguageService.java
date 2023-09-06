/*
 * This file was last modified at 2023.09.06 17:04 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * LanguageService.java
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
import su.svn.daybook.models.domain.Language;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.cache.LanguageCacheProvider;
import su.svn.daybook.services.domain.LanguageDataService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.UUID;

@PrincipalLogging
@ApplicationScoped
public class LanguageService extends AbstractService<Long, Language> {

    @Inject
    LanguageCacheProvider languageCacheProvider;

    @Inject
    LanguageDataService languageDataService;

    /**
     * This is method a Vertx message consumer and Language creater
     *
     * @param request - Language
     * @return - a lazy asynchronous action (LAA) with the Answer containing the Language id as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionDuplicateAnswer
    @ConsumeEvent(EventAddress.LANGUAGE_ADD)
    public Uni<Answer> add(Request<Language> request) {
        //noinspection DuplicatedCode
        return languageDataService
                .add(request.payload())
                .map(this::apiResponseCreatedAnswer)
                .flatMap(languageCacheProvider::invalidate);
    }

    /**
     * This is method a Vertx message consumer and Language deleter
     *
     * @param request - id of the Language
     * @return - a LAA with the Answer containing Language id as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.LANGUAGE_DEL)
    public Uni<Answer> delete(Request<Long> request) {
        //noinspection DuplicatedCode
        return languageDataService
                .delete(request.payload())
                .map(this::apiResponseOkAnswer)
                .flatMap(answer -> languageCacheProvider.invalidateByKey(request.payload(), answer));
    }

    /**
     * This is method a Vertx message consumer and Language provider by id
     *
     * @param request - id of the Language
     * @return - a lazy asynchronous action with the Answer containing the Language as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.LANGUAGE_GET)
    public Uni<Answer> get(Request<Long> request) {
        //noinspection DuplicatedCode
        return languageCacheProvider
                .get(request.payload())
                .map(Answer::of);
    }

    /**
     * The method provides the Answer's flow with all entries of Language
     *
     * @return - the Answer's Multi-flow with all entries of Language
     */
    public Multi<Answer> getAll() {
        //noinspection DuplicatedCode
        return languageDataService
                .getAll()
                .map(Answer::of);
    }

    @ExceptionBadRequestAnswer
    @ConsumeEvent(EventAddress.LANGUAGE_PAGE)
    public Uni<Page<Answer>> getPage(Request<PageRequest> request) {
        //noinspection DuplicatedCode
        return languageCacheProvider.getPage(request.payload());
    }

    /**
     * This is method a Vertx message consumer and Language updater
     *
     * @param request - Language
     * @return - a LAA with the Answer containing Language id as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionDuplicateAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.LANGUAGE_PUT)
    public Uni<Answer> put(Request<Language> request) {
        //noinspection DuplicatedCode
        return languageDataService
                .put(request.payload())
                .map(this::apiResponseAcceptedAnswer)
                .flatMap(answer -> languageCacheProvider.invalidateByKey(request.payload().id(), answer));
    }
}
