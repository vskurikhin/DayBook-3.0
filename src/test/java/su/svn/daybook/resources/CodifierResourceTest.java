/*
 * This file was last modified at 2022.03.24 13:26 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * CodifierResourceTest.java
 * $Id$
 */

package su.svn.daybook.resources;

import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import su.svn.daybook.DataTest;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.messages.ApiResponse;
import su.svn.daybook.services.CodifierService;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;

@QuarkusTest
class CodifierResourceTest {

    static Uni<Answer> tezd = Uni.createFrom()
            .item(1)
            .onItem()
            .transform(i -> Answer.of(DataTest.OBJECT_Codifier_0));

    static Uni<Answer> empty = Uni.createFrom().item(Answer.empty());

    static Uni<Answer> nullAnswer = Uni.createFrom().item(() -> null);

    static Uni<Answer> tezdId = Uni.createFrom().item(Answer.of(new ApiResponse("0")));

    @BeforeAll
    public static void setup() {
        CodifierService mock = Mockito.mock(CodifierService.class);
        Mockito.when(mock.codeGet("0")).thenReturn(tezd);
        Mockito.when(mock.codeGet(Integer.toString(Integer.MAX_VALUE))).thenReturn(empty);
        Mockito.when(mock.codeGet(Integer.toString(Integer.MIN_VALUE))).thenReturn(nullAnswer);
        Mockito.when(mock.codeAdd(DataTest.OBJECT_Codifier_0)).thenReturn(tezdId);
        Mockito.when(mock.codePut(DataTest.OBJECT_Codifier_0)).thenReturn(tezdId);
        Mockito.when(mock.codeDelete("0")).thenReturn(tezdId);
        QuarkusMock.installMockForType(mock, CodifierService.class);
    }

    @Test
    void testEndpoint_get() {
        given()
                .when()
                .get("/code/0")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith("{\"id\":\"0\"}"));
    }

    @Test
    void testEndpoint_add() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(DataTest.JSON_Codifier_0)
                .when()
                .post("/code/add")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith("{\"id\":\"0\"}"));
    }

    @Test
    void testEndpoint_put() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(DataTest.JSON_Codifier_0)
                .when()
                .put("/code/put")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith("{\"id\":\"0\"}"));
    }

    @Test
    void testEndpoint_delete() {
        given()
                .when()
                .delete("/code/0")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith("{\"id\":\"0\"}"));
    }
}