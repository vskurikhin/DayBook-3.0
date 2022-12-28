/*
 * This file was last modified at 2022.01.11 17:44 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * UserNameService.java
 * $Id$
 */

package su.svn.daybook.services;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.dao.UserNameDao;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.UserName;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UserNameService extends AbstractService<UUID, UserName> {

    private static final Logger LOG = Logger.getLogger(UserNameService.class);

    @Inject
    UserNameDao userNameDao;

    /**
     * This is method a Vertx message consumer and UserName provider by id
     *
     * @param o - id of the UserName
     * @return - a lazy asynchronous action with the Answer containing the UserName as payload or empty payload
     */
    @ConsumeEvent(EventAddress.USER_GET)
    public Uni<Answer> get(Object o) {
        LOG.infof("get(%s)", o);
        if (o instanceof UUID id) {
            return getEntry(id);
        }
        if (o instanceof String id) {
            return getEntry(UUID.fromString(id));
        }
        return Uni.createFrom().item(Answer.empty());
    }

    private Uni<Answer> getEntry(UUID id) {
        return userNameDao.findById(id)
                .map(this::getAnswerApiResponseWithValue)
                .onFailure(onFailureNoSuchElementPredicate())
                .recoverWithUni(this::toNoSuchElementAnswer)
                .onFailure(onFailurePredicate())
                .recoverWithItem(new Answer("bad request", 400));
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
        return addEntry(o);
    }

    private Uni<Answer> addEntry(UserName entry) {
        return userNameDao.insert(entry)
                .map(o -> getAnswerApiResponseWithKey(201, o))
                .onFailure(onFailureDuplicatePredicate())
                .recoverWithUni(this::toDuplicateKeyValueAnswer)
                .onFailure(onFailurePredicate())
                .recoverWithUni(get(entry.getId()));
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
        return putEntry(o);
    }

    private Uni<Answer> putEntry(UserName entry) {
        return userNameDao.update(entry)
                .flatMap(id -> this.getAnswerForPut(id, entry))
                .onFailure(onFailureDuplicatePredicate())
                .recoverWithUni(this::toDuplicateKeyValueAnswer)
                .onFailure(onFailurePredicate())
                .recoverWithUni(get(entry.getId()))
                .onFailure(onFailureNoSuchElementPredicate())
                .recoverWithUni(this::toNoSuchElementAnswer);
    }

    private Uni<Answer> getAnswerForPut(Optional<UUID> o, UserName entry) {
        if (o.isEmpty()) {
            throw new NoSuchElementException();
        }
        return Uni.createFrom().item(getAnswerApiResponseWithKey(202, o));
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
        if (o instanceof String id) {
            return deleteEntry(UUID.fromString(id));
        }
        return Uni.createFrom().item(Answer.empty());
    }

    private Uni<Answer> deleteEntry(UUID id) {
        return userNameDao.delete(id)
                .map(this::getAnswerApiResponseWithKey)
                .onFailure(onFailureNoSuchElementPredicate())
                .recoverWithUni(this::toNoSuchElementAnswer)
                .onFailure(onFailurePredicate())
                .recoverWithItem(new Answer("bad request", 400));
    }

    /**
     * The method provides the Answer's flow with all entries of UserName
     *
     * @return - the Answer's Multi-flow with all entries of UserName
     */
    public Multi<Answer> getAll() {
        LOG.trace("getAll");
        return userNameDao.findAll()
                .onItem()
                .transform(this::getAnswer);
    }
}
