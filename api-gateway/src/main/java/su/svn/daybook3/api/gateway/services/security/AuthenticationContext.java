/*
 * This file was last modified at 2024-05-14 23:08 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * AuthenticationContext.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.services.security;

import org.jboss.logging.Logger;

import jakarta.enterprise.context.RequestScoped;

import java.io.Closeable;
import java.security.Principal;
import java.util.UUID;

@RequestScoped
public class AuthenticationContext implements Closeable {

    private static final Logger LOG = Logger.getLogger(AuthenticationContext.class);

    private volatile Principal principal;

    private UUID id;

    public AuthenticationContext() {
        this.id = UUID.randomUUID();
        LOG.debugf("AuthenticationContext(%s).id: %s", this, id);
    }

    public Principal getPrincipal() {
        return principal;
    }

    public void setPrincipal(Principal principal) {
        this.principal = principal;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public void close() {
        LOG.debugf("close(%s).id: %s", this, id);
        this.principal = null;
        this.id = null;
    }

    @SuppressWarnings("removal")
    @Override
    protected void finalize() throws Throwable {
        LOG.debugf("finalize(%s).id: %s", this, id);
        //noinspection removal
        super.finalize();
    }
}
