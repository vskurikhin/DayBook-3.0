/*
 * This file was last modified at 2023.01.13 21:42 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * ExceptionBadRequestAnswerInterceptor.java
 * $Id$
 */

package su.svn.daybook.interceptors;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.operators.uni.builders.UniCreateFromItemSupplier;

import javax.interceptor.InvocationContext;
import java.util.function.Function;
import java.util.function.Predicate;

@SuppressWarnings({"ReactiveStreamsUnusedPublisher", "unchecked", "rawtypes"})
public class ExceptionInterceptor {

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
}
