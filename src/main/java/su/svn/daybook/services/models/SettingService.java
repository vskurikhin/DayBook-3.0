/*
 * This file was last modified at 2023.01.09 21:44 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * SettingService.java
 * $Id$
 */

package su.svn.daybook.services.models;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.models.domain.Setting;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.ExceptionAnswerService;
import su.svn.daybook.services.cache.SettingCacheProvider;
import su.svn.daybook.services.domain.SettingDataService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class SettingService extends AbstractService<Long, Setting> {

    private static final Logger LOG = Logger.getLogger(SettingService.class);

    @Inject
    ExceptionAnswerService exceptionAnswerService;

    @Inject
    SettingCacheProvider settingCacheProvider;

    @Inject
    SettingDataService settingDataService;

    /**
     * This is method a Vertx message consumer and Setting creater
     *
     * @param o - Setting
     * @return - a lazy asynchronous action (LAA) with the Answer containing the Setting id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.SETTING_ADD)
    public Uni<Answer> add(Setting o) {
        //noinspection DuplicatedCode
        LOG.tracef("add(%s)", o);
        return settingDataService
                .add(o)
                .map(this::apiResponseCreatedAnswer)
                .flatMap(settingCacheProvider::invalidate)
                .onFailure(exceptionAnswerService::testDuplicateException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);

    }

    /**
     * This is method a Vertx message consumer and Setting deleter
     *
     * @param id - id of the Setting
     * @return - a LAA with the Answer containing Setting id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.SETTING_DEL)
    public Uni<Answer> delete(Long id) {
        //noinspection DuplicatedCode
        LOG.tracef("delete(%s)", id);
        return settingDataService
                .delete(id)
                .map(this::apiResponseOkAnswer)
                .flatMap(answer -> settingCacheProvider.invalidateById(id, answer))
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * This is method a Vertx message consumer and Setting provider by id
     *
     * @param id - id of the Setting
     * @return - a lazy asynchronous action with the Answer containing the Setting as payload or empty payload
     */
    @ConsumeEvent(EventAddress.SETTING_GET)
    public Uni<Answer> get(Long id) {
        //noinspection DuplicatedCode
        LOG.tracef("get(%s)", id);
        return settingCacheProvider
                .get(id)
                .map(Answer::of)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * The method provides the Answer's flow with all entries of Setting
     *
     * @return - the Answer's Multi-flow with all entries of Setting
     */
    public Multi<Answer> getAll() {
        //noinspection DuplicatedCode
        LOG.trace("getAll()");
        return settingDataService
                .getAll()
                .map(Answer::of);
    }

    @ConsumeEvent(EventAddress.SETTING_PAGE)
    public Uni<Page<Answer>> getPage(PageRequest pageRequest) {
        //noinspection DuplicatedCode
        LOG.tracef("getPage(%s)", pageRequest);
        return settingCacheProvider.getPage(pageRequest);
    }

    /**
     * This is method a Vertx message consumer and Setting updater
     *
     * @param o - Setting
     * @return - a LAA with the Answer containing Setting id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.SETTING_PUT)
    public Uni<Answer> put(Setting o) {
        //noinspection DuplicatedCode
        LOG.tracef("put(%s)", o);
        return settingDataService
                .put(o)
                .map(this::apiResponseAcceptedAnswer)
                .flatMap(answer -> settingCacheProvider.invalidateById(o.getId(), answer))
                .onFailure(exceptionAnswerService::testDuplicateException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateAnswer)
                .onFailure(exceptionAnswerService::testIllegalArgumentException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer);
    }
}
