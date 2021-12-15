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
import su.svn.daybook.services.CodifierService;

import static io.restassured.RestAssured.given;

@QuarkusTest
class CodifierResourceTest {

    @BeforeAll
    public static void setup() {
        Uni<Answer> tezd = Uni.createFrom()
                .item(1)
                .onItem()
                .transform(i -> Answer.of(DataTest.TEZD_Codifier));
        Uni<Answer> empty = Uni.createFrom()
                .item(Answer.empty());
        Uni<Answer> tezdId = Uni.createFrom().item(Answer.of(0L));

        CodifierService mock = Mockito.mock(CodifierService.class);
        Mockito.when(mock.codeGet("0")).thenReturn(tezd);
        Mockito.when(mock.codeGet("2147483647")).thenReturn(empty);
        Mockito.when(mock.codeAdd(DataTest.TEZD_Codifier)).thenReturn(tezdId);
        QuarkusMock.installMockForType(mock, CodifierService.class);
    }

    @Test
    void get() {
        given()
                .when()
                .get("/code/0")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(DataTest.TEZD_Codifier_JSON));
    }

    @Test
    void testGetNoneEndpoint() {
        given()
                .when()
                .get("/code/" + Integer.MAX_VALUE)
                .then()
                .statusCode(404);
    }

    @Test
    void add() {
    }
}