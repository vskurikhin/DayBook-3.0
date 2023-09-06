/*
 * This file was last modified at 2023.09.06 17:04 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * PrincipledInterceptor.java
 * $Id$
 */

package su.svn.daybook.interceptors;

import jakarta.annotation.Priority;
import org.jboss.logging.Logger;
import su.svn.daybook.annotations.Principled;
import su.svn.daybook.domain.messages.Request;
import su.svn.daybook.services.security.AuthenticationContext;

import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@Deprecated
@Priority(Interceptor.Priority.APPLICATION + 20)
@Interceptor
@Principled
public class PrincipledInterceptor {

    private static final Logger LOG = Logger.getLogger(PrincipledInterceptor.class);

    @Inject
    AuthenticationContext authContext;

    @AroundInvoke
    Object setAuthenticationContext(InvocationContext context) {
        Object ret = null;
        try {
            var method = context.getMethod();
            if (method.getParameterCount() > 0 && context.getParameters()[0] instanceof Request<?> request) {
                authContext.setPrincipal(request.principal());
            } else {
                LOG.warnf("method: %s, parameter count %d", method.getName(), method.getParameterCount());
            }
            ret = context.proceed();
        } catch (Exception e) {
            LOG.error(" ", e);
        } finally {
            authContext.close();
        }
        return ret;
    }
}
