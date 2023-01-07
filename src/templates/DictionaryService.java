/*
 * This file was last modified at 2023.01.07 11:52 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * @Name@Service.java
 * $Id$
 */

package su.svn.daybook.services.models;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.models.domain.@Name@;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.ExceptionAnswerService;
import su.svn.daybook.services.cache.@Name@CacheProvider;
import su.svn.daybook.services.domain.@Name@DataService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

@ApplicationScoped
public class @Name@Service extends AbstractService<@IdType@, @Name@> {

    private static final Logger LOG = Logger.getLogger(@Name@Service.class);

    @Inject
    ExceptionAnswerService exceptionAnswerService;

    @Inject
    @Name@CacheProvider @name@CacheProvider;

    @Inject
    @Name@DataService @name@DataService;

    /**
     * This is method a Vertx message consumer and @Name@ creater
     *
     * @param o - @Name@
     * @return - a lazy asynchronous action (LAA) with the Answer containing the @Name@ id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.@TABLE@_ADD)
    public Uni<Answer> add(@Name@ o) {
        //noinspection DuplicatedCode
        LOG.tracef("add(%s)", o);
        return @name@DataService
                .add(o)
                .map(this::apiResponseCreatedAnswer)
                .flatMap(@name@CacheProvider::invalidate)
                .onFailure(exceptionAnswerService::testDuplicateException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * This is method a Vertx message consumer and @Name@ deleter
     *
     * @param id - id of the @Name@
     * @return - a LAA with the Answer containing @Name@ id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.@TABLE@_DEL)
    public Uni<Answer> delete(@IdType@ id) {
        //noinspection DuplicatedCode
        LOG.tracef("delete(%s)", id);
        return @name@DataService
                .delete(id)
                .map(this::apiResponseOkAnswer)
                .flatMap(answer -> @name@CacheProvider.invalidateById(id, answer))
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * This is method a Vertx message consumer and @Name@ provider by id
     *
     * @param id - id of the @Name@
     * @return - a lazy asynchronous action with the Answer containing the @Name@ as payload or empty payload
     */
    @ConsumeEvent(EventAddress.@TABLE@_GET)
    public Uni<Answer> get(@IdType@ id) {
        //noinspection DuplicatedCode
        LOG.tracef("get(%s)", id);
        return @name@CacheProvider
                .get(id)
                .map(Answer::of)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * The method provides the Answer's flow with all entries of @Name@
     *
     * @return - the Answer's Multi-flow with all entries of @Name@
     */
    public Multi<Answer> getAll() {
        //noinspection DuplicatedCode
        LOG.trace("getAll()");
        return @name@DataService
                .getAll()
                .map(Answer::of);
    }

    @ConsumeEvent(@value@ = EventAddress.@TABLE@_PAGE)
    public Uni<Page<Answer>> getPage(PageRequest pageRequest) {
        //noinspection DuplicatedCode
        LOG.tracef("getPage(%s)", pageRequest);
        return @name@CacheProvider.getPage(pageRequest);
    }

    /**
     * This is method a Vertx message consumer and @Name@ updater
     *
     * @param o - @Name@
     * @return - a LAA with the Answer containing @Name@ id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.@TABLE@_PUT)
    public Uni<Answer> put(@Name@ o) {
        //noinspection DuplicatedCode
        LOG.tracef("put(%s)", o);
        return @name@DataService
                .put(o)
                .map(this::apiResponseAcceptedAnswer)
                .flatMap(answer -> @name@CacheProvider.invalidateById(o.getId(), answer))
                .onFailure(exceptionAnswerService::testDuplicateException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateAnswer)
                .onFailure(exceptionAnswerService::testIllegalArgumentException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer);
    }
}
