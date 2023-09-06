/*
 * This file was last modified at 2023.09.06 17:04 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * UserResourceTest.java
 * $Id$
 */

package su.svn.daybook.resources;

import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import su.svn.daybook.TestData;
import su.svn.daybook.domain.enums.ResourcePath;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.messages.Request;
import su.svn.daybook.services.models.UserService;

import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;

import java.util.NoSuchElementException;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class UserResourceTest {

    static UserService mock;

    static Uni<Answer> test = Uni.createFrom()
            .item(1)
            .map(i -> Answer.of(TestData.USER.MODEL_0));

    @BeforeAll
    public static void setup() {
        mock = Mockito.mock(UserService.class);
        Mockito.when(mock.get(TestData.request.UUID_REQUEST_0)).thenReturn(test);
        Mockito.when(mock.get(TestData.request.UUID_REQUEST_2)).thenReturn(TestData.UNI_ANSWER_EMPTY);
        Mockito.when(mock.get(TestData.request.UUID_REQUEST_3)).thenReturn(TestData.UNI_ANSWER_NULL);
        Mockito.when(mock.getAll()).thenReturn(Multi.createFrom().item(Answer.of(TestData.USER.MODEL_0)));
        Mockito.when(mock.getPage(TestData.request.REQUEST_4)).thenReturn(TestData.USER.UNI_PAGE_ANSWER_SINGLETON_TEST);
        Mockito.when(mock.add(new Request<>(TestData.USER.MODEL_0, null)))
                .thenReturn(TestData.UNI_ANSWER_API_RESPONSE_ZERO_UUID);
        Mockito.when(mock.put(new Request<>(TestData.USER.MODEL_0, null)))
                .thenReturn(TestData.UNI_ANSWER_API_RESPONSE_ZERO_UUID);
        Mockito.when(mock.delete(TestData.request.UUID_REQUEST_0)).thenReturn(TestData.UNI_ANSWER_API_RESPONSE_ZERO_UUID);
        QuarkusMock.installMockForType(mock, UserService.class);
    }


    @Test
    void testEndpointGet() {
        given()
                .when()
                .get(ResourcePath.API_PATH + "/user/" + TestData.uuid.STRING_ZERO)
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.USER.JSON_0));
    }

    @Test
    void testEndpointGetNoSuchElementException() {
        Mockito.when(mock.get(TestData.request.UUID_REQUEST_0)).thenThrow(NoSuchElementException.class);
        given()
                .when()
                .get(ResourcePath.API_PATH + "/user/" + TestData.uuid.STRING_ZERO)
                .then()
                .statusCode(400)
                .body(CoreMatchers.startsWith("{\"error\": 400,\"message\": \"java.util.NoSuchElementException\"}"));
    }

    @Test
    void testEndpointGetAll() {
        given()
                .when()
                .get(ResourcePath.API_PATH + "/users")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.USER.JSON_ARRAY_SINGLETON_0));
    }


    @Test
    void testEndpointGetPage() {
        given()
                .when()
                .get(ResourcePath.API_PATH + "/user/-?page=0&limit=1")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.USER.JSON_PAGE_ARRAY_0));
    }


    @Test
    @TestSecurity(user = "testUser", roles = {"ADMIN"})
    void testEndpointAdd() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(TestData.USER.JSON_0_1)
                .when()
                .post(ResourcePath.API_PATH + "/user")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.USER.JSON_ID_0));
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"USER"})
    void testEndpointPut() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(TestData.USER.JSON_0_1)
                .when()
                .put(ResourcePath.API_PATH + "/user")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.USER.JSON_ID_0));
    }

    @Test
    void testEndpointDelete() {
        given()
                .when()
                .delete(ResourcePath.API_PATH + "/user/" + TestData.uuid.STRING_ZERO)
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.USER.JSON_ID_0));
    }
}
