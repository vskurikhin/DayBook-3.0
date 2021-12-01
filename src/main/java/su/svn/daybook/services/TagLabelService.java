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
                .transform(this::getTagLabelAnswerFunction);
    }

    private Answer getTagLabelAnswerFunction(TagLabel tagLabel) {
        if (tagLabel != null) {
            LOG.tracef("getTagLabelAnswerFunction tagLabel: %s", tagLabel);
            return Answer.of(tagLabel);
        } else  {
            LOG.trace("getTagLabelAnswerFunction tagLabel: is null");
            return Answer.empty();
        }
    }
}
