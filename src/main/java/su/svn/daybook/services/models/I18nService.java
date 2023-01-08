/*
 * This file was last modified at 2023.01.07 11:52 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * I18nService.java
 * $Id$
 */

package su.svn.daybook.services.models;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.models.domain.I18n;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.ExceptionAnswerService;
import su.svn.daybook.services.cache.I18nCacheProvider;
import su.svn.daybook.services.domain.I18nDataService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class I18nService extends AbstractService<Long, I18n> {

    private static final Logger LOG = Logger.getLogger(I18nService.class);

    @Inject
    ExceptionAnswerService exceptionAnswerService;

    @Inject
    I18nCacheProvider i18nCacheProvider;

    @Inject
    I18nDataService i18nDataService;

    /**
     * This is method a Vertx message consumer and I18n creater
     *
     * @param o - I18n
     * @return - a lazy asynchronous action (LAA) with the Answer containing the I18n id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.I18N_ADD)
    public Uni<Answer> add(I18n o) {
        //noinspection DuplicatedCode
        LOG.tracef("add(%s)", o);
        return i18nDataService
                .add(o)
                .map(this::apiResponseCreatedAnswer)
                .flatMap(i18nCacheProvider::invalidate)
                .onFailure(exceptionAnswerService::testDuplicateException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);

    }

    /**
     * This is method a Vertx message consumer and I18n deleter
     *
     * @param id - id of the I18n
     * @return - a LAA with the Answer containing I18n id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.I18N_DEL)
    public Uni<Answer> delete(Long id) {
        //noinspection DuplicatedCode
        LOG.tracef("delete(%s)", id);
        return i18nDataService
                .delete(id)
                .map(this::apiResponseOkAnswer)
                .flatMap(answer -> i18nCacheProvider.invalidateById(id, answer))
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * This is method a Vertx message consumer and I18n provider by id
     *
     * @param o - id of the I18n
     * @return - a lazy asynchronous action with the Answer containing the I18n as payload or empty payload
     */
    @ConsumeEvent(EventAddress.I18N_GET)
    public Uni<Answer> get(Long id) {
        //noinspection DuplicatedCode
        LOG.tracef("get(%s)", id);
        return i18nCacheProvider
                .get(id)
                .map(Answer::of)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * The method provides the Answer's flow with all entries of I18n
     *
     * @return - the Answer's Multi-flow with all entries of I18n
     */
    public Multi<Answer> getAll() {
        //noinspection DuplicatedCode
        LOG.trace("getAll()");
        return i18nDataService
                .getAll()
                .map(Answer::of);
    }

    @ConsumeEvent(EventAddress.I18N_PAGE)
    public Uni<Page<Answer>> getPage(PageRequest pageRequest) {
        //noinspection DuplicatedCode
        LOG.tracef("getPage(%s)", pageRequest);
        return i18nCacheProvider.getPage(pageRequest);
    }

    /**
     * This is method a Vertx message consumer and I18n updater
     *
     * @param o - I18n
     * @return - a LAA with the Answer containing I18n id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.I18N_PUT)
    public Uni<Answer> put(I18n o) {
        //noinspection DuplicatedCode
        LOG.tracef("put(%s)", o);
        return i18nDataService
                .put(o)
                .map(this::apiResponseAcceptedAnswer)
                .flatMap(answer -> i18nCacheProvider.invalidateById(o.getId(), answer))
                .onFailure(exceptionAnswerService::testDuplicateException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateAnswer)
                .onFailure(exceptionAnswerService::testIllegalArgumentException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer);
    }
}
