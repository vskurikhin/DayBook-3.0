package su.svn.daybook.resources;

import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Multi;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import su.svn.daybook.DataTest;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.services.TagLabelService;

import static io.restassured.RestAssured.given;

@QuarkusTest
class TagLabelsResourceTest {

    @BeforeEach
    void setUp() {
        Multi<Answer> test = Multi.createFrom().item(Answer.of(DataTest.OBJECT_TagLabel_0));
        TagLabelService mock = Mockito.mock(TagLabelService.class);
        Mockito.when(mock.getAll()).thenReturn(test);
        QuarkusMock.installMockForType(mock, TagLabelService.class);
    }

    @Test
    void all() {
        given()
                .when()
                .get("/tags/all")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(DataTest.JSON_ARRAY_TagLabel_0));
    }
}