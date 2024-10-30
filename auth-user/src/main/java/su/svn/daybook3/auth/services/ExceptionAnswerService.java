/*
 * This file was last modified at 2024-10-30 14:17 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * ExceptionAnswerService.java
 * $Id$
 */

package su.svn.daybook3.auth.services;

import io.quarkus.security.AuthenticationFailedException;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import su.svn.daybook3.domain.messages.Answer;

import java.util.Arrays;

@ApplicationScoped
public class ExceptionAnswerService {

    private static final Logger LOG = Logger.getLogger(ExceptionAnswerService.class);

    public boolean testAuthenticationFailedException(Throwable t) {
        LOG.tracef("%s: %s [[%s]]", t.getClass().getName(), t.getMessage(), Arrays.toString(t.getStackTrace()));
        return t instanceof AuthenticationFailedException;
    }

    @Deprecated
    public boolean testException(Throwable t) {
        LOG.tracef("%s: %s [[%s]]", t.getClass().getName(), t.getMessage(), Arrays.toString(t.getStackTrace()));
        return t instanceof Exception;
    }

    public Uni<Answer> authenticationFailedUniAnswer(Throwable t) {
        LOG.errorf("%s: %s", String.valueOf(t.getClass().getName()), String.valueOf(t.getMessage()));
        return Uni.createFrom().item(
                Answer.builder()
                        .message("unauthorized")
                        .error(Response.Status.UNAUTHORIZED.getStatusCode())
                        .payload(String.valueOf(t.getMessage()))
                        .build()
        );
    }
}
