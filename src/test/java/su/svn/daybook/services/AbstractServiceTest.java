package su.svn.daybook.services;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import io.vertx.pgclient.PgException;
import org.jboss.resteasy.reactive.RestResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import su.svn.daybook.DataTest;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.*;

import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.NoSuchElementException;

class AbstractServiceTest {

    TestAbstractService service;

    @BeforeEach
    void setUp() {
        service = new TestAbstractService();
    }

    protected RestResponse<String> badRequest(Throwable x) {
        return RestResponse.status(Response.Status.BAD_REQUEST, errorJson(x));
    }

    private String errorJson(Throwable x) {
        return String.format("""
                {"error": 400,"message": "%s"}\
                """, String.valueOf(x.getMessage()).replaceAll("\"", "'"));
    }


    @Test
    void testPgException() {
        var expected = Answer.builder()
                .error(406)
                .message("duplicate key value")
                .payload("ERROR: duplicate key value violates unique constraint (code)")
                .build();
        var o = service.testPgException()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted()
                .getItem();
        Assertions.assertEquals(expected, o);
    }


    @Test
    void testNoSuchElementException() {
        var expected = Answer.builder()
                .error(404)
                .message(DataTest.NO_SUCH_ELEMENT)
                .payload(String.valueOf((String) null))
                .build();
        var o = service.testNoSuchElementException()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted()
                .getItem();
        Assertions.assertEquals(expected, o);
    }

    static class TestEntry implements LongIdentification, Serializable {
        @Override
        public Long getId() {
            return 0L;
        }
    }

    static class TestAbstractService extends AbstractService<Long, TestEntry> {

        Uni<Object> testPgException() {
            return Uni.createFrom().failure(new PgException("duplicate key value violates unique constraint", "ERROR", "code", "detail"))
                    .onFailure(onFailureDuplicatePredicate())
                    .recoverWithUni(this::toDuplicateKeyValueAnswer);
        }

        Uni<Object> testNoSuchElementException() {
            return Uni.createFrom().failure(new NoSuchElementException())
                    .onFailure(onFailureNoSuchElementPredicate())
                    .recoverWithUni(this::toNoSuchElementAnswer);
        }

        @Override
        public Multi<Answer> getAll() {
            return null;
        }
    }
}