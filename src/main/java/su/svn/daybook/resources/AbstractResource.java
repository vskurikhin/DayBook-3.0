package su.svn.daybook.resources;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.eventbus.EventBus;
import io.vertx.mutiny.core.eventbus.Message;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestResponse;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.messages.ApiResponse;
import su.svn.daybook.models.Identification;
import su.svn.daybook.models.pagination.Page;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.Serializable;
import java.net.URI;
import java.util.Collections;
import java.util.regex.Pattern;

abstract class AbstractResource {

    private static final Logger LOG = Logger.getLogger(AbstractResource.class);

    private static final Pattern PATH_PATTERN = Pattern.compile("^(/?.*[-a-zA-Z\\d]+)/*$");

    @Inject
    protected EventBus bus;

    protected Uni<Response> request(String address, Object o, UriInfo uriInfo) {
        return bus.<Answer>request(address, o)
                .onItem()
                .transform(msg -> createResponseBuilder(uriInfo, msg))
                .onItem()
                .transform(Response.ResponseBuilder::build);
    }

    protected Uni<Response> requestPage(String address, Object o) {
        return bus.<Page<Answer>>request(address, o)
                .onItem()
                .transform(this::createPageResponseBuilder)
                .onItem()
                .transform(Response.ResponseBuilder::build);
    }

    protected Response.ResponseBuilder createResponseBuilder(UriInfo uriInfo, Message<Answer> message) {
        if (message.body() == null) {
            return Response.status(406, "body is null");
        }
        return message.body().getPayload() != null
                ? constructResponseBuilder(uriInfo, message.body())
                : Response.status(message.body().getError(), message.body().getMessage());
    }

    protected Response.ResponseBuilder createPageResponseBuilder(Message<Page<Answer>> message) {
        if (message.body() == null) {
            return Response.status(406, "body is null");
        }
        var page = message.body();
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
        if (answer.getPayload() != null) {
            if (answer.getPayload() instanceof Serializable s) {
                return s;
            }
        }
        return Answer.empty();
    }

    private boolean nonEmpty(Object o) {
        if (o instanceof Answer answer) {
            return ! Answer.empty().equals(answer);
        }
        return true;
    }

    protected RestResponse<String> badRequest(Throwable x) {
        return RestResponse.status(Response.Status.BAD_REQUEST, errorJson(x));
    }

    private String errorJson(Throwable x) {
        return String.format("""
                {"error": 400,"message": "%s"}\
                """, String.valueOf(x.getMessage()).replaceAll("\"", "'"));
    }

    private Response.ResponseBuilder constructResponseBuilder(UriInfo uriInfo, Answer answer) {
        LOG.infof("uriInfo: %s, answer: %s", uriInfo, answer);
        return switch (answer.getError()) {
            case 200 -> Response.ok(answer.getPayload()).location(createUri(uriInfo, answer.getPayload()));
            case 201 -> Response.created(createUri(uriInfo, answer.getPayload())).entity(answer.getPayload());
            case 202 -> Response.accepted(answer.getPayload()).location(createUri(uriInfo, answer.getPayload()));
            case 404 -> Response.status(Response.Status.NOT_FOUND).entity(answer);
            case 406 -> Response.status(Response.Status.NOT_ACCEPTABLE).entity(answer);
            default -> Response.status(Response.Status.BAD_REQUEST).entity(answer);
        };
    }

    private URI createUri(UriInfo uriInfo, Object payload) {
        var matcher = PATH_PATTERN.matcher(uriInfo.getPath());
        var path = matcher.matches() ? matcher.group(1) : uriInfo.getPath();
        LOG.tracef("path: %s, count: %d, m.group(1): %s", path, matcher.groupCount(), matcher.group(1));
        if (payload instanceof ApiResponse<?> api) {
            var builder = uriInfo.getRequestUriBuilder();
            return builder.replacePath(path+ "/" + api.getId().toString())
                    .buildFromMap(Collections.emptyMap());
        }
        if (payload instanceof Identification<?> entry) {
            var builder = uriInfo.getRequestUriBuilder();
            return builder.replacePath(path + "/" + entry.getId().toString())
                    .buildFromMap(Collections.emptyMap());
        }
        return uriInfo.getRequestUri();
    }
}
