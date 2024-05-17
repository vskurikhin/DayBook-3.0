package su.svn.daybook3.api.gateway.utils;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.test.vertx.UniAsserter;
import io.quarkus.test.vertx.UniAsserterInterceptor;
import io.smallrye.mutiny.Uni;

import java.util.function.Supplier;

public class TransactionalUniAsserterInterceptor extends UniAsserterInterceptor {
    public TransactionalUniAsserterInterceptor(UniAsserter asserter) {
        super(asserter);
    }

    /**
     * Assert/execute methods are invoked within a database transaction
     */
    @Override
    protected <T> Supplier<Uni<T>> transformUni(Supplier<Uni<T>> uniSupplier) {
        return () -> Panache.withTransaction(uniSupplier);
    }
}
