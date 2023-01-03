/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * VocabularyService.java
 * $Id$
 */

package su.svn.daybook.services.domain;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.dao.VocabularyDao;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.VocabularyTable;
import su.svn.daybook.models.domain.Vocabulary;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.ExceptionAnswerService;
import su.svn.daybook.services.cache.VocabularyCacheProvider;
import su.svn.daybook.services.mappers.VocabularyMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.NoSuchElementException;

@ApplicationScoped
public class VocabularyService extends AbstractService<Long, Vocabulary> {

    private static final Logger LOG = Logger.getLogger(VocabularyService.class);

    @Inject
    VocabularyCacheProvider vocabularyCacheProvider;

    @Inject
    VocabularyDao vocabularyDao;

    @Inject
    VocabularyMapper vocabularyMapper;

    @Inject
    ExceptionAnswerService exceptionAnswerService;

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
            return getEntry(getIdLong(o));
        } catch (NumberFormatException e) {
            LOG.errorf("get(%s)", o, e);
            var numberError = new Answer(e.getMessage(), 404);
            return Uni.createFrom().item(numberError);
        } catch (NoSuchElementException e) {
            LOG.errorf("get(%s)", o, e);
            return Uni.createFrom().item(Answer.empty());
        }
    }

    /**
     * The method provides the Answer's flow with all entries of Vocabulary
     *
     * @return - the Answer's Multi-flow with all entries of Vocabulary
     */
    public Multi<Answer> getAll() {
        LOG.trace("getAll");
        return vocabularyDao
                .count()
                .onItem()
                .transformToMulti(count -> getAllIfNotOverSize(count, this::getAllModels));
    }

    private Multi<Vocabulary> getAllModels() {
        return vocabularyDao
                .findAll()
                .map(vocabularyMapper::convertToModel);
    }

    private Uni<Answer> getEntry(long id) {
        return vocabularyCacheProvider
                .get(id)
                .map(Answer::of)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    @ConsumeEvent(value = EventAddress.VOCABULARY_PAGE)
    public Uni<Page<Answer>> getPage(PageRequest pageRequest) {
        //noinspection DuplicatedCode
        LOG.tracef("getPage(%s)", pageRequest);
        return vocabularyCacheProvider.getPage(pageRequest);
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
        return addEntry(vocabularyMapper.convertToDomain(o));
    }

    private Uni<Answer> addEntry(VocabularyTable entry) {
        return vocabularyDao
                .insert(entry)
                .map(o -> apiResponseAnswer(201, o))
                .flatMap(vocabularyCacheProvider::invalidate)
                .onFailure(exceptionAnswerService::testDuplicateException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
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
        return putEntry(vocabularyMapper.convertToDomain(o));
    }

    private Uni<Answer> putEntry(VocabularyTable entry) {
        return vocabularyDao
                .update(entry)
                .flatMap(this::apiResponseAcceptedUniAnswer)
                .flatMap(answer -> vocabularyCacheProvider.invalidateById(entry.getId(), answer))
                .onFailure(exceptionAnswerService::testDuplicateException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateAnswer)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(get(entry.getId()))
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
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
            return deleteEntry(getIdLong(o));
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
        return vocabularyDao
                .delete(id)
                .map(this::apiResponseAnswer)
                .flatMap(answer -> vocabularyCacheProvider.invalidateById(id, answer))
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }
}
