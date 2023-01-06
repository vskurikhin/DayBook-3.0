package su.svn.daybook.services;

import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.messages.Answer;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Response;
import java.util.Arrays;

@RequestScoped
public class ExceptionAnswerService {

    private static final Logger LOG = Logger.getLogger(ExceptionAnswerService.class);

    public Uni<Answer> getUniAnswerNoNumber(NumberFormatException e) {
        return Uni.createFrom().item(Answer.noNumber(e.getMessage()));
    }

    public boolean testDuplicateException(Throwable t) {
        LOG.tracef("%s: %s [[%s]]", t.getClass().getName(), t.getMessage(), Arrays.toString(t.getStackTrace()));
        if (t instanceof io.vertx.pgclient.PgException) {
            return t.getMessage().contains("ERROR: duplicate key value violates unique constraint");
        }
        return false;
    }

    public boolean testCompositeException(Throwable t) {
        LOG.tracef("ieh 1 %s: %s [[%s]]", t.getClass().getName(), t.getMessage(), Arrays.toString(t.getStackTrace()));
        if (t instanceof io.smallrye.mutiny.CompositeException compositeException) {
            for (var e : compositeException.getCauses()) {
                if (!(e instanceof io.smallrye.mutiny.CompositeException)) {
                    LOG.warnf("ieh 2 %s: %s [[%s]]", e.getClass().getName(), e.getMessage(), Arrays.toString(e.getStackTrace()));
                }
            }
            return true;
        }
        return false;
    }

    public boolean testIllegalArgumentException(Throwable t) {
        LOG.tracef("%s: %s [[%s]]", t.getClass().getName(), t.getMessage(), Arrays.toString(t.getStackTrace()));
        return t instanceof IllegalArgumentException;
    }

    public boolean testIndexOutOfBoundsException(Throwable t) {
        LOG.tracef("%s: %s [[%s]]", t.getClass().getName(), t.getMessage(), Arrays.toString(t.getStackTrace()));
        return t instanceof IndexOutOfBoundsException;
    }

    public boolean testNoSuchElementException(Throwable t) {
        LOG.tracef("%s: %s [[%s]]", t.getClass().getName(), t.getMessage(), Arrays.toString(t.getStackTrace()));
        return t instanceof java.util.NoSuchElementException;
    }

    public boolean testException(Throwable t) {
        LOG.tracef("%s: %s [[%s]]", t.getClass().getName(), t.getMessage(), Arrays.toString(t.getStackTrace()));
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

    public Uni<Answer> notAcceptableDuplicateAnswer(Throwable t) {
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
