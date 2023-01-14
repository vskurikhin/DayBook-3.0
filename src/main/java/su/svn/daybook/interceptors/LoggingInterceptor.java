/*
 * This file was last modified at 2023.01.11 21:34 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * LoggingInterceptor.java
 * $Id$
 */

package su.svn.daybook.interceptors;

import io.quarkus.arc.Priority;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.JBossLogManagerProvider;
import org.jboss.logging.Logger;
import su.svn.daybook.annotations.Logged;
import su.svn.daybook.domain.messages.Request;
import su.svn.daybook.models.security.SessionPrincipal;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Logged
@Priority(Interceptor.Priority.APPLICATION + 21)
@Interceptor
public class LoggingInterceptor {

    JBossLogManagerProvider provider = new JBossLogManagerProvider();

    @SuppressWarnings("ReactiveStreamsUnusedPublisher")
    @AroundInvoke
    Object logInvocation(InvocationContext context) {
        Object ret = null;
        Logger logger = provider.getLogger(context.getTarget().getClass().getSuperclass().getName());
        logger.tracef("%s%s", context.getMethod().getName(), toString(context.getParameters()));
        try {
            ret = context.proceed();
        } catch (Exception e) {
            log(logger, context, e);
        }
        if (ret instanceof Multi<?> multi) {
            return multi
                    .onItem()
                    .invoke(o -> log(logger, context, o, null));
        } else if (ret instanceof Uni<?> uni) {
            return uni
                    .onItemOrFailure()
                    .invoke((o, t) -> log(logger, context, o, t));
        } else {
            var str = String.valueOf(ret);
            var s = str.substring(0, Math.min(str.length(), 1024));
            log(logger, context, s, null);
        }
        return ret;
    }

    private String getSid(InvocationContext ctx) {
        var method = ctx.getMethod();
        if (method.getParameterCount() > 0 && ctx.getParameters()[0] instanceof Request<?> request) {
            if (request.principal() instanceof SessionPrincipal sessionPrincipal) {
                return sessionPrincipal.getSessionId();
            }
        }
        return null;
    }

    private void log(Logger log, InvocationContext ctx, Throwable t) {
        log.errorf("%s <- %s%s", t, ctx.getMethod().getName(), toString(ctx.getParameters()));
    }

    private void log(Logger log, InvocationContext ctx, Object o, Throwable t) {
        if (o != null) {
            var str = o.toString();
            var s = str.substring(0, Math.min(str.length(), 1024));
            log.tracef("%s <- %s%s", s, ctx.getMethod().getName(), toString(ctx.getParameters()));
        } else {
            log.errorf("%s <- %s%s", t, ctx.getMethod().getName(), toString(ctx.getParameters()));
        }
    }

    private static String toString(Object[] a) {
        if (a == null)
            return "null";

        int iMax = a.length - 1;
        if (iMax == -1)
            return "()";

        StringBuilder b = new StringBuilder();
        b.append('(');
        for (int i = 0; ; i++) {
            b.append(String.valueOf(a[i]));
            if (i == iMax)
                return b.append(')').toString();
            b.append(", ");
        }
    }
}
