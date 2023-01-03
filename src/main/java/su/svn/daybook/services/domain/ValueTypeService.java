/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * ValueTypeService.java
 * $Id$
 */

package su.svn.daybook.services.domain;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.dao.ValueTypeDao;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.ValueTypeTable;
import su.svn.daybook.models.domain.ValueType;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.ExceptionAnswerService;
import su.svn.daybook.services.cache.ValueTypeCacheProvider;
import su.svn.daybook.services.mappers.ValueTypeMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.NoSuchElementException;

@ApplicationScoped
public class ValueTypeService extends AbstractService<Long, ValueType> {

    private static final Logger LOG = Logger.getLogger(ValueTypeService.class);

    @Inject
    ValueTypeCacheProvider valueTypeCacheProvider;

    @Inject
    ValueTypeDao valueTypeDao;

    @Inject
    ValueTypeMapper valueTypeMapper;

    @Inject
    ExceptionAnswerService exceptionAnswerService;

    /**
     * This is method a Vertx message consumer and ValueType provider by id
     *
     * @param o - id of the ValueType
     * @return - a lazy asynchronous action with the Answer containing the ValueType as payload or empty payload
     */
    @ConsumeEvent(EventAddress.VALUE_TYPE_GET)
    public Uni<Answer> get(Object o) {
        //noinspection DuplicatedCode
        LOG.tracef("get(%s)", o);
        try {
            return getEntry(getIdLong(o));
        } catch (NumberFormatException e) {
            LOG.errorf("get(%s)", o, e);
            return Uni.createFrom().item(Answer.noNumber(e.getMessage()));
        } catch (NoSuchElementException e) {
            LOG.errorf("get(%s)", o, e);
            return Uni.createFrom().item(Answer.empty());
        }
    }

    /**
     * The method provides the Answer's flow with all entries of ValueType
     *
     * @return - the Answer's Multi-flow with all entries of ValueType
     */
    public Multi<Answer> getAll() {
        LOG.trace("getAll");
        return valueTypeDao
                .count()
                .onItem()
                .transformToMulti(count -> getAllIfNotOverSize(count, this::getAllModels));
    }

    private Multi<ValueType> getAllModels() {
        return valueTypeDao
                .findAll()
                .map(valueTypeMapper::convertToModel);
    }

    private Uni<Answer> getEntry(Long id) {
        return valueTypeCacheProvider
                .get(id)
                .map(Answer::of)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    @ConsumeEvent(value = EventAddress.VALUE_TYPE_PAGE)
    public Uni<Page<Answer>> getPage(PageRequest pageRequest) {
        //noinspection DuplicatedCode
        LOG.tracef("getPage(%s)", pageRequest);
        return valueTypeCacheProvider.getPage(pageRequest);
    }

    /**
     * This is method a Vertx message consumer and ValueType creater
     *
     * @param o - ValueType
     * @return - a lazy asynchronous action (LAA) with the Answer containing the ValueType id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.VALUE_TYPE_ADD)
    public Uni<Answer> add(ValueType o) {
        LOG.tracef("add(%s)", o);
        return addEntry(valueTypeMapper.convertToDomain(o));
    }

    private Uni<Answer> addEntry(ValueTypeTable entry) {
        return valueTypeDao
                .insert(entry)
                .map(o -> apiResponseWithKeyAnswer(201, o))
                .flatMap(valueTypeCacheProvider::invalidate)
                .onFailure(exceptionAnswerService::testDuplicateKeyException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateKeyValAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * This is method a Vertx message consumer and ValueType updater
     *
     * @param o - ValueType
     * @return - a LAA with the Answer containing ValueType id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.VALUE_TYPE_PUT)
    public Uni<Answer> put(ValueType o) {
        LOG.tracef("put(%s)", o);
        return putEntry(valueTypeMapper.convertToDomain(o));
    }

    private Uni<Answer> putEntry(ValueTypeTable entry) {
        return valueTypeDao
                .update(entry)
                .flatMap(this::apiResponseAcceptedUniAnswer)
                .flatMap(answer -> valueTypeCacheProvider.invalidateById(entry.getId(), answer))
                .onFailure(exceptionAnswerService::testDuplicateKeyException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateKeyValAnswer)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(get(entry.getId()))
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * This is method a Vertx message consumer and ValueType deleter
     *
     * @param o - id of the ValueType
     * @return - a LAA with the Answer containing ValueType id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.VALUE_TYPE_DEL)
    public Uni<Answer> delete(Object o) {
        //noinspection DuplicatedCode
        LOG.tracef("delete(%s)", o);
        try {
            return deleteEntry(getIdLong(o));
        } catch (NumberFormatException e) {
            LOG.errorf("delete(%s)", o, e);
            return Uni.createFrom().item(Answer.noNumber(e.getMessage()));
        } catch (NoSuchElementException e) {
            LOG.errorf("delete(%s)", o, e);
            return Uni.createFrom().item(Answer.empty());
        }
    }

    private Uni<Answer> deleteEntry(Long id) {
        return valueTypeDao
                .delete(id)
                .map(this::apiResponseWithKeyAnswer)
                .flatMap(answer -> valueTypeCacheProvider.invalidateById(id, answer))
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }
}
