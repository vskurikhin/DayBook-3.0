/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * TagLabelService.java
 * $Id$
 */

package su.svn.daybook.services.domain;

import io.quarkus.cache.CacheResult;
import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.dao.TagLabelDao;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.TagLabelTable;
import su.svn.daybook.models.domain.TagLabel;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.ExceptionAnswerService;
import su.svn.daybook.services.cache.TagLabelCacheProvider;
import su.svn.daybook.services.mappers.TagLabelMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.NoSuchElementException;

@ApplicationScoped
public class TagLabelService extends AbstractService<String, TagLabel> {

    private static final Logger LOG = Logger.getLogger(TagLabelService.class);

    @Inject
    TagLabelCacheProvider tagLabelCacheProvider;

    @Inject
    TagLabelDao tagLabelDao;

    @Inject
    TagLabelMapper tagLabelMapper;

    @Inject
    ExceptionAnswerService exceptionAnswerService;

    /**
     * This is method a Vertx message consumer and TagLabel provider by id
     *
     * @param o - id of the TagLabel
     * @return - a lazy asynchronous action with the Answer containing the TagLabel as payload or empty payload
     */
    @ConsumeEvent(EventAddress.TAG_LABEL_GET)
    @CacheResult(cacheName = EventAddress.TAG_LABEL_GET)
    public Uni<Answer> get(Object o) {
        LOG.tracef("get(%s)", o);
        try {
            return getEntry(getIdString(o));
        } catch (NoSuchElementException e) {
            LOG.errorf("get(%s)", o, e);
            return Uni.createFrom().item(Answer.empty());
        }
    }

    /**
     * The method provides the Answer's flow with all entries of TagLabel
     *
     * @return - the Answer's Multi-flow with all entries of TagLabel
     */
    public Multi<Answer> getAll() {
        LOG.trace("getAll");
        return tagLabelDao
                .count()
                .onItem()
                .transformToMulti(count -> getAllIfNotOverSize(count, this::getAllModels));
    }


    private Multi<TagLabel> getAllModels() {
        return tagLabelDao
                .findAll()
                .map(tagLabelMapper::convertToModel);
    }

    private Uni<Answer> getEntry(String id) {
        return tagLabelCacheProvider
                .get(id)
                .map(Answer::of)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    @ConsumeEvent(value = EventAddress.TAG_LABEL_PAGE)
    public Uni<Page<Answer>> getPage(PageRequest pageRequest) {
        //noinspection DuplicatedCode
        LOG.tracef("getPage(%s)", pageRequest);
        return tagLabelCacheProvider.getPage(pageRequest);
    }

    /**
     * This is method a Vertx message consumer and TagLabel creater
     *
     * @param o - TagLabel
     * @return - a lazy asynchronous action (LAA) with the Answer containing the TagLabel id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.TAG_LABEL_ADD)
    public Uni<Answer> add(TagLabel o) {
        LOG.tracef("add(%s)", o);
        return addEntry(tagLabelMapper.convertToDomain(o));
    }

    private Uni<Answer> addEntry(TagLabelTable entry) {
        return tagLabelDao
                .insert(entry)
                .map(o -> apiResponseAnswer(201, o))
                .flatMap(tagLabelCacheProvider::invalidate)
                .onFailure(exceptionAnswerService::testDuplicateException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * This is method a Vertx message consumer and TagLabel updater
     *
     * @param o - TagLabel
     * @return - a LAA with the Answer containing TagLabel id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.TAG_LABEL_PUT)
    public Uni<Answer> put(TagLabel o) {
        LOG.tracef("put(%s)", o);
        return putEntry(tagLabelMapper.convertToDomain(o));
    }

    private Uni<Answer> putEntry(TagLabelTable entry) {
        return tagLabelDao
                .update(entry)
                .flatMap(this::apiResponseAcceptedUniAnswer)
                .flatMap(answer -> tagLabelCacheProvider.invalidateById(entry.getId(), answer))
                .onFailure(exceptionAnswerService::testDuplicateException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateAnswer)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(get(entry.getId()))
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * This is method a Vertx message consumer and TagLabel deleter
     *
     * @param o - id of the TagLabel
     * @return - a LAA with the Answer containing TagLabel id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.TAG_LABEL_DEL)
    public Uni<Answer> delete(Object o) {
        LOG.tracef("delete(%s)", o);
        try {
            return deleteEntry(getIdString(o));
        } catch (NoSuchElementException e) {
            LOG.errorf("delete(%s)", o, e);
            return Uni.createFrom().item(Answer.empty());
        }
    }

    private Uni<Answer> deleteEntry(String id) {
        return tagLabelDao
                .delete(id)
                .map(this::apiResponseAnswer)
                .flatMap(answer -> tagLabelCacheProvider.invalidateById(id, answer))
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }
}
