/*
 * This file was last modified at 2024-05-14 23:10 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * TagLabelResourceTest.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.resources;

import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import su.svn.daybook3.api.gateway.TestData;
import su.svn.daybook3.api.gateway.domain.enums.ResourcePath;
import su.svn.daybook3.api.gateway.domain.messages.Answer;
import su.svn.daybook3.api.gateway.domain.messages.Request;
import su.svn.daybook3.api.gateway.services.models.TagLabelService;

import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;

@QuarkusTest
class TagLabelResourceTest {

    static Uni<Answer> test = Uni.createFrom()
            .item(1)
            .map(i -> Answer.of(TestData.TAG_LABEL.MODEL_0));

    TagLabelService mock;

    @BeforeEach
    void setUp() {
        mock = Mockito.mock(TagLabelService.class);
        Mockito.when(mock.get(TestData.request.STRING_REQUEST_0)).thenReturn(test);
        Mockito.when(mock.get(TestData.request.STRING_REQUEST_1)).thenThrow(RuntimeException.class);
        Mockito.when(mock.get(TestData.request.STRING_REQUEST_2)).thenReturn(TestData.UNI_ANSWER_EMPTY);
        Mockito.when(mock.get(TestData.request.STRING_REQUEST_3)).thenReturn(TestData.UNI_ANSWER_NULL);
        Mockito.when(mock.getAll()).thenReturn(Multi.createFrom().item(Answer.of(TestData.TAG_LABEL.MODEL_0)));
        Mockito.when(mock.getPage(TestData.request.REQUEST_4)).thenReturn(TestData.TAG_LABEL.UNI_PAGE_ANSWER_SINGLETON_TEST);
        Mockito.when(mock.add(new Request<>(TestData.TAG_LABEL.MODEL_0, null)))
                .thenReturn(TestData.UNI_ANSWER_API_RESPONSE_ZERO_LONG);
        Mockito.when(mock.put(new Request<>(TestData.TAG_LABEL.MODEL_0, null)))
                .thenReturn(TestData.UNI_ANSWER_API_RESPONSE_ZERO_LONG);
        Mockito.when(mock.delete(TestData.request.STRING_REQUEST_0)).thenReturn(TestData.UNI_ANSWER_API_RESPONSE_ZERO_LONG);
        QuarkusMock.installMockForType(mock, TagLabelService.class);
    }

    @Test
    void testEndpointGet() {
        given()
                .when()
                .get(ResourcePath.API_PATH + "/tag/" + 0)
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.TAG_LABEL.JSON_0));
    }

    @Test
    void testEndpointGetThenRuntimeException() {
        given()
                .when()
                .get(ResourcePath.API_PATH + "/tag/" + 1)
                .then()
                .statusCode(400);
    }

    @Test
    void testEndpointGetWhenEmpty() {
        given()
                .when()
                .get(ResourcePath.API_PATH + "/tag/" + 2)
                .then()
                .statusCode(404);
    }

    @Test
    void testEndpointGetWhenNull() {
        given()
                .when()
                .get(ResourcePath.API_PATH + "/tag/" + 3)
                .then()
                .statusCode(406);
    }

    @Test
    void testEndpointGetAll() {
        given()
                .when()
                .get(ResourcePath.API_PATH + "/tags")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.TAG_LABEL.JSON_ARRAY_SINGLETON_0));
    }

    @Test
    void testEndpointGetPage() {
        given()
                .when()
                .get(ResourcePath.API_PATH + "/tag/-?page=0&limit=1")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.TAG_LABEL.JSON_PAGE_ARRAY_0));
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"ADMIN"})
    void testEndpointAdd() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(TestData.TAG_LABEL.JSON_0)
                .when()
                .post(ResourcePath.API_PATH + "/tag")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.TAG_LABEL.JSON_ID_0));
    }

    @Test
    void testEndpointAddForbidden() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(TestData.TAG_LABEL.JSON_0)
                .when()
                .post(ResourcePath.API_PATH + "/tag")
                .then()
                .statusCode(403);
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"ADMIN"})
    void testEndpointPut() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(TestData.TAG_LABEL.JSON_0)
                .when()
                .put(ResourcePath.API_PATH + "/tag")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.TAG_LABEL.JSON_ID_0));
    }

    @Test
    void testEndpointPutForbidden() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(TestData.TAG_LABEL.JSON_0)
                .when()
                .put(ResourcePath.API_PATH + "/tag")
                .then()
                .statusCode(403);
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"ADMIN"})
    void testEndpointDelete() {
        given()
                .when()
                .delete(ResourcePath.API_PATH + "/tag/" + 0)
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.TAG_LABEL.JSON_ID_0));
    }

    @Test
    void testEndpointDeleteForbidden() {
        given()
                .when()
                .delete(ResourcePath.API_PATH + "/tag/" + 0)
                .then()
                .statusCode(403);
    }
}