/*
 * This file was last modified at 2021.12.15 12:44 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * ValueTypeResourceTest.java
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
import su.svn.daybook.services.ValueTypeService;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;

@QuarkusTest
class ValueTypeResourceTest {

    static Uni<Answer> test = Uni.createFrom()
            .item(1)
            .onItem()
            .transform(i -> Answer.of(TestData.VALUE_TYPE.OBJECT_0));

    ValueTypeService mock;

    @BeforeEach
    void setUp() {
        mock = Mockito.mock(ValueTypeService.class);
        Mockito.when(mock.get(0L)).thenReturn(test);
        Mockito.when(mock.get("0")).thenReturn(test);
        Mockito.when(mock.get(1L)).thenThrow(RuntimeException.class);
        Mockito.when(mock.get((long) Integer.MAX_VALUE)).thenReturn(TestData.UNI_ANSWER_EMPTY);
        Mockito.when(mock.get(Long.toString(Integer.MAX_VALUE))).thenReturn(TestData.UNI_ANSWER_EMPTY);
        Mockito.when(mock.get((long) Integer.MIN_VALUE)).thenReturn(TestData.UNI_ANSWER_NULL);
        Mockito.when(mock.get(Long.toString(Integer.MIN_VALUE))).thenReturn(TestData.UNI_ANSWER_NULL);
        Mockito.when(mock.getAll()).thenReturn(Multi.createFrom().item(Answer.of(TestData.VALUE_TYPE.OBJECT_0)));
        Mockito.when(mock.add(TestData.VALUE_TYPE.OBJECT_0)).thenReturn(TestData.UNI_ANSWER_API_RESPONSE_ZERO_LONG);
        Mockito.when(mock.put(TestData.VALUE_TYPE.OBJECT_0)).thenReturn(TestData.UNI_ANSWER_API_RESPONSE_ZERO_LONG);
        Mockito.when(mock.delete(0L)).thenReturn(TestData.UNI_ANSWER_API_RESPONSE_ZERO_LONG);
        Mockito.when(mock.delete("0")).thenReturn(TestData.UNI_ANSWER_API_RESPONSE_ZERO_LONG);
        QuarkusMock.installMockForType(mock, ValueTypeService.class);
    }

    @Test
    void testEndpointGet() {
        given()
                .when()
                .get("/value-type/0")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.VALUE_TYPE.JSON_0));
    }

    @Test
    void testEndpointGetThenRuntimeException() {
        given()
                .when()
                .get("/value-type/1")
                .then()
                .statusCode(400);
    }

    @Test
    void testEndpointGetWhenEmpty() {
        given()
                .when()
                .get("/value-type/" + Integer.MAX_VALUE)
                .then()
                .statusCode(404);
    }

    @Test
    void testEndpointGetWhenNull() {
        given()
                .when()
                .get("/value-type/" + Integer.MIN_VALUE)
                .then()
                .statusCode(406);
    }

    @Test
    void testEndpointGetAll() {
        given()
                .when()
                .get("/value-type/all")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.VALUE_TYPE.JSON_ARRAY_SINGLETON_0));
    }

    @Test
    void testEndpointAdd() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(TestData.VALUE_TYPE.JSON_0)
                .when()
                .post("/value-type")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith("{\"id\":0"));
    }

    @Test
    void testEndpointPut() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(TestData.VALUE_TYPE.JSON_0)
                .when()
                .put("/value-type")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.VALUE_TYPE.JSON_ID_0));
    }

    @Test
    void testEndpointDelete() {
        given()
                .when()
                .delete("/value-type/0")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.VALUE_TYPE.JSON_ID_0));
    }
}