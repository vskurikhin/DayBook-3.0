/*
 * This file was last modified at 2023.01.09 23:14 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * AuthenticationContextImpl.java
 * $Id$
 */

package su.svn.daybook.services.security;

import org.jboss.logging.Logger;

import javax.enterprise.context.RequestScoped;
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
        LOG.infof("AuthenticationContext(%s).id: %s", this, id);
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
        LOG.infof("close(%s).id: %s", this, id);
        this.principal = null;
        this.id = null;
    }

    @Override
    protected void finalize() throws Throwable {
        LOG.infof("finalize(%s).id: %s", this, id);
        super.finalize();
    }
}
