/*
 * This file was last modified at 2021.12.06 18:10 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * TagLabelService.java
 * $Id$
 */

package su.svn.daybook.services;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.Multi;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.messages.ApiResponse;
import su.svn.daybook.domain.model.TagLabel;
import su.svn.daybook.domain.dao.TagLabelDao;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class TagLabelService {

    private static final Logger LOG = Logger.getLogger(TagLabelService.class);

    @Inject
    TagLabelDao tagLabelDao;

    /**
     * This is method a Vertx message consumer and TagLabel provider by id
     *
     * @param o - id of the TagLabel
     * @return - a lazy asynchronous action with the Answer containing the TagLabel as payload or empty payload
     */
    @ConsumeEvent(EventAddress.TAG_GET)
    public Uni<Answer> tagGet(Object o) {
        LOG.tracef("tagGet(%s)", o);
        if (o instanceof String) {
            return get((String) o);
        }
        return Uni.createFrom().item(Answer.empty());
    }

    private Uni<Answer> get(String id) {
        return tagLabelDao.findById(id)
                .map(t -> t.isEmpty() ? Answer.empty() : Answer.of(t));
    }

    /**
     * This is method a Vertx message consumer and TagLabel creater
     *
     * @param o - TagLabel
     * @return - a lazy asynchronous action (LAA) with the Answer containing the TagLabel id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.TAG_ADD)
    public Uni<Answer> tagAdd(TagLabel o) {
        LOG.tracef("tagAdd(%s)", o);
        return add(o);
    }

    private Uni<Answer> add(TagLabel entry) {
        return tagLabelDao.insert(entry)
                .map(o -> o.isEmpty() ? Answer.empty() : Answer.of(new ApiResponse<>(o.get())));
    }

    /**
     * This is method a Vertx message consumer and TagLabel updater
     *
     * @param o - TagLabel
     * @return - a LAA with the Answer containing TagLabel id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.TAG_PUT)
    public Uni<Answer> tagPut(TagLabel o) {
        LOG.tracef("tagPut(%s)", o);
        return put(o);
    }

    private Uni<Answer> put(TagLabel entry) {
        return tagLabelDao.update(entry)
                .map(o -> o.isEmpty() ? Answer.empty() : Answer.of(new ApiResponse<>(o.get())));
    }

    /**
     * The method provides the Answer's flow with all entries of TagLabel
     *
     * @return - the Answer's Multi-flow with all entries of TagLabel
     */
    public Multi<Answer> getAll() {
        LOG.trace("getAll");
        return tagLabelDao.findAll()
                .onItem()
                .transform(Answer::of);
    }

    /**
     * This is method a Vertx message consumer and TagLabel deleter
     *
     * @param o - id of the TagLabel
     * @return - a LAA with the Answer containing TagLabel id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.TAG_DEL)
    public Uni<Answer> tagDelete(Object o) {
        var methodCall = String.format("wordDelete(%s)", o);
        if (o instanceof String) {
            return delete(o.toString());
        }
        return Uni.createFrom().item(Answer.empty());
    }

    private Uni<Answer> delete(String id) {
        return tagLabelDao.delete(id)
                .map(o -> o.isEmpty() ? Answer.empty() : Answer.of(new ApiResponse<>(o.get())));
    }
}
