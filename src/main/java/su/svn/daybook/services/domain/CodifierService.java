/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * CodifierService.java
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
import su.svn.daybook.domain.dao.CodifierDao;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.CodifierTable;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.ExceptionAnswerService;
import su.svn.daybook.services.PageService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.NoSuchElementException;

@ApplicationScoped
public class CodifierService extends AbstractService<String, CodifierTable> {

    private static final Logger LOG = Logger.getLogger(CodifierService.class);

    @Inject
    CacheManager cacheManager;

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
    @CacheResult(cacheName = EventAddress.CODIFIER_GET)
    public Uni<Answer> get(@CacheKey Object o) {
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
        return codifierDao
                .findById(id)
                .map(this::apiResponseWithValueAnswer)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    @ConsumeEvent(value = EventAddress.CODIFIER_PAGE)
    @CacheResult(cacheName = EventAddress.CODIFIER_PAGE)
    public Uni<Page<Answer>> getPage(@CacheKey PageRequest pageRequest) {
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
    public Uni<Answer> add(CodifierTable o) {
        LOG.tracef("add(%s)", o);
        return addEntry(o);
    }

    private Uni<Answer> addEntry(CodifierTable entry) {
        return codifierDao
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
     * This is method a Vertx message consumer and Codifier updater
     *
     * @param o - Codifier
     * @return - a LAA with the Answer containing Codifier id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.CODIFIER_PUT)
    public Uni<Answer> put(CodifierTable o) {
        LOG.tracef("put(%s)", o);
        return putEntry(o);
    }

    private Uni<Answer> putEntry(CodifierTable entry) {
        return codifierDao
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
        return codifierDao
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
                .getCache(EventAddress.CODIFIER_GET)
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
                .getCache(EventAddress.CODIFIER_PAGE)
                .map(Cache::invalidateAll)
                .orElse(Uni.createFrom().voidItem());
    }
}
