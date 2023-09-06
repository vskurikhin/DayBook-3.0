/*
 * This file was last modified at 2023.09.06 17:04 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * Filters.java
 * $Id$
 */

package su.svn.daybook.configs;

import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.server.ServerRequestFilter;
import org.jboss.resteasy.reactive.server.ServerResponseFilter;
import su.svn.daybook.services.security.AuthenticationContext;

import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;

import java.io.IOException;

class Filters {

    Logger LOG = Logger.getLogger(Filters.class);

    @Inject
    AuthenticationContext authContext;

    @Context
    SecurityContext securityContext;

    @ServerRequestFilter(preMatching = true)
    public void doRequestFilter(ContainerRequestContext requestContext) {
        var principal = securityContext != null ? securityContext.getUserPrincipal() : null;
        LOG.tracef(
                "Request: method: %s, URI: %s , principal: %s",
                requestContext.getMethod(), requestContext.getUriInfo().getRequestUri(), principal
        );
        authContext.setPrincipal(principal);
    }

    @ServerResponseFilter
    public void doResponseFilter(ContainerRequestContext requestContext,
                                 ContainerResponseContext responseContext) {
        authContext.close();
        LOG.tracef(
                "Response: method: %s, URI: %s, headers: %s, entity: %s",
                requestContext.getMethod(), requestContext.getUriInfo().getRequestUri(),
                responseContext.getHeaders(), responseContext.getEntity()
        );
    }
}
