/*
 * This file was last modified at 2023.01.13 21:42 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * ExceptionBadRequestAnswerInterceptor.java
 * $Id$
 */

package su.svn.daybook.interceptors;

import io.quarkus.arc.Priority;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.operators.uni.builders.UniCreateFromItemSupplier;
import org.jboss.logging.Logger;
import su.svn.daybook.annotations.ExceptionBadRequestAnswer;
import su.svn.daybook.services.ExceptionAnswerService;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.function.Function;

@SuppressWarnings({"ReactiveStreamsUnusedPublisher", "unchecked", "rawtypes"})
@ExceptionBadRequestAnswer
@Priority(Interceptor.Priority.LIBRARY_BEFORE + 1)
@Interceptor
public class ExceptionBadRequestAnswerInterceptor extends ExceptionInterceptor {

    private static final Logger LOG = Logger.getLogger(ExceptionBadRequestAnswerInterceptor.class);

    @Inject
    ExceptionAnswerService exceptionAnswerService;

    @AroundInvoke
    Object onFailureRecoverWithUniAnswerBadRequest(InvocationContext context) {
        Object ret = null;
        Function<Throwable, Object> f = t -> exceptionAnswerService.badRequestObject(t);
        try {
            ret = onFailureRecoverWithUniAnswer(context, f, t -> exceptionAnswerService.testIllegalArgumentException(t));
        } catch (Exception e) {
            LOG.error(" ", e);
            return Uni.createFrom().item(exceptionAnswerService.badRequestObject(e));
        }
        if (ret instanceof Uni<?> uni) {
            return uni
                    .onFailure(exceptionAnswerService::testException)
                    .recoverWithUni(t -> new UniCreateFromItemSupplier(() -> f.apply(t)));
        }
        return ret;
    }
}
