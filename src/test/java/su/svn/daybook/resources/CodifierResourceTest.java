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
import su.svn.daybook.domain.messages.DictionaryResponse;
import su.svn.daybook.domain.model.Codifier;
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

    static Uni<Answer> tezdId = Uni.createFrom().item(Answer.of(DictionaryResponse.code(Codifier.NONE)));

    @BeforeAll
    public static void setup() {
        CodifierService mock = Mockito.mock(CodifierService.class);
        Mockito.when(mock.codeGet("code")).thenReturn(tezd);
        Mockito.when(mock.codeGet(Integer.toString(Integer.MAX_VALUE))).thenReturn(empty);
        Mockito.when(mock.codeGet(Integer.toString(Integer.MIN_VALUE))).thenReturn(nullAnswer);
        Mockito.when(mock.codeAdd(DataTest.OBJECT_Codifier_0)).thenReturn(tezdId);
        Mockito.when(mock.codePut(DataTest.OBJECT_Codifier_0)).thenReturn(tezdId);
        Mockito.when(mock.codeDelete("code")).thenReturn(tezdId);
        QuarkusMock.installMockForType(mock, CodifierService.class);
    }

    @Test
    void testEndpointGet() {
        given()
                .when()
                .get("/code/code")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(DataTest.JSON_Codifier_0));
    }

    @Test
    void testEndpointAdd() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(DataTest.JSON_Codifier_0)
                .when()
                .post("/code/add")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(DataTest.JSON_Codifier_Id_0));
    }

    @Test
    void testEndpointPut() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(DataTest.JSON_Codifier_0)
                .when()
                .put("/code/put")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(DataTest.JSON_Codifier_Id_0));
    }

    @Test
    void testEndpointDelete() {
        given()
                .when()
                .delete("/code/code")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(DataTest.JSON_Codifier_Id_0));
    }
}