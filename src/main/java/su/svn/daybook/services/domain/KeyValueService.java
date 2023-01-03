/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * KeyValueService.java
 * $Id$
 */

package su.svn.daybook.services.domain;

import io.quarkus.cache.CacheKey;
import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.dao.KeyValueDao;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.KeyValueTable;
import su.svn.daybook.models.domain.KeyValue;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.ExceptionAnswerService;
import su.svn.daybook.services.cache.KeyValueCacheProvider;
import su.svn.daybook.services.mappers.KeyValueMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.NoSuchElementException;

@ApplicationScoped
public class KeyValueService extends AbstractService<Long, KeyValue> {

    private static final Logger LOG = Logger.getLogger(KeyValueService.class);

    @Inject
    KeyValueCacheProvider keyValueCacheProvider;

    @Inject
    KeyValueDao keyValueDao;

    @Inject
    KeyValueMapper keyValueMapper;

    @Inject
    ExceptionAnswerService exceptionAnswerService;

    /**
     * This is method a Vertx message consumer and KeyValue provider by id
     *
     * @param o - id of the KeyValue
     * @return - a lazy asynchronous action with the Answer containing the KeyValue as payload or empty payload
     */
    @ConsumeEvent(EventAddress.KEY_VALUE_GET)
    public Uni<Answer> get(Object o) {
        //noinspection DuplicatedCode
        LOG.tracef("get(%s)", o);
        try {
            return getEntry(getIdLong(o));
        } catch (NumberFormatException e) {
            LOG.errorf("get(%s)", o, e);
            return exceptionAnswerService.getUniAnswerNoNumber(e);
        } catch (NoSuchElementException e) {
            LOG.errorf("get(%s)", o, e);
            return Uni.createFrom().item(Answer.empty());
        }
    }

    /**
     * The method provides the Answer's flow with all entries of KeyValue
     *
     * @return - the Answer's Multi-flow with all entries of KeyValue
     */
    public Multi<Answer> getAll() {
        //noinspection DuplicatedCode
        LOG.trace("getAll()");
        return keyValueDao
                .count()
                .onItem()
                .transformToMulti(count -> getAllIfNotOverSize(count, this::getAllModels));
    }

    private Multi<KeyValue> getAllModels() {
        return keyValueDao
                .findAll()
                .map(keyValueMapper::convertToModel);
    }

    private Uni<Answer> getEntry(Long id) {
        return keyValueCacheProvider
                .get(id)
                .map(Answer::of)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    @ConsumeEvent(value = EventAddress.KEY_VALUE_PAGE)
    public Uni<Page<Answer>> getPage(PageRequest pageRequest) {
        //noinspection DuplicatedCode
        LOG.tracef("getPage(%s)", pageRequest);
        return keyValueCacheProvider.getPage(pageRequest);
    }

    /**
     * This is method a Vertx message consumer and KeyValue creater
     *
     * @param o - KeyValue
     * @return - a lazy asynchronous action (LAA) with the Answer containing the KeyValue id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.KEY_VALUE_ADD)
    public Uni<Answer> add(KeyValue o) {
        //noinspection DuplicatedCode
        LOG.tracef("add(%s)", o);
        return addEntry(keyValueMapper.convertToDomain(o));
    }

    private Uni<Answer> addEntry(KeyValueTable entry) {
        return keyValueDao
                .insert(entry)
                .map(o -> apiResponseWithKeyAnswer(201, o))
                .flatMap(keyValueCacheProvider::invalidate)
                .onFailure(exceptionAnswerService::testDuplicateKeyException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateKeyValAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * This is method a Vertx message consumer and KeyValue updater
     *
     * @param o - KeyValue
     * @return - a LAA with the Answer containing KeyValue id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.KEY_VALUE_PUT)
    public Uni<Answer> put(KeyValue o) {
        //noinspection DuplicatedCode
        LOG.tracef("put(%s)", o);
        return putEntry(keyValueMapper.convertToDomain(o));
    }

    private Uni<Answer> putEntry(KeyValueTable entry) {
        return keyValueDao
                .update(entry)
                .flatMap(this::apiResponseAcceptedUniAnswer)
                .flatMap(answer -> keyValueCacheProvider.invalidateById(entry.getId(), answer))
                .onFailure(exceptionAnswerService::testDuplicateKeyException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateKeyValAnswer)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(get(entry.getId()))
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * This is method a Vertx message consumer and KeyValue deleter
     *
     * @param o - id of the KeyValue
     * @return - a LAA with the Answer containing KeyValue id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.KEY_VALUE_DEL)
    public Uni<Answer> delete(Object o) {
        //noinspection DuplicatedCode
        LOG.tracef("delete(%s)", o);
        try {
            return deleteEntry(getIdLong(o));
        } catch (NumberFormatException e) {
            LOG.errorf("delete(%s)", o, e);
            return exceptionAnswerService.getUniAnswerNoNumber(e);
        } catch (NoSuchElementException e) {
            LOG.errorf("delete(%s)", o, e);
            return Uni.createFrom().item(Answer.empty());
        }
    }

    private Uni<Answer> deleteEntry(Long id) {
        return keyValueDao
                .delete(id)
                .map(this::apiResponseWithKeyAnswer)
                .flatMap(answer -> keyValueCacheProvider.invalidateById(id, answer))
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }
}
