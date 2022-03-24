/*
 * This file was last modified at 2022.03.24 13:27 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * WordService.java
 * $Id$
 */

package su.svn.daybook.services;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.dao.WordDao;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.messages.ApiResponse;
import su.svn.daybook.domain.model.Word;

import javax.annotation.Nonnull;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class WordService {

    private static final Logger LOG = Logger.getLogger(WordService.class);

    @Inject
    WordDao wordDao;

    /**
     * This is method a Vertx message consumer and Word provider by id
     *
     * @param o - id of the Word
     * @return - a lazy asynchronous action with the Answer containing the Word as payload or empty payload
     */
    @ConsumeEvent(EventAddress.WORD_GET)
    public Uni<Answer> wordGet(Object o) {
        var methodCall = String.format("wordGet(%s)", o);
        if (o instanceof String) {
            return get(o.toString());
        }
        return Uni.createFrom().item(Answer.empty());
    }

    private Uni<Answer> get(String word) {
        return wordDao.findByWord(word)
                .map(t -> t.isEmpty() ? Answer.empty() : Answer.of(t));
    }

    /**
     * This is method a Vertx message consumer and Word creater
     *
     * @param o - Word
     * @return - a lazy asynchronous action (LAA) with the Answer containing the Word id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.WORD_ADD)
    public Uni<Answer> wordAdd(Word o) {
        LOG.tracef("wordAdd(%s)", o);
        return add(o);
    }

    private Uni<Answer> add(Word entry) {
        return wordDao.insert(entry)
                .map(o -> o.isEmpty() ? Answer.empty() : Answer.of(new ApiResponse<>(o.get())));
    }

    /**
     * This is method a Vertx message consumer and Word updater
     *
     * @param o - Word
     * @return - a LAA with the Answer containing Word id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.WORD_PUT)
    public Uni<Answer> wordPut(Word o) {
        LOG.tracef("wordPut(%s)", o);
        return put(o);
    }

    private Uni<Answer> put(Word entry) {
        return wordDao.update(entry)
                .map(o -> o.isEmpty() ? Answer.empty() : Answer.of(new ApiResponse<>(o.get())));
    }

    /**
     * This is method a Vertx message consumer and Word deleter
     *
     * @param o - id of the Word
     * @return - a LAA with the Answer containing Word id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.WORD_DEL)
    public Uni<Answer> wordDelete(Object o) {
        var methodCall = String.format("wordDelete(%s)", o);
        if (o instanceof String) {
            return delete(o.toString());
        }
        return Uni.createFrom().item(Answer.empty());
    }

    private Uni<Answer> delete(String word) {
        return wordDao.delete(word)
                .map(o -> o.isEmpty() ? Answer.empty() : Answer.of(new ApiResponse<>(o.get())));
    }

    /**
     * The method provides the Answer's flow with all entries of Word
     *
     * @return - the Answer's Multi-flow with all entries of Word
     */
    public Multi<Answer> getAll() {
        LOG.trace("getAll");
        return wordDao.findAll()
                .onItem()
                .transform(this::getAnswer);
    }

    private Answer getAnswer(@Nonnull Word Word) {
        LOG.tracef("getAnswer Word: %s", Word);
        return Answer.of(Word);
    }
}
