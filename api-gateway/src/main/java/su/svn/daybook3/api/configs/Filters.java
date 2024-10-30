/*
 * This file was last modified at 2024-10-31 15:44 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * Filters.java
 * $Id$
 */

package su.svn.daybook3.api.configs;

import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.server.ServerRequestFilter;
import org.jboss.resteasy.reactive.server.ServerResponseFilter;
import su.svn.daybook3.api.services.security.AuthenticationContext;

class Filters {

    Logger LOG = Logger.getLogger(Filters.class);

    @Inject
    AuthenticationContext authContext;

    @Context
    SecurityContext securityContext;

    @ServerRequestFilter(preMatching = true)
    public void doRequestFilter(ContainerRequestContext requestContext) {
        var principal = securityContext != null ? securityContext.getUserPrincipal() : null;
        LOG.debugf(
                "Request: method: %s, URI: %s , principal: %s",
                requestContext.getMethod(), requestContext.getUriInfo().getRequestUri(), principal
        );
        authContext.setPrincipal(principal);
    }

    @ServerResponseFilter
    public void doResponseFilter(ContainerRequestContext requestContext,
                                 ContainerResponseContext responseContext) {
        var principal = securityContext != null ? securityContext.getUserPrincipal() : null;
        LOG.debugf(
                "Response: method: %s, URI: %s , principal: %s, headers: %s, entity: %s",
                requestContext.getMethod(), requestContext.getUriInfo().getRequestUri(), principal,
                responseContext.getHeaders(), responseContext.getEntity()
        );
        authContext.close();
    }
}