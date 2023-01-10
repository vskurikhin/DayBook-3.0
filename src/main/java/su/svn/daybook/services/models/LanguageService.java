/*
 * This file was last modified at 2023.01.09 21:44 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * LanguageService.java
 * $Id$
 */

package su.svn.daybook.services.models;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.models.domain.Language;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.ExceptionAnswerService;
import su.svn.daybook.services.cache.LanguageCacheProvider;
import su.svn.daybook.services.domain.LanguageDataService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class LanguageService extends AbstractService<Long, Language> {

    private static final Logger LOG = Logger.getLogger(LanguageService.class);

    @Inject
    ExceptionAnswerService exceptionAnswerService;

    @Inject
    LanguageCacheProvider languageCacheProvider;

    @Inject
    LanguageDataService languageDataService;

    /**
     * This is method a Vertx message consumer and Language creater
     *
     * @param o - Language
     * @return - a lazy asynchronous action (LAA) with the Answer containing the Language id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.LANGUAGE_ADD)
    public Uni<Answer> add(Language o) {
        //noinspection DuplicatedCode
        LOG.tracef("add(%s)", o);
        return languageDataService
                .add(o)
                .map(this::apiResponseCreatedAnswer)
                .flatMap(languageCacheProvider::invalidate)
                .onFailure(exceptionAnswerService::testDuplicateException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);

    }

    /**
     * This is method a Vertx message consumer and Language deleter
     *
     * @param id - id of the Language
     * @return - a LAA with the Answer containing Language id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.LANGUAGE_DEL)
    public Uni<Answer> delete(Long id) {
        //noinspection DuplicatedCode
        LOG.tracef("delete(%s)", id);
        return languageDataService
                .delete(id)
                .map(this::apiResponseOkAnswer)
                .flatMap(answer -> languageCacheProvider.invalidateById(id, answer))
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * This is method a Vertx message consumer and Language provider by id
     *
     * @param id - id of the Language
     * @return - a lazy asynchronous action with the Answer containing the Language as payload or empty payload
     */
    @ConsumeEvent(EventAddress.LANGUAGE_GET)
    public Uni<Answer> get(Long id) {
        //noinspection DuplicatedCode
        LOG.tracef("get(%s)", id);
        return languageCacheProvider
                .get(id)
                .map(Answer::of)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * The method provides the Answer's flow with all entries of Language
     *
     * @return - the Answer's Multi-flow with all entries of Language
     */
    public Multi<Answer> getAll() {
        //noinspection DuplicatedCode
        LOG.trace("getAll()");
        return languageDataService
                .getAll()
                .map(Answer::of);
    }

    @ConsumeEvent(EventAddress.LANGUAGE_PAGE)
    public Uni<Page<Answer>> getPage(PageRequest pageRequest) {
        //noinspection DuplicatedCode
        LOG.tracef("getPage(%s)", pageRequest);
        return languageCacheProvider.getPage(pageRequest);
    }

    /**
     * This is method a Vertx message consumer and Language updater
     *
     * @param o - Language
     * @return - a LAA with the Answer containing Language id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.LANGUAGE_PUT)
    public Uni<Answer> put(Language o) {
        //noinspection DuplicatedCode
        LOG.tracef("put(%s)", o);
        return languageDataService
                .put(o)
                .map(this::apiResponseAcceptedAnswer)
                .flatMap(answer -> languageCacheProvider.invalidateById(o.getId(), answer))
                .onFailure(exceptionAnswerService::testDuplicateException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateAnswer)
                .onFailure(exceptionAnswerService::testIllegalArgumentException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer);
    }
}
