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
import su.svn.daybook.services.domain.RoleService;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.NoSuchElementException;

import static io.restassured.RestAssured.given;

@QuarkusTest
class RoleResourceTest {

    static RoleService mock;

    static Uni<Answer> test = Uni.createFrom()
            .item(1)
            .map(i -> Answer.of(TestData.ROLE.MODEL_0));

    @BeforeAll
    public static void setup() {
        PageRequest pageRequest = new PageRequest(0, (short) 1);
        mock = Mockito.mock(RoleService.class);
        Mockito.when(mock.get(TestData.ZERO_UUID)).thenReturn(test);
        Mockito.when(mock.get(TestData.RANDOM1_UUID.toString())).thenReturn(TestData.UNI_ANSWER_EMPTY);
        Mockito.when(mock.get(TestData.RANDOM2_UUID.toString())).thenReturn(TestData.UNI_ANSWER_NULL);
        Mockito.when(mock.getAll()).thenReturn(Multi.createFrom().item(Answer.of(TestData.ROLE.MODEL_0)));
        Mockito.when(mock.getPage(pageRequest)).thenReturn(TestData.ROLE.UNI_PAGE_ANSWER_SINGLETON_TEST);
        Mockito.when(mock.add(TestData.ROLE.MODEL_0)).thenReturn(TestData.UNI_ANSWER_API_RESPONSE_ZERO_UUID);
        Mockito.when(mock.put(TestData.ROLE.MODEL_0)).thenReturn(TestData.UNI_ANSWER_API_RESPONSE_ZERO_UUID);
        Mockito.when(mock.delete(TestData.ZERO_UUID)).thenReturn(TestData.UNI_ANSWER_API_RESPONSE_ZERO_UUID);
        QuarkusMock.installMockForType(mock, RoleService.class);
    }

    @Test
    void testEndpointGet() {
        given()
                .when()
                .get("/role/" + TestData.STRING_ZERO_UUID)
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.ROLE.JSON_0));
    }

    @Test
    void testEndpointGetNoSuchElementException() {
        Mockito.when(mock.get(TestData.RANDOM1_UUID)).thenThrow(NoSuchElementException.class);
        given()
                .when()
                .get("/role/" + TestData.RANDOM1_UUID.toString())
                .then()
                .statusCode(400)
                .body(CoreMatchers.startsWith("{\"error\": 400,\"message\": \"java.util.NoSuchElementException\"}"));
    }

    @Test
    void testEndpointGetAll() {
        given()
                .when()
                .get("/role/_?get-all")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.ROLE.JSON_ARRAY_SINGLETON_0));
    }

    @Test
    void testEndpointGetPage() {
        given()
                .when()
                .get("/role/?page=0&limit=1")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.ROLE.JSON_PAGE_ARRAY_0));
    }

    @Test
    void testEndpointAdd() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(TestData.ROLE.JSON_0)
                .when()
                .post("/role")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.ROLE.JSON_ID_0));
    }

    @Test
    void testEndpointPut() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(TestData.ROLE.JSON_0)
                .when()
                .put("/role")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.ROLE.JSON_ID_0));
    }

    @Test
    void testEndpointDelete() {
        given()
                .when()
                .delete("/role/" + TestData.STRING_ZERO_UUID)
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.ROLE.JSON_ID_0));
    }
}