/*
 * This file was last modified at 2022.01.24 22:09 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * LanguageResourceTest.java
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
import su.svn.daybook.services.models.LanguageService;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;

@QuarkusTest
class LanguageResourceTest {

    static Uni<Answer> test = Uni.createFrom()
            .item(1)
            .map(i -> Answer.of(TestData.LANGUAGE.MODEL_0));

    LanguageService mock;

    @BeforeEach
    void setUp() {
        PageRequest pageRequest = new PageRequest(0, (short) 1);
        mock = Mockito.mock(LanguageService.class);
        Mockito.when(mock.get(0L)).thenReturn(test);
        Mockito.when(mock.get(1L)).thenThrow(RuntimeException.class);
        Mockito.when(mock.get((long) Integer.MAX_VALUE)).thenReturn(TestData.UNI_ANSWER_EMPTY);
        Mockito.when(mock.get((long) Integer.MIN_VALUE)).thenReturn(TestData.UNI_ANSWER_NULL);
        Mockito.when(mock.getAll()).thenReturn(Multi.createFrom().item(Answer.of(TestData.LANGUAGE.MODEL_0)));
        Mockito.when(mock.getPage(pageRequest)).thenReturn(TestData.LANGUAGE.UNI_PAGE_ANSWER_SINGLETON_TEST);
        Mockito.when(mock.add(TestData.LANGUAGE.MODEL_0)).thenReturn(TestData.UNI_ANSWER_API_RESPONSE_ZERO_LONG);
        Mockito.when(mock.put(TestData.LANGUAGE.MODEL_0)).thenReturn(TestData.UNI_ANSWER_API_RESPONSE_ZERO_LONG);
        Mockito.when(mock.delete(0L)).thenReturn(TestData.UNI_ANSWER_API_RESPONSE_ZERO_LONG);
        QuarkusMock.installMockForType(mock, LanguageService.class);
    }

    @Test
    void testEndpointGet() {
        given()
                .when()
                .get("/lang/0")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.LANGUAGE.JSON_0));
    }

    @Test
    void testEndpointGetThenRuntimeException() {
        given()
                .when()
                .get("/lang/1")
                .then()
                .statusCode(400);
    }

    @Test
    void testEndpointGetWhenEmpty() {
        given()
                .when()
                .get("/lang/" + Integer.MAX_VALUE)
                .then()
                .statusCode(404);
    }

    @Test
    void testEndpointGetWhenNull() {
        given()
                .when()
                .get("/lang/" + Integer.MIN_VALUE)
                .then()
                .statusCode(406);
    }

    @Test
    void testEndpointGetAll() {
        given()
                .when()
                .get("/langs")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.LANGUAGE.JSON_ARRAY_SINGLETON_0));
    }

    @Test
    void testEndpointGetPage() {
        given()
                .when()
                .get("/lang/?page=0&limit=1")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.LANGUAGE.JSON_PAGE_ARRAY_0));
    }

    @Test
    void testEndpointAdd() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(TestData.LANGUAGE.JSON_0)
                .when()
                .post("/lang")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith("{\"id\":0"));
    }

    @Test
    void testEndpointPut() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(TestData.LANGUAGE.JSON_0)
                .when()
                .put("/lang")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.LANGUAGE.JSON_ID_0));
    }

    @Test
    void testEndpointDelete() {
        given()
                .when()
                .delete("/lang/0")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.LANGUAGE.JSON_ID_0));
    }
}