/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * TagLabelService.java
 * $Id$
 */

package su.svn.daybook.services;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.dao.TagLabelDao;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.TagLabel;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.NoSuchElementException;

@ApplicationScoped
public class TagLabelService extends AbstractService<String, TagLabel> {

    private static final Logger LOG = Logger.getLogger(TagLabelService.class);

    @Inject
    TagLabelDao vocabularyDao;

    /**
     * This is method a Vertx message consumer and TagLabel provider by id
     *
     * @param o - id of the TagLabel
     * @return - a lazy asynchronous action with the Answer containing the TagLabel as payload or empty payload
     */
    @ConsumeEvent(EventAddress.TAG_LABEL_GET)
    public Uni<Answer> get(Object o) {
        LOG.tracef("get(%s)", o);
        try {
            return getEntry(getIdString(o));
        } catch (NoSuchElementException e) {
            LOG.errorf("get(%s)", o, e);
            return Uni.createFrom().item(Answer.empty());
        }
    }

    private Uni<Answer> getEntry(String id) {
        return vocabularyDao.findById(id)
                .map(this::getAnswerApiResponseWithValue)
                .onFailure(onFailureNoSuchElementPredicate())
                .recoverWithUni(this::toNoSuchElementAnswer)
                .onFailure(onFailurePredicate())
                .recoverWithItem(new Answer("bad request", 400));
    }

    /**
     * This is method a Vertx message consumer and TagLabel creater
     *
     * @param o - TagLabel
     * @return - a lazy asynchronous action (LAA) with the Answer containing the TagLabel id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.TAG_LABEL_ADD)
    public Uni<Answer> add(TagLabel o) {
        LOG.tracef("add(%s)", o);
        return addEntry(o);
    }

    private Uni<Answer> addEntry(TagLabel entry) {
        return vocabularyDao.insert(entry)
                .map(o -> getAnswerApiResponseWithKey(201, o))
                .onFailure(onFailureDuplicatePredicate())
                .recoverWithUni(this::toDuplicateKeyValueAnswer)
                .onFailure(onFailurePredicate())
                .recoverWithUni(get(entry.getId()));
    }

    /**
     * This is method a Vertx message consumer and TagLabel updater
     *
     * @param o - TagLabel
     * @return - a LAA with the Answer containing TagLabel id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.TAG_LABEL_PUT)
    public Uni<Answer> put(TagLabel o) {
        LOG.tracef("put(%s)", o);
        return putEntry(o);
    }

    private Uni<Answer> putEntry(TagLabel entry) {
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
     * This is method a Vertx message consumer and TagLabel deleter
     *
     * @param o - id of the TagLabel
     * @return - a LAA with the Answer containing TagLabel id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.TAG_LABEL_DEL)
    public Uni<Answer> delete(Object o) {
        LOG.tracef("delete(%s)", o);
        try {
            return deleteEntry(getIdString(o));
        } catch (NoSuchElementException e) {
            LOG.errorf("delete(%s)", o, e);
            return Uni.createFrom().item(Answer.empty());
        }
    }

    private Uni<Answer> deleteEntry(String id) {
        return vocabularyDao.delete(id)
                .map(this::getAnswerApiResponseWithKey)
                .onFailure(onFailureNoSuchElementPredicate())
                .recoverWithUni(this::toNoSuchElementAnswer)
                .onFailure(onFailurePredicate())
                .recoverWithItem(new Answer("bad request", 400));
    }

    /**
     * The method provides the Answer's flow with all entries of TagLabel
     *
     * @return - the Answer's Multi-flow with all entries of TagLabel
     */
    public Multi<Answer> getAll() {
        LOG.trace("getAll");
        return vocabularyDao.findAll()
                .onItem()
                .transform(this::getAnswer);
    }
}
