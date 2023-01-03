/*
 * This file was last modified at 2021.12.15 12:44 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * KeyValueResourceTest.java
 * $Id$
 */

package su.svn.daybook.resources;

import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import su.svn.daybook.TestData;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.domain.KeyValueService;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;

@QuarkusTest
class KeyValueResourceTest {

    static Uni<Answer> test = Uni.createFrom()
            .item(1)
            .map(i -> Answer.of(TestData.KEY_VALUE.MODEL_0));

    KeyValueService mock;

    @BeforeEach
    void setUp() {
        PageRequest pageRequest = new PageRequest(0, (short) 1);
        mock = Mockito.mock(KeyValueService.class);
        Mockito.when(mock.get(0L)).thenReturn(test);
        Mockito.when(mock.get("0")).thenReturn(test);
        Mockito.when(mock.get(1L)).thenThrow(RuntimeException.class);
        Mockito.when(mock.get((long) Integer.MAX_VALUE)).thenReturn(TestData.UNI_ANSWER_EMPTY);
        Mockito.when(mock.get(Long.toString(Integer.MAX_VALUE))).thenReturn(TestData.UNI_ANSWER_EMPTY);
        Mockito.when(mock.get((long) Integer.MIN_VALUE)).thenReturn(TestData.UNI_ANSWER_NULL);
        Mockito.when(mock.get(Long.toString(Integer.MIN_VALUE))).thenReturn(TestData.UNI_ANSWER_NULL);
        Mockito.when(mock.getAll()).thenReturn(Multi.createFrom().item(Answer.of(TestData.KEY_VALUE.MODEL_0)));
        Mockito.when(mock.getPage(pageRequest)).thenReturn(TestData.KEY_VALUE.UNI_PAGE_ANSWER_SINGLETON_TEST);
        Mockito.when(mock.add(TestData.KEY_VALUE.MODEL_0)).thenReturn(TestData.UNI_ANSWER_API_RESPONSE_ZERO_LONG);
        Mockito.when(mock.put(TestData.KEY_VALUE.MODEL_0)).thenReturn(TestData.UNI_ANSWER_API_RESPONSE_ZERO_LONG);
        Mockito.when(mock.delete(0L)).thenReturn(TestData.UNI_ANSWER_API_RESPONSE_ZERO_LONG);
        Mockito.when(mock.delete("0")).thenReturn(TestData.UNI_ANSWER_API_RESPONSE_ZERO_LONG);
        QuarkusMock.installMockForType(mock, KeyValueService.class);
    }

    @Test
    void testEndpointGet() {
        given()
                .when()
                .get("/key-value/0")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.KEY_VALUE.JSON_0));
    }

    @Test
    void testEndpointGetThenRuntimeException() {
        given()
                .when()
                .get("/key-value/1")
                .then()
                .statusCode(400);
    }

    @Test
    void testEndpointGetWhenEmpty() {
        given()
                .when()
                .get("/key-value/" + Integer.MAX_VALUE)
                .then()
                .statusCode(404);
    }

    @Test
    void testEndpointGetWhenNull() {
        given()
                .when()
                .get("/key-value/" + Integer.MIN_VALUE)
                .then()
                .statusCode(406);
    }

    @Test
    void testEndpointGetAll() {
        given()
                .when()
                .get("/key-value/_?get-all")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.KEY_VALUE.JSON_ARRAY_SINGLETON_0));
    }

    @Test
    void testEndpointGetPage() {
        given()
                .when()
                .get("/key-value/?page=0&limit=1")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.KEY_VALUE.JSON_PAGE_ARRAY_0));
    }

    @Test
    void testEndpointAdd() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(TestData.KEY_VALUE.JSON_0)
                .when()
                .post("/key-value")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith("{\"id\":0"));
    }

    @Test
    void testEndpointPut() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(TestData.KEY_VALUE.JSON_0)
                .when()
                .put("/key-value")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.KEY_VALUE.JSON_ID_0));
    }

    @Test
    void testEndpointDelete() {
        given()
                .when()
                .delete("/key-value/0")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.KEY_VALUE.JSON_ID_0));
    }
}