package su.svn.daybook.domain.dao;

import io.smallrye.mutiny.Uni;
import su.svn.daybook.domain.model.TagLabel;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class TagLabelDao {

    @Inject
    io.vertx.mutiny.pgclient.PgPool client;

    public Uni<Optional<TagLabel>> findyId(String id) {
        return TagLabel.findById(client, id)
                .map(t -> t != null ? Optional.of(t) : Optional.empty());
    }

    public Uni<String> insert(TagLabel entry) {
        return entry.insert(client);
    }

    public Uni<String> update(TagLabel entry) {
        return entry.update(client);
    }
}
