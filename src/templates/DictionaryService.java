/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * @Name@Service.java
 * $Id$
 */

package su.svn.daybook.services;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.dao.@Name@Dao;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.@Name@;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.NoSuchElementException;

@ApplicationScoped
public class @Name@Service extends AbstractService<@IdType@, @Name@> {

    private static final Logger LOG = Logger.getLogger(@Name@Service.class);

    @Inject
    @Name@Dao vocabularyDao;

    /**
     * This is method a Vertx message consumer and @Name@ provider by id
     *
     * @param o - id of the @Name@
     * @return - a lazy asynchronous action with the Answer containing the @Name@ as payload or empty payload
     */
    @ConsumeEvent(EventAddress.@TABLE@_GET)
    public Uni<Answer> get(Object o) {
        LOG.tracef("get(%s)", o);
        try {
            return getEntry(getId@IdType@(o));
        } catch (NumberFormatException e) {
            LOG.errorf("get(%s)", o, e);
            return Uni.createFrom().item(Answer.noNumber(e.getMessage()));
        } catch (NoSuchElementException e) {
            LOG.errorf("get(%s)", o, e);
            return Uni.createFrom().item(Answer.empty());
        }
    }

    private Uni<Answer> getEntry(@IdType@ id) {
        return vocabularyDao.findById(id)
                .map(this::getAnswerApiResponseWithValue)
                .onFailure(onFailureNoSuchElementPredicate())
                .recoverWithUni(this::toNoSuchElementAnswer)
                .onFailure(onFailurePredicate())
                .recoverWithItem(new Answer("bad request", 400));
    }

    /**
     * This is method a Vertx message consumer and @Name@ creater
     *
     * @param o - @Name@
     * @return - a lazy asynchronous action (LAA) with the Answer containing the @Name@ id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.@TABLE@_ADD)
    public Uni<Answer> add(@Name@ o) {
        LOG.tracef("add(%s)", o);
        return addEntry(o);
    }

    private Uni<Answer> addEntry(@Name@ entry) {
        return vocabularyDao.insert(entry)
                .map(o -> getAnswerApiResponseWithKey(201, o))
                .onFailure(onFailureDuplicatePredicate())
                .recoverWithUni(this::toDuplicateKeyValueAnswer)
                .onFailure(onFailurePredicate())
                .recoverWithUni(get(entry.getId()));
    }

    /**
     * This is method a Vertx message consumer and @Name@ updater
     *
     * @param o - @Name@
     * @return - a LAA with the Answer containing @Name@ id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.@TABLE@_PUT)
    public Uni<Answer> put(@Name@ o) {
        LOG.tracef("put(%s)", o);
        return putEntry(o);
    }

    private Uni<Answer> putEntry(@Name@ entry) {
        return vocabularyDao.update(entry)
                .flatMap(this::getAnswerForPut)
                .onFailure(onFailureDuplicatePredicate())
                .recoverWithUni(this::toDuplicateKeyValueAnswer)
                .onFailure(onFailurePredicate())
                .recoverWithUni(get(entry.getId()))
                .onFailure(onFailureNoSuchElementPredicate())
                .recoverWithUni(this::toNoSuchElementAnswer);
    }

    /**
     * This is method a Vertx message consumer and @Name@ deleter
     *
     * @param o - id of the @Name@
     * @return - a LAA with the Answer containing @Name@ id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.@TABLE@_DEL)
    public Uni<Answer> delete(Object o) {
        LOG.tracef("delete(%s)", o);
        try {
            return deleteEntry(getId@IdType@(o));
        } catch (NumberFormatException e) {
            LOG.errorf("delete(%s)", o, e);
            return Uni.createFrom().item(Answer.noNumber(e.getMessage()));
        } catch (NoSuchElementException e) {
            LOG.errorf("delete(%s)", o, e);
            return Uni.createFrom().item(Answer.empty());
        }
    }

    private Uni<Answer> deleteEntry(@IdType@ id) {
        return vocabularyDao.delete(id)
                .map(this::getAnswerApiResponseWithKey)
                .onFailure(onFailureNoSuchElementPredicate())
                .recoverWithUni(this::toNoSuchElementAnswer)
                .onFailure(onFailurePredicate())
                .recoverWithItem(new Answer("bad request", 400));
    }

    /**
     * The method provides the Answer's flow with all entries of @Name@
     *
     * @return - the Answer's Multi-flow with all entries of @Name@
     */
    public Multi<Answer> getAll() {
        LOG.trace("getAll");
        return vocabularyDao.findAll()
                .onItem()
                .transform(this::getAnswer);
    }
}
