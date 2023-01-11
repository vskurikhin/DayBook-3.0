/*
 * This file was last modified at 2023.01.11 21:34 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * LoggingInterceptor.java
 * $Id$
 */

package su.svn.daybook.interceptors;

import io.quarkus.arc.Priority;
import org.jboss.logging.JBossLogManagerProvider;
import org.jboss.logging.Logger;
import su.svn.daybook.annotations.Logged;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Logged
@Priority(2020)
@Interceptor
public class LoggingInterceptor {

    JBossLogManagerProvider provider = new JBossLogManagerProvider();

    @AroundInvoke
    Object logInvocation(InvocationContext context) {
        Logger logger = provider.getLogger(context.getTarget().getClass().getName());
        logger.tracef("%s%s", context.getMethod().getName(), toString(context.getParameters()));
        Object ret = null;
        try {
            ret = context.proceed();
        } catch (Exception e) {
            logger.error(" ", e);
        }
        logger.tracef("%s <- %s%s", ret, context.getMethod().getName(), toString(context.getParameters()));
        return ret;
    }

    public static String toString(Object[] a) {
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
