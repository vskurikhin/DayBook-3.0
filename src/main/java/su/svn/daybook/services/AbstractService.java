package su.svn.daybook.services;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.messages.ApiResponse;
import su.svn.daybook.domain.model.Identification;

import javax.annotation.Nonnull;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Predicate;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public abstract class AbstractService<K extends Comparable<? extends Serializable>, V extends Identification<K>> {

    private static final Logger LOG = Logger.getLogger(AbstractService.class);

    public abstract Multi<Answer> getAll();

    protected Answer getAnswer(@Nonnull V entry) {
        LOG.tracef("getAnswer(%s)", entry);
        return Answer.of(entry);
    }

    protected Answer getAnswerApiResponseWithKey(Optional<K> o) {
        return o.isEmpty() ? Answer.empty() : Answer.of(new ApiResponse<>(o.get()));
    }

    protected Answer getAnswerApiResponseWithKey(int code, Optional<K> o) {
        return o.isEmpty()
                ? Answer.empty()
                : Answer.builder()
                .error(code)
                .payload(new ApiResponse<>(o.get()))
                .build();
    }

    protected Answer getAnswerApiResponseWithValue(Optional<V> o) {
        return o.isEmpty() ? Answer.empty() : Answer.of(o.get());
    }

    protected Uni<Answer> getAnswerForPut(Optional<K> o) {
        if (o.isEmpty()) {
            throw new NoSuchElementException();
        }
        return Uni.createFrom().item(getAnswerApiResponseWithKey(202, o));
    }

    protected Predicate<Throwable> onFailurePredicate() {
        return throwable -> {
            LOG.errorf("%s: %s", throwable.getClass().getName(), throwable.getMessage());
            return switch (throwable.getClass().getName()) {
                case "java.util.NoSuchElementException" -> true;
                default -> false;
            };
        };
    }

    protected Predicate<Throwable> onFailureNoSuchElementPredicate() {
        return throwable -> {
            LOG.errorf("%s: %s", throwable.getClass().getName(), throwable.getMessage());
            if (throwable instanceof java.util.NoSuchElementException) {
                return true;
            }
            if (throwable instanceof RuntimeException runtimeException) {
                throw runtimeException;
            }
            return false;
        };
    }

    protected Predicate<Throwable> onFailureDuplicatePredicate() {
        return throwable -> {
            LOG.errorf("%s: %s", throwable.getClass().getName(), throwable.getMessage());
            if (throwable instanceof io.vertx.pgclient.PgException) {
                return throwable.getMessage().contains("ERROR: duplicate key value violates unique constraint");
            }
            if (throwable instanceof RuntimeException runtimeException) {
                throw runtimeException;
            }
            return false;
        };
    }

    protected Uni<Answer> toDuplicateKeyValueAnswer(Throwable throwable) {
        return Uni.createFrom().item(
                Answer.builder()
                        .message("duplicate key value")
                        .error(Response.Status.NOT_ACCEPTABLE.getStatusCode())
                        .payload(String.valueOf(throwable.getMessage()))
                        .build()
        );
    }

    protected Uni<Answer> toNoSuchElementAnswer(Throwable throwable) {
        return Uni.createFrom().item(
                Answer.builder()
                        .message("no such element")
                        .error(Response.Status.NOT_FOUND.getStatusCode())
                        .payload(String.valueOf(throwable.getMessage()))
                        .build()
        );
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
}
