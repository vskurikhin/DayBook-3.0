/*
 * This file was last modified at 2022.01.11 17:59 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * CodifiersResourceTest.java
 * $Id$
 */

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
import su.svn.daybook.services.CodifierService;

import static io.restassured.RestAssured.given;

@QuarkusTest
class CodifiersResourceTest {
    @BeforeEach
    void setUp() {
        Multi<Answer> test = Multi.createFrom().item(Answer.of(DataTest.OBJECT_Codifier_0));
        CodifierService mock = Mockito.mock(CodifierService.class);
        Mockito.when(mock.getAll()).thenReturn(test);
        QuarkusMock.installMockForType(mock, CodifierService.class);
    }

    @Test
    void all() {
        given()
                .when()
                .get("/codes/all")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(DataTest.JSON_ARRAY_Codifier_0));
    }
}