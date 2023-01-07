/*
 * This file was last modified at 2023.01.07 11:52 by Victor N. Skurikhin.
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
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.models.domain.Role;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.ExceptionAnswerService;
import su.svn.daybook.services.cache.RoleCacheProvider;
import su.svn.daybook.services.domain.RoleDataService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

@ApplicationScoped
public class RoleService extends AbstractService<UUID, Role> {

    private static final Logger LOG = Logger.getLogger(RoleService.class);

    @Inject
    ExceptionAnswerService exceptionAnswerService;

    @Inject
    RoleCacheProvider roleCacheProvider;

    @Inject
    RoleDataService roleDataService;

    /**
     * This is method a Vertx message consumer and Role creater
     *
     * @param o - Role
     * @return - a lazy asynchronous action (LAA) with the Answer containing the Role id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.ROLE_ADD)
    public Uni<Answer> add(Role o) {
        //noinspection DuplicatedCode
        LOG.tracef("add(%s)", o);
        return roleDataService
                .add(o)
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
     * @param id - id of the Role
     * @return - a LAA with the Answer containing Role id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.ROLE_DEL)
    public Uni<Answer> delete(UUID id) {
        //noinspection DuplicatedCode
        LOG.tracef("delete(%s)", id);
        return roleDataService
                .delete(id)
                .map(this::apiResponseOkAnswer)
                .flatMap(answer -> roleCacheProvider.invalidateById(id, answer))
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * This is method a Vertx message consumer and Role provider by id
     *
     * @param id - id of the Role
     * @return - a lazy asynchronous action with the Answer containing the Role as payload or empty payload
     */
    @ConsumeEvent(EventAddress.ROLE_GET)
    public Uni<Answer> get(UUID id) {
        //noinspection DuplicatedCode
        LOG.tracef("get(%s)", id);
        return roleCacheProvider
                .get(id)
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
        LOG.trace("getAll()");
        return roleDataService
                .getAll()
                .map(Answer::of);
    }

    @ConsumeEvent(EventAddress.ROLE_PAGE)
    public Uni<Page<Answer>> getPage(PageRequest pageRequest) {
        //noinspection DuplicatedCode
        LOG.tracef("getPage(%s)", pageRequest);
        return roleCacheProvider.getPage(pageRequest);
    }

    /**
     * This is method a Vertx message consumer and Role updater
     *
     * @param o - Role
     * @return - a LAA with the Answer containing Role id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.ROLE_PUT)
    public Uni<Answer> put(Role o) {
        //noinspection DuplicatedCode
        LOG.tracef("put(%s)", o);
        return roleDataService
                .put(o)
                .map(this::apiResponseAcceptedAnswer)
                .flatMap(answer -> roleCacheProvider.invalidateById(o.getId(), answer))
                .onFailure(exceptionAnswerService::testDuplicateException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateAnswer)
                .onFailure(exceptionAnswerService::testIllegalArgumentException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer);
    }
}
