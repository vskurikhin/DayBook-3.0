/*
 * This file was last modified at 2022.01.15 10:05 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * WordResourceTest.java
 * $Id$
 */

package su.svn.daybook.resources;

import io.quarkus.security.runtime.QuarkusPrincipal;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import su.svn.daybook.TestData;
import su.svn.daybook.domain.enums.ResourcePath;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.messages.Request;
import su.svn.daybook.domain.model.WordTable;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.models.WordService;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.security.Principal;

import static io.restassured.RestAssured.given;

@QuarkusTest
class WordResourceTest {

    public static final Principal PRINCIPAL = new QuarkusPrincipal(null);
    public static final PageRequest PAGE_REQUEST = new PageRequest(0, (short) 1);

    static Uni<Answer> test = Uni.createFrom()
            .item(1)
            .onItem()
            .transform(i -> Answer.of(TestData.WORD.MODEL_0));

    WordService mock;

    @BeforeEach
    void setUp() {
        mock = Mockito.mock(WordService.class);
        Mockito.when(mock.get(new Request<>(WordTable.NONE, null))).thenReturn(test);
        Mockito.when(mock.get(new Request<>(RuntimeException.class.getSimpleName(), null))).thenThrow(RuntimeException.class);
        Mockito.when(mock.get(new Request<>(Integer.toString(Integer.MAX_VALUE), null))).thenReturn(TestData.UNI_ANSWER_EMPTY);
        Mockito.when(mock.get(new Request<>(Integer.toString(Integer.MIN_VALUE), null))).thenReturn(TestData.UNI_ANSWER_NULL);
        Mockito.when(mock.getAll()).thenReturn(Multi.createFrom().item(Answer.of(TestData.WORD.MODEL_0)));
        Mockito.when(mock.getPage(new Request<>(PAGE_REQUEST, null))).thenReturn(TestData.WORD.UNI_PAGE_ANSWER_SINGLETON_TEST);
        Mockito.when(mock.add(new Request<>(TestData.WORD.MODEL_0, null)))
                .thenReturn(TestData.WORD.UNI_ANSWER_API_RESPONSE_NONE_STRING);
        Mockito.when(mock.put(new Request<>(TestData.WORD.MODEL_0, null)))
                .thenReturn(TestData.WORD.UNI_ANSWER_API_RESPONSE_NONE_STRING);
        Mockito.when(mock.delete(new Request<>(WordTable.NONE, null)))
                .thenReturn(TestData.WORD.UNI_ANSWER_API_RESPONSE_NONE_STRING);
        QuarkusMock.installMockForType(mock, WordService.class);
    }

    @Test
    void testEndpointGet() {
        given()
                .when()
                .get(ResourcePath.API_PATH + "/word/" + WordTable.NONE)
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.WORD.JSON_0));
    }

    @Test
    void testEndpointGetWhenRuntimeException() {
        given()
                .when()
                .get(ResourcePath.API_PATH + "/word/" + RuntimeException.class.getSimpleName())
                .then()
                .statusCode(400);
    }

    @Test
    void testEndpointGetWhenEmpty() {
        given()
                .when()
                .get(ResourcePath.API_PATH + "/word/" + Integer.MAX_VALUE)
                .then()
                .statusCode(404);
    }

    @Test
    void testEndpointGetWhenNull() {
        given()
                .when()
                .get(ResourcePath.API_PATH + "/word/" + Integer.MIN_VALUE)
                .then()
                .statusCode(405);
    }

    @Test
    void testEndpointGetAll() {
        given()
                .when()
                .get(ResourcePath.API_PATH + "/words")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.WORD.JSON_ARRAY_SINGLETON_0));
    }

    @Test
    void testEndpointGetPage() {
        given()
                .when()
                .get(ResourcePath.API_PATH + "/word/-?page=0&limit=1")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.WORD.JSON_PAGE_ARRAY_0));
    }

    @Test
    void testEndpointAdd() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(TestData.WORD.JSON_0)
                .when()
                .post(ResourcePath.API_PATH + "/word")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.WORD.JSON_ID_0));
    }

    @Test
    void testEndpointPut() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(TestData.WORD.JSON_0)
                .when()
                .put(ResourcePath.API_PATH + "/word")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.WORD.JSON_ID_0));
    }

    @Test
    void testEndpointDelete() {
        given()
                .when()
                .delete(ResourcePath.API_PATH + "/word/" + WordTable.NONE)
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.WORD.JSON_ID_0));
    }

}