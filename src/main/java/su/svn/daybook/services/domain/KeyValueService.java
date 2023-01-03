/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * KeyValueService.java
 * $Id$
 */

package su.svn.daybook.services.domain;

import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheManager;
import io.quarkus.cache.CacheResult;
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
import su.svn.daybook.services.PageService;
import su.svn.daybook.services.mappers.KeyValueMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@ApplicationScoped
public class KeyValueService extends AbstractService<Long, KeyValue> {

    private static final Logger LOG = Logger.getLogger(KeyValueService.class);

    @Inject
    CacheManager cacheManager;

    @Inject
    KeyValueDao keyValueDao;

    @Inject
    KeyValueMapper keyValueMapper;

    @Inject
    ExceptionAnswerService exceptionAnswerService;

    @Inject
    PageService pageService;

    /**
     * This is method a Vertx message consumer and KeyValue provider by id
     *
     * @param o - id of the KeyValue
     * @return - a lazy asynchronous action with the Answer containing the KeyValue as payload or empty payload
     */
    @ConsumeEvent(EventAddress.KEY_VALUE_GET)
    @CacheResult(cacheName = EventAddress.KEY_VALUE_GET)
    public Uni<Answer> get(@CacheKey Object o) {
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
        return keyValueDao.findAll().map(keyValueMapper::convertToModel);
    }

    private Uni<Answer> getEntry(Long id) {
        return keyValueDao
                .findById(id)
                .onItem()
                .transform(Optional::get)
                .onItem()
                .transform(keyValueMapper::convertToModel)
                .onItem()
                .transform(Answer::of)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    @ConsumeEvent(value = EventAddress.KEY_VALUE_PAGE)
    @CacheResult(cacheName = EventAddress.KEY_VALUE_PAGE)
    public Uni<Page<Answer>> getPage(@CacheKey PageRequest pageRequest) {
        //noinspection DuplicatedCode
        LOG.tracef("getPage(%s)", pageRequest);
        return pageService.getPage(pageRequest, keyValueDao::count, keyValueDao::findRange, this::answerOfModel);
    }

    private Answer answerOfModel(KeyValueTable w) {
        return Answer.of(keyValueMapper.convertToModel(w));
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
                .onItem()
                .transformToUni(this::invalidateAllAndAnswer)
                .onFailure(exceptionAnswerService::testDuplicateKeyException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateKeyValueAnswer)
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
                .onItem()
                .transformToUni(answer -> invalidateAndAnswer(entry.getId(), answer))
                .onFailure(exceptionAnswerService::testDuplicateKeyException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateKeyValueAnswer)
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
                .onItem()
                .transformToUni(answer -> invalidateAndAnswer(id, answer))
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    @Override
    protected Uni<List<Void>> invalidate(Object o) {
        LOG.tracef("invalidate(%s)", o);

        var wordGetVoid = cacheManager
                .getCache(EventAddress.KEY_VALUE_GET)
                .map(cache -> cache.invalidate(o))
                .orElse(Uni.createFrom().voidItem());
        var wordPageVoid = invalidateAllPage();

        return joinCollectFailures(wordGetVoid, wordPageVoid)
                .onItem()
                .invoke(voids -> LOG.tracef("invalidate of %d caches", voids.size()));
    }

    @Override
    protected Uni<Void> invalidateAllPage() {
        LOG.trace("invalidateAllPage()");
        return cacheManager
                .getCache(EventAddress.KEY_VALUE_PAGE)
                .map(Cache::invalidateAll)
                .orElse(Uni.createFrom().voidItem());
    }
}
