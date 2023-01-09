/*
 * This file was last modified at 2023.01.09 21:44 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * KeyValueService.java
 * $Id$
 */

package su.svn.daybook.services.models;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.models.domain.KeyValue;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.ExceptionAnswerService;
import su.svn.daybook.services.cache.KeyValueCacheProvider;
import su.svn.daybook.services.domain.KeyValueDataService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

@ApplicationScoped
public class KeyValueService extends AbstractService<UUID, KeyValue> {

    private static final Logger LOG = Logger.getLogger(KeyValueService.class);

    @Inject
    ExceptionAnswerService exceptionAnswerService;

    @Inject
    KeyValueCacheProvider keyValueCacheProvider;

    @Inject
    KeyValueDataService keyValueDataService;

    /**
     * This is method a Vertx message consumer and KeyValue creater
     *
     * @param o - KeyValue
     * @return - a lazy asynchronous action (LAA) with the Answer containing the KeyValue id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.KEY_VALUE_ADD)
    public Uni<Answer> add(KeyValue o) {
        //noinspection DuplicatedCode
        LOG.tracef("add(%s)", o);
        return keyValueDataService
                .add(o)
                .map(this::apiResponseCreatedAnswer)
                .flatMap(keyValueCacheProvider::invalidate)
                .onFailure(exceptionAnswerService::testDuplicateException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * This is method a Vertx message consumer and KeyValue deleter
     *
     * @param id - id of the KeyValue
     * @return - a LAA with the Answer containing KeyValue id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.KEY_VALUE_DEL)
    public Uni<Answer> delete(UUID id) {
        //noinspection DuplicatedCode
        LOG.tracef("delete(%s)", id);
        return keyValueDataService
                .delete(id)
                .map(this::apiResponseOkAnswer)
                .flatMap(answer -> keyValueCacheProvider.invalidateById(id, answer))
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * This is method a Vertx message consumer and KeyValue provider by id
     *
     * @param id - id of the KeyValue
     * @return - a lazy asynchronous action with the Answer containing the KeyValue as payload or empty payload
     */
    @ConsumeEvent(EventAddress.KEY_VALUE_GET)
    public Uni<Answer> get(UUID id) {
        //noinspection DuplicatedCode
        LOG.tracef("get(%s)", id);
        return keyValueCacheProvider
                .get(id)
                .map(Answer::of)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * The method provides the Answer's flow with all entries of KeyValue
     *
     * @return - the Answer's Multi-flow with all entries of KeyValue
     */
    public Multi<Answer> getAll() {
        //noinspection DuplicatedCode
        LOG.trace("getAll()");
        return keyValueDataService
                .getAll()
                .map(Answer::of);
    }

    @ConsumeEvent(EventAddress.KEY_VALUE_PAGE)
    public Uni<Page<Answer>> getPage(PageRequest pageRequest) {
        //noinspection DuplicatedCode
        LOG.tracef("getPage(%s)", pageRequest);
        return keyValueCacheProvider.getPage(pageRequest);
    }

    /**
     * This is method a Vertx message consumer and KeyValue updater
     *
     * @param o - KeyValue
     * @return - a LAA with the Answer containing KeyValue id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.KEY_VALUE_PUT)
    public Uni<Answer> put(KeyValue o) {
        //noinspection DuplicatedCode
        LOG.tracef("put(%s)", o);
        return keyValueDataService
                .put(o)
                .map(this::apiResponseAcceptedAnswer)
                .flatMap(answer -> keyValueCacheProvider.invalidateById(o.getId(), answer))
                .onFailure(exceptionAnswerService::testDuplicateException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateAnswer)
                .onFailure(exceptionAnswerService::testIllegalArgumentException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer);
    }
}
