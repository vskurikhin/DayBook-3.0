/*
 * This file was last modified at 2023.01.09 21:44 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * SessionService.java
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
import su.svn.daybook.models.domain.Session;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.ExceptionAnswerService;
import su.svn.daybook.services.cache.SessionCacheProvider;
import su.svn.daybook.services.domain.SessionDataService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

@ApplicationScoped
@Logged
public class SessionService extends AbstractService<UUID, Session> {

    private static final Logger LOG = Logger.getLogger(SessionService.class);

    @Inject
    ExceptionAnswerService exceptionAnswerService;

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
    @Principled
    @ConsumeEvent(EventAddress.SESSION_ADD)
    public Uni<Answer> add(Request<Session> request) {
        //noinspection DuplicatedCode
        return sessionDataService
                .add(request.payload())
                .map(this::apiResponseCreatedAnswer)
                .flatMap(sessionCacheProvider::invalidate)
                .onFailure(exceptionAnswerService::testDuplicateException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * This is method a Vertx message consumer and Session deleter
     *
     * @param request - id of the Session
     * @return - a LAA with the Answer containing Session id as payload or empty payload
     */
    @Principled
    @ConsumeEvent(EventAddress.SESSION_DEL)
    public Uni<Answer> delete(Request<UUID> request) {
        //noinspection DuplicatedCode
        return sessionDataService
                .delete(request.payload())
                .map(this::apiResponseOkAnswer)
                .flatMap(answer -> sessionCacheProvider.invalidateById(request.payload(), answer))
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * This is method a Vertx message consumer and Session provider by id
     *
     * @param request - id of the Session
     * @return - a lazy asynchronous action with the Answer containing the Session as payload or empty payload
     */
    @Principled
    @ConsumeEvent(EventAddress.SESSION_GET)
    public Uni<Answer> get(Request<UUID> request) {
        //noinspection DuplicatedCode
        return sessionCacheProvider
                .get(request.payload())
                .map(Answer::of)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * The method provides the Answer's flow with all entries of Session
     *
     * @return - the Answer's Multi-flow with all entries of Session
     */
    public Multi<Answer> getAll() {
        //noinspection DuplicatedCode
        return sessionDataService
                .getAll()
                .map(Answer::of);
    }

    @Principled
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
    @Principled
    @ConsumeEvent(EventAddress.SESSION_PUT)
    public Uni<Answer> put(Request<Session> request) {
        //noinspection DuplicatedCode
        return sessionDataService
                .put(request.payload())
                .map(this::apiResponseAcceptedAnswer)
                .flatMap(answer -> sessionCacheProvider.invalidateById(request.payload().id(), answer))
                .onFailure(exceptionAnswerService::testDuplicateException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateAnswer)
                .onFailure(exceptionAnswerService::testIllegalArgumentException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer);
    }
}
