/*
 * This file was last modified at 2023.01.07 11:52 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * VocabularyService.java
 * $Id$
 */

package su.svn.daybook.services.models;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.models.domain.Vocabulary;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.ExceptionAnswerService;
import su.svn.daybook.services.cache.VocabularyCacheProvider;
import su.svn.daybook.services.domain.VocabularyDataService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class VocabularyService extends AbstractService<Long, Vocabulary> {

    private static final Logger LOG = Logger.getLogger(VocabularyService.class);

    @Inject
    ExceptionAnswerService exceptionAnswerService;

    @Inject
    VocabularyCacheProvider vocabularyCacheProvider;

    @Inject
    VocabularyDataService vocabularyDataService;

    /**
     * This is method a Vertx message consumer and Vocabulary creater
     *
     * @param o - Vocabulary
     * @return - a lazy asynchronous action (LAA) with the Answer containing the Vocabulary id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.VOCABULARY_ADD)
    public Uni<Answer> add(Vocabulary o) {
        //noinspection DuplicatedCode
        LOG.tracef("add(%s)", o);
        return vocabularyDataService
                .add(o)
                .map(this::apiResponseCreatedAnswer)
                .flatMap(vocabularyCacheProvider::invalidate)
                .onFailure(exceptionAnswerService::testDuplicateException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);

    }

    /**
     * This is method a Vertx message consumer and Vocabulary deleter
     *
     * @param id - id of the Vocabulary
     * @return - a LAA with the Answer containing Vocabulary id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.VOCABULARY_DEL)
    public Uni<Answer> delete(Long id) {
        //noinspection DuplicatedCode
        LOG.tracef("delete(%s)", id);
        return vocabularyDataService
                .delete(id)
                .map(this::apiResponseOkAnswer)
                .flatMap(answer -> vocabularyCacheProvider.invalidateById(id, answer))
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * This is method a Vertx message consumer and Vocabulary provider by id
     *
     * @param id - id of the Vocabulary
     * @return - a lazy asynchronous action with the Answer containing the Vocabulary as payload or empty payload
     */
    @ConsumeEvent(EventAddress.VOCABULARY_GET)
    public Uni<Answer> get(Long id) {
        //noinspection DuplicatedCode
        LOG.tracef("get(%s)", id);
        return vocabularyCacheProvider
                .get(id)
                .map(Answer::of)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * The method provides the Answer's flow with all entries of Vocabulary
     *
     * @return - the Answer's Multi-flow with all entries of Vocabulary
     */
    public Multi<Answer> getAll() {
        //noinspection DuplicatedCode
        LOG.trace("getAll()");
        return vocabularyDataService
                .getAll()
                .map(Answer::of);
    }

    @ConsumeEvent(EventAddress.VOCABULARY_PAGE)
    public Uni<Page<Answer>> getPage(PageRequest pageRequest) {
        //noinspection DuplicatedCode
        LOG.tracef("getPage(%s)", pageRequest);
        return vocabularyCacheProvider.getPage(pageRequest);
    }

    /**
     * This is method a Vertx message consumer and Vocabulary updater
     *
     * @param o - Vocabulary
     * @return - a LAA with the Answer containing Vocabulary id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.VOCABULARY_PUT)
    public Uni<Answer> put(Vocabulary o) {
        //noinspection DuplicatedCode
        LOG.tracef("put(%s)", o);
        return vocabularyDataService
                .put(o)
                .map(this::apiResponseAcceptedAnswer)
                .flatMap(answer -> vocabularyCacheProvider.invalidateById(o.getId(), answer))
                .onFailure(exceptionAnswerService::testDuplicateException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateAnswer)
                .onFailure(exceptionAnswerService::testIllegalArgumentException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer);
    }
}
