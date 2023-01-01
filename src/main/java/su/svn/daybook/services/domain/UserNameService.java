/*
 * This file was last modified at 2022.01.11 17:44 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * UserNameService.java
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
import su.svn.daybook.domain.dao.UserNameDao;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.UserName;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.ExceptionAnswerService;
import su.svn.daybook.services.PageService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class UserNameService extends AbstractService<UUID, UserName> {

    private static final Logger LOG = Logger.getLogger(UserNameService.class);

    @Inject
    CacheManager cacheManager;

    @Inject
    UserNameDao userNameDao;

    @Inject
    ExceptionAnswerService exceptionAnswerService;

    @Inject
    PageService pageService;

    /**
     * This is method a Vertx message consumer and UserName provider by id
     *
     * @param o - id of the UserName
     * @return - a lazy asynchronous action with the Answer containing the UserName as payload or empty payload
     */
    @ConsumeEvent(EventAddress.USER_GET)
    @CacheResult(cacheName = EventAddress.USER_GET)
    public Uni<Answer> get(@CacheKey Object o) {
        LOG.infof("get(%s)", o);
        if (o instanceof UUID id) {
            return getEntry(id);
        }
        if (o instanceof String id) {
            return getEntry(UUID.fromString(id));
        }
        return Uni.createFrom().item(Answer.empty());
    }

    /**
     * The method provides the Answer's flow with all entries of UserName
     *
     * @return - the Answer's Multi-flow with all entries of UserName
     */
    public Multi<Answer> getAll() {
        LOG.trace("getAll");
        return userNameDao
                .findAll()
                .onItem()
                .transform(this::answerOf);
    }

    private Uni<Answer> getEntry(UUID id) {
        return userNameDao
                .findById(id)
                .map(this::apiResponseWithValueAnswer)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    @ConsumeEvent(value = EventAddress.USER_PAGE)
    @CacheResult(cacheName = EventAddress.USER_PAGE)
    public Uni<Page<Answer>> getPage(@CacheKey PageRequest pageRequest) {
        //noinspection DuplicatedCode
        LOG.tracef("getPage(%s)", pageRequest);
        return pageService.getPage(pageRequest, userNameDao::count, userNameDao::findRange);
    }

    /**
     * This is method a Vertx message consumer and UserName creater
     *
     * @param o - UserName
     * @return - a lazy asynchronous action (LAA) with the Answer containing the UserName id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.USER_ADD)
    public Uni<Answer> add(UserName o) {
        LOG.infof("add(%s)", o);
        return addEntry(o);
    }

    private Uni<Answer> addEntry(UserName entry) {
        return userNameDao
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
     * This is method a Vertx message consumer and UserName updater
     *
     * @param o - UserName
     * @return - a LAA with the Answer containing UserName id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.USER_PUT)
    public Uni<Answer> put(UserName o) {
        LOG.tracef("put(%s)", o);
        return putEntry(o);
    }

    private Uni<Answer> putEntry(UserName entry) {
        return userNameDao
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
     * This is method a Vertx message consumer and UserName deleter
     *
     * @param o - id of the UserName
     * @return - a LAA with the Answer containing UserName id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.USER_DEL)
    public Uni<Answer> delete(Object o) {
        LOG.tracef("delete(%s)", o);
        if (o instanceof UUID id) {
            return deleteEntry(id);
        }
        if (o instanceof String sid) {
            return deleteEntry(UUID.fromString(sid));
        }
        return Uni.createFrom().item(Answer.empty());
    }

    private Uni<Answer> deleteEntry(UUID id) {
        return userNameDao
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
                .getCache(EventAddress.USER_GET)
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
                .getCache(EventAddress.USER_PAGE)
                .map(Cache::invalidateAll)
                .orElse(Uni.createFrom().voidItem());
    }
}
