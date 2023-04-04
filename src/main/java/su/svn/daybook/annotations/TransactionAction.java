package su.svn.daybook.annotations;

import org.intellij.lang.annotations.Language;
import su.svn.daybook.domain.enums.IteratorNextMapperEnum;
import su.svn.daybook.domain.enums.SQLMapperEnum;
import su.svn.daybook.domain.enums.TupleMapperEnum;

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
