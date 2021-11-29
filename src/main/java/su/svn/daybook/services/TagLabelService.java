package su.svn.daybook.services;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Uni;
import su.svn.daybook.model.TagLabel;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class TagLabelService {

    @Inject
    io.vertx.mutiny.pgclient.PgPool client;

    @ConsumeEvent("tag-get")
    public Uni<TagLabel> tagGet(Object o) {
        if (o instanceof String) {
            return get((String) o);
        }
        return null;
    }

    private Uni<TagLabel> get(String id) {
        return TagLabel.findById(client, id);
    }

    @ConsumeEvent("tag-add")
    public Uni<String> tagAdd(Object o) {
        if (o instanceof TagLabel) {
            return add((TagLabel) o);
        }
        return null;
    }

    private Uni<String> add(TagLabel tagLabel) {
        return tagLabel.insert(client);
    }
}
