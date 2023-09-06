/*
 * This file was last modified at 2023.09.06 17:04 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * ExceptionInterceptor.java
 * $Id$
 */

package su.svn.daybook.interceptors;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.operators.uni.builders.UniCreateFromItemSupplier;
import jakarta.interceptor.InvocationContext;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.messages.Answer;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

@SuppressWarnings({"ReactiveStreamsUnusedPublisher", "unchecked", "rawtypes"})
public class ExceptionInterceptor {

    private static final Logger LOG = Logger.getLogger(ExceptionInterceptor.class);

    Object onFailureRecoverWithUniAnswer(InvocationContext ctx, Function<Throwable, Object> f, Predicate<Throwable> p)
            throws Exception {

        Object ret = ctx.proceed();
        if (ret instanceof Uni<?> uni) {
            return uni
                    .onFailure(p)
                    .recoverWithUni(t -> new UniCreateFromItemSupplier(() -> f.apply(t)));
        }
        return ret;
    }

    protected Object badRequestObject(Throwable t) {
        return badRequestAnswer(t);
    }

    protected Answer badRequestAnswer(Throwable t) {
        LOG.errorf("badRequestAnswer: %s: %s", t.getClass().getName(), String.valueOf(t.getMessage()));
        return Answer.builder()
                .message("bad request")
                .error(Response.Status.BAD_REQUEST.getStatusCode())
                .payload(String.valueOf(t.getMessage()))
                .build();
    }

    protected Object notAcceptableDuplicateObject(Throwable t) {
        return notAcceptableDuplicateAnswer(t);
    }

    protected Answer notAcceptableDuplicateAnswer(Throwable t) {
        LOG.errorf("%s: %s", t.getClass().getName(), String.valueOf(t.getMessage()));
        return Answer.builder()
                .message("duplicate key value")
                .error(Response.Status.NOT_ACCEPTABLE.getStatusCode())
                .payload(String.valueOf(t.getMessage()))
                .build();
    }
    protected Object noSuchElementObject(Throwable t) {
        return noSuchElementAnswer(t);
    }

    protected Answer noSuchElementAnswer(Throwable t) {
        LOG.errorf("%s: %s", t.getClass().getName(), String.valueOf(t.getMessage()));
        return Answer.builder()
                .message("no such element")
                .error(Response.Status.NOT_FOUND.getStatusCode())
                .payload(String.valueOf(t.getMessage()))
                .build();
    }

    protected boolean testDuplicateException(Throwable t) {
        LOG.tracef("%s: %s [[%s]]", t.getClass().getName(), t.getMessage(), Arrays.toString(t.getStackTrace()));
        if (t instanceof io.vertx.pgclient.PgException) {
            return t.getMessage().contains("ERROR: duplicate key value violates unique constraint");
        }
        return false;
    }

    protected boolean testException(Throwable t) {
        LOG.tracef("%s: %s [[%s]]", t.getClass().getName(), t.getMessage(), Arrays.toString(t.getStackTrace()));
        return t instanceof Exception;
    }

    protected boolean testIllegalArgumentException(Throwable t) {
        LOG.tracef("%s: %s [[%s]]", t.getClass().getName(), t.getMessage(), Arrays.toString(t.getStackTrace()));
        return t instanceof IllegalArgumentException;
    }

    protected boolean testNoSuchElementException(Throwable t) {
        LOG.tracef("%s: %s [[%s]]", t.getClass().getName(), t.getMessage(), Arrays.toString(t.getStackTrace()));
        return t instanceof java.util.NoSuchElementException;
    }
}
