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
import su.svn.daybook.domain.messages.DictionaryResponse;
import su.svn.daybook.domain.model.Codifier;
import su.svn.daybook.services.CodifierService;
import su.svn.daybook.services.CodifierService;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import java.util.Optional;

import static io.restassured.RestAssured.given;

@QuarkusTest
class CodifierResourceTest {

    static final Uni<Answer> UNI_ANSWER_API_RESPONSE_NONE_STRING = Uni.createFrom().item(Answer.of(new ApiResponse<>(Codifier.NONE)));

    static Uni<Answer> test = Uni.createFrom()
            .item(1)
            .onItem()
            .transform(i -> Answer.of(DataTest.OBJECT_Codifier_0));

    CodifierService mock;

    @BeforeEach
    void setUp() {
        mock = Mockito.mock(CodifierService.class);
        Mockito.when(mock.get(Codifier.NONE)).thenReturn(test);
        Mockito.when(mock.get(RuntimeException.class.getSimpleName())).thenThrow(RuntimeException.class);
        Mockito.when(mock.get(Integer.toString(Integer.MAX_VALUE))).thenReturn(DataTest.UNI_ANSWER_EMPTY);
        Mockito.when(mock.get(Integer.toString(Integer.MIN_VALUE))).thenReturn(DataTest.UNI_ANSWER_NULL);
        Mockito.when(mock.getAll()).thenReturn(Multi.createFrom().item(Answer.of(DataTest.OBJECT_Codifier_0)));
        Mockito.when(mock.add(DataTest.OBJECT_Codifier_0)).thenReturn(UNI_ANSWER_API_RESPONSE_NONE_STRING);
        Mockito.when(mock.put(DataTest.OBJECT_Codifier_0)).thenReturn(UNI_ANSWER_API_RESPONSE_NONE_STRING);
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
                .body(CoreMatchers.startsWith(DataTest.JSON_Codifier_0));
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
                .body(CoreMatchers.startsWith(DataTest.JSON_ARRAY_Codifier_0));
    }

    @Test
    void testEndpointAdd() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(DataTest.JSON_Codifier_0)
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
                .body(DataTest.JSON_Codifier_0)
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


//
//    static Uni<Answer> tezd = Uni.createFrom()
//            .item(1)
//            .onItem()
//            .transform(i -> Answer.of(DataTest.OBJECT_Codifier_0));
//
//    static Uni<Answer> empty = Uni.createFrom().item(Answer.empty());
//
//    static Uni<Answer> nullAnswer = Uni.createFrom().item(() -> null);
//
//    static Uni<Answer> tezdId = Uni.createFrom().item(Answer.of(DictionaryResponse.code(Codifier.NONE)));
//
//    @BeforeAll
//    public static void setup() {
//        CodifierService mock = Mockito.mock(CodifierService.class);
//        Mockito.when(mock.get("code")).thenReturn(tezd);
//        Mockito.when(mock.get(Integer.toString(Integer.MAX_VALUE))).thenReturn(empty);
//        Mockito.when(mock.get(Integer.toString(Integer.MIN_VALUE))).thenReturn(nullAnswer);
//        Mockito.when(mock.add(DataTest.OBJECT_Codifier_0)).thenReturn(tezdId);
//        Mockito.when(mock.put(DataTest.OBJECT_Codifier_0)).thenReturn(tezdId);
//        Mockito.when(mock.delete("code")).thenReturn(tezdId);
//        QuarkusMock.installMockForType(mock, CodifierService.class);
//    }
//
//    @Test
//    void testEndpointGet() {
//        given()
//                .when()
//                .get("/code/code")
//                .then()
//                .statusCode(200)
//                .body(CoreMatchers.startsWith(DataTest.JSON_Codifier_0));
//    }
//
//    @Test
//    void testEndpointAdd() {
//        given()
//                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
//                .body(DataTest.JSON_Codifier_0)
//                .when()
//                .post("/code/add")
//                .then()
//                .statusCode(200)
//                .body(CoreMatchers.startsWith(DataTest.JSON_Codifier_Id_0));
//    }
//
//    @Test
//    void testEndpointPut() {
//        given()
//                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
//                .body(DataTest.JSON_Codifier_0)
//                .when()
//                .put("/code/put")
//                .then()
//                .statusCode(200)
//                .body(CoreMatchers.startsWith(DataTest.JSON_Codifier_Id_0));
//    }
//
//    @Test
//    void testEndpointDelete() {
//        given()
//                .when()
//                .delete("/code/code")
//                .then()
//                .statusCode(200)
//                .body(CoreMatchers.startsWith(DataTest.JSON_Codifier_Id_0));
//    }
}