/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * WordService.java
 * $Id$
 */

package su.svn.daybook.services.domain;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.dao.WordDao;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.Word;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.ExceptionAnswerService;
import su.svn.daybook.services.PageService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.NoSuchElementException;

@ApplicationScoped
public class WordService extends AbstractService<String, Word> {

    private static final Logger LOG = Logger.getLogger(WordService.class);

    @Inject
    WordDao wordDao;

    @Inject
    ExceptionAnswerService exceptionAnswerService;

    @Inject
    PageService pageService;

    /**
     * This is method a Vertx message consumer and Word provider by id
     *
     * @param o - id of the Word
     * @return - a lazy asynchronous action with the Answer containing the Word as payload or empty payload
     */
    @ConsumeEvent(EventAddress.WORD_GET)
    public Uni<Answer> get(Object o) {
        //noinspection DuplicatedCode
        LOG.tracef("get(%s)", o);
        try {
            return getEntry(getIdString(o));
        } catch (NoSuchElementException e) {
            LOG.errorf("get(%s)", o, e);
            return Uni.createFrom().item(Answer.empty());
        }
    }

    /**
     * The method provides the Answer's flow with all entries of Word
     *
     * @return - the Answer's Multi-flow with all entries of Word
     */
    public Multi<Answer> getAll() {
        LOG.trace("getAll");
        return wordDao.count()
                .onItem()
                .transformToMulti(count -> getAllIfNotOverSize(count, () -> wordDao.findAll()));
    }

    private Uni<Answer> getEntry(String id) {
        return wordDao.findById(id)
                .map(this::apiResponseWithValueAnswer)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    @ConsumeEvent(value = EventAddress.WORD_PAGE)
    public Uni<Page<Answer>> getPage(PageRequest pageRequest) {
        //noinspection DuplicatedCode
        LOG.tracef("getPage(%s)", pageRequest);
        return pageService.getPage(pageRequest, wordDao::count, wordDao::findRange);
    }

    /**
     * This is method a Vertx message consumer and Word creater
     *
     * @param o - Word
     * @return - a lazy asynchronous action (LAA) with the Answer containing the Word id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.WORD_ADD)
    public Uni<Answer> add(Word o) {
        LOG.tracef("add(%s)", o);
        return addEntry(o);
    }

    private Uni<Answer> addEntry(Word entry) {
        return wordDao.insert(entry)
                .map(o -> apiResponseWithKeyAnswer(201, o))
                .onFailure(exceptionAnswerService::testDuplicateKeyException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateKeyValueAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * This is method a Vertx message consumer and Word updater
     *
     * @param o - Word
     * @return - a LAA with the Answer containing Word id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.WORD_PUT)
    public Uni<Answer> put(Word o) {
        LOG.tracef("put(%s)", o);
        return putEntry(o);
    }

    private Uni<Answer> putEntry(Word entry) {
        return wordDao.update(entry)
                .flatMap(this::apiResponseAcceptedUniAnswer)
                .onFailure(exceptionAnswerService::testDuplicateKeyException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateKeyValueAnswer)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(get(entry.getId()))
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * This is method a Vertx message consumer and Word deleter
     *
     * @param o - id of the Word
     * @return - a LAA with the Answer containing Word id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.WORD_DEL)
    public Uni<Answer> delete(Object o) {
        //noinspection DuplicatedCode
        LOG.tracef("delete(%s)", o);
        try {
            return deleteEntry(getIdString(o));
        } catch (NoSuchElementException e) {
            LOG.errorf("delete(%s)", o, e);
            return Uni.createFrom().item(Answer.empty());
        }
    }

    private Uni<Answer> deleteEntry(String id) {
        return wordDao.delete(id)
                .map(this::apiResponseWithKeyAnswer)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }
}
