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
import su.svn.daybook.annotations.Logged;
import su.svn.daybook.annotations.Principled;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.messages.Request;
import su.svn.daybook.models.domain.User;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.ExceptionAnswerService;
import su.svn.daybook.services.cache.UserCacheProvider;
import su.svn.daybook.services.domain.UserDataService;
import su.svn.daybook.services.security.AuthenticationContext;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

@ApplicationScoped @Logged
public class UserService extends AbstractService<UUID, User> {

    private static final Logger LOG = Logger.getLogger(UserService.class);

    @Inject
    ExceptionAnswerService exceptionAnswerService;

    @Inject
    UserCacheProvider userCacheProvider;

    @Inject
    UserDataService userDataService;

    @Inject
    AuthenticationContext authContext;

    /**
     * This is method a Vertx message consumer and User creater
     *
     * @param request - User
     * @return - a lazy asynchronous action (LAA) with the Answer containing the User id as payload or empty payload
     */
    @Principled
    @ConsumeEvent(EventAddress.USER_ADD)
    public Uni<Answer> add(Request<User> request) {
        var principal = authContext.getPrincipal();
        LOG.tracef("getPage(%s), principal: %s", request, principal);
        return userDataService
                .add(request.payload())
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
    @Principled
    @ConsumeEvent(EventAddress.USER_DEL)
    public Uni<Answer> delete(Request<UUID> request) {
        return userDataService
                .delete(request.payload())
                .map(this::apiResponseOkAnswer)
                .flatMap(answer -> userCacheProvider.invalidateById(request.payload(), answer))
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
    @Principled
    @ConsumeEvent(EventAddress.USER_GET)
    public Uni<Answer> get(Request<UUID> request) {
        return userCacheProvider
                .get(request.payload())
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
        return userDataService
                .getAll()
                .map(Answer::of);
    }

    @Principled
    @ConsumeEvent(value = EventAddress.USER_PAGE)
    public Uni<Page<Answer>> getPage(Request<PageRequest> request) {
        //noinspection DuplicatedCode
        return userCacheProvider.getPage(request.payload());
    }

    /**
     * This is method a Vertx message consumer and User updater
     *
     * @param request - User
     * @return - a LAA with the Answer containing User id as payload or empty payload
     */
    @Principled
    @ConsumeEvent(EventAddress.USER_PUT)
    public Uni<Answer> put(Request<User> request) {
        return userDataService
                .put(request.payload())
                .map(this::apiResponseAcceptedAnswer)
                .flatMap(answer -> userCacheProvider.invalidateById(request.payload().id(), answer))
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
