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
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.TagLabel;
import su.svn.daybook.domain.dao.TagLabelDao;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class TagLabelService {

    private static final Logger LOG = Logger.getLogger(TagLabelService.class);

    @Inject
    TagLabelDao tagLabelDao;

    @ConsumeEvent("tag-get")
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

    @ConsumeEvent("tag-add")
    public Uni<Answer> tagAdd(TagLabel o) {
        LOG.tracef("tagAdd(%s)", o);
        return add(o);
    }

    private Uni<Answer> add(TagLabel entry) {
        return tagLabelDao.insert(entry)
                .map(o -> o.isEmpty() ? Answer.empty() : Answer.of(o.get()));
    }

    public Multi<Answer> getAll() {
        LOG.trace("getAll");
        return tagLabelDao.findAll()
                .onItem()
                .transform(this::getTagLabelAnswer);
    }

    private Answer getTagLabelAnswer(TagLabel tagLabel) {
        LOG.infof("getTagLabelAnswer %s", tagLabel);
        if (tagLabel != null) {
            LOG.tracef("getTagLabelAnswerFunction tagLabel: %s", tagLabel);
            return Answer.of(tagLabel);
        } else  {
            LOG.trace("getTagLabelAnswerFunction tagLabel: is null");
            return Answer.empty();
        }
    }
}
