/*
 * This file was last modified at 2023.01.07 11:52 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * WordService.java
 * $Id$
 */

package su.svn.daybook.services.models;

import io.quarkus.cache.CacheKey;
import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.models.domain.Word;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.ExceptionAnswerService;
import su.svn.daybook.services.cache.WordCacheProvider;
import su.svn.daybook.services.domain.WordDataService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
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
     * @param o - Word
     * @return - a lazy asynchronous action (LAA) with the Answer containing the Word id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.WORD_ADD)
    public Uni<Answer> add(Word o) {
        LOG.tracef("add(%s)", o);
        return wordDataService
                .add(o)
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
     * @param id - id of the Word
     * @return - a LAA with the Answer containing Word id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.WORD_DEL)
    public Uni<Answer> delete(String id) {
        //noinspection DuplicatedCode
        LOG.tracef("delete(%s)", id);
        return wordDataService
                .delete(id)
                .map(this::apiResponseOkAnswer)
                .flatMap(answer -> wordCacheProvider.invalidateById(id, answer))
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * This is method a Vertx message consumer and Word provider by id
     *
     * @param id - id of the Word
     * @return - a lazy asynchronous action with the Answer containing the Word as payload or empty payload
     */
    @ConsumeEvent(EventAddress.WORD_GET)
    public Uni<Answer> get(String id) {
        LOG.tracef("get(%s)", id);
        return wordCacheProvider
                .get(id)
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
        LOG.trace("getAll");
        return wordDataService
                .getAll()
                .map(Answer::of);
    }

    @ConsumeEvent(value = EventAddress.WORD_PAGE)
    public Uni<Page<Answer>> getPage(@CacheKey PageRequest pageRequest) {
        //noinspection DuplicatedCode
        LOG.tracef("getPage(%s)", pageRequest);
        return wordCacheProvider.getPage(pageRequest);
    }

    /**
     * This is method a Vertx message consumer and Word updater
     *
     * @param o - Word
     * @return - a LAA with the Answer containing Word id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.WORD_PUT)
    public Uni<Answer> put(Word o) {
        //noinspection DuplicatedCode
        LOG.tracef("put(%s)", o);
        return wordDataService
                .put(o)
                .map(this::apiResponseAcceptedAnswer)
                .flatMap(answer -> wordCacheProvider.invalidateById(o.getId(), answer))
                .onFailure(exceptionAnswerService::testDuplicateException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateAnswer)
                .onFailure(exceptionAnswerService::testIllegalArgumentException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer);
    }
}
