/*
 * This file was last modified at 2023.01.09 21:44 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * CodifierService.java
 * $Id$
 */

package su.svn.daybook.services.models;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.models.domain.Codifier;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.ExceptionAnswerService;
import su.svn.daybook.services.cache.CodifierCacheProvider;
import su.svn.daybook.services.domain.CodifierDataService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class CodifierService extends AbstractService<String, Codifier> {

    private static final Logger LOG = Logger.getLogger(CodifierService.class);

    @Inject
    ExceptionAnswerService exceptionAnswerService;

    @Inject
    CodifierCacheProvider codifierCacheProvider;

    @Inject
    CodifierDataService codifierDataService;

    /**
     * This is method a Vertx message consumer and Codifier creater
     *
     * @param o - Codifier
     * @return - a lazy asynchronous action (LAA) with the Answer containing the Codifier id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.CODIFIER_ADD)
    public Uni<Answer> add(Codifier o) {
        //noinspection DuplicatedCode
        LOG.tracef("add(%s)", o);
        return codifierDataService
                .add(o)
                .map(this::apiResponseCreatedAnswer)
                .flatMap(codifierCacheProvider::invalidate)
                .onFailure(exceptionAnswerService::testDuplicateException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);

    }

    /**
     * This is method a Vertx message consumer and Codifier deleter
     *
     * @param id - id of the Codifier
     * @return - a LAA with the Answer containing Codifier id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.CODIFIER_DEL)
    public Uni<Answer> delete(String id) {
        //noinspection DuplicatedCode
        LOG.tracef("delete(%s)", id);
        return codifierDataService
                .delete(id)
                .map(this::apiResponseOkAnswer)
                .flatMap(answer -> codifierCacheProvider.invalidateById(id, answer))
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * This is method a Vertx message consumer and Codifier provider by id
     *
     * @param id - id of the Codifier
     * @return - a lazy asynchronous action with the Answer containing the Codifier as payload or empty payload
     */
    @ConsumeEvent(EventAddress.CODIFIER_GET)
    public Uni<Answer> get(String id) {
        //noinspection DuplicatedCode
        LOG.tracef("get(%s)", id);
        return codifierCacheProvider
                .get(id)
                .map(Answer::of)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * The method provides the Answer's flow with all entries of Codifier
     *
     * @return - the Answer's Multi-flow with all entries of Codifier
     */
    public Multi<Answer> getAll() {
        //noinspection DuplicatedCode
        LOG.trace("getAll()");
        return codifierDataService
                .getAll()
                .map(Answer::of);
    }

    @ConsumeEvent(EventAddress.CODIFIER_PAGE)
    public Uni<Page<Answer>> getPage(PageRequest pageRequest) {
        //noinspection DuplicatedCode
        LOG.tracef("getPage(%s)", pageRequest);
        return codifierCacheProvider.getPage(pageRequest);
    }

    /**
     * This is method a Vertx message consumer and Codifier updater
     *
     * @param o - Codifier
     * @return - a LAA with the Answer containing Codifier id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.CODIFIER_PUT)
    public Uni<Answer> put(Codifier o) {
        //noinspection DuplicatedCode
        LOG.tracef("put(%s)", o);
        return codifierDataService
                .put(o)
                .map(this::apiResponseAcceptedAnswer)
                .flatMap(answer -> codifierCacheProvider.invalidateById(o.getId(), answer))
                .onFailure(exceptionAnswerService::testDuplicateException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateAnswer)
                .onFailure(exceptionAnswerService::testIllegalArgumentException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer);
    }
}
