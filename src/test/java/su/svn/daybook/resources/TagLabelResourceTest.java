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
import su.svn.daybook.services.domain.TagLabelService;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;

@QuarkusTest
class TagLabelResourceTest {

    static Uni<Answer> test = Uni.createFrom()
            .item(1)
            .onItem()
            .transform(i -> Answer.of(TestData.TAG_LABEL.TABLE_0));

    TagLabelService mock;

    @BeforeEach
    void setUp() {
        PageRequest pageRequest = new PageRequest(0, (short) 1);
        mock = Mockito.mock(TagLabelService.class);
        Mockito.when(mock.get(TestData.TAG_LABEL.ID)).thenReturn(test);
        Mockito.when(mock.get(RuntimeException.class.getSimpleName())).thenThrow(RuntimeException.class);
        Mockito.when(mock.get(Integer.toString(Integer.MAX_VALUE))).thenReturn(TestData.UNI_ANSWER_EMPTY);
        Mockito.when(mock.get(Integer.toString(Integer.MIN_VALUE))).thenReturn(TestData.UNI_ANSWER_NULL);
        Mockito.when(mock.getAll()).thenReturn(Multi.createFrom().item(Answer.of(TestData.TAG_LABEL.TABLE_0)));
        Mockito.when(mock.getPage(pageRequest)).thenReturn(TestData.TAG_LABEL.UNI_PAGE_ANSWER_SINGLETON_TEST);
        Mockito.when(mock.add(TestData.TAG_LABEL.TABLE_0)).thenReturn(TestData.TAG_LABEL.UNI_ANSWER_API_RESPONSE_ID);
        Mockito.when(mock.put(TestData.TAG_LABEL.TABLE_0)).thenReturn(TestData.TAG_LABEL.UNI_ANSWER_API_RESPONSE_ID);
        Mockito.when(mock.delete(TestData.TAG_LABEL.ID)).thenReturn(TestData.TAG_LABEL.UNI_ANSWER_API_RESPONSE_ID);
        QuarkusMock.installMockForType(mock, TagLabelService.class);
    }

    @Test
    void testEndpointGet() {
        given()
                .when()
                .get("/tag/" + TestData.TAG_LABEL.ID)
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.TAG_LABEL.JSON_0));
    }

    @Test
    void testEndpointGetWhenRuntimeException() {
        given()
                .when()
                .get("/tag/" + RuntimeException.class.getSimpleName())
                .then()
                .statusCode(400);
    }

    @Test
    void testEndpointGetWhenEmpty() {
        given()
                .when()
                .get("/tag/" + Integer.MAX_VALUE)
                .then()
                .statusCode(404);
    }

    @Test
    void testEndpointGetWhenNull() {
        given()
                .when()
                .get("/tag/" + Integer.MIN_VALUE)
                .then()
                .statusCode(406);
    }

    @Test
    void testEndpointGetPage() {
        given()
                .when()
                .get("/tag/?page=0&limit=1")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.TAG_LABEL.JSON_PAGE_ARRAY_0));
    }

    @Test
    void testEndpointGetAll() {
        given()
                .when()
                .get("/tag/_?get-all")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(TestData.TAG_LABEL.JSON_ARRAY_SINGLETON_0));
    }

    @Test
    void testEndpointAdd() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(TestData.TAG_LABEL.TABLE_0)
                .when()
                .post("/tag")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith("{\"id\":\"" + TestData.TAG_LABEL.ID));
    }

    @Test
    void testEndpointPut() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(TestData.TAG_LABEL.JSON_0)
                .when()
                .put("/tag")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith("{\"id\":\"" + TestData.TAG_LABEL.ID));
    }

    @Test
    void testEndpointDelete() {
        given()
                .when()
                .delete("/tag/" + TestData.TAG_LABEL.ID)
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith("{\"id\":\"" + TestData.TAG_LABEL.ID));
    }
}