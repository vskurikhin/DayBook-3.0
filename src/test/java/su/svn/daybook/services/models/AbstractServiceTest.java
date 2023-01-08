package su.svn.daybook.services.models;

import io.smallrye.mutiny.Multi;
import org.jboss.resteasy.reactive.RestResponse;
import org.junit.jupiter.api.BeforeEach;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.models.LongIdentification;
import su.svn.daybook.services.models.AbstractService;

import javax.ws.rs.core.Response;
import java.io.Serializable;

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


    static class TestEntry implements LongIdentification, Serializable {
        @Override
        public Long getId() {
            return 0L;
        }
    }

    static class TestAbstractService extends AbstractService<Long, TestEntry> {

        @Override
        public Multi<Answer> getAll() {
            return null;
        }

        public boolean onFailureDuplicatePredicate(Throwable t) {
            if (t instanceof io.vertx.pgclient.PgException) {
                return t.getMessage().contains("ERROR: duplicate key value violates unique constraint");
            }
            if (t instanceof RuntimeException runtimeException) {
                throw runtimeException;
            }
            return false;
        }

        public boolean onFailureNoSuchElementPredicate(Throwable t) {
            if (t instanceof java.util.NoSuchElementException) {
                return true;
            }
            if (t instanceof RuntimeException runtimeException) {
                throw runtimeException;
            }
            return false;
        }
    }
}