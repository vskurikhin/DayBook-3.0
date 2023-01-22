/*
 * This file was last modified at 2023.01.13 21:42 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * ExceptionBadRequestAnswerInterceptor.java
 * $Id$
 */

package su.svn.daybook.interceptors;

import io.quarkus.arc.Priority;
import su.svn.daybook.annotations.ExceptionNoSuchElementAnswer;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@ExceptionNoSuchElementAnswer
@Priority(Interceptor.Priority.LIBRARY_BEFORE + 3)
@Interceptor
public class ExceptionNoSuchElementAnswerInterceptor extends ExceptionInterceptor {

    @AroundInvoke
    Object onFailureRecoverWithUniAnswerNoSuchElement(InvocationContext context) throws Exception {
        return onFailureRecoverWithUniAnswer(context, this::noSuchElementObject, this::testNoSuchElementException);
    }
}
