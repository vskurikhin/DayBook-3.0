/*
 * This file was last modified at 2022.12.24 21:17 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * HelloIT.java
 * $Id$
 */

package su.svn.daybook;

import io.quarkus.test.junit.QuarkusTest;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class HelloIT {

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