/*
 * This file was last modified at 2023.09.12 22:06 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * PrincipalLoggingInterceptor.java
 * $Id$
 */

package su.svn.daybook.interceptors;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.Priority;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.JBossLogManagerProvider;
import org.jboss.logging.Logger;
import su.svn.daybook.annotations.PrincipalLogging;
import su.svn.daybook.domain.messages.Request;
import su.svn.daybook.services.security.AuthenticationContext;

import jakarta.annotation.Nonnull;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

import java.util.concurrent.CopyOnWriteArrayList;

@PrincipalLogging
@Priority(Interceptor.Priority.APPLICATION + 20)
@Interceptor
public class PrincipalLoggingInterceptor {

    public static final String TRACE = "TRACE";
    @Inject
    @ConfigProperty(defaultValue = TRACE)
    String logLevel;

    @Inject
    AuthenticationContext authContext;

    JBossLogManagerProvider provider = new JBossLogManagerProvider();

    @SuppressWarnings("ReactiveStreamsUnusedPublisher")
    @AroundInvoke
    Object invocation(InvocationContext context) {
        Object ret = null;
        Runnable fin = () -> {};
        Logger logger = provider.getLogger(context.getTarget().getClass().getSuperclass().getName());
        logb(logger, context);
        try {
            var method = context.getMethod();
            if (method.getParameterCount() > 0 && context.getParameters()[0] instanceof Request<?> request) {
                authContext.setPrincipal(request.principal());
                fin = () -> authContext.close();
            }
            ret = context.proceed();
            if (!TRACE.equals(logLevel)) {
                logr(logger, context, ret);
                return ret;
            }
        } catch (Exception e) {
            logt(logger, context, e);
        } finally {
            fin.run();
        }
        if (ret instanceof Multi<?> multi) {
            var list = new CopyOnWriteArrayList<>();
            return multi
                    .onItem()
                    .invoke(list::add)
                    .onTermination()
                    .invoke(() -> logr(logger, context, list));
        } else if (ret instanceof Uni<?> uni) {
            return uni
                    .onItem()
                    .invoke(o -> logr(logger, context, o));
        } else if (ret != null) {
            logr(logger, context, ret);
        } else {
            loge(logger, context);
        }
        return ret;
    }

    private void logb(Logger log, InvocationContext ctx) {
        var level = Logger.Level.valueOf(logLevel);
        log.logf(level, "%s%s", ctx.getMethod().getName(), toString(ctx.getParameters()));
    }

    private void loge(Logger log, InvocationContext ctx) {
        var level = Logger.Level.valueOf(logLevel);
        log.logf(level, "void <- %s%s", ctx.getMethod().getName(), toString(ctx.getParameters()));
    }

    private void logr(Logger log, InvocationContext ctx, @Nonnull Object o) {
        var str = o.toString();
        var s = str.substring(0, Math.min(str.length(), 1024));
        var level = Logger.Level.valueOf(logLevel);
        log.logf(level, "%s <- %s%s", s, ctx.getMethod().getName(), toString(ctx.getParameters()));
    }

    private void logt(Logger log, InvocationContext ctx, Throwable t) {
        log.errorf("%s <- %s%s", t, ctx.getMethod().getName(), toString(ctx.getParameters()));
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
