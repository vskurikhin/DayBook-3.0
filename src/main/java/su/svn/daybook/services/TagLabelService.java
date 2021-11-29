package su.svn.daybook.services;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Uni;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.TagLabel;
import su.svn.daybook.domain.dao.TagLabelDao;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class TagLabelService {

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
        return tagLabelDao.findyId(id)
                .map(t -> t.isEmpty() ? Answer.empty() : Answer.of(t));
    }

    @ConsumeEvent("tag-add")
    public Uni<Answer> tagAdd(TagLabel o) {
        return add(o);
    }

    private Uni<Answer> add(TagLabel entry) {
        return tagLabelDao.insert(entry)
                .map(s -> s != null ? Answer.of(s) : Answer.empty());
    }
}
