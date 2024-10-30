/*
 * This file was last modified at 2024-10-30 09:54 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * TransactionAction.java
 * $Id$
 */

package su.svn.daybook3.annotations;

import org.intellij.lang.annotations.Language;
import su.svn.daybook3.enums.IteratorNextMapperEnum;
import su.svn.daybook3.enums.SQLMapperEnum;
import su.svn.daybook3.enums.TupleMapperEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TransactionAction {
    @Language("SQL")
    String value() default "";

    String name();

    IteratorNextMapperEnum iteratorNextMapper() default IteratorNextMapperEnum.Null;

    SQLMapperEnum sqlMapper() default SQLMapperEnum.Null;

    TupleMapperEnum tupleMapper() default TupleMapperEnum.Null;
}
