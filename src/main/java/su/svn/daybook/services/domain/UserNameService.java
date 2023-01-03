/*
 * This file was last modified at 2022.01.11 17:44 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * UserNameService.java
 * $Id$
 */

package su.svn.daybook.services.domain;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.dao.UserNameDao;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.UserNameTable;
import su.svn.daybook.models.domain.UserName;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.ExceptionAnswerService;
import su.svn.daybook.services.cache.UserNameCacheProvider;
import su.svn.daybook.services.mappers.UserNameMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.NoSuchElementException;
import java.util.UUID;

@ApplicationScoped
public class UserNameService extends AbstractService<UUID, UserName> {

    private static final Logger LOG = Logger.getLogger(UserNameService.class);

    @Inject
    UserNameCacheProvider userNameCacheProvider;

    @Inject
    UserNameDao userNameDao;

    @Inject
    UserNameMapper userNameMapper;

    @Inject
    ExceptionAnswerService exceptionAnswerService;

    /**
     * This is method a Vertx message consumer and UserName provider by id
     *
     * @param o - id of the UserName
     * @return - a lazy asynchronous action with the Answer containing the UserName as payload or empty payload
     */
    @ConsumeEvent(EventAddress.USER_GET)
    public Uni<Answer> get(Object o) {
        LOG.infof("get(%s)", o);
        try {
            return getEntry(getIdUUID(o));
        } catch (NoSuchElementException e) {
            LOG.errorf("get(%s)", o, e);
            return Uni.createFrom().item(Answer.empty());
        }
    }

    /**
     * The method provides the Answer's flow with all entries of UserName
     *
     * @return - the Answer's Multi-flow with all entries of UserName
     */
    public Multi<Answer> getAll() {
        LOG.trace("getAll");
        return userNameDao
                .count()
                .onItem()
                .transformToMulti(count -> getAllIfNotOverSize(count, this::getAllModels));
    }

    private Multi<UserName> getAllModels() {
        return userNameDao
                .findAll()
                .map(userNameMapper::convertToModel);
    }

    private Uni<Answer> getEntry(UUID id) {
        return userNameCacheProvider
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
        return userNameCacheProvider.getPage(pageRequest);
    }

    /**
     * This is method a Vertx message consumer and UserName creater
     *
     * @param o - UserName
     * @return - a lazy asynchronous action (LAA) with the Answer containing the UserName id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.USER_ADD)
    public Uni<Answer> add(UserName o) {
        LOG.infof("add(%s)", o);
        return addEntry(userNameMapper.convertToDomain(o));
    }

    private Uni<Answer> addEntry(UserNameTable entry) {
        return userNameDao
                .insert(entry)
                .map(o -> apiResponseAnswer(201, o))
                .flatMap(userNameCacheProvider::invalidate)
                .onFailure(exceptionAnswerService::testDuplicateException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * This is method a Vertx message consumer and UserName updater
     *
     * @param o - UserName
     * @return - a LAA with the Answer containing UserName id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.USER_PUT)
    public Uni<Answer> put(UserName o) {
        LOG.tracef("put(%s)", o);
        return putEntry(userNameMapper.convertToDomain(o));
    }

    private Uni<Answer> putEntry(UserNameTable entry) {
        return userNameDao
                .update(entry)
                .flatMap(this::apiResponseAcceptedUniAnswer)
                .flatMap(answer -> userNameCacheProvider.invalidateById(entry.getId(), answer))
                .onFailure(exceptionAnswerService::testDuplicateException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateAnswer)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(get(entry.getId()))
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * This is method a Vertx message consumer and UserName deleter
     *
     * @param o - id of the UserName
     * @return - a LAA with the Answer containing UserName id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.USER_DEL)
    public Uni<Answer> delete(Object o) {
        LOG.tracef("delete(%s)", o);
        if (o instanceof UUID id) {
            return deleteEntry(id);
        }
        if (o instanceof String sid) {
            return deleteEntry(UUID.fromString(sid));
        }
        return Uni.createFrom().item(Answer.empty());
    }

    private Uni<Answer> deleteEntry(UUID id) {
        return userNameDao
                .delete(id)
                .map(this::apiResponseAnswer)
                .flatMap(answer -> userNameCacheProvider.invalidateById(id, answer))
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }
}
