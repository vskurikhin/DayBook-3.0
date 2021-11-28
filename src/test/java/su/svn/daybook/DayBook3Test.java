package su.svn.daybook;

import io.quarkus.test.junit.QuarkusTest;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class DayBook3Test {

    @Test
    public void testHelloEndpoint() {
        given()
                .queryParam("name", "Quarkus")
                .when()
                .get("/hello")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith("Hello Quarkus"));
    }
}