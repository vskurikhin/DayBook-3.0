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
import su.svn.daybook.domain.messages.ApiResponse;
import su.svn.daybook.domain.model.Codifier;
import su.svn.daybook.services.CodifierService;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;

@QuarkusTest
class CodifierResourceTest {

    static final Uni<Answer> UNI_ANSWER_API_RESPONSE_NONE_STRING = Uni.createFrom().item(Answer.of(new ApiResponse<>(Codifier.NONE)));

    static Uni<Answer> test = Uni.createFrom()
            .item(1)
            .onItem()
            .transform(i -> Answer.of(TestData.CODIFIER.OBJECT_0));

    CodifierService mock;

    @BeforeEach
    void setUp() {
        mock = Mockito.mock(CodifierService.class);
        Mockito.when(mock.get(Codifier.NONE)).thenReturn(test);
        Mockito.when(mock.get(RuntimeException.class.getSimpleName())).thenThrow(RuntimeException.class);
        Mockito.when(mock.get(Integer.toString(Integer.MAX_VALUE))).thenReturn(TestData.UNI_ANSWER_EMPTY);
        Mockito.when(mock.get(Integer.toString(Integer.MIN_VALUE))).thenReturn(TestData.UNI_ANSWER_NULL);
        Mockito.when(mock.getAll()).thenReturn(Multi.createFrom().item(Answer.of(TestData.CODIFIER.OBJECT_0)));
        Mockito.when(mock.add(TestData.CODIFIER.OBJECT_0)).thenReturn(UNI_ANSWER_API_RESPONSE_NONE_STRING);
        Mockito.when(mock.put(TestData.CODIFIER.OBJECT_0)).thenReturn(UNI_ANSWER_API_RESPONSE_NONE_STRING);
        Mockito.when(mock.delete(Codifier.NONE)).thenReturn(UNI_ANSWER_API_RESPONSE_NONE_STRING);
        QuarkusMock.installMockForType(mock, CodifierService.class);
    }

    @Test
    void testEndpointGet() {
        given()
                .when()
                .get("/code/" + Codifier.NONE)
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.CODIFIER.JSON_0));
    }

    @Test
    void testEndpointGetWhenRuntimeException() {
        given()
                .when()
                .get("/code/" + RuntimeException.class.getSimpleName())
                .then()
                .statusCode(400);
    }

    @Test
    void testEndpointGetWhenEmpty() {
        given()
                .when()
                .get("/code/" + Integer.MAX_VALUE)
                .then()
                .statusCode(404);
    }

    @Test
    void testEndpointGetWhenNull() {
        given()
                .when()
                .get("/code/" + Integer.MIN_VALUE)
                .then()
                .statusCode(406);
    }

    @Test
    void testEndpointGetAll() {
        given()
                .when()
                .get("/code/all")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.CODIFIER.JSON_ARRAY_SINGLETON_0));
    }

    @Test
    void testEndpointAdd() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(TestData.CODIFIER.JSON_0)
                .when()
                .post("/code")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith("{\"id\":\"" + Codifier.NONE));
    }

    @Test
    void testEndpointPut() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(TestData.CODIFIER.JSON_0)
                .when()
                .put("/code")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith("{\"id\":\"" + Codifier.NONE));
    }

    @Test
    void testEndpointDelete() {
        given()
                .when()
                .delete("/code/" + Codifier.NONE)
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith("{\"id\":\"" + Codifier.NONE));
    }
}