/*
 * This file was last modified at 2024-10-30 14:17 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * ExceptionDuplicateAnswerInterceptor.java
 * $Id$
 */

package su.svn.daybook3.auth.interceptors;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import su.svn.daybook3.auth.annotations.ExceptionDuplicateAnswer;

@ExceptionDuplicateAnswer
@Priority(Interceptor.Priority.LIBRARY_BEFORE + 2)
@Interceptor
public class ExceptionDuplicateAnswerInterceptor extends ExceptionInterceptor {

    @AroundInvoke
    Object onFailureRecoverWithUniAnswerDuplicate(InvocationContext context) throws Exception {
        return onFailureRecoverWithUniAnswer(context, this::notAcceptableDuplicateObject, this::testDuplicateException);
    }
}
