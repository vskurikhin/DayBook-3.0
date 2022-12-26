/*
 * This file was last modified at 2022.01.15 10:05 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * WordResourceTest.java
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
import su.svn.daybook.domain.messages.DictionaryResponse;
import su.svn.daybook.domain.model.Word;
import su.svn.daybook.services.WordService;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;

@QuarkusTest
class WordResourceTest {

    static Uni<Answer> tezd = Uni.createFrom()
            .item(1)
            .onItem()
            .transform(i -> Answer.of(DataTest.OBJECT_Word_0));

    static Uni<Answer> empty = Uni.createFrom().item(Answer.empty());

    static Uni<Answer> nullAnswer = Uni.createFrom().item(() -> null);

    static Uni<Answer> tezdId = Uni.createFrom().item(Answer.of(DictionaryResponse.word(Word.NONE)));

    @BeforeEach
    void setUp() {
        WordService mock = Mockito.mock(WordService.class);
        Mockito.when(mock.wordGet("word")).thenReturn(tezd);
        Mockito.when(mock.wordGet(Integer.toString(Integer.MAX_VALUE))).thenReturn(empty);
        Mockito.when(mock.wordGet(Integer.toString(Integer.MIN_VALUE))).thenReturn(nullAnswer);
        Mockito.when(mock.wordAdd(DataTest.OBJECT_Word_0)).thenReturn(tezdId);
        Mockito.when(mock.wordPut(DataTest.OBJECT_Word_0)).thenReturn(tezdId);
        Mockito.when(mock.wordDelete("word")).thenReturn(tezdId);
        QuarkusMock.installMockForType(mock, WordService.class);
    }

    @Test
    void testEndpointGet() {
        given()
                .when()
                .get("/word/word")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(DataTest.JSON_Word_0));
    }

    @Test
    void testEndpointGetWhenNone() {
        given()
                .when()
                .get("/word/" + Integer.MAX_VALUE)
                .then()
                .statusCode(404);
    }

    @Test
    void testEndpointGetWhenNull() {
        given()
                .when()
                .get("/word/" + Integer.MIN_VALUE)
                .then()
                .statusCode(406);
    }

    @Test
    void testEndpointAdd() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(DataTest.JSON_Word_0)
                .when()
                .post("/word/add")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(DataTest.JSON_Word_Id_0));
    }

    @Test
    void testEndpointPut() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(DataTest.JSON_Word_0)
                .when()
                .put("/word/put")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(DataTest.JSON_Word_Id_0));
    }

    @Test
    void testEndpointDelete() {
        given()
                .when()
                .delete("/word/word")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(DataTest.JSON_Word_Id_0));
    }
}