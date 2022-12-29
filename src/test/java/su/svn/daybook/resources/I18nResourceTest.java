/*
 * This file was last modified at 2021.12.15 12:44 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * I18nResourceTest.java
 * $Id$
 */

package su.svn.daybook.resources;

import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import su.svn.daybook.DataTest;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.messages.ApiResponse;
import su.svn.daybook.services.I18nService;
import su.svn.daybook.services.I18nService;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;

@QuarkusTest
class I18nResourceTest {

    static Uni<Answer> test = Uni.createFrom()
            .item(1)
            .onItem()
            .transform(i -> Answer.of(DataTest.OBJECT_I18n_0));

    I18nService mock;

    @BeforeEach
    void setUp() {
        mock = Mockito.mock(I18nService.class);
        Mockito.when(mock.get(0L)).thenReturn(test);
        Mockito.when(mock.get("0")).thenReturn(test);
        Mockito.when(mock.get(1L)).thenThrow(RuntimeException.class);
        Mockito.when(mock.get((long) Integer.MAX_VALUE)).thenReturn(DataTest.UNI_ANSWER_EMPTY);
        Mockito.when(mock.get(Long.toString(Integer.MAX_VALUE))).thenReturn(DataTest.UNI_ANSWER_EMPTY);
        Mockito.when(mock.get((long) Integer.MIN_VALUE)).thenReturn(DataTest.UNI_ANSWER_NULL);
        Mockito.when(mock.get(Long.toString(Integer.MIN_VALUE))).thenReturn(DataTest.UNI_ANSWER_NULL);
        Mockito.when(mock.getAll()).thenReturn(Multi.createFrom().item(Answer.of(DataTest.OBJECT_I18n_0)));
        Mockito.when(mock.add(DataTest.OBJECT_I18n_0)).thenReturn(DataTest.UNI_ANSWER_API_RESPONSE_ZERO_LONG);
        Mockito.when(mock.put(DataTest.OBJECT_I18n_0)).thenReturn(DataTest.UNI_ANSWER_API_RESPONSE_ZERO_LONG);
        Mockito.when(mock.delete(0L)).thenReturn(DataTest.UNI_ANSWER_API_RESPONSE_ZERO_LONG);
        Mockito.when(mock.delete("0")).thenReturn(DataTest.UNI_ANSWER_API_RESPONSE_ZERO_LONG);
        QuarkusMock.installMockForType(mock, I18nService.class);
    }

    @Test
    void testEndpointGet() {
        given()
                .when()
                .get("/i18n/0")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(DataTest.JSON_I18n_0));
    }

    @Test
    void testEndpointGetThenRuntimeException() {
        given()
                .when()
                .get("/i18n/1")
                .then()
                .statusCode(400);
    }

    @Test
    void testEndpointGetWhenEmpty() {
        given()
                .when()
                .get("/i18n/" + Integer.MAX_VALUE)
                .then()
                .statusCode(404);
    }

    @Test
    void testEndpointGetWhenNull() {
        given()
                .when()
                .get("/i18n/" + Integer.MIN_VALUE)
                .then()
                .statusCode(406);
    }

    @Test
    void testEndpointGetAll() {
        given()
                .when()
                .get("/i18n/all")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(DataTest.JSON_ARRAY_I18n_0));
    }

    @Test
    void testEndpointAdd() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(DataTest.JSON_I18n_0)
                .when()
                .post("/i18n")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith("{\"id\":0"));
    }

    @Test
    void testEndpointPut() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(DataTest.JSON_I18n_0)
                .when()
                .put("/i18n")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(DataTest.JSON_I18n_Id_0));
    }

    @Test
    void testEndpointDelete() {
        given()
                .when()
                .delete("/i18n/0")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(DataTest.JSON_I18n_Id_0));
    }
}