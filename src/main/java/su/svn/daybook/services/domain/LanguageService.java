/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * LanguageService.java
 * $Id$
 */

package su.svn.daybook.services.domain;

import io.quarkus.cache.CacheKey;
import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.dao.LanguageDao;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.LanguageTable;
import su.svn.daybook.models.domain.Language;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.ExceptionAnswerService;
import su.svn.daybook.services.cache.LanguageCacheProvider;
import su.svn.daybook.services.mappers.LanguageMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.NoSuchElementException;

@ApplicationScoped
public class LanguageService extends AbstractService<Long, Language> {

    private static final Logger LOG = Logger.getLogger(LanguageService.class);

    @Inject
    LanguageCacheProvider languageCacheProvider;

    @Inject
    LanguageDao languageDao;

    @Inject
    LanguageMapper languageMapper;

    @Inject
    ExceptionAnswerService exceptionAnswerService;

    /**
     * This is method a Vertx message consumer and Language provider by id
     *
     * @param o - id of the Language
     * @return - a lazy asynchronous action with the Answer containing the Language as payload or empty payload
     */
    @ConsumeEvent(EventAddress.LANGUAGE_GET)
    public Uni<Answer> get(Object o) {
        //noinspection DuplicatedCode
        LOG.tracef("get(%s)", o);
        try {
            return getEntry(getIdLong(o));
        } catch (NumberFormatException e) {
            LOG.errorf("get(%s)", o, e);
            return Uni.createFrom().item(Answer.noNumber(e.getMessage()));
        } catch (NoSuchElementException e) {
            LOG.errorf("get(%s)", o, e);
            return Uni.createFrom().item(Answer.empty());
        }
    }

    /**
     * The method provides the Answer's flow with all entries of Language
     *
     * @return - the Answer's Multi-flow with all entries of Language
     */
    public Multi<Answer> getAll() {
        LOG.trace("getAll");
        return languageDao
                .count()
                .onItem()
                .transformToMulti(count -> getAllIfNotOverSize(count, this::getAllModels));
    }

    private Multi<Language> getAllModels() {
        return languageDao
                .findAll()
                .map(languageMapper::convertToModel);
    }

    private Uni<Answer> getEntry(Long id) {
        return languageCacheProvider
                .get(id)
                .map(Answer::of)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    @ConsumeEvent(value = EventAddress.LANGUAGE_PAGE)
    public Uni<Page<Answer>> getPage(PageRequest pageRequest) {
        //noinspection DuplicatedCode
        LOG.tracef("getPage(%s)", pageRequest);
        return languageCacheProvider.getPage(pageRequest);
    }

    /**
     * This is method a Vertx message consumer and Language creater
     *
     * @param o - Language
     * @return - a lazy asynchronous action (LAA) with the Answer containing the Language id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.LANGUAGE_ADD)
    public Uni<Answer> add(Language o) {
        LOG.tracef("add(%s)", o);
        return addEntry(languageMapper.convertToDomain(o));
    }

    private Uni<Answer> addEntry(LanguageTable entry) {
        return languageDao
                .insert(entry)
                .map(o -> apiResponseWithKeyAnswer(201, o))
                .flatMap(languageCacheProvider::invalidate)
                .onFailure(exceptionAnswerService::testDuplicateKeyException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateKeyValAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * This is method a Vertx message consumer and Language updater
     *
     * @param o - Language
     * @return - a LAA with the Answer containing Language id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.LANGUAGE_PUT)
    public Uni<Answer> put(Language o) {
        LOG.tracef("put(%s)", o);
        return putEntry(languageMapper.convertToDomain(o));
    }

    private Uni<Answer> putEntry(LanguageTable entry) {
        return languageDao
                .update(entry)
                .flatMap(this::apiResponseAcceptedUniAnswer)
                .flatMap(answer -> languageCacheProvider.invalidateById(entry.getId(), answer))
                .onFailure(exceptionAnswerService::testDuplicateKeyException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateKeyValAnswer)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(get(entry.getId()))
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * This is method a Vertx message consumer and Language deleter
     *
     * @param o - id of the Language
     * @return - a LAA with the Answer containing Language id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.LANGUAGE_DEL)
    public Uni<Answer> delete(Object o) {
        //noinspection DuplicatedCode
        LOG.tracef("delete(%s)", o);
        try {
            return deleteEntry(getIdLong(o));
        } catch (NumberFormatException e) {
            LOG.errorf("delete(%s)", o, e);
            return Uni.createFrom().item(Answer.noNumber(e.getMessage()));
        } catch (NoSuchElementException e) {
            LOG.errorf("delete(%s)", o, e);
            return Uni.createFrom().item(Answer.empty());
        }
    }

    private Uni<Answer> deleteEntry(Long id) {
        return languageDao
                .delete(id)
                .map(this::apiResponseWithKeyAnswer)
                .flatMap(answer -> languageCacheProvider.invalidateById(id, answer))
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }
}
