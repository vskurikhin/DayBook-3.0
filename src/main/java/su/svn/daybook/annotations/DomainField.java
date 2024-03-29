package su.svn.daybook.annotations;

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
