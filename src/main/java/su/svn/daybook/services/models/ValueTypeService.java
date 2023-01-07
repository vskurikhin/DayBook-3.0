/*
 * This file was last modified at 2023.01.07 11:52 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * ValueTypeService.java
 * $Id$
 */

package su.svn.daybook.services.models;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.models.domain.ValueType;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.ExceptionAnswerService;
import su.svn.daybook.services.cache.ValueTypeCacheProvider;
import su.svn.daybook.services.domain.ValueTypeDataService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ValueTypeService extends AbstractService<Long, ValueType> {

    private static final Logger LOG = Logger.getLogger(ValueTypeService.class);

    @Inject
    ExceptionAnswerService exceptionAnswerService;

    @Inject
    ValueTypeCacheProvider valueTypeCacheProvider;

    @Inject
    ValueTypeDataService valueTypeDataService;

    /**
     * This is method a Vertx message consumer and ValueType creater
     *
     * @param o - ValueType
     * @return - a lazy asynchronous action (LAA) with the Answer containing the ValueType id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.VALUE_TYPE_ADD)
    public Uni<Answer> add(ValueType o) {
        //noinspection DuplicatedCode
        LOG.tracef("add(%s)", o);
        return valueTypeDataService
                .add(o)
                .map(this::apiResponseCreatedAnswer)
                .flatMap(valueTypeCacheProvider::invalidate)
                .onFailure(exceptionAnswerService::testDuplicateException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);

    }

    /**
     * This is method a Vertx message consumer and ValueType deleter
     *
     * @param id - id of the ValueType
     * @return - a LAA with the Answer containing ValueType id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.VALUE_TYPE_DEL)
    public Uni<Answer> delete(Long id) {
        //noinspection DuplicatedCode
        LOG.tracef("delete(%s)", id);
        return valueTypeDataService
                .delete(id)
                .map(this::apiResponseOkAnswer)
                .flatMap(answer -> valueTypeCacheProvider.invalidateById(id, answer))
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * This is method a Vertx message consumer and ValueType provider by id
     *
     * @param id - id of the ValueType
     * @return - a lazy asynchronous action with the Answer containing the ValueType as payload or empty payload
     */
    @ConsumeEvent(EventAddress.VALUE_TYPE_GET)
    public Uni<Answer> get(Long id) {
        //noinspection DuplicatedCode
        LOG.tracef("get(%s)", id);
        return valueTypeCacheProvider
                .get(id)
                .map(Answer::of)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * The method provides the Answer's flow with all entries of ValueType
     *
     * @return - the Answer's Multi-flow with all entries of ValueType
     */
    public Multi<Answer> getAll() {
        //noinspection DuplicatedCode
        LOG.trace("getAll()");
        return valueTypeDataService
                .getAll()
                .map(Answer::of);
    }

    @ConsumeEvent(EventAddress.VALUE_TYPE_PAGE)
    public Uni<Page<Answer>> getPage(PageRequest pageRequest) {
        //noinspection DuplicatedCode
        LOG.tracef("getPage(%s)", pageRequest);
        return valueTypeCacheProvider.getPage(pageRequest);
    }

    /**
     * This is method a Vertx message consumer and ValueType updater
     *
     * @param o - ValueType
     * @return - a LAA with the Answer containing ValueType id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.VALUE_TYPE_PUT)
    public Uni<Answer> put(ValueType o) {
        //noinspection DuplicatedCode
        LOG.tracef("put(%s)", o);
        return valueTypeDataService
                .put(o)
                .map(this::apiResponseAcceptedAnswer)
                .flatMap(answer -> valueTypeCacheProvider.invalidateById(o.getId(), answer))
                .onFailure(exceptionAnswerService::testDuplicateException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateAnswer)
                .onFailure(exceptionAnswerService::testIllegalArgumentException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer);
    }
}
