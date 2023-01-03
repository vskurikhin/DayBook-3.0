package su.svn.daybook.services;

import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.models.pagination.Page;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RequestScoped
public class ExceptionAnswerService {

    private static final Logger LOG = Logger.getLogger(ExceptionAnswerService.class);

    public Uni<Answer> getUniAnswerNoNumber(NumberFormatException e) {
        return Uni.createFrom().item(Answer.noNumber(e.getMessage()));
    }

    public Uni<Page<Answer>> getUniPageAnswerNoNumber(NumberFormatException e) {
        return Uni.createFrom()
                .item(Page.<Answer>builder().content(getListWithAnswerThrowableMessage(e)).build());
    }

    private List<Answer> getListWithAnswerThrowableMessage(Throwable t) {
        return Collections.singletonList(Answer.noNumber(t.getMessage()));
    }

    public boolean testDuplicateKeyException(Throwable t) {
        LOG.tracef("%s: %s [[$s]]", t.getClass().getName(), t.getMessage(), Arrays.toString(t.getStackTrace()));
        if (t instanceof io.vertx.pgclient.PgException) {
            return t.getMessage().contains("ERROR: duplicate key value violates unique constraint");
        }
        if (t instanceof RuntimeException runtimeException) {
            throw runtimeException;
        }
        return false;
    }

    public boolean testIndexOutOfBoundsException(Throwable t) {
        LOG.infof("%s: %s [[$s]]", t.getClass().getName(), t.getMessage(), Arrays.toString(t.getStackTrace()));
        if (t instanceof IndexOutOfBoundsException) {
            return true;
        }
        if (t instanceof RuntimeException runtimeException) {
            throw runtimeException;
        }
        return false;
    }

    public boolean testNoSuchElementException(Throwable t) {
        LOG.tracef("%s: %s [[$s]]", t.getClass().getName(), t.getMessage(), Arrays.toString(t.getStackTrace()));
        if (t instanceof java.util.NoSuchElementException) {
            return true;
        }
        if (t instanceof RuntimeException runtimeException) {
            throw runtimeException;
        }
        return false;
    }

    public boolean testException(Throwable t) {
        LOG.tracef("%s: %s [[$s]]", t.getClass().getName(), t.getMessage(), Arrays.toString(t.getStackTrace()));
        return t instanceof Exception;
    }

    public Uni<Answer> badRequestUniAnswer(Throwable t) {
        LOG.errorf("%s: %s", t.getClass().getName(), t.getMessage());
        return Uni.createFrom().item(
                Answer.builder()
                        .message("bad request")
                        .error(Response.Status.BAD_REQUEST.getStatusCode())
                        .payload(String.valueOf(t.getMessage()))
                        .build()
        );
    }

    public Uni<Answer> notAcceptableDuplicateKeyValAnswer(Throwable t) {
        LOG.errorf("%s: %s", t.getClass().getName(), t.getMessage());
        return Uni.createFrom().item(
                Answer.builder()
                        .message("duplicate key value")
                        .error(Response.Status.NOT_ACCEPTABLE.getStatusCode())
                        .payload(String.valueOf(t.getMessage()))
                        .build()
        );
    }

    public Uni<Answer> noSuchElementAnswer(Throwable t) {
        LOG.errorf("%s: %s", t.getClass().getName(), t.getMessage());
        return Uni.createFrom().item(
                Answer.builder()
                        .message("no such element")
                        .error(Response.Status.NOT_FOUND.getStatusCode())
                        .payload(String.valueOf(t.getMessage()))
                        .build()
        );
    }
}
