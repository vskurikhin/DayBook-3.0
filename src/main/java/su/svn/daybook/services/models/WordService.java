/*
 * This file was last modified at 2023.01.09 21:44 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * WordService.java
 * $Id$
 */

package su.svn.daybook.services.models;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.annotations.Logged;
import su.svn.daybook.annotations.Principled;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.messages.Request;
import su.svn.daybook.models.domain.Word;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.ExceptionAnswerService;
import su.svn.daybook.services.cache.WordCacheProvider;
import su.svn.daybook.services.domain.WordDataService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped @Logged
public class WordService extends AbstractService<String, Word> {

    private static final Logger LOG = Logger.getLogger(WordService.class);

    @Inject
    ExceptionAnswerService exceptionAnswerService;

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
    @Principled
    @ConsumeEvent(EventAddress.WORD_ADD)
    public Uni<Answer> add(Request<Word> request) {
        return wordDataService
                .add(request.payload())
                .map(this::apiResponseCreatedAnswer)
                .flatMap(wordCacheProvider::invalidate)
                .onFailure(exceptionAnswerService::testDuplicateException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * This is method a Vertx message consumer and Word deleter
     *
     * @param request - id of the Word
     * @return - a LAA with the Answer containing Word id as payload or empty payload
     */
    @Principled
    @ConsumeEvent(EventAddress.WORD_DEL)
    public Uni<Answer> delete(Request<String> request) {
        //noinspection DuplicatedCode
        return wordDataService
                .delete(request.payload())
                .map(this::apiResponseOkAnswer)
                .flatMap(answer -> wordCacheProvider.invalidateByKey(request.payload(), answer))
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * This is method a Vertx message consumer and Word provider by id
     *
     * @param request - id of the Word
     * @return - a lazy asynchronous action with the Answer containing the Word as payload or empty payload
     */
    @Principled
    @ConsumeEvent(EventAddress.WORD_GET)
    public Uni<Answer> get(Request<String> request) {
        return wordCacheProvider
                .get(request.payload())
                .map(Answer::of)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * The method provides the Answer's flow with all entries of Word
     *
     * @return - the Answer's Multi-flow with all entries of Word
     */
    public Multi<Answer> getAll() {
        return wordDataService
                .getAll()
                .map(Answer::of);
    }

    @Principled
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
    @Principled
    @ConsumeEvent(EventAddress.WORD_PUT)
    public Uni<Answer> put(Request<Word> request) {
        //noinspection DuplicatedCode
        return wordDataService
                .put(request.payload())
                .map(this::apiResponseAcceptedAnswer)
                .flatMap(answer -> wordCacheProvider.invalidateByKey(request.payload().id(), answer))
                .onFailure(exceptionAnswerService::testDuplicateException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateAnswer)
                .onFailure(exceptionAnswerService::testIllegalArgumentException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer);
    }
}
