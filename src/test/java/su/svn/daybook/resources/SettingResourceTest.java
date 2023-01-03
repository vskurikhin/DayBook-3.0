/*
 * This file was last modified at 2021.12.15 12:44 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * SettingResourceTest.java
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
import su.svn.daybook.services.domain.SettingService;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;

@QuarkusTest
class SettingResourceTest {

    static Uni<Answer> test = Uni.createFrom()
            .item(1)
            .onItem()
            .transform(i -> Answer.of(TestData.SETTING.TABLE_0));

    SettingService mock;

    @BeforeEach
    void setUp() {
        PageRequest pageRequest = new PageRequest(0, (short) 1);
        mock = Mockito.mock(SettingService.class);
        Mockito.when(mock.get(0L)).thenReturn(test);
        Mockito.when(mock.get("0")).thenReturn(test);
        Mockito.when(mock.get(1L)).thenThrow(RuntimeException.class);
        Mockito.when(mock.get((long) Integer.MAX_VALUE)).thenReturn(TestData.UNI_ANSWER_EMPTY);
        Mockito.when(mock.get(Long.toString(Integer.MAX_VALUE))).thenReturn(TestData.UNI_ANSWER_EMPTY);
        Mockito.when(mock.get((long) Integer.MIN_VALUE)).thenReturn(TestData.UNI_ANSWER_NULL);
        Mockito.when(mock.get(Long.toString(Integer.MIN_VALUE))).thenReturn(TestData.UNI_ANSWER_NULL);
        Mockito.when(mock.getAll()).thenReturn(Multi.createFrom().item(Answer.of(TestData.SETTING.TABLE_0)));
        Mockito.when(mock.getPage(pageRequest)).thenReturn(TestData.SETTING.UNI_PAGE_ANSWER_SINGLETON_TEST);
        Mockito.when(mock.add(TestData.SETTING.TABLE_0)).thenReturn(TestData.UNI_ANSWER_API_RESPONSE_ZERO_LONG);
        Mockito.when(mock.put(TestData.SETTING.TABLE_0)).thenReturn(TestData.UNI_ANSWER_API_RESPONSE_ZERO_LONG);
        Mockito.when(mock.delete(0L)).thenReturn(TestData.UNI_ANSWER_API_RESPONSE_ZERO_LONG);
        Mockito.when(mock.delete("0")).thenReturn(TestData.UNI_ANSWER_API_RESPONSE_ZERO_LONG);
        QuarkusMock.installMockForType(mock, SettingService.class);
    }

    @Test
    void testEndpointGet() {
        given()
                .when()
                .get("/setting/0")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.SETTING.JSON_0));
    }

    @Test
    void testEndpointGetThenRuntimeException() {
        given()
                .when()
                .get("/setting/1")
                .then()
                .statusCode(400);
    }

    @Test
    void testEndpointGetWhenEmpty() {
        given()
                .when()
                .get("/setting/" + Integer.MAX_VALUE)
                .then()
                .statusCode(404);
    }

    @Test
    void testEndpointGetWhenNull() {
        given()
                .when()
                .get("/setting/" + Integer.MIN_VALUE)
                .then()
                .statusCode(406);
    }

    @Test
    void testEndpointGetAll() {
        given()
                .when()
                .get("/setting/_?get-all")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.SETTING.JSON_ARRAY_SINGLETON_0));
    }

    @Test
    void testEndpointGetPage() {
        given()
                .when()
                .get("/setting/?page=0&limit=1")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.SETTING.JSON_PAGE_ARRAY_0));
    }

    @Test
    void testEndpointAdd() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(TestData.SETTING.JSON_0)
                .when()
                .post("/setting")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith("{\"id\":0"));
    }

    @Test
    void testEndpointPut() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(TestData.SETTING.JSON_0)
                .when()
                .put("/setting")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.SETTING.JSON_ID_0));
    }

    @Test
    void testEndpointDelete() {
        given()
                .when()
                .delete("/setting/0")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.SETTING.JSON_ID_0));
    }
}