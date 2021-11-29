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
import su.svn.daybook.services.TagLabelService;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;

@QuarkusTest
class TagLabelResourceTest {

    @BeforeAll
    public static void setup() {
        Uni<Answer> tezd = Uni.createFrom()
                .item(1)
                .onItem()
                .transform(i -> Answer.of(DataTest.TEZD_TagLabel));
        Uni<Answer> empty = Uni.createFrom()
                .item(Answer.empty());
        Uni<Answer> tezdString = Uni.createFrom().item(Answer.of("tezd"));

        TagLabelService mock = Mockito.mock(TagLabelService.class);
        Mockito.when(mock.tagGet("none")).thenReturn(empty);
        Mockito.when(mock.tagGet("tezd")).thenReturn(tezd);
        Mockito.when(mock.tagAdd(DataTest.TEZD_TagLabel)).thenReturn(tezdString);
        QuarkusMock.installMockForType(mock, TagLabelService.class);
    }

    @Test
    void testGetEndpoint() {
        given()
                .when()
                .get("/tag/tezd")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(DataTest.TEZD_TagLabel_JSON));
    }

    @Test
    void testGetNoneEndpoint() {
        given()
                .when()
                .get("/tag/none")
                .then()
                .statusCode(404);
    }

    @Test
    void testAddEndpoint() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(DataTest.TEZD_TagLabel_JSON)
                .when()
                .post("/tag/add")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith("tezd"));
    }
}