/*
 * This file was last modified at 2022.01.11 17:44 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * UserService.java
 * $Id$
 */

package su.svn.daybook.services.domain;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.dao.UserNameDao;
import su.svn.daybook.domain.dao.UserViewDao;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.UserNameTable;
import su.svn.daybook.domain.transact.UserTransactionalJob;
import su.svn.daybook.models.domain.User;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.ExceptionAnswerService;
import su.svn.daybook.services.cache.UserCacheProvider;
import su.svn.daybook.services.mappers.UserMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@ApplicationScoped
public class UserService extends AbstractService<UUID, User> {

    private static final Logger LOG = Logger.getLogger(UserService.class);

    @Inject
    UserCacheProvider userCacheProvider;

    @Inject
    UserNameDao userNameDao;

    @Inject
    UserViewDao userViewDao;

    @Inject
    UserMapper userMapper;

    @Inject
    UserTransactionalJob userTransactionalJob;

    @Inject
    ExceptionAnswerService exceptionAnswerService;

    /**
     * This is method a Vertx message consumer and User provider by id
     *
     * @param o - id of the User
     * @return - a lazy asynchronous action with the Answer containing the User as payload or empty payload
     */
    @ConsumeEvent(EventAddress.USER_GET)
    public Uni<Answer> get(UUID id) {
        LOG.infof("get(%s)", id);
        return getEntry(id);
    }

    /**
     * The method provides the Answer's flow with all entries of User
     *
     * @return - the Answer's Multi-flow with all entries of User
     */
    public Multi<Answer> getAll() {
        LOG.trace("getAll");
        return userViewDao
                .count()
                .onItem()
                .transformToMulti(count -> getAllIfNotOverSize(count, this::getAllModels));
    }

    private Multi<User> getAllModels() {
        return userViewDao
                .findAll()
                .map(userMapper::convertToModel);
    }

    private Uni<Answer> getEntry(UUID id) {
        return userCacheProvider
                .get(id)
                .map(Answer::of)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    @ConsumeEvent(value = EventAddress.USER_PAGE)
    public Uni<Page<Answer>> getPage(PageRequest pageRequest) {
        //noinspection DuplicatedCode
        LOG.tracef("getPage(%s)", pageRequest);
        return userCacheProvider.getPage(pageRequest);
    }

    /**
     * This is method a Vertx message consumer and User creater
     *
     * @param o - User
     * @return - a lazy asynchronous action (LAA) with the Answer containing the User id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.USER_ADD)
    public Uni<Answer> add(User o) {
        LOG.infof("add(%s)", o);
        return addUserAndRoles(userMapper.convertToUserNameTable(o), o.getRoles());
    }

    private Uni<Answer> addUserAndRoles(UserNameTable entry, Set<String> roles) {
        LOG.infof("addUserAndRoles(%s, %s)", entry, roles);
        return userTransactionalJob
                .insert(entry, roles, UserNameTable::getUserName)
                .map(uuid -> apiResponseAnswer(201, uuid))
                .flatMap(userCacheProvider::invalidate)
                .onFailure(exceptionAnswerService::testDuplicateException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
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
        return putEntry(userMapper.convertToUserNameTable(o), o.getRoles());
    }

    private Uni<Answer> putEntry(UserNameTable entry, Set<String> roles) {
        return userTransactionalJob
                .update(entry, roles, UserNameTable::getUserName)
                .flatMap(this::apiResponseAcceptedUniAnswer)
                .flatMap(answer -> userCacheProvider.invalidateById(entry.getId(), answer))
                .onFailure(exceptionAnswerService::testCompositeException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer)
                .onFailure(exceptionAnswerService::testDuplicateException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateAnswer)
                .onFailure(exceptionAnswerService::testIllegalArgumentException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(get(entry.getId()));
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
        return deleteEntry(id);
    }

    private Uni<Answer> deleteEntry(UUID id) {
        return userNameDao
                .findById(id)
                .map(Optional::get)
                .flatMap(table -> userTransactionalJob.delete(table, UserNameTable::getUserName))
                .map(this::apiResponseAnswer)
                .flatMap(answer -> userCacheProvider.invalidateById(id, answer))
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }
}
