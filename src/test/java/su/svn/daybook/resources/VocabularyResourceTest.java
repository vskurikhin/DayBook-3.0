/*
 * This file was last modified at 2022.01.15 10:49 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * VocabularyResourceTest.java
 * $Id$
 */

package su.svn.daybook.resources;

import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import su.svn.daybook.DataTest;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.messages.ApiResponse;
import su.svn.daybook.services.VocabularyService;
import su.svn.daybook.services.WordService;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class VocabularyResourceTest {

    static Uni<Answer> tezd = Uni.createFrom()
            .item(1)
            .onItem()
            .transform(i -> Answer.of(DataTest.OBJECT_Vocabulary_0));

    static Uni<Answer> empty = Uni.createFrom().item(Answer.empty());

    static Uni<Answer> nullAnswer = Uni.createFrom().item(() -> null);

    static Uni<Answer> tezdId = Uni.createFrom().item(Answer.of(new ApiResponse(0L)));

    @BeforeEach
    void setUp() {
        VocabularyService mock = Mockito.mock(VocabularyService.class);
        Mockito.when(mock.vocabularyGet("0")).thenReturn(tezd);
        Mockito.when(mock.vocabularyGet(Integer.toString(Integer.MAX_VALUE))).thenReturn(empty);
        Mockito.when(mock.vocabularyGet(Integer.toString(Integer.MIN_VALUE))).thenReturn(nullAnswer);
        Mockito.when(mock.vocabularyAdd(DataTest.OBJECT_Vocabulary_0)).thenReturn(tezdId);
        Mockito.when(mock.vocabularyPut(DataTest.OBJECT_Vocabulary_0)).thenReturn(tezdId);
        Mockito.when(mock.vocabularyDelete("0")).thenReturn(tezdId);
        QuarkusMock.installMockForType(mock, VocabularyService.class);
    }

    @Test
    void testEndpoint_get() {
        given()
                .when()
                .get("/vocabulary/0")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith("{\"id\":0}"));
    }

    @Test
    void testEndpoint_get_whenNone() {
        given()
                .when()
                .get("/vocabulary/" + Integer.MAX_VALUE)
                .then()
                .statusCode(404);
    }

    @Test
    void testEndpoint_get_whenNull() {
        given()
                .when()
                .get("/vocabulary/" + Integer.MIN_VALUE)
                .then()
                .statusCode(406);
    }

    @Test
    void testEndpoint_add() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(DataTest.JSON_Vocabulary_0)
                .when()
                .post("/vocabulary/add")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(DataTest.JSON_Vocabulary_0));
    }

    @Test
    void testEndpoint_put() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(DataTest.JSON_Vocabulary_0)
                .when()
                .put("/vocabulary/put")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(DataTest.JSON_Vocabulary_0));
    }

    @Test
    void delete() {
        given()
                .when()
                .delete("/vocabulary/0")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(DataTest.JSON_Vocabulary_0));
    }
}