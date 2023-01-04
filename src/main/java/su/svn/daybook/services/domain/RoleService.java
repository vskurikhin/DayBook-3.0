/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * RoleService.java
 * $Id$
 */

package su.svn.daybook.services.domain;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.dao.RoleDao;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.RoleTable;
import su.svn.daybook.models.domain.Role;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.ExceptionAnswerService;
import su.svn.daybook.services.cache.RoleCacheProvider;
import su.svn.daybook.services.mappers.RoleMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.NoSuchElementException;
import java.util.UUID;

@ApplicationScoped
public class RoleService extends AbstractService<UUID, Role> {

    private static final Logger LOG = Logger.getLogger(RoleService.class);

    @Inject
    RoleCacheProvider roleCacheProvider;

    @Inject
    RoleDao roleDao;

    @Inject
    RoleMapper roleMapper;

    @Inject
    ExceptionAnswerService exceptionAnswerService;

    /**
     * This is method a Vertx message consumer and Role provider by id
     *
     * @param o - id of the Role
     * @return - a lazy asynchronous action with the Answer containing the Role as payload or empty payload
     */
    @ConsumeEvent(EventAddress.ROLE_GET)
    public Uni<Answer> get(Object o) {
        //noinspection DuplicatedCode
        LOG.tracef("get(%s)", o);
        try {
            return getEntry(getIdUUID(o));
        } catch (NumberFormatException e) {
            LOG.errorf("get(%s)", o, e);
            return exceptionAnswerService.getUniAnswerNoNumber(e);
        } catch (NoSuchElementException e) {
            LOG.errorf("get(%s)", o, e);
            return Uni.createFrom().item(Answer.empty());
        }
    }

    /**
     * The method provides the Answer's flow with all entries of Role
     *
     * @return - the Answer's Multi-flow with all entries of Role
     */
    public Multi<Answer> getAll() {
        //noinspection DuplicatedCode
        LOG.trace("getAll()");
        return roleDao
                .count()
                .onItem()
                .transformToMulti(count -> getAllIfNotOverSize(count, this::getAllModels));
    }

    private Multi<Role> getAllModels() {
        return roleDao
                .findAll()
                .map(roleMapper::convertToModel);
    }

    private Uni<Answer> getEntry(UUID id) {
        return roleCacheProvider
                .get(id)
                .map(Answer::of)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    @ConsumeEvent(EventAddress.ROLE_PAGE)
    public Uni<Page<Answer>> getPage(PageRequest pageRequest) {
        //noinspection DuplicatedCode
        LOG.tracef("getPage(%s)", pageRequest);
        return roleCacheProvider.getPage(pageRequest);
    }

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
        return addEntry(roleMapper.convertToDomain(o));
    }

    private Uni<Answer> addEntry(RoleTable entry) {
        return roleDao
                .insert(entry)
                .map(o -> apiResponseAnswer(201, o))
                .flatMap(roleCacheProvider::invalidate)
                .onFailure(exceptionAnswerService::testDuplicateException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
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
        return putEntry(roleMapper.convertToDomain(o));
    }

    private Uni<Answer> putEntry(RoleTable entry) {
        return roleDao
                .update(entry)
                .flatMap(this::apiResponseAcceptedUniAnswer)
                .flatMap(answer -> roleCacheProvider.invalidateById(entry.getId(), answer))
                .onFailure(exceptionAnswerService::testDuplicateException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateAnswer)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(get(entry.getId()))
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * This is method a Vertx message consumer and Role deleter
     *
     * @param o - id of the Role
     * @return - a LAA with the Answer containing Role id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.ROLE_DEL)
    public Uni<Answer> delete(Object o) {
        //noinspection DuplicatedCode
        LOG.tracef("delete(%s)", o);
        try {
            return deleteEntry(getIdUUID(o));
        } catch (NumberFormatException e) {
            LOG.errorf("delete(%s)", o, e);
            return exceptionAnswerService.getUniAnswerNoNumber(e);
        } catch (NoSuchElementException e) {
            LOG.errorf("delete(%s)", o, e);
            return Uni.createFrom().item(Answer.empty());
        }
    }

    private Uni<Answer> deleteEntry(UUID id) {
        return roleDao
                .delete(id)
                .map(this::apiResponseAnswer)
                .flatMap(answer -> roleCacheProvider.invalidateById(id, answer))
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }
}
