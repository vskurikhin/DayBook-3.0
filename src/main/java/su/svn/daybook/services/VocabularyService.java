/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * VocabularyService.java
 * $Id$
 */

package su.svn.daybook.services;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.dao.VocabularyDao;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.Vocabulary;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.NoSuchElementException;

@ApplicationScoped
public class VocabularyService extends AbstractService<Long, Vocabulary> {

    private static final Logger LOG = Logger.getLogger(VocabularyService.class);

    @Inject
    VocabularyDao vocabularyDao;

    /**
     * This is method a Vertx message consumer and Vocabulary provider by id
     *
     * @param o - id of the Vocabulary
     * @return - a lazy asynchronous action with the Answer containing the Vocabulary as payload or empty payload
     */
    @ConsumeEvent(EventAddress.VOCABULARY_GET)
    public Uni<Answer> get(Object o) {
        LOG.tracef("get(%s)", o);
        try {
            return getEntry(getId(o));
        } catch (NumberFormatException e) {
            LOG.errorf("get(%s)", o, e);
            var numberError = new Answer(e.getMessage(), 404);
            return Uni.createFrom().item(numberError);
        } catch (NoSuchElementException e) {
            LOG.errorf("get(%s)", o, e);
            return Uni.createFrom().item(Answer.empty());
        }
    }

    private Uni<Answer> getEntry(long id) {
        return vocabularyDao.findById(id)
                .map(this::getAnswerApiResponseWithValue)
                .onFailure(onFailureNoSuchElementPredicate())
                .recoverWithUni(this::toNoSuchElementAnswer)
                .onFailure(onFailurePredicate())
                .recoverWithItem(new Answer("bad request", 400));
    }

    /**
     * This is method a Vertx message consumer and Vocabulary creater
     *
     * @param o - Vocabulary
     * @return - a lazy asynchronous action (LAA) with the Answer containing the Vocabulary id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.VOCABULARY_ADD)
    public Uni<Answer> add(Vocabulary o) {
        LOG.tracef("add(%s)", o);
        return addEntry(o);
    }

    private Uni<Answer> addEntry(Vocabulary entry) {
        return vocabularyDao.insert(entry)
                .map(o -> getAnswerApiResponseWithKey(201, o))
                .onFailure(onFailureDuplicatePredicate())
                .recoverWithUni(this::toDuplicateKeyValueAnswer)
                .onFailure(onFailurePredicate())
                .recoverWithUni(get(entry.getId()));
    }

    /**
     * This is method a Vertx message consumer and Vocabulary updater
     *
     * @param o - Vocabulary
     * @return - a LAA with the Answer containing Vocabulary id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.VOCABULARY_PUT)
    public Uni<Answer> put(Vocabulary o) {
        LOG.tracef("put(%s)", o);
        return putEntry(o);
    }

    private Uni<Answer> putEntry(Vocabulary entry) {
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
     * This is method a Vertx message consumer and Vocabulary deleter
     *
     * @param o - id of the Vocabulary
     * @return - a LAA with the Answer containing Vocabulary id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.VOCABULARY_DEL)
    public Uni<Answer> delete(Object o) {
        LOG.tracef("delete(%s)", o);
        try {
            return deleteEntry(getId(o));
        } catch (NumberFormatException e) {
            LOG.errorf("delete(%s)", o, e);
            var numberError = new Answer(e.getMessage(), 404);
            return Uni.createFrom().item(numberError);
        } catch (NoSuchElementException e) {
            LOG.errorf("delete(%s)", o, e);
            return Uni.createFrom().item(Answer.empty());
        }
    }

    private Uni<Answer> deleteEntry(long id) {
        return vocabularyDao.delete(id)
                .map(this::getAnswerApiResponseWithKey)
                .onFailure(onFailureNoSuchElementPredicate())
                .recoverWithUni(this::toNoSuchElementAnswer)
                .onFailure(onFailurePredicate())
                .recoverWithItem(new Answer("bad request", 400));
    }

    /**
     * The method provides the Answer's flow with all entries of Vocabulary
     *
     * @return - the Answer's Multi-flow with all entries of Vocabulary
     */
    public Multi<Answer> getAll() {
        LOG.trace("getAll");
        return vocabularyDao.findAll()
                .onItem()
                .transform(this::getAnswer);
    }
}
