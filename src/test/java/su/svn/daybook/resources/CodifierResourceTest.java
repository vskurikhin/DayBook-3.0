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
import su.svn.daybook.domain.model.CodifierTable;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.domain.CodifierService;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;

@QuarkusTest
class CodifierResourceTest {

    static final Uni<Answer> UNI_ANSWER_API_RESPONSE_NONE_STRING = Uni.createFrom().item(Answer.of(new ApiResponse<>(CodifierTable.NONE)));

    static Uni<Answer> test = Uni.createFrom()
            .item(1)
            .onItem()
            .transform(i -> Answer.of(TestData.CODIFIER.TABLE_0));

    CodifierService mock;

    @BeforeEach
    void setUp() {
        PageRequest pageRequest = new PageRequest(0, (short) 1);
        mock = Mockito.mock(CodifierService.class);
        Mockito.when(mock.get(CodifierTable.NONE)).thenReturn(test);
        Mockito.when(mock.get(RuntimeException.class.getSimpleName())).thenThrow(RuntimeException.class);
        Mockito.when(mock.get(Integer.toString(Integer.MAX_VALUE))).thenReturn(TestData.UNI_ANSWER_EMPTY);
        Mockito.when(mock.get(Integer.toString(Integer.MIN_VALUE))).thenReturn(TestData.UNI_ANSWER_NULL);
        Mockito.when(mock.getAll()).thenReturn(Multi.createFrom().item(Answer.of(TestData.CODIFIER.TABLE_0)));
        Mockito.when(mock.getPage(pageRequest)).thenReturn(TestData.CODIFIER.UNI_PAGE_ANSWER_SINGLETON_TEST);
        Mockito.when(mock.add(TestData.CODIFIER.TABLE_0)).thenReturn(UNI_ANSWER_API_RESPONSE_NONE_STRING);
        Mockito.when(mock.put(TestData.CODIFIER.TABLE_0)).thenReturn(UNI_ANSWER_API_RESPONSE_NONE_STRING);
        Mockito.when(mock.delete(CodifierTable.NONE)).thenReturn(UNI_ANSWER_API_RESPONSE_NONE_STRING);
        QuarkusMock.installMockForType(mock, CodifierService.class);
    }

    @Test
    void testEndpointGet() {
        given()
                .when()
                .get("/code/" + CodifierTable.NONE)
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
                .get("/code/_?get-all")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.CODIFIER.JSON_ARRAY_SINGLETON_0));
    }

    @Test
    void testEndpointGetPage() {
        given()
                .when()
                .get("/code/?page=0&limit=1")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.CODIFIER.JSON_PAGE_ARRAY_0));
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
                .body(CoreMatchers.startsWith("{\"id\":\"" + CodifierTable.NONE));
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
                .body(CoreMatchers.startsWith("{\"id\":\"" + CodifierTable.NONE));
    }

    @Test
    void testEndpointDelete() {
        given()
                .when()
                .delete("/code/" + CodifierTable.NONE)
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith("{\"id\":\"" + CodifierTable.NONE));
    }
}