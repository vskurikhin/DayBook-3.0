/*
 * This file was last modified at 2023.01.09 21:44 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * UserService.java
 * $Id$
 */

package su.svn.daybook.services.models;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.models.domain.User;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.ExceptionAnswerService;
import su.svn.daybook.services.cache.UserCacheProvider;
import su.svn.daybook.services.domain.UserDataService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

@ApplicationScoped
public class UserService extends AbstractService<UUID, User> {

    private static final Logger LOG = Logger.getLogger(UserService.class);

    @Inject
    ExceptionAnswerService exceptionAnswerService;

    @Inject
    UserCacheProvider userCacheProvider;

    @Inject
    UserDataService userDataService;

    /**
     * This is method a Vertx message consumer and User creater
     *
     * @param o - User
     * @return - a lazy asynchronous action (LAA) with the Answer containing the User id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.USER_ADD)
    public Uni<Answer> add(User o) {
        LOG.tracef("add(%s)", o);
        return userDataService
                .add(o)
                .map(this::apiResponseCreatedAnswer)
                .flatMap(userCacheProvider::invalidate)
                .onFailure(exceptionAnswerService::testDuplicateException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * This is method a Vertx message consumer and User deleter
     *
     * @param o - id of the User
     * @return - a LAA with the Answer containing User id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.USER_DEL)
    public Uni<Answer> delete(UUID id) {
        LOG.tracef("delete(%s)", id);
        return userDataService
                .delete(id)
                .map(this::apiResponseOkAnswer)
                .flatMap(answer -> userCacheProvider.invalidateById(id, answer))
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * This is method a Vertx message consumer and User provider by id
     *
     * @param o - id of the User
     * @return - a lazy asynchronous action with the Answer containing the User as payload or empty payload
     */
    @ConsumeEvent(EventAddress.USER_GET)
    public Uni<Answer> get(UUID id) {
        LOG.tracef("get(%s)", id);
        return userCacheProvider
                .get(id)
                .map(Answer::of)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * The method provides the Answer's flow with all entries of User
     *
     * @return - the Answer's Multi-flow with all entries of User
     */
    public Multi<Answer> getAll() {
        LOG.trace("getAll");
        return userDataService
                .getAll()
                .map(Answer::of);
    }

    @ConsumeEvent(value = EventAddress.USER_PAGE)
    public Uni<Page<Answer>> getPage(PageRequest pageRequest) {
        //noinspection DuplicatedCode
        LOG.tracef("getPage(%s)", pageRequest);
        return userCacheProvider.getPage(pageRequest);
    }

    /**
     * This is method a Vertx message consumer and User updater
     *
     * @param o - User
     * @return - a LAA with the Answer containing User id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.USER_PUT)
    public Uni<Answer> put(User o) {
        LOG.tracef("put(%s)", o);
        return userDataService
                .put(o)
                .map(this::apiResponseAcceptedAnswer)
                .flatMap(answer -> userCacheProvider.invalidateById(o.id(), answer))
                .onFailure(exceptionAnswerService::testCompositeException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer)
                .onFailure(exceptionAnswerService::testDuplicateException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateAnswer)
                .onFailure(exceptionAnswerService::testIllegalArgumentException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer);
    }
}
