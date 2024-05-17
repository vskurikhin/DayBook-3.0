/*
 * This file was last modified at 2024-05-14 23:10 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * AbstractServiceTest.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.services.models;

import io.smallrye.mutiny.Multi;
import org.jboss.resteasy.reactive.RestResponse;
import org.junit.jupiter.api.BeforeEach;
import su.svn.daybook3.api.gateway.domain.messages.Answer;
import su.svn.daybook3.api.gateway.models.LongIdentification;

import jakarta.ws.rs.core.Response;

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
        public Long id() {
            return 0L;
        }
    }

    static class TestAbstractService extends AbstractService<Long, TestEntry> implements MultiAnswerAllService {

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