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
import io.smallrye.mutiny.Uni;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import su.svn.daybook.DataTest;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.messages.ApiResponse;
import su.svn.daybook.services.I18nService;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;

@QuarkusTest
class I18nResourceTest {

    static Uni<Answer> tezd = Uni.createFrom()
            .item(1)
            .onItem()
            .transform(i -> Answer.of(DataTest.OBJECT_I18n_0));

    static Uni<Answer> empty = Uni.createFrom().item(Answer.empty());

    static Uni<Answer> nullAnswer = Uni.createFrom().item(() -> null);

    static Uni<Answer> tezdId = Uni.createFrom().item(Answer.of(new ApiResponse(0L)));

    @BeforeAll
    public static void setup() {
        I18nService mock = Mockito.mock(I18nService.class);
        Mockito.when(mock.i18nGet("0")).thenReturn(tezd);
        Mockito.when(mock.i18nGet(Integer.toString(Integer.MAX_VALUE))).thenReturn(empty);
        Mockito.when(mock.i18nGet(Integer.toString(Integer.MIN_VALUE))).thenReturn(nullAnswer);
        Mockito.when(mock.i18nAdd(DataTest.OBJECT_I18n_0)).thenReturn(tezdId);
        Mockito.when(mock.i18nPut(DataTest.OBJECT_I18n_0)).thenReturn(tezdId);
        Mockito.when(mock.i18nDelete("0")).thenReturn(tezdId);
        QuarkusMock.installMockForType(mock, I18nService.class);
    }

    @Test
    void testEndpoint_get() {
        given()
                .when()
                .get("/i18n/0")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith("{\"id\":0}"));
    }

    @Test
    void testEndpoint_get_whenNone() {
        given()
                .when()
                .get("/i18n/" + Integer.MAX_VALUE)
                .then()
                .statusCode(404);
    }

    @Test
    void testEndpoint_get_whenNull() {
        given()
                .when()
                .get("/i18n/" + Integer.MIN_VALUE)
                .then()
                .statusCode(406);
    }

    @Test
    void testEndpoint_add() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(DataTest.JSON_I18n_0)
                .when()
                .post("/i18n/add")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(DataTest.JSON_I18n_0));
    }

    @Test
    void testEndpoint_put() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(DataTest.JSON_I18n_0)
                .when()
                .put("/i18n/put")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(DataTest.JSON_I18n_0));
    }

    @Test
    void delete() {
        given()
                .when()
                .delete("/i18n/0")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(DataTest.JSON_I18n_0));
    }
}