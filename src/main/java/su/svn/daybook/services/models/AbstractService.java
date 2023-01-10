/*
 * This file was last modified at 2023.01.09 21:44 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * AbstractService.java
 * $Id$
 */

package su.svn.daybook.services.models;

import io.smallrye.mutiny.Multi;
import org.jboss.logging.Logger;
import org.reactivestreams.Publisher;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.messages.ApiResponse;
import su.svn.daybook.models.Identification;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Supplier;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public abstract class AbstractService<K extends Comparable<? extends Serializable>, V extends Identification<K>> {

    private static final Logger LOG = Logger.getLogger(AbstractService.class);

    public abstract Multi<Answer> getAll();

    protected Publisher<Answer> getAllIfNotOverSize(Optional<Long> count, Supplier<Multi<V>> supplier) {
        if (count.isPresent()) {
            if (-1 < count.get() && count.get() < (Short.MAX_VALUE / 2)) {
                return supplier.get()
                        .onItem()
                        .transform(Answer::of);
            }
            throw new IndexOutOfBoundsException("content too long");
        }
        throw new IndexOutOfBoundsException("content too long");
    }

    protected Answer apiResponseOkAnswer(K id) {
        return Answer.of(new ApiResponse<>(id, 200));
    }

    protected Answer apiResponseCreatedAnswer(K id) {
        return Answer.from(new ApiResponse<>(id, 201), 201);
    }

    protected Answer apiResponseAcceptedAnswer(K id) {
        return Answer.from(new ApiResponse<>(id, 202), 202);
    }
}
