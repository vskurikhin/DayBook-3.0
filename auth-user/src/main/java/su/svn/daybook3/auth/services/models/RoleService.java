/*
 * This file was last modified at 2024-10-30 14:17 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * RoleService.java
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
import su.svn.daybook3.auth.models.domain.Role;
import su.svn.daybook3.auth.models.pagination.Page;
import su.svn.daybook3.auth.models.pagination.PageRequest;
import su.svn.daybook3.auth.services.cache.RoleCacheProvider;
import su.svn.daybook3.auth.services.domain.RoleDataService;
import su.svn.daybook3.domain.messages.Answer;
import su.svn.daybook3.domain.messages.Request;

import java.util.UUID;

@PrincipalLogging
@ApplicationScoped
public class RoleService
        extends AbstractService<UUID, Role>
        implements MultiAnswerAllService {

    @Inject
    RoleCacheProvider roleCacheProvider;

    @Inject
    RoleDataService roleDataService;

    /**
     * This is method a Vertx message consumer and Role creater
     *
     * @param request - Role
     * @return - a lazy asynchronous action (LAA) with the Answer containing the Role id as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionDuplicateAnswer
    @ConsumeEvent(EventAddress.ROLE_ADD)
    public Uni<Answer> add(Request<Role> request) {
        //noinspection DuplicatedCode
        return roleDataService
                .add(request.payload())
                .map(this::apiResponseCreatedAnswer)
                .flatMap(roleCacheProvider::invalidate);
    }

    /**
     * This is method a Vertx message consumer and Role deleter
     *
     * @param request - id of the Role
     * @return - a LAA with the Answer containing Role id as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.ROLE_DEL)
    public Uni<Answer> delete(Request<UUID> request) {
        //noinspection DuplicatedCode
        return roleDataService
                .delete(request.payload())
                .map(this::apiResponseOkAnswer)
                .flatMap(answer -> roleCacheProvider.invalidateByKey(request.payload(), answer));
    }

    /**
     * This is method a Vertx message consumer and Role provider by id
     *
     * @param request - id of the Role
     * @return - a lazy asynchronous action with the Answer containing the Role as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.ROLE_GET)
    public Uni<Answer> get(Request<UUID> request) {
        //noinspection DuplicatedCode
        return roleCacheProvider
                .get(request.payload())
                .map(Answer::of);
    }

    /**
     * The method provides the Answer's flow with all entries of Role
     *
     * @return - the Answer's Multi-flow with all entries of Role
     */
    @Override
    public Multi<Answer> getAll() {
        //noinspection DuplicatedCode
        return roleDataService
                .getAll()
                .map(Answer::of);
    }

    @ExceptionBadRequestAnswer
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
    @ExceptionBadRequestAnswer
    @ExceptionDuplicateAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.ROLE_PUT)
    public Uni<Answer> put(Request<Role> request) {
        //noinspection DuplicatedCode
        return roleDataService
                .put(request.payload())
                .map(this::apiResponseAcceptedAnswer)
                .flatMap(answer -> roleCacheProvider.invalidateByKey(request.payload().id(), answer));
    }
}
