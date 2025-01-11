/*
 * This file was last modified at 2024-10-30 14:17 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * LoginDataService.java
 * $Id$
 */

package su.svn.daybook3.auth.services.domain;

import io.quarkus.security.AuthenticationFailedException;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import su.svn.daybook3.auth.domain.dao.SessionDao;
import su.svn.daybook3.auth.domain.dao.UserViewDao;
import su.svn.daybook3.auth.domain.model.SessionTable;
import su.svn.daybook3.auth.domain.model.UserView;
import su.svn.daybook3.auth.services.security.AuthRequestContext;

import java.util.UUID;

@ApplicationScoped
public class LoginDataService {

    private static final Logger LOG = Logger.getLogger(LoginDataService.class);

    @Inject
    SessionDao sessionDao;

    @Inject
    UserViewDao userViewDao;

    @Inject
    AuthRequestContext authRequestContext;

    public Uni<UserView> findByUserName(String username) {
        LOG.infof("findByUserName(%s): requestId: %s", username, authRequestContext.getRequestId());
        return userViewDao
                .findByUserName(username)
                .map(o -> o.orElseThrow(this::userAuthenticationFailedException));
    }

    public Uni<UUID> insert(SessionTable session) {
        LOG.tracef("insert(%s): requestId: %s", session, authRequestContext.getRequestId());
        return sessionDao
                .insert(session)
                .map(o -> o.orElseThrow(this::sessionAuthenticationFailedException));
    }

    private AuthenticationFailedException userAuthenticationFailedException() {
        return authRequestContext.authenticationFailed(true);
    }

    private AuthenticationFailedException sessionAuthenticationFailedException() {
        return new AuthenticationFailedException("Create session fail!");
    }
}
