/*
 * This file was last modified at 2024-05-14 23:10 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * AuthResourceTest.java
 * $Id$
 */

package su.svn.daybook3.auth.resources;

import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import su.svn.daybook3.auth.TestData;
import su.svn.daybook3.auth.domain.enums.ResourcePath;
import su.svn.daybook3.auth.models.security.AuthRequest;
import su.svn.daybook3.auth.services.security.LoginService;
import su.svn.daybook3.domain.messages.Answer;
import su.svn.daybook3.domain.messages.ApiResponse;
import su.svn.daybook3.domain.messages.Request;

import static io.restassured.RestAssured.given;

@QuarkusTest
class AuthResourceTest {

    LoginService mock;

    Request<AuthRequest> requestStub;

    Uni<Answer> answerStub;

    Uni<Answer> unauthorizedStub;

    String tokenStub = "test";

    @BeforeEach
    void setUp() {
        AuthRequest authRequest = new AuthRequest("root", "password");
        requestStub = new Request<>(authRequest, null);
        answerStub = Uni.createFrom().item(Answer.of(ApiResponse.auth(tokenStub)));
        mock = Mockito.mock(LoginService.class);
        QuarkusMock.installMockForType(mock, LoginService.class);
        unauthorizedStub = Uni.createFrom().item(
                Answer.builder()
                        .message("unauthorized")
                        .error(Response.Status.UNAUTHORIZED.getStatusCode())
                        .payload("test")
                        .build()
        );
    }

    @Test
    void whenLoginThenOk() {
        Mockito.when(mock.login(requestStub)).thenReturn(answerStub);
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(TestData.JSON_AUTH_LOGIN)
                .when()
                .post(ResourcePath.API_PATH + "/auth/login")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith("{\"code\":202"));
    }

    @Test
    void whenLoginThenEx() {
        Mockito.when(mock.login(requestStub)).thenReturn(unauthorizedStub);
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(TestData.JSON_AUTH_LOGIN)
                .when()
                .post(ResourcePath.API_PATH + "/auth/login")
                .then()
                .statusCode(401)
                .body(CoreMatchers.startsWith("{\"error\":401,\"message\":\"unauthorized\",\"payload\":\"test\"}"));
    }

    @Test
    void whenLoginThenRuntimeException() {
        Mockito.when(mock.login(requestStub)).thenThrow(new RuntimeException());
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(TestData.JSON_AUTH_LOGIN)
                .when()
                .post(ResourcePath.API_PATH + "/auth/login")
                .then()
                .statusCode(400)
                .body(CoreMatchers.startsWith("{\"error\": 400,\"message\": \"java.lang.RuntimeException\"}"));
    }
}