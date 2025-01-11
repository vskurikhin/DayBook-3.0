/*
 * This file was last modified at 2024-10-30 14:17 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * Resources.java
 * $Id$
 */

package su.svn.daybook3.auth.resources;

import io.smallrye.mutiny.Multi;
import org.jboss.logging.Logger;
import su.svn.daybook3.auth.services.models.MultiAnswerAllService;
import su.svn.daybook3.domain.messages.Answer;
import su.svn.daybook3.models.Identification;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public interface Resources<K extends Comparable<? extends Serializable>, V extends Identification<K>> {

    Logger LOG = Logger.getLogger(Resources.class);

    MultiAnswerAllService getService();

    default Multi<V> getAll() {
        final AtomicInteger counter = new AtomicInteger();
        return getService().getAll()
                .filter(Objects::nonNull)
                .onItem()
                .transform(this::extract)
                .filter(Objects::nonNull)
                .onItem()
                .invoke(counter::incrementAndGet)
                .onTermination()
                .invoke(() -> LOG.tracef("getAll() counter %s", counter.get()));
    }

    private V extract(Answer answer) {
        try {
            //noinspection unchecked
            return (V) answer.payload();
        } catch (ClassCastException e) {
            LOG.error(e.getMessage());
        }
        return null;
    }
}
