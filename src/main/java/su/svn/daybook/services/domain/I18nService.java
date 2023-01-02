/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * I18nService.java
 * $Id$
 */

package su.svn.daybook.services.domain;

import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheManager;
import io.quarkus.cache.CacheResult;
import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.dao.I18nDao;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.I18nTable;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.ExceptionAnswerService;
import su.svn.daybook.services.PageService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.NoSuchElementException;

@ApplicationScoped
public class I18nService extends AbstractService<Long, I18nTable> {

    private static final Logger LOG = Logger.getLogger(I18nService.class);

    @Inject
    CacheManager cacheManager;

    @Inject
    I18nDao i18nDao;

    @Inject
    ExceptionAnswerService exceptionAnswerService;

    @Inject
    PageService pageService;

    /**
     * This is method a Vertx message consumer and I18n provider by id
     *
     * @param o - id of the I18n
     * @return - a lazy asynchronous action with the Answer containing the I18n as payload or empty payload
     */
    @ConsumeEvent(EventAddress.I18N_GET)
    @CacheResult(cacheName = EventAddress.I18N_GET)
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
     * The method provides the Answer's flow with all entries of I18n
     *
     * @return - the Answer's Multi-flow with all entries of I18n
     */
    public Multi<Answer> getAll() {
        LOG.trace("getAll");
        return i18nDao
                .findAll()
                .onItem()
                .transform(this::answerOf);
    }

    private Uni<Answer> getEntry(Long id) {
        return i18nDao
                .findById(id)
                .map(this::apiResponseWithValueAnswer)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    @ConsumeEvent(value = EventAddress.I18N_PAGE)
    @CacheResult(cacheName = EventAddress.I18N_PAGE)
    public Uni<Page<Answer>> getPage(PageRequest pageRequest) {
        //noinspection DuplicatedCode
        LOG.tracef("getPage(%s)", pageRequest);
        return pageService.getPage(pageRequest, i18nDao::count, i18nDao::findRange);
    }

    /**
     * This is method a Vertx message consumer and I18n creater
     *
     * @param o - I18n
     * @return - a lazy asynchronous action (LAA) with the Answer containing the I18n id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.I18N_ADD)
    public Uni<Answer> add(I18nTable o) {
        LOG.tracef("add(%s)", o);
        return addEntry(o);
    }

    private Uni<Answer> addEntry(I18nTable entry) {
        return i18nDao
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
     * This is method a Vertx message consumer and I18n updater
     *
     * @param o - I18n
     * @return - a LAA with the Answer containing I18n id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.I18N_PUT)
    public Uni<Answer> put(I18nTable o) {
        LOG.tracef("put(%s)", o);
        return putEntry(o)
                .onItem()
                .transformToUni(answer -> invalidateAndAnswer(o.getId(), answer));
    }

    private Uni<Answer> putEntry(I18nTable entry) {
        return i18nDao
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
     * This is method a Vertx message consumer and I18n deleter
     *
     * @param o - id of the I18n
     * @return - a LAA with the Answer containing I18n id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.I18N_DEL)
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
        return i18nDao
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
                .getCache(EventAddress.I18N_GET)
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
                .getCache(EventAddress.I18N_PAGE)
                .map(Cache::invalidateAll)
                .orElse(Uni.createFrom().voidItem());
    }
}
