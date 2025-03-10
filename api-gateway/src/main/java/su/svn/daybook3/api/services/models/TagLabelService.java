/*
 * This file was last modified at 2024-10-30 17:27 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * TagLabelService.java
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
import su.svn.daybook3.api.models.domain.TagLabel;
import su.svn.daybook3.api.models.pagination.Page;
import su.svn.daybook3.api.models.pagination.PageRequest;
import su.svn.daybook3.api.services.cache.TagLabelCacheProvider;
import su.svn.daybook3.api.services.domain.TagLabelDataService;
import su.svn.daybook3.domain.messages.Answer;
import su.svn.daybook3.domain.messages.Request;

@PrincipalLogging
@ApplicationScoped
public class TagLabelService
        extends AbstractService<String, TagLabel>
        implements MultiAnswerAllService {

    @Inject
    TagLabelCacheProvider tagLabelCacheProvider;

    @Inject
    TagLabelDataService tagLabelDataService;

    /**
     * This is method a Vertx message consumer and TagLabel creater
     *
     * @param request - TagLabel
     * @return - a lazy asynchronous action (LAA) with the Answer containing the TagLabel id as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionDuplicateAnswer
    @ConsumeEvent(EventAddress.TAG_LABEL_ADD)
    public Uni<Answer> add(Request<TagLabel> request) {
        //noinspection DuplicatedCode
        return tagLabelDataService
                .add(request.payload())
                .map(this::apiResponseCreatedAnswer)
                .flatMap(tagLabelCacheProvider::invalidate);
    }

    /**
     * This is method a Vertx message consumer and TagLabel deleter
     *
     * @param request - id of the TagLabel
     * @return - a LAA with the Answer containing TagLabel id as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.TAG_LABEL_DEL)
    public Uni<Answer> delete(Request<String> request) {
        //noinspection DuplicatedCode
        return tagLabelDataService
                .delete(request.payload())
                .map(this::apiResponseOkAnswer)
                .flatMap(answer -> tagLabelCacheProvider.invalidateByKey(request.payload(), answer));
    }

    /**
     * This is method a Vertx message consumer and TagLabel provider by id
     *
     * @param request - id of the TagLabel
     * @return - a lazy asynchronous action with the Answer containing the TagLabel as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.TAG_LABEL_GET)
    public Uni<Answer> get(Request<String> request) {
        //noinspection DuplicatedCode
        return tagLabelCacheProvider
                .get(request.payload())
                .map(Answer::of);
    }

    /**
     * The method provides the Answer's flow with all entries of TagLabel
     *
     * @return - the Answer's Multi-flow with all entries of TagLabel
     */
    @Override
    public Multi<Answer> getAll() {
        //noinspection DuplicatedCode
        return tagLabelDataService
                .getAll()
                .map(Answer::of);
    }

    @ExceptionBadRequestAnswer
    @ConsumeEvent(EventAddress.TAG_LABEL_PAGE)
    public Uni<Page<Answer>> getPage(Request<PageRequest> request) {
        //noinspection DuplicatedCode
        return tagLabelCacheProvider.getPage(request.payload());
    }

    /**
     * This is method a Vertx message consumer and TagLabel updater
     *
     * @param request - TagLabel
     * @return - a LAA with the Answer containing TagLabel id as payload or empty payload
     */
    @ExceptionBadRequestAnswer
    @ExceptionDuplicateAnswer
    @ExceptionNoSuchElementAnswer
    @ConsumeEvent(EventAddress.TAG_LABEL_PUT)
    public Uni<Answer> put(Request<TagLabel> request) {
        //noinspection DuplicatedCode
        return tagLabelDataService
                .put(request.payload())
                .map(this::apiResponseAcceptedAnswer)
                .flatMap(answer -> tagLabelCacheProvider.invalidateByKey(request.payload().id(), answer));
    }
}
