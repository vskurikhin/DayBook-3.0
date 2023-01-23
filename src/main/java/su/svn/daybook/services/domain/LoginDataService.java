/*
 * This file was last modified at 2023.01.09 20:57 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * LoginDataService.java
 * $Id$
 */

package su.svn.daybook.services.domain;

import io.quarkus.security.AuthenticationFailedException;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.dao.SessionDao;
import su.svn.daybook.domain.dao.UserViewDao;
import su.svn.daybook.domain.model.SessionTable;
import su.svn.daybook.domain.model.UserView;
import su.svn.daybook.services.security.AuthRequestContext;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
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
