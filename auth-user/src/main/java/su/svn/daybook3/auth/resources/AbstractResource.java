/*
 * This file was last modified at 2024-10-30 14:17 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * AbstractResource.java
 * $Id$
 */

package su.svn.daybook3.auth.resources;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.eventbus.EventBus;
import io.vertx.mutiny.core.eventbus.Message;
import jakarta.annotation.Nonnull;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestResponse;
import su.svn.daybook3.auth.models.pagination.Page;
import su.svn.daybook3.auth.models.pagination.PageRequest;
import su.svn.daybook3.auth.services.security.AuthenticationContext;
import su.svn.daybook3.domain.messages.Answer;
import su.svn.daybook3.domain.messages.ApiResponse;
import su.svn.daybook3.domain.messages.Request;
import su.svn.daybook3.models.Identification;

import java.io.Serializable;
import java.net.URI;
import java.util.Collections;
import java.util.regex.Pattern;

abstract class AbstractResource {

    private static final Logger LOG = Logger.getLogger(AbstractResource.class);

    private static final Pattern PATH_PATTERN = Pattern.compile("^(/?.*[-a-zA-Z\\d]+)/*$");

    @Context
    UriInfo uriInfo;

    @Inject
    AuthenticationContext authContext;

    @Inject
    protected EventBus bus;

    protected <T> Uni<Response> request(String address, T o) {
        return bus.<Answer>request(address, new Request<>(o, authContext.getPrincipal()))
                .map(this::createResponseBuilder)
                .map(Response.ResponseBuilder::build);
    }

    protected Uni<Response> requestPage(String address, PageRequest o) {
        return bus.<Page<Answer>>request(address, new Request<>(o, authContext.getPrincipal()))
                .map(this::createPageResponseBuilder)
                .map(Response.ResponseBuilder::build);
    }

    @Nonnull
    protected Response.ResponseBuilder createResponseBuilder(Message<Answer> message) {
        if (message == null) {
            return Response.status(406, "message is null");
        }
        return createResponseBuilder(message.body());
    }

    @Nonnull
    protected Response.ResponseBuilder createResponseBuilder(Answer body) {
        if (body == null) {
            return Response.status(406, "body is null");
        }
        return body.payload() != null
                ? constructResponseBuilder(body)
                : Response.status(body.error(), body.message());
    }

    @Nonnull
    protected Response.ResponseBuilder createPageResponseBuilder(Message<Page<Answer>> message) {
        if (message == null) {
            return Response.status(406, "message is null");
        }
        return createPageResponseBuilder(message.body());
    }

    @Nonnull
    protected Response.ResponseBuilder createPageResponseBuilder(Page<Answer> page) {
        if (page == null) {
            return Response.status(406, "body is null");
        }
        var content = page.getContent()
                .stream()
                .map(this::getAnswerPayload)
                .filter(this::nonEmpty)
                .toList();
        var result = page.convertToBuilderWith(content).build();
        return Response.ok(result);
    }

    @Nonnull
    private Serializable getAnswerPayload(Answer answer) {
        if (answer.payload() != null) {
            if (answer.payload() instanceof Serializable s) {
                return s;
            }
        }
        return Answer.empty();
    }

    private boolean nonEmpty(Object o) {
        if (o instanceof Answer answer) {
            return !Answer.empty().equals(answer);
        }
        return true;
    }

    protected RestResponse<String> exceptionMapper(Throwable x) {
        return switch (x.getClass().getSimpleName()) {
            case "AuthenticationFailedException", "ForbiddenException",
                 "ParseException", "UnauthorizedException" ->
                    RestResponse.status(Response.Status.FORBIDDEN, forbidden(x));
            default -> RestResponse.status(Response.Status.BAD_REQUEST, badRequest(x));
        };
    }

    private String forbidden(Throwable x) {
        return String.format("""
                {"error": 403,"message": "forbidden %s"}\
                """, String.valueOf(x.getMessage()).replaceAll("\"", "'"));
    }

    private String badRequest(Throwable x) {
        return String.format("""
                {"error": 400,"message": "%s"}\
                """, String.valueOf(x.getMessage()).replaceAll("\"", "'"));
    }

    Response.ResponseBuilder constructResponseBuilder(Answer answer) {
        return switch (answer.error()) {
            case 200 -> Response.ok(answer.payload()).location(createUri(answer.payload()));
            case 201 -> Response.created(createUri(answer.payload())).entity(answer.payload());
            case 202 -> Response.accepted(answer.payload()).location(createUri(answer.payload()));
            case 401 -> Response.status(Response.Status.UNAUTHORIZED).entity(answer);
            case 404 -> Response.status(Response.Status.NOT_FOUND).entity(answer);
            case 406 -> Response.status(Response.Status.NOT_ACCEPTABLE).entity(answer);
            default -> Response.status(Response.Status.BAD_REQUEST).entity(answer);
        };
    }

    private URI createUri(Object payload) {
        var matcher = PATH_PATTERN.matcher(uriInfo.getPath());
        var path = matcher.matches() ? matcher.group(1) : uriInfo.getPath();
        LOG.tracef("path: %s, count: %d, m.group(1): %s", path, matcher.groupCount(), matcher.group(1));
        if (payload instanceof ApiResponse<?> api) {
            var builder = uriInfo.getRequestUriBuilder();
            return builder.replacePath(path + "/" + String.valueOf(api.id()))
                    .buildFromMap(Collections.emptyMap());
        }
        if (payload instanceof Identification<?> entry) {
            var builder = uriInfo.getRequestUriBuilder();
            return builder.replacePath(path + "/" + String.valueOf(entry.id()))
                    .buildFromMap(Collections.emptyMap());
        }
        return uriInfo.getRequestUri();
    }
}
