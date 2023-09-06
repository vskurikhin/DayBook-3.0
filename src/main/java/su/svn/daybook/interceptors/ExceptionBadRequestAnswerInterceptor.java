/*
 * This file was last modified at 2023.09.06 17:04 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * ExceptionBadRequestAnswerInterceptor.java
 * $Id$
 */

package su.svn.daybook.interceptors;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.operators.uni.builders.UniCreateFromItemSupplier;
import jakarta.annotation.Priority;
import org.jboss.logging.Logger;
import su.svn.daybook.annotations.ExceptionBadRequestAnswer;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

import java.util.function.Function;

@SuppressWarnings({"ReactiveStreamsUnusedPublisher", "unchecked", "rawtypes"})
@ExceptionBadRequestAnswer
@Priority(Interceptor.Priority.LIBRARY_BEFORE + 1)
@Interceptor
public class ExceptionBadRequestAnswerInterceptor extends ExceptionInterceptor {

    private static final Logger LOG = Logger.getLogger(ExceptionBadRequestAnswerInterceptor.class);

    @AroundInvoke
    Object onFailureRecoverWithUniAnswerBadRequest(InvocationContext context) {
        Object ret;
        Function<Throwable, Object> f = this::badRequestObject;
        try {
            ret = onFailureRecoverWithUniAnswer(context, f, this::testIllegalArgumentException);
        } catch (Exception e) {
            LOG.error(" ", e);
            return Uni.createFrom().item(badRequestObject(e));
        }
        if (ret instanceof Uni<?> uni) {
            return uni
                    .onFailure(this::testException)
                    .recoverWithUni(t -> new UniCreateFromItemSupplier(() -> f.apply(t)));
        }
        return ret;
    }
}
