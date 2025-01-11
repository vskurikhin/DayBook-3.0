/*
 * This file was last modified at 2024-10-30 17:27 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * VocabularyService.java
 * $Id$
 */

package su.svn.daybook3.api.services.models;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import su.svn.daybook3.api.annotations.ExceptionBadRequestAnswer;
import su.svn.daybook3.api.annotations.ExceptionDuplicateAnswer;
import su.svn.daybook3.api.annotations.ExceptionNoSuchElementAnswer;
import su.svn.daybook3.api.annotations.PrincipalLogging;
import su.svn.daybook3.api.domain.enums.EventAddress;
import su.svn.daybook3.api.models.domain.Vocabulary;
import su.svn.daybook3.api.models.pagination.Page;
import su.svn.daybook3.api.models.pagination.PageRequest;
import su.svn.daybook3.api.services.cache.VocabularyCacheProvider;
import su.svn.daybook3.api.services.domain.VocabularyDataService;
import su.svn.daybook3.domain.messages.Answer;
import su.svn.daybook3.domain.messages.Request;

@PrincipalLogging
@ApplicationScoped
public class VocabularyService
        extends AbstractService<Long, Vocabulary>
        implements MultiAnswerAllService {

    @Inject
    VocabularyCacheProvider vocabularyCacheProvider;

    @Inject
    VocabularyDataService vocabularyDataService;

    /**
     * This is method a Vertx message consumer and Vocabulary creater
     *
     * @param request - Vocabulary
     * @return - a lazy asynchronous action (LAA) with the Answer containing the Vocabulary id as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionDuplicateAnswer
    @ConsumeEvent(EventAddress.VOCABULARY_ADD)
    public Uni<Answer> add(Request<Vocabulary> request) {
        //noinspection DuplicatedCode
        return vocabularyDataService
                .add(request.payload())
                .map(this::apiResponseCreatedAnswer)
                .flatMap(vocabularyCacheProvider::invalidate);
    }

    /**
     * This is method a Vertx message consumer and Vocabulary deleter
     *
     * @param request - id of the Vocabulary
     * @return - a LAA with the Answer containing Vocabulary id as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.VOCABULARY_DEL)
    public Uni<Answer> delete(Request<Long> request) {
        //noinspection DuplicatedCode
        return vocabularyDataService
                .delete(request.payload())
                .map(this::apiResponseOkAnswer)
                .flatMap(answer -> vocabularyCacheProvider.invalidateByKey(request.payload(), answer));
    }

    /**
     * This is method a Vertx message consumer and Vocabulary provider by id
     *
     * @param request - id of the Vocabulary
     * @return - a lazy asynchronous action with the Answer containing the Vocabulary as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.VOCABULARY_GET)
    public Uni<Answer> get(Request<Long> request) {
        //noinspection DuplicatedCode
        return vocabularyCacheProvider
                .get(request.payload())
                .map(Answer::of);
    }

    /**
     * The method provides the Answer's flow with all entries of Vocabulary
     *
     * @return - the Answer's Multi-flow with all entries of Vocabulary
     */
    @Override
    public Multi<Answer> getAll() {
        //noinspection DuplicatedCode
        return vocabularyDataService
                .getAll()
                .map(Answer::of);
    }

    @ExceptionBadRequestAnswer
    @ConsumeEvent(EventAddress.VOCABULARY_PAGE)
    public Uni<Page<Answer>> getPage(Request<PageRequest> request) {
        //noinspection DuplicatedCode
        return vocabularyCacheProvider.getPage(request.payload());
    }

    /**
     * This is method a Vertx message consumer and Vocabulary updater
     *
     * @param request - Vocabulary
     * @return - a LAA with the Answer containing Vocabulary id as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionDuplicateAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.VOCABULARY_PUT)
    public Uni<Answer> put(Request<Vocabulary> request) {
        //noinspection DuplicatedCode
        return vocabularyDataService
                .put(request.payload())
                .map(this::apiResponseAcceptedAnswer)
                .flatMap(answer -> vocabularyCacheProvider.invalidateByKey(request.payload().id(), answer));
    }
}
