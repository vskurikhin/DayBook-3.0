package su.svn.daybook.resources;

import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import su.svn.daybook.TestData;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.domain.UserNameService;

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
            .transform(i -> Answer.of(TestData.USERNAME.OBJECT_0));

    @BeforeAll
    public static void setup() {
        PageRequest pageRequest = new PageRequest(0, (short) 1);
        mock = Mockito.mock(UserNameService.class);
        Mockito.when(mock.get(TestData.STRING_ZERO_UUID)).thenReturn(test);
        Mockito.when(mock.get(TestData.RANDOM1_UUID.toString())).thenReturn(TestData.UNI_ANSWER_EMPTY);
        Mockito.when(mock.get(TestData.RANDOM2_UUID.toString())).thenReturn(TestData.UNI_ANSWER_NULL);
        Mockito.when(mock.getAll()).thenReturn(Multi.createFrom().item(Answer.of(TestData.USERNAME.OBJECT_0)));
        Mockito.when(mock.getPage(pageRequest)).thenReturn(TestData.USERNAME.UNI_PAGE_ANSWER_SINGLETON_TEST);
        Mockito.when(mock.add(TestData.USERNAME.OBJECT_0)).thenReturn(TestData.UNI_ANSWER_API_RESPONSE_ZERO_UUID);
        Mockito.when(mock.put(TestData.USERNAME.OBJECT_0)).thenReturn(TestData.UNI_ANSWER_API_RESPONSE_ZERO_UUID);
        Mockito.when(mock.delete(TestData.STRING_ZERO_UUID)).thenReturn(TestData.UNI_ANSWER_API_RESPONSE_ZERO_UUID);
        QuarkusMock.installMockForType(mock, UserNameService.class);
    }

    @Test
    void testEndpointGet() {
        given()
                .when()
                .get("/user/" + TestData.STRING_ZERO_UUID)
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.USERNAME.JSON_0));
    }

    @Test
    void testEndpointGetNoSuchElementException() {
        Mockito.when(mock.get(TestData.STRING_ZERO_UUID)).thenThrow(NoSuchElementException.class);
        given()
                .when()
                .get("/user/" + TestData.STRING_ZERO_UUID)
                .then()
                .statusCode(400)
                .body(CoreMatchers.startsWith("{\"error\": 400,\"message\": \"java.util.NoSuchElementException\"}"));
    }

    @Test
    void testEndpointGetAll() {
        given()
                .when()
                .get("/user/_?get-all")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.USERNAME.JSON_ARRAY_SINGLETON_0));
    }

    @Test
    void testEndpointGetPage() {
        given()
                .when()
                .get("/user/?page=0&limit=1")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.USERNAME.JSON_PAGE_ARRAY_0));
    }

    @Test
    void testEndpointAdd() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(TestData.USERNAME.JSON_0)
                .when()
                .post("/user")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.USERNAME.JSON_ID_0));
    }

    @Test
    void testEndpointPut() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(TestData.USERNAME.JSON_0)
                .when()
                .put("/user")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.USERNAME.JSON_ID_0));
    }

    @Test
    void testEndpointDelete() {
        given()
                .when()
                .delete("/user/" + TestData.STRING_ZERO_UUID)
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.USERNAME.JSON_ID_0));
    }
}