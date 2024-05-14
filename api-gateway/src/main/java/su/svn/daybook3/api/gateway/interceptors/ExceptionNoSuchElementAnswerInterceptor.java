/*
 * This file was last modified at 2024-05-14 21:36 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * ExceptionNoSuchElementAnswerInterceptor.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.interceptors;

import jakarta.annotation.Priority;
import su.svn.daybook3.api.gateway.annotations.ExceptionNoSuchElementAnswer;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@ExceptionNoSuchElementAnswer
@Priority(Interceptor.Priority.LIBRARY_BEFORE + 3)
@Interceptor
public class ExceptionNoSuchElementAnswerInterceptor extends ExceptionInterceptor {

    @AroundInvoke
    Object onFailureRecoverWithUniAnswerNoSuchElement(InvocationContext context) throws Exception {
        return onFailureRecoverWithUniAnswer(context, this::noSuchElementObject, this::testNoSuchElementException);
    }
}
