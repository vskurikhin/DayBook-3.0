/*
 * This file was last modified at 2024-10-30 09:55 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * WordService.java
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
import su.svn.daybook3.api.gateway.models.domain.Word;
import su.svn.daybook3.api.gateway.models.pagination.Page;
import su.svn.daybook3.api.gateway.models.pagination.PageRequest;
import su.svn.daybook3.api.gateway.services.cache.WordCacheProvider;
import su.svn.daybook3.api.gateway.services.domain.WordDataService;
import su.svn.daybook3.domain.messages.Answer;
import su.svn.daybook3.domain.messages.Request;

@PrincipalLogging
@ApplicationScoped
public class WordService
        extends AbstractService<String, Word>
        implements MultiAnswerAllService {

    @Inject
    WordCacheProvider wordCacheProvider;

    @Inject
    WordDataService wordDataService;

    /**
     * This is method a Vertx message consumer and Word creater
     *
     * @param request - Word
     * @return - a lazy asynchronous action (LAA) with the Answer containing the Word id as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionDuplicateAnswer
    @ConsumeEvent(EventAddress.WORD_ADD)
    public Uni<Answer> add(Request<Word> request) {
        return wordDataService
                .add(request.payload())
                .map(this::apiResponseCreatedAnswer)
                .flatMap(wordCacheProvider::invalidate);
    }

    /**
     * This is method a Vertx message consumer and Word deleter
     *
     * @param request - id of the Word
     * @return - a LAA with the Answer containing Word id as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.WORD_DEL)
    public Uni<Answer> delete(Request<String> request) {
        //noinspection DuplicatedCode
        return wordDataService
                .delete(request.payload())
                .map(this::apiResponseOkAnswer)
                .flatMap(answer -> wordCacheProvider.invalidateByKey(request.payload(), answer));
    }

    /**
     * This is method a Vertx message consumer and Word provider by id
     *
     * @param request - id of the Word
     * @return - a lazy asynchronous action with the Answer containing the Word as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.WORD_GET)
    public Uni<Answer> get(Request<String> request) {
        return wordCacheProvider
                .get(request.payload())
                .map(Answer::of);
    }

    /**
     * The method provides the Answer's flow with all entries of Word
     *
     * @return - the Answer's Multi-flow with all entries of Word
     */
    @Override
    public Multi<Answer> getAll() {
        return wordDataService
                .getAll()
                .map(Answer::of);
    }

    @ExceptionBadRequestAnswer
    @ConsumeEvent(value = EventAddress.WORD_PAGE)
    public Uni<Page<Answer>> getPage(Request<PageRequest> request) {
        //noinspection DuplicatedCode
        return wordCacheProvider.getPage(request.payload());
    }

    /**
     * This is method a Vertx message consumer and Word updater
     *
     * @param request - Word
     * @return - a LAA with the Answer containing Word id as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionDuplicateAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.WORD_PUT)
    public Uni<Answer> put(Request<Word> request) {
        //noinspection DuplicatedCode
        return wordDataService
                .put(request.payload())
                .map(this::apiResponseAcceptedAnswer)
                .flatMap(answer -> wordCacheProvider.invalidateByKey(request.payload().id(), answer));
    }
}
