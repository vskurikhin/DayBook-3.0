/*
 * This file was last modified at 2023.01.09 21:44 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * TagLabelService.java
 * $Id$
 */

package su.svn.daybook.services.models;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.models.domain.TagLabel;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.ExceptionAnswerService;
import su.svn.daybook.services.cache.TagLabelCacheProvider;
import su.svn.daybook.services.domain.TagLabelDataService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class TagLabelService extends AbstractService<String, TagLabel> {

    private static final Logger LOG = Logger.getLogger(TagLabelService.class);

    @Inject
    ExceptionAnswerService exceptionAnswerService;

    @Inject
    TagLabelCacheProvider tagLabelCacheProvider;

    @Inject
    TagLabelDataService tagLabelDataService;

    /**
     * This is method a Vertx message consumer and TagLabel creater
     *
     * @param o - TagLabel
     * @return - a lazy asynchronous action (LAA) with the Answer containing the TagLabel id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.TAG_LABEL_ADD)
    public Uni<Answer> add(TagLabel o) {
        //noinspection DuplicatedCode
        LOG.tracef("add(%s)", o);
        return tagLabelDataService
                .add(o)
                .map(this::apiResponseCreatedAnswer)
                .flatMap(tagLabelCacheProvider::invalidate)
                .onFailure(exceptionAnswerService::testDuplicateException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);

    }

    /**
     * This is method a Vertx message consumer and TagLabel deleter
     *
     * @param id - id of the TagLabel
     * @return - a LAA with the Answer containing TagLabel id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.TAG_LABEL_DEL)
    public Uni<Answer> delete(String id) {
        //noinspection DuplicatedCode
        LOG.tracef("delete(%s)", id);
        return tagLabelDataService
                .delete(id)
                .map(this::apiResponseOkAnswer)
                .flatMap(answer -> tagLabelCacheProvider.invalidateById(id, answer))
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * This is method a Vertx message consumer and TagLabel provider by id
     *
     * @param id - id of the TagLabel
     * @return - a lazy asynchronous action with the Answer containing the TagLabel as payload or empty payload
     */
    @ConsumeEvent(EventAddress.TAG_LABEL_GET)
    public Uni<Answer> get(String id) {
        //noinspection DuplicatedCode
        LOG.tracef("get(%s)", id);
        return tagLabelCacheProvider
                .get(id)
                .map(Answer::of)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * The method provides the Answer's flow with all entries of TagLabel
     *
     * @return - the Answer's Multi-flow with all entries of TagLabel
     */
    public Multi<Answer> getAll() {
        //noinspection DuplicatedCode
        LOG.trace("getAll()");
        return tagLabelDataService
                .getAll()
                .map(Answer::of);
    }

    @ConsumeEvent(EventAddress.TAG_LABEL_PAGE)
    public Uni<Page<Answer>> getPage(PageRequest pageRequest) {
        //noinspection DuplicatedCode
        LOG.tracef("getPage(%s)", pageRequest);
        return tagLabelCacheProvider.getPage(pageRequest);
    }

    /**
     * This is method a Vertx message consumer and TagLabel updater
     *
     * @param o - TagLabel
     * @return - a LAA with the Answer containing TagLabel id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.TAG_LABEL_PUT)
    public Uni<Answer> put(TagLabel o) {
        //noinspection DuplicatedCode
        LOG.tracef("put(%s)", o);
        return tagLabelDataService
                .put(o)
                .map(this::apiResponseAcceptedAnswer)
                .flatMap(answer -> tagLabelCacheProvider.invalidateById(o.getId(), answer))
                .onFailure(exceptionAnswerService::testDuplicateException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateAnswer)
                .onFailure(exceptionAnswerService::testIllegalArgumentException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer);
    }
}
