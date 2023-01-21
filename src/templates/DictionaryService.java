/*
 * This file was last modified at 2023.01.09 21:44 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * @Name@Service.java
 * $Id$
 */

package su.svn.daybook.services.models;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import su.svn.daybook.annotations.ExceptionBadRequestAnswer;
import su.svn.daybook.annotations.ExceptionDuplicateAnswer;
import su.svn.daybook.annotations.ExceptionNoSuchElementAnswer;
import su.svn.daybook.annotations.Logged;
import su.svn.daybook.annotations.Principled;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.messages.Request;
import su.svn.daybook.models.domain.@Name@;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.cache.@Name@CacheProvider;
import su.svn.daybook.services.domain.@Name@DataService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

@ApplicationScoped
@Logged
public class @Name@Service extends AbstractService<@IdType@, @Name@> {

    @Inject
    @Name@CacheProvider @name@CacheProvider;

    @Inject
    @Name@DataService @name@DataService;

    /**
     * This is method a Vertx message consumer and @Name@ creater
     *
     * @param request - @Name@
     * @return - a lazy asynchronous action (LAA) with the Answer containing the @Name@ id as payload or empty payload
     */
    @Principled
    @ExceptionBadRequestAnswer
    @ExceptionDuplicateAnswer
    @ConsumeEvent(EventAddress.@TABLE@_ADD)
    public Uni<Answer> add(Request<@Name@> request) {
        //noinspection DuplicatedCode
        return @name@DataService
                .add(request.payload())
                .map(this::apiResponseCreatedAnswer)
                .flatMap(@name@CacheProvider::invalidate);
    }

    /**
     * This is method a Vertx message consumer and @Name@ deleter
     *
     * @param request - id of the @Name@
     * @return - a LAA with the Answer containing @Name@ id as payload or empty payload
     */
    @Principled
    @ExceptionBadRequestAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.@TABLE@_DEL)
    public Uni<Answer> delete(Request<@IdType@> request) {
        //noinspection DuplicatedCode
        return @name@DataService
                .delete(request.payload())
                .map(this::apiResponseOkAnswer)
                .flatMap(answer -> @name@CacheProvider.invalidateBy@Key@(request.payload(), answer));
    }

    /**
     * This is method a Vertx message consumer and @Name@ provider by id
     *
     * @param request - id of the @Name@
     * @return - a lazy asynchronous action with the Answer containing the @Name@ as payload or empty payload
     */
    @Principled
    @ExceptionBadRequestAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.@TABLE@_GET)
    public Uni<Answer> get(Request<@IdType@> request) {
        //noinspection DuplicatedCode
        return @name@CacheProvider
                .get(request.payload())
                .map(Answer::of);
    }

    /**
     * The method provides the Answer's flow with all entries of @Name@
     *
     * @return - the Answer's Multi-flow with all entries of @Name@
     */
    public Multi<Answer> getAll() {
        //noinspection DuplicatedCode
        return @name@DataService
                .getAll()
                .map(Answer::of);
    }

    @Principled
    @ExceptionBadRequestAnswer
    @ConsumeEvent(EventAddress.@TABLE@_PAGE)
    public Uni<Page<Answer>> getPage(Request<PageRequest> request) {
        //noinspection DuplicatedCode
        return @name@CacheProvider.getPage(request.payload());
    }

    /**
     * This is method a Vertx message consumer and @Name@ updater
     *
     * @param request - @Name@
     * @return - a LAA with the Answer containing @Name@ id as payload or empty payload
     */
    @Principled
    @ExceptionBadRequestAnswer
    @ExceptionDuplicateAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.@TABLE@_PUT)
    public Uni<Answer> put(Request<@Name@> request) {
        //noinspection DuplicatedCode
        return @name@DataService
                .put(request.payload())
                .map(this::apiResponseAcceptedAnswer)
                .flatMap(answer -> @name@CacheProvider.invalidateBy@Key@(request.payload().id(), answer));
    }
}
