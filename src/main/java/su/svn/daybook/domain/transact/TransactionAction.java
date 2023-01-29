package su.svn.daybook.domain.transact;

import org.intellij.lang.annotations.Language;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
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
