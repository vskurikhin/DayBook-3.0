/*
 * This file was last modified at 2024-10-31 13:16 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * SessionService.java
 * $Id$
 */

package su.svn.daybook3.api.services.models;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import su.svn.daybook3.api.annotations.ExceptionBadRequestAnswer;
import su.svn.daybook3.api.annotations.ExceptionDuplicateAnswer;
import su.svn.daybook3.api.annotations.ExceptionNoSuchElementAnswer;
import su.svn.daybook3.api.annotations.PrincipalLogging;
import su.svn.daybook3.api.domain.enums.EventAddress;
import su.svn.daybook3.api.models.domain.Session;
import su.svn.daybook3.api.models.pagination.Page;
import su.svn.daybook3.api.models.pagination.PageRequest;
import su.svn.daybook3.api.services.cache.SessionCacheProvider;
import su.svn.daybook3.api.services.domain.SessionDataService;
import su.svn.daybook3.domain.messages.Answer;
import su.svn.daybook3.domain.messages.Request;

import java.util.UUID;

@PrincipalLogging
@ApplicationScoped
public class SessionService
        extends AbstractService<UUID, Session>
        implements MultiAnswerAllService {

    @Inject
    SessionCacheProvider sessionCacheProvider;

    @Inject
    SessionDataService sessionDataService;

    /**
     * This is method a Vertx message consumer and Session creater
     *
     * @param request - Session
     * @return - a lazy asynchronous action (LAA) with the Answer containing the Session id as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionDuplicateAnswer
    @ConsumeEvent(EventAddress.SESSION_ADD)
    public Uni<Answer> add(Request<Session> request) {
        //noinspection DuplicatedCode
        return sessionDataService
                .add(request.payload())
                .map(this::apiResponseCreatedAnswer)
                .flatMap(sessionCacheProvider::invalidate);
    }

    /**
     * This is method a Vertx message consumer and Session deleter
     *
     * @param request - id of the Session
     * @return - a LAA with the Answer containing Session id as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.SESSION_DEL)
    public Uni<Answer> delete(Request<UUID> request) {
        //noinspection DuplicatedCode
        return sessionDataService
                .delete(request.payload())
                .map(this::apiResponseOkAnswer)
                .flatMap(answer -> sessionCacheProvider.invalidateByKey(request.payload(), answer));
    }

    /**
     * This is method a Vertx message consumer and Session provider by id
     *
     * @param request - id of the Session
     * @return - a lazy asynchronous action with the Answer containing the Session as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.SESSION_GET)
    public Uni<Answer> get(Request<UUID> request) {
        //noinspection DuplicatedCode
        return sessionCacheProvider
                .get(request.payload())
                .map(Answer::of);
    }

    /**
     * The method provides the Answer's flow with all entries of Session
     *
     * @return - the Answer's Multi-flow with all entries of Session
     */
    @Override
    public Multi<Answer> getAll() {
        //noinspection DuplicatedCode
        return sessionDataService
                .getAll()
                .map(Answer::of);
    }

    @ExceptionBadRequestAnswer
    @ConsumeEvent(EventAddress.SESSION_PAGE)
    public Uni<Page<Answer>> getPage(Request<PageRequest> request) {
        //noinspection DuplicatedCode
        return sessionCacheProvider.getPage(request.payload());
    }

    /**
     * This is method a Vertx message consumer and Session updater
     *
     * @param request - Session
     * @return - a LAA with the Answer containing Session id as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionDuplicateAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.SESSION_PUT)
    public Uni<Answer> put(Request<Session> request) {
        //noinspection DuplicatedCode
        return sessionDataService
                .put(request.payload())
                .map(this::apiResponseAcceptedAnswer)
                .flatMap(answer -> sessionCacheProvider.invalidateByKey(request.payload().id(), answer));
    }
}
