package su.svn.daybook.configs;

import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.server.ServerRequestFilter;
import org.jboss.resteasy.reactive.server.ServerResponseFilter;
import su.svn.daybook.services.security.AuthenticationContext;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
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
        LOG.infof(
                "Request: method: %s, URI: %s , principal: %s",
                requestContext.getMethod(), requestContext.getUriInfo().getRequestUri(), principal
        );
        authContext.setPrincipal(principal);
    }

    @ServerResponseFilter
    public void doResponseFilter(ContainerRequestContext requestContext,
                                 ContainerResponseContext responseContext) throws IOException {
        authContext.close();
        LOG.infof(
                "Response: method: %s, URI: %s, headers: %s, entity: %s",
                requestContext.getMethod(), requestContext.getUriInfo().getRequestUri(),
                responseContext.getHeaders(), responseContext.getEntity()
        );
    }
}
