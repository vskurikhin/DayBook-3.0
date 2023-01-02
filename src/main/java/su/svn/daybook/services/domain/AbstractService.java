package su.svn.daybook.services.domain;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import org.reactivestreams.Publisher;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.messages.ApiResponse;
import su.svn.daybook.models.Identification;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public abstract class AbstractService<K extends Comparable<? extends Serializable>, V extends Identification<K>> {

    private static final Logger LOG = Logger.getLogger(AbstractService.class);

    public abstract Multi<Answer> getAll();

    protected abstract Uni<List<Void>> invalidate(Object o);

    protected abstract Uni<Void> invalidateAllPage();

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

    protected Answer answerOf(@Nonnull V entry) {
        LOG.tracef("answerOf(%s)", entry);
        return Answer.of(entry);
    }

    protected Answer apiResponseWithKeyAnswer(Optional<K> o) {
        LOG.tracef("apiResponseWithKeyAnswer(%s)", o);
        return o.isEmpty() ? Answer.empty() : Answer.of(new ApiResponse<>(o.get()));
    }

    protected Answer apiResponseWithKeyAnswer(int code, Optional<K> o) {
        LOG.tracef("apiResponseWithKeyAnswer(%d, %s)", code, o);
        return o.isEmpty()
                ? Answer.empty()
                : Answer.builder()
                .error(code)
                .payload(new ApiResponse<>(o.get()))
                .build();
    }

    protected Answer apiResponseWithValueAnswer(Optional<V> o) {
        LOG.tracef("apiResponseWithValueAnswer(%s)", o);
        return o.isEmpty() ? Answer.empty() : Answer.of(o.get());
    }

    protected Uni<Answer> apiResponseAcceptedUniAnswer(Optional<K> o) {
        LOG.tracef("apiResponseAcceptedUniAnswer(%s)", o);
        if (o.isEmpty()) {
            throw new NoSuchElementException();
        }
        return Uni.createFrom().item(apiResponseWithKeyAnswer(202, o));
    }

    protected Uni<Answer> invalidateAndAnswer(K key, Answer answer) {
        return invalidate(key)
                .onItem()
                .transform(v -> answer)
                .onFailure()
                .recoverWithUni(t -> Uni.createFrom().item(answer));
    }

    protected Uni<Answer> invalidateAllAndAnswer(Answer answer) {
        return invalidateAllPage()
                .onItem()
                .transform(u -> answer)
                .onFailure()
                .recoverWithUni(t -> Uni.createFrom().item(answer));
    }

    protected Uni<List<Void>> joinCollectFailures(Uni<Void> void1, Uni<Void> void2) {
        return Uni.join()
                .all(void1, void2)
                .andCollectFailures();
    }

    public long getIdLong(Object o) {
        if (o instanceof Long id) {
            return id;
        }
        if (o instanceof String s) {
            return Long.parseLong(s);
        }
        throw new NoSuchElementException();
    }

    public String getIdString(Object o) {
        if (o instanceof String s) {
            return s;
        }
        throw new NoSuchElementException();
    }

    public UUID getIdUUID(Object o) {
        if (o instanceof UUID id) {
            return id;
        }
        if (o instanceof String s) {
            return UUID.fromString(s);
        }
        throw new NoSuchElementException();
    }
}
