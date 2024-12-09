/*
 * This file was last modified at 2024-10-30 14:17 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * AuthRequestContext.java
 * $Id$
 */

package su.svn.daybook3.auth.services.security;

import io.quarkus.security.AuthenticationFailedException;
import jakarta.enterprise.context.RequestScoped;
import org.jboss.logging.Logger;
import su.svn.daybook3.auth.models.security.AuthRequest;

import java.io.Closeable;
import java.util.UUID;

@RequestScoped
public class AuthRequestContext implements Closeable {

    private static final Logger LOG = Logger.getLogger(AuthRequestContext.class);

    public static final String AUTHENTICATION_FAILED = "Authentication failed: ";
    public static final String USER_NAME_INCORRECT = " user name incorrect!";
    public static final String PASSWORD_INCORRECT = " password incorrect!";

    private volatile AuthRequest authRequest;

    private UUID requestId;

    public AuthRequestContext() {
        this.requestId = UUID.randomUUID();
        LOG.infof("authRequestContext(%s).requestId: %s", this, requestId);
    }

    public AuthRequest getAuthRequest() {
        return authRequest;
    }

    public void setAuthRequest(AuthRequest authRequest) {
        this.authRequest = authRequest;
    }

    public UUID getRequestId() {
        return requestId;
    }

    public AuthenticationFailedException authenticationFailed() {
        return this.authenticationFailed(false);
    }

    public AuthenticationFailedException authenticationFailed(boolean unknownUser) {
        return unknownUser
                ? new AuthenticationFailedException(AUTHENTICATION_FAILED + authRequest.username() + USER_NAME_INCORRECT)
                : new AuthenticationFailedException(AUTHENTICATION_FAILED + authRequest.username() + PASSWORD_INCORRECT);
    }

    @Override
    public void close() {
        LOG.infof("close(%s).requestId: %s", this, requestId);
        this.authRequest = null;
        this.requestId = null;
    }
}
