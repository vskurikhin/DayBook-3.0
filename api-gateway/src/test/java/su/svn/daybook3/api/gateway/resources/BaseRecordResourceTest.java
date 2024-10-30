/*
 * This file was last modified at 2024-05-14 23:10 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * BaseRecordResourceTest.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.resources;

import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import su.svn.daybook3.api.gateway.TestData;
import su.svn.daybook3.api.gateway.domain.enums.ResourcePath;
import su.svn.daybook3.api.gateway.services.models.BaseRecordService;
import su.svn.daybook3.domain.messages.Answer;
import su.svn.daybook3.domain.messages.Request;

import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
class BaseRecordResourceTest {

    static Uni<Answer> test = Uni.createFrom()
            .item(1)
            .map(i -> Answer.of(TestData.BASE_RECORD.MODEL_0));

    BaseRecordService mock;

    @BeforeEach
    void setUp() {
        mock = Mockito.mock(BaseRecordService.class);
        Mockito.when(mock.get(TestData.request.UUID_REQUEST_0)).thenReturn(test);
        Mockito.when(mock.get(TestData.request.UUID_REQUEST_1)).thenThrow(RuntimeException.class);
        Mockito.when(mock.get(TestData.request.UUID_REQUEST_2)).thenReturn(TestData.UNI_ANSWER_EMPTY);
        Mockito.when(mock.get(TestData.request.UUID_REQUEST_3)).thenReturn(TestData.UNI_ANSWER_NULL);
        Mockito.when(mock.getPage(TestData.request.REQUEST_4)).thenReturn(TestData.BASE_RECORD.UNI_PAGE_ANSWER_SINGLETON_TEST);
        Mockito.when(mock.add(new Request<>(TestData.BASE_RECORD.MODEL_0, any())))
                .thenReturn(TestData.uuid.UNI_ANSWER_API_RESPONSE_ZERO_201);
        Mockito.when(mock.put(new Request<>(TestData.BASE_RECORD.MODEL_0, any())))
                .thenReturn(TestData.uuid.UNI_ANSWER_API_RESPONSE_ZERO);
        Mockito.when(mock.delete(TestData.request.UUID_REQUEST_0)).thenReturn(TestData.uuid.UNI_ANSWER_API_RESPONSE_ZERO);
        QuarkusMock.installMockForType(mock, BaseRecordService.class);
    }

    @Test
    void testEndpointGet() {
        given()
                .when()
                .get(ResourcePath.API_PATH + "/base-record/" + TestData.uuid.STRING_ZERO)
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.BASE_RECORD.JSON_0));
    }

    @Test
    void testEndpointGetWhenEmpty() {
        given()
                .when()
                .get(ResourcePath.API_PATH + "/base-record/" + TestData.uuid.RANDOM1)
                .then()
                .statusCode(404);
    }

    @Test
    void testEndpointGetWhenNull() {
        given()
                .when()
                .get(ResourcePath.API_PATH + "/base-record/" + TestData.uuid.RANDOM2)
                .then()
                .statusCode(406);
    }

    @Test
    void testEndpointGetPage() {
        given()
                .when()
                .get(ResourcePath.API_PATH + "/base-record/-?page=0&limit=1")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.BASE_RECORD.JSON_PAGE_ARRAY_0));
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"ADMIN"})
    void testEndpointAdd() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(TestData.BASE_RECORD.JSON_NEW)
                .when()
                .post(ResourcePath.API_PATH + "/base-record")
                .then()
                .statusCode(201)
                .body(CoreMatchers.startsWith(TestData.BASE_RECORD.JSON_ID_0_201));
    }

    @Test
    void testEndpointAddForbidden() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(TestData.BASE_RECORD.JSON_0)
                .when()
                .post(ResourcePath.API_PATH + "/base-record")
                .then()
                .statusCode(403);
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"ADMIN"})
    void testEndpointPut() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(TestData.BASE_RECORD.JSON_0)
                .when()
                .put(ResourcePath.API_PATH + "/base-record")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.BASE_RECORD.JSON_ID_0_200));
    }

    @Test
    void testEndpointPutForbidden() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(TestData.BASE_RECORD.JSON_0)
                .when()
                .put(ResourcePath.API_PATH + "/base-record")
                .then()
                .statusCode(403);
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"ADMIN"})
    void testEndpointDelete() {
        given()
                .when()
                .delete(ResourcePath.API_PATH + "/base-record/" + TestData.uuid.STRING_ZERO)
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.BASE_RECORD.JSON_ID_0_200));
    }

    @Test
    void testEndpointDeleteForbidden() {
        given()
                .when()
                .delete(ResourcePath.API_PATH + "/base-record/" + TestData.uuid.STRING_ZERO)
                .then()
                .statusCode(403);
    }
}