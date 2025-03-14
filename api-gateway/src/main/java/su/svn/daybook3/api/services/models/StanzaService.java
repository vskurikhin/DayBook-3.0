/*
 * This file was last modified at 2024-10-30 17:27 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * StanzaService.java
 * $Id$
 */

package su.svn.daybook3.api.services.models;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import su.svn.daybook3.api.annotations.ExceptionBadRequestAnswer;
import su.svn.daybook3.api.annotations.ExceptionDuplicateAnswer;
import su.svn.daybook3.api.annotations.ExceptionNoSuchElementAnswer;
import su.svn.daybook3.api.annotations.PrincipalLogging;
import su.svn.daybook3.api.domain.enums.EventAddress;
import su.svn.daybook3.api.models.domain.Stanza;
import su.svn.daybook3.api.models.pagination.Page;
import su.svn.daybook3.api.models.pagination.PageRequest;
import su.svn.daybook3.api.services.cache.StanzaCacheProvider;
import su.svn.daybook3.api.services.domain.StanzaDataService;
import su.svn.daybook3.domain.messages.Answer;
import su.svn.daybook3.domain.messages.Request;

@PrincipalLogging
@ApplicationScoped
public class StanzaService
        extends AbstractService<Long, Stanza>
        implements MultiAnswerAllService {

    @Inject
    StanzaCacheProvider stanzaCacheProvider;

    @Inject
    StanzaDataService stanzaDataService;

    /**
     * This is method a Vertx message consumer and Stanza creater
     *
     * @param request - Stanza
     * @return - a lazy asynchronous action (LAA) with the Answer containing the Stanza id as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionDuplicateAnswer
    @ConsumeEvent(EventAddress.STANZA_ADD)
    public Uni<Answer> add(Request<Stanza> request) {
        //noinspection DuplicatedCode
        return stanzaDataService
                .add(request.payload())
                .map(this::apiResponseCreatedAnswer)
                .flatMap(stanzaCacheProvider::invalidate);
    }

    /**
     * This is method a Vertx message consumer and Stanza deleter
     *
     * @param request - id of the Stanza
     * @return - a LAA with the Answer containing Stanza id as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.STANZA_DEL)
    public Uni<Answer> delete(Request<Long> request) {
        //noinspection DuplicatedCode
        return stanzaDataService
                .delete(request.payload())
                .map(this::apiResponseOkAnswer)
                .flatMap(answer -> stanzaCacheProvider.invalidateByKey(request.payload(), answer));
    }

    /**
     * This is method a Vertx message consumer and Stanza provider by id
     *
     * @param request - id of the Stanza
     * @return - a lazy asynchronous action with the Answer containing the Stanza as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.STANZA_GET)
    public Uni<Answer> get(Request<Long> request) {
        //noinspection DuplicatedCode
        return stanzaCacheProvider
                .get(request.payload())
                .map(Answer::of);
    }

    /**
     * The method provides the Answer's flow with all entries of Stanza
     *
     * @return - the Answer's Multi-flow with all entries of Stanza
     */
    @Override
    public Multi<Answer> getAll() {
        //noinspection DuplicatedCode
        return stanzaDataService
                .getAll()
                .map(Answer::of);
    }

    @ExceptionBadRequestAnswer
    @ConsumeEvent(EventAddress.STANZA_PAGE)
    public Uni<Page<Answer>> getPage(Request<PageRequest> request) {
        //noinspection DuplicatedCode
        return stanzaCacheProvider.getPage(request.payload());
    }

    /**
     * This is method a Vertx message consumer and Stanza updater
     *
     * @param request - Stanza
     * @return - a LAA with the Answer containing Stanza id as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionDuplicateAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.STANZA_PUT)
    public Uni<Answer> put(Request<Stanza> request) {
        //noinspection DuplicatedCode
        return stanzaDataService
                .put(request.payload())
                .map(this::apiResponseAcceptedAnswer)
                .flatMap(answer -> stanzaCacheProvider.invalidateByKey(request.payload().id(), answer));
    }
}
