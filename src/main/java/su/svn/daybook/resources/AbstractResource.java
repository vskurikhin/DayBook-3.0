package su.svn.daybook.resources;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.eventbus.EventBus;
import io.vertx.mutiny.core.eventbus.Message;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestResponse;
import su.svn.daybook.domain.enums.ResourcePath;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.messages.ApiResponse;
import su.svn.daybook.domain.model.Identification;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Collections;

abstract class AbstractResource {

    private static final Logger LOG = Logger.getLogger(AbstractResource.class);

    @Inject
    protected EventBus bus;

    protected Uni<Response> request(String address, Object o, UriInfo uriInfo) {
        return bus.<Answer>request(address, o)
                .onItem()
                .transform(msg -> createResponseBuilder(uriInfo, msg))
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
            default -> Response.status(Response.Status.BAD_REQUEST);
        };
    }

    private URI createUri(UriInfo uriInfo, Object payload) {
        if (payload instanceof ApiResponse<?> api) {
            var builder = uriInfo.getRequestUriBuilder();
            return builder.replacePath(ResourcePath.USER + "/" + api.getId().toString())
                    .buildFromMap(Collections.emptyMap());
        }
        if (payload instanceof Identification<?> entry) {
            var builder = uriInfo.getRequestUriBuilder();
            return builder.replacePath(ResourcePath.USER + "/" + entry.getId().toString())
                    .buildFromMap(Collections.emptyMap());
        }
        return uriInfo.getRequestUri();
    }
}
