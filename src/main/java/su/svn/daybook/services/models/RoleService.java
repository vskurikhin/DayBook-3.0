/*
 * This file was last modified at 2023.01.09 21:44 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * RoleService.java
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
import su.svn.daybook.models.domain.Role;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.ExceptionAnswerService;
import su.svn.daybook.services.cache.RoleCacheProvider;
import su.svn.daybook.services.domain.RoleDataService;
import su.svn.daybook.services.security.AuthenticationContext;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

@ApplicationScoped @Logged
public class RoleService extends AbstractService<UUID, Role> {

    private static final Logger LOG = Logger.getLogger(RoleService.class);

    @Inject
    ExceptionAnswerService exceptionAnswerService;

    @Inject
    RoleCacheProvider roleCacheProvider;

    @Inject
    RoleDataService roleDataService;

    @Inject
    AuthenticationContext authContext;

    /**
     * This is method a Vertx message consumer and Role creater
     *
     * @param request - Role
     * @return - a lazy asynchronous action (LAA) with the Answer containing the Role id as payload or empty payload
     */
    @Principled
    @ConsumeEvent(EventAddress.ROLE_ADD)
    public Uni<Answer> add(Request<Role> request) {
        //noinspection DuplicatedCode
        var principal = authContext.getPrincipal();
        LOG.tracef("add(%s), principal: %s", request, principal);
        return roleDataService
                .add(request.payload())
                .map(this::apiResponseCreatedAnswer)
                .flatMap(roleCacheProvider::invalidate)
                .onFailure(exceptionAnswerService::testDuplicateException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * This is method a Vertx message consumer and Role deleter
     *
     * @param request - id of the Role
     * @return - a LAA with the Answer containing Role id as payload or empty payload
     */
    @Principled
    @ConsumeEvent(EventAddress.ROLE_DEL)
    public Uni<Answer> delete(Request<UUID> request) {
        //noinspection DuplicatedCode
        return roleDataService
                .delete(request.payload())
                .map(this::apiResponseOkAnswer)
                .flatMap(answer -> roleCacheProvider.invalidateById(request.payload(), answer))
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * This is method a Vertx message consumer and Role provider by id
     *
     * @param request - id of the Role
     * @return - a lazy asynchronous action with the Answer containing the Role as payload or empty payload
     */
    @Principled
    @ConsumeEvent(EventAddress.ROLE_GET)
    public Uni<Answer> get(Request<UUID> request) {
        //noinspection DuplicatedCode
        return roleCacheProvider
                .get(request.payload())
                .map(Answer::of)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * The method provides the Answer's flow with all entries of Role
     *
     * @return - the Answer's Multi-flow with all entries of Role
     */
    public Multi<Answer> getAll() {
        //noinspection DuplicatedCode
        return roleDataService
                .getAll()
                .map(Answer::of);
    }

    @Principled
    @ConsumeEvent(EventAddress.ROLE_PAGE)
    public Uni<Page<Answer>> getPage(Request<PageRequest> request) {
        //noinspection DuplicatedCode
        return roleCacheProvider.getPage(request.payload());
    }

    /**
     * This is method a Vertx message consumer and Role updater
     *
     * @param request - Role
     * @return - a LAA with the Answer containing Role id as payload or empty payload
     */
    @Principled
    @ConsumeEvent(EventAddress.ROLE_PUT)
    public Uni<Answer> put(Request<Role> request) {
        //noinspection DuplicatedCode
        return roleDataService
                .put(request.payload())
                .map(this::apiResponseAcceptedAnswer)
                .flatMap(answer -> roleCacheProvider.invalidateById(request.payload().id(), answer))
                .onFailure(exceptionAnswerService::testDuplicateException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateAnswer)
                .onFailure(exceptionAnswerService::testIllegalArgumentException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer);
    }
}
