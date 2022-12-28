package su.svn.daybook.resources;

import io.smallrye.mutiny.Multi;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.Identification;
import su.svn.daybook.services.AbstractService;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public interface Resources<K extends Comparable<? extends Serializable>, V extends Identification<K>> {

    Logger LOG = Logger.getLogger(Resources.class);

    AbstractService<K, V> getService();

    default Multi<V> getAll() {
        final AtomicInteger counter = new AtomicInteger();
        LOG.trace("getAll()");
        return getService().getAll()
                .invoke(new Consumer<Object>() {
                    @Override
                    public void accept(Object answer) {
                        LOG.errorf("HA-HA-HA %s", answer);
                    }
                })
                .filter(Objects::nonNull)
                .onItem()
                .transform(this::extract)
                .filter(Objects::nonNull)
                .onItem()
                .invoke(counter::incrementAndGet)
                .onTermination()
                .invoke(() -> LOG.debugf("getAll() counter %s", counter.get()));
    }

    private V extract(Answer answer) {
        try {
            //noinspection unchecked
            return (V) answer.getPayload();
        } catch (ClassCastException e) {
            LOG.error(e.getMessage());
        }
        return null;
    }
}
