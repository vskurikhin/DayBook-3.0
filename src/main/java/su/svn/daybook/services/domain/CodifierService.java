/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * CodifierService.java
 * $Id$
 */

package su.svn.daybook.services.domain;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.dao.CodifierDao;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.Codifier;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.ExceptionAnswerService;
import su.svn.daybook.services.PageService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.NoSuchElementException;

@ApplicationScoped
public class CodifierService extends AbstractService<String, Codifier> {

    private static final Logger LOG = Logger.getLogger(CodifierService.class);

    @Inject
    CodifierDao codifierDao;

    @Inject
    ExceptionAnswerService exceptionAnswerService;

    @Inject
    PageService pageService;

    /**
     * This is method a Vertx message consumer and Codifier provider by id
     *
     * @param o - id of the Codifier
     * @return - a lazy asynchronous action with the Answer containing the Codifier as payload or empty payload
     */
    @ConsumeEvent(EventAddress.CODIFIER_GET)
    public Uni<Answer> get(Object o) {
        LOG.tracef("get(%s)", o);
        try {
            return getEntry(getIdString(o));
        } catch (NoSuchElementException e) {
            LOG.errorf("get(%s)", o, e);
            return Uni.createFrom().item(Answer.empty());
        }
    }

    /**
     * The method provides the Answer's flow with all entries of Codifier
     *
     * @return - the Answer's Multi-flow with all entries of Codifier
     */
    public Multi<Answer> getAll() {
        LOG.trace("getAll");
        return codifierDao.findAll()
                .onItem()
                .transform(this::answerOf);
    }

    private Uni<Answer> getEntry(String id) {
        return codifierDao.findById(id)
                .map(this::apiResponseWithValueAnswer)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    @ConsumeEvent(value = EventAddress.CODIFIER_PAGE)
    public Uni<Page<Answer>> getPage(PageRequest pageRequest) {
        //noinspection DuplicatedCode
        LOG.tracef("getPage(%s)", pageRequest);
        return pageService.getPage(pageRequest, codifierDao::count, codifierDao::findRange);
    }

    /**
     * This is method a Vertx message consumer and Codifier creater
     *
     * @param o - Codifier
     * @return - a lazy asynchronous action (LAA) with the Answer containing the Codifier id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.CODIFIER_ADD)
    public Uni<Answer> add(Codifier o) {
        LOG.tracef("add(%s)", o);
        return addEntry(o);
    }

    private Uni<Answer> addEntry(Codifier entry) {
        return codifierDao.insert(entry)
                .map(o -> apiResponseWithKeyAnswer(201, o))
                .onFailure(exceptionAnswerService::testDuplicateKeyException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateKeyValueAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * This is method a Vertx message consumer and Codifier updater
     *
     * @param o - Codifier
     * @return - a LAA with the Answer containing Codifier id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.CODIFIER_PUT)
    public Uni<Answer> put(Codifier o) {
        LOG.tracef("put(%s)", o);
        return putEntry(o);
    }

    private Uni<Answer> putEntry(Codifier entry) {
        return codifierDao.update(entry)
                .flatMap(this::apiResponseAcceptedUniAnswer)
                .onFailure(exceptionAnswerService::testDuplicateKeyException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateKeyValueAnswer)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(get(entry.getId()))
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * This is method a Vertx message consumer and Codifier deleter
     *
     * @param o - id of the Codifier
     * @return - a LAA with the Answer containing Codifier id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.CODIFIER_DEL)
    public Uni<Answer> delete(Object o) {
        LOG.tracef("delete(%s)", o);
        try {
            return deleteEntry(getIdString(o));
        } catch (NoSuchElementException e) {
            LOG.errorf("delete(%s)", o, e);
            return Uni.createFrom().item(Answer.empty());
        }
    }

    private Uni<Answer> deleteEntry(String id) {
        return codifierDao.delete(id)
                .map(this::apiResponseWithKeyAnswer)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }
}
