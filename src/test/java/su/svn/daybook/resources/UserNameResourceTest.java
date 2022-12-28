package su.svn.daybook.resources;

import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import su.svn.daybook.DataTest;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.services.UserNameService;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.NoSuchElementException;

import static io.restassured.RestAssured.given;

@QuarkusTest
class UserNameResourceTest {

    static UserNameService mock;

    static Uni<Answer> test = Uni.createFrom()
            .item(1)
            .onItem()
            .transform(i -> Answer.of(DataTest.OBJECT_UserName_0));

    @BeforeAll
    public static void setup() {
        mock = Mockito.mock(UserNameService.class);
        Mockito.when(mock.get(DataTest.STRING_ZERO_UUID)).thenReturn(test);
        Mockito.when(mock.get(DataTest.RANDOM1_UUID.toString())).thenReturn(DataTest.UNI_ANSWER_EMPTY);
        Mockito.when(mock.get(DataTest.RANDOM2_UUID.toString())).thenReturn(DataTest.UNI_ANSWER_NULL);
        Mockito.when(mock.getAll()).thenReturn(Multi.createFrom().item(Answer.of(DataTest.OBJECT_UserName_0)));
        Mockito.when(mock.add(DataTest.OBJECT_UserName_0)).thenReturn(DataTest.UNI_ANSWER_API_RESPONSE_ZERO_UUID);
        Mockito.when(mock.put(DataTest.OBJECT_UserName_0)).thenReturn(DataTest.UNI_ANSWER_API_RESPONSE_ZERO_UUID);
        Mockito.when(mock.delete(DataTest.STRING_ZERO_UUID)).thenReturn(DataTest.UNI_ANSWER_API_RESPONSE_ZERO_UUID);
        QuarkusMock.installMockForType(mock, UserNameService.class);
    }

    @Test
    void testEndpointGet() {
        given()
                .when()
                .get("/user/" + DataTest.STRING_ZERO_UUID)
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(DataTest.JSON_UserName_0));
    }

    @Test
    void testEndpointGetNoSuchElementException() {
        Mockito.when(mock.get(DataTest.STRING_ZERO_UUID)).thenThrow(NoSuchElementException.class);
        given()
                .when()
                .get("/user/" + DataTest.STRING_ZERO_UUID)
                .then()
                .statusCode(400)
                .body(CoreMatchers.startsWith("{\"error\": 400,\"message\": \"java.util.NoSuchElementException\"}"));
    }

    @Test
    void testEndpointGetAll() {
        given()
                .when()
                .get("/user/all")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(DataTest.JSON_ARRAY_UserName_0));
    }

    @Test
    void testEndpointAdd() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(DataTest.JSON_UserName_0)
                .when()
                .post("/user")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(DataTest.JSON_UserName_Id_0));
    }

    @Test
    void testEndpointPut() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(DataTest.JSON_UserName_0)
                .when()
                .put("/user")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(DataTest.JSON_UserName_Id_0));
    }

    @Test
    void testEndpointDelete() {
        given()
                .when()
                .delete("/user/" + DataTest.STRING_ZERO_UUID)
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(DataTest.JSON_UserName_Id_0));
    }
}