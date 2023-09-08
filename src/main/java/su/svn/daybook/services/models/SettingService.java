/*
 * This file was last modified at 2023.09.07 16:35 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * SettingService.java
 * $Id$
 */

package su.svn.daybook.services.models;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import su.svn.daybook.annotations.ExceptionBadRequestAnswer;
import su.svn.daybook.annotations.ExceptionDuplicateAnswer;
import su.svn.daybook.annotations.ExceptionNoSuchElementAnswer;
import su.svn.daybook.annotations.PrincipalLogging;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.messages.Request;
import su.svn.daybook.models.domain.Setting;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.cache.SettingCacheProvider;
import su.svn.daybook.services.domain.SettingDataService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@PrincipalLogging
@ApplicationScoped
public class SettingService extends AbstractService<Long, Setting> {

    @Inject
    SettingCacheProvider settingCacheProvider;

    @Inject
    SettingDataService settingDataService;

    /**
     * This is method a Vertx message consumer and Setting creater
     *
     * @param request - Setting
     * @return - a lazy asynchronous action (LAA) with the Answer containing the Setting id as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionDuplicateAnswer
    @ConsumeEvent(EventAddress.SETTING_ADD)
    public Uni<Answer> add(Request<Setting> request) {
        //noinspection DuplicatedCode
        return settingDataService
                .add(request.payload())
                .map(this::apiResponseCreatedAnswer)
                .flatMap(settingCacheProvider::invalidate);
    }

    /**
     * This is method a Vertx message consumer and Setting deleter
     *
     * @param request - id of the Setting
     * @return - a LAA with the Answer containing Setting id as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.SETTING_DEL)
    public Uni<Answer> delete(Request<Long> request) {
        //noinspection DuplicatedCode
        return settingDataService
                .delete(request.payload())
                .map(this::apiResponseOkAnswer)
                .flatMap(answer -> settingCacheProvider.invalidateByKey(request.payload(), answer));
    }

    /**
     * This is method a Vertx message consumer and Setting provider by id
     *
     * @param request - id of the Setting
     * @return - a lazy asynchronous action with the Answer containing the Setting as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.SETTING_GET)
    public Uni<Answer> get(Request<Long> request) {
        //noinspection DuplicatedCode
        return settingCacheProvider
                .get(request.payload())
                .map(Answer::of);
    }

    /**
     * The method provides the Answer's flow with all entries of Setting
     *
     * @return - the Answer's Multi-flow with all entries of Setting
     */
    public Multi<Answer> getAll() {
        //noinspection DuplicatedCode
        return settingDataService
                .getAll()
                .map(Answer::of);
    }

    @ExceptionBadRequestAnswer
    @ConsumeEvent(EventAddress.SETTING_PAGE)
    public Uni<Page<Answer>> getPage(Request<PageRequest> request) {
        //noinspection DuplicatedCode
        return settingCacheProvider.getPage(request.payload());
    }

    /**
     * This is method a Vertx message consumer and Setting updater
     *
     * @param request - Setting
     * @return - a LAA with the Answer containing Setting id as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionDuplicateAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.SETTING_PUT)
    public Uni<Answer> put(Request<Setting> request) {
        //noinspection DuplicatedCode
        return settingDataService
                .put(request.payload())
                .map(this::apiResponseAcceptedAnswer)
                .flatMap(answer -> settingCacheProvider.invalidateByKey(request.payload().id(), answer));
    }
}
