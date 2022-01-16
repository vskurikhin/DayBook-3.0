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
import su.svn.daybook.domain.messages.ApiResponse;
import su.svn.daybook.domain.model.Vocabulary;

import javax.annotation.Nonnull;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class VocabularyService {

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
    public Uni<Answer> vocabularyGet(Object o) {
        var methodCall = String.format("vocabularyGet(%s)", o);
        if (o instanceof String) {
            try {
                return get(Long.parseLong(o.toString()));
            } catch (NumberFormatException e) {
                LOG.error(methodCall, e);
                var numberError = new Answer(e.getMessage(), 404);
                return Uni.createFrom().item(numberError);
            }
        }
        return Uni.createFrom().item(Answer.empty());
    }

    private Uni<Answer> get(Long id) {
        return vocabularyDao.findById(id)
                .map(t -> t.isEmpty() ? Answer.empty() : Answer.of(t));
    }

    /**
     * This is method a Vertx message consumer and Vocabulary creater
     *
     * @param o - Vocabulary
     * @return - a lazy asynchronous action (LAA) with the Answer containing the Vocabulary id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.VOCABULARY_ADD)
    public Uni<Answer> vocabularyAdd(Vocabulary o) {
        LOG.tracef("vocabularyAdd(%s)", o);
        return add(o);
    }

    private Uni<Answer> add(Vocabulary entry) {
        return vocabularyDao.insert(entry)
                .map(o -> o.isEmpty() ? Answer.empty() : Answer.of(new ApiResponse<>(o.get())));
    }

    /**
     * This is method a Vertx message consumer and Vocabulary updater
     *
     * @param o - Vocabulary
     * @return - a LAA with the Answer containing Vocabulary id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.VOCABULARY_PUT)
    public Uni<Answer> vocabularyPut(Vocabulary o) {
        LOG.tracef("vocabularyPut(%s)", o);
        return put(o);
    }

    private Uni<Answer> put(Vocabulary entry) {
        return vocabularyDao.update(entry)
                .map(o -> o.isEmpty() ? Answer.empty() : Answer.of(new ApiResponse<>(o.get())));
    }

    /**
     * This is method a Vertx message consumer and Vocabulary deleter
     *
     * @param o - id of the Vocabulary
     * @return - a LAA with the Answer containing Vocabulary id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.VOCABULARY_DEL)
    public Uni<Answer> vocabularyDelete(Object o) {
        var methodCall = String.format("vocabularyDelete(%s)", o);
        if (o instanceof String) {
            try {
                return delete(Long.parseLong(o.toString()));
            } catch (NumberFormatException e) {
                LOG.error(methodCall, e);
                var numberError = new Answer(e.getMessage(), 404);
                return Uni.createFrom().item(numberError);
            }
        }
        return Uni.createFrom().item(Answer.empty());
    }

    private Uni<Answer> delete(long id) {
        return vocabularyDao.delete(id)
                .map(o -> o.isEmpty() ? Answer.empty() : Answer.of(new ApiResponse<>(o.get())));
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

    private Answer getAnswer(@Nonnull Vocabulary Vocabulary) {
        LOG.tracef("getAnswer Vocabulary: %s", Vocabulary);
        return Answer.of(Vocabulary);
    }
}
