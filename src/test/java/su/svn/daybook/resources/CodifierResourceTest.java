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

    static Uni<Answer> tezdId = Uni.createFrom().item(Answer.of(new ApiResponse(0L)));

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
                .body(CoreMatchers.startsWith("{\"id\":0}"));
    }

    @Test
    void testEndpoint_get_whenNone() {
        given()
                .when()
                .get("/code/" + Integer.MAX_VALUE)
                .then()
                .statusCode(404);
    }

    @Test
    void testEndpoint_get_whenNull() {
        given()
                .when()
                .get("/code/" + Integer.MIN_VALUE)
                .then()
                .statusCode(406);
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
                .body(CoreMatchers.startsWith(DataTest.JSON_Codifier_0));
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
                .body(CoreMatchers.startsWith(DataTest.JSON_Codifier_0));
    }

    @Test
    void testEndpoint_delete() {
        given()
                .when()
                .delete("/code/0")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(DataTest.JSON_Codifier_0));
    }
}