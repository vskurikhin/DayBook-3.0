/*
 * This file was last modified at 2024-10-30 14:17 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * UserService.java
 * $Id$
 */

package su.svn.daybook3.auth.services.models;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import su.svn.daybook3.auth.annotations.ExceptionBadRequestAnswer;
import su.svn.daybook3.auth.annotations.ExceptionDuplicateAnswer;
import su.svn.daybook3.auth.annotations.ExceptionNoSuchElementAnswer;
import su.svn.daybook3.auth.annotations.PrincipalLogging;
import su.svn.daybook3.auth.domain.enums.EventAddress;
import su.svn.daybook3.auth.models.domain.User;
import su.svn.daybook3.auth.models.pagination.Page;
import su.svn.daybook3.auth.models.pagination.PageRequest;
import su.svn.daybook3.auth.services.cache.LoginCacheProvider;
import su.svn.daybook3.auth.services.cache.UserCacheProvider;
import su.svn.daybook3.auth.services.domain.UserDataService;
import su.svn.daybook3.domain.messages.Answer;
import su.svn.daybook3.domain.messages.Request;

import java.util.UUID;

@PrincipalLogging
@ApplicationScoped
public class UserService
        extends AbstractService<UUID, User>
        implements MultiAnswerAllService {

    @Inject
    LoginCacheProvider loginCacheProvider;

    @Inject
    UserCacheProvider userCacheProvider;

    @Inject
    UserDataService userDataService;

    /**
     * This is method a Vertx message consumer and User creater
     *
     * @param request - User
     * @return - a lazy asynchronous action (LAA) with the Answer containing the User id as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionDuplicateAnswer
    @ConsumeEvent(EventAddress.USER_ADD)
    public Uni<Answer> add(Request<User> request) {
        return userDataService
                .add(request.payload())
                .map(this::apiResponseCreatedAnswer)
                .flatMap(userCacheProvider::invalidate);
    }

    /**
     * This is method a Vertx message consumer and User deleter
     *
     * @param request - id of the User
     * @return - a LAA with the Answer containing User id as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.USER_DEL)
    public Uni<Answer> delete(Request<UUID> request) {
        return userDataService
                .delete(request.payload())
                .map(this::apiResponseOkAnswer)
                .flatMap(answer -> userCacheProvider.invalidateByKey(request.payload(), answer))
                .flatMap(answer -> loginCacheProvider.invalidateById(request.payload(), answer));
    }

    /**
     * This is method a Vertx message consumer and User provider by id
     *
     * @param request - id of the User
     * @return - a lazy asynchronous action with the Answer containing the User as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.USER_GET)
    public Uni<Answer> get(Request<UUID> request) {
        return userCacheProvider
                .get(request.payload())
                .map(Answer::of);
    }

    /**
     * The method provides the Answer's flow with all entries of User
     *
     * @return - the Answer's Multi-flow with all entries of User
     */
    @Override
    public Multi<Answer> getAll() {
        return userDataService
                .getAll()
                .map(Answer::of);
    }

    @ExceptionBadRequestAnswer
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
    @ExceptionBadRequestAnswer
    @ExceptionDuplicateAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.USER_PUT)
    public Uni<Answer> put(Request<User> request) {
        return userDataService
                .put(request.payload())
                .map(this::apiResponseAcceptedAnswer)
                .flatMap(answer -> userCacheProvider.invalidateByKey(request.payload().id(), answer))
                .flatMap(answer -> loginCacheProvider.invalidateByKey(request.payload().userName(), answer));
    }
}
