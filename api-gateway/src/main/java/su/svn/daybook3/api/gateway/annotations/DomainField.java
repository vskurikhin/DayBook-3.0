/*
 * This file was last modified at 2024-05-14 19:07 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * DomainField.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DomainField {
    String value() default "";

    boolean nullable() default true;

    boolean getterOnly() default false;

    String buildPartPrefix() default "";

    String getterPrefix() default "";
}
