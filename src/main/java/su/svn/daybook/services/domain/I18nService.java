/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * I18nService.java
 * $Id$
 */

package su.svn.daybook.services.domain;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.dao.I18nDao;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.I18nTable;
import su.svn.daybook.models.domain.I18n;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.ExceptionAnswerService;
import su.svn.daybook.services.cache.I18nCacheProvider;
import su.svn.daybook.services.mappers.I18nMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.NoSuchElementException;

@ApplicationScoped
public class I18nService extends AbstractService<Long, I18n> {

    private static final Logger LOG = Logger.getLogger(I18nService.class);

    @Inject
    I18nCacheProvider i18nCacheProvider;

    @Inject
    I18nDao i18nDao;

    @Inject
    I18nMapper i18nMapper;

    @Inject
    ExceptionAnswerService exceptionAnswerService;

    /**
     * This is method a Vertx message consumer and I18n provider by id
     *
     * @param o - id of the I18n
     * @return - a lazy asynchronous action with the Answer containing the I18n as payload or empty payload
     */
    @ConsumeEvent(EventAddress.I18N_GET)
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
     * The method provides the Answer's flow with all entries of I18n
     *
     * @return - the Answer's Multi-flow with all entries of I18n
     */
    public Multi<Answer> getAll() {
        LOG.trace("getAll");
        return i18nDao
                .count()
                .onItem()
                .transformToMulti(count -> getAllIfNotOverSize(count, this::getAllModels));
    }

    private Multi<I18n> getAllModels() {
        return i18nDao
                .findAll()
                .map(i18nMapper::convertToModel);
    }

    private Uni<Answer> getEntry(Long id) {
        return i18nCacheProvider
                .get(id)
                .map(Answer::of)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    @ConsumeEvent(value = EventAddress.I18N_PAGE)
    public Uni<Page<Answer>> getPage(PageRequest pageRequest) {
        //noinspection DuplicatedCode
        LOG.tracef("getPage(%s)", pageRequest);
        return i18nCacheProvider.getPage(pageRequest);
    }

    /**
     * This is method a Vertx message consumer and I18n creater
     *
     * @param o - I18n
     * @return - a lazy asynchronous action (LAA) with the Answer containing the I18n id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.I18N_ADD)
    public Uni<Answer> add(I18n o) {
        LOG.tracef("add(%s)", o);
        return addEntry(i18nMapper.convertToDomain(o));
    }

    private Uni<Answer> addEntry(I18nTable entry) {
        return i18nDao
                .insert(entry)
                .map(o -> apiResponseWithKeyAnswer(201, o))
                .flatMap(i18nCacheProvider::invalidate)
                .onFailure(exceptionAnswerService::testDuplicateKeyException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateKeyValAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * This is method a Vertx message consumer and I18n updater
     *
     * @param o - I18n
     * @return - a LAA with the Answer containing I18n id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.I18N_PUT)
    public Uni<Answer> put(I18n o) {
        LOG.tracef("put(%s)", o);
        return putEntry(i18nMapper.convertToDomain(o));
    }

    private Uni<Answer> putEntry(I18nTable entry) {
        return i18nDao
                .update(entry)
                .flatMap(this::apiResponseAcceptedUniAnswer)
                .flatMap(answer -> i18nCacheProvider.invalidateById(entry.getId(), answer))
                .onFailure(exceptionAnswerService::testDuplicateKeyException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateKeyValAnswer)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(get(entry.getId()))
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * This is method a Vertx message consumer and I18n deleter
     *
     * @param o - id of the I18n
     * @return - a LAA with the Answer containing I18n id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.I18N_DEL)
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
        return i18nDao
                .delete(id)
                .map(this::apiResponseWithKeyAnswer)
                .flatMap(answer -> i18nCacheProvider.invalidateById(id, answer))
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }
}
