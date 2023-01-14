/*
 * This file was last modified at 2023.01.13 21:41 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * ExceptionBadRequestAnswer.java
 * $Id$
 */

package su.svn.daybook.annotations;

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.*;

@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
public @interface ExceptionDuplicateAnswer {
}
