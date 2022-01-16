/*
 * This file was last modified at 2022.01.16 10:47 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * VocabulariesResourceTest.java
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
import su.svn.daybook.services.VocabularyService;

import static io.restassured.RestAssured.given;

@QuarkusTest
class VocabulariesResourceTest {

    @BeforeEach
    void setUp() {
        Multi<Answer> test = Multi.createFrom().item(Answer.of(DataTest.OBJECT_Vocabulary_0));
        VocabularyService mock = Mockito.mock(VocabularyService.class);
        Mockito.when(mock.getAll()).thenReturn(test);
        QuarkusMock.installMockForType(mock, VocabularyService.class);
    }

    @Test
    void testEndpoint_all() {
        given()
                .when()
                .get("/vocabularies/all")
                .then()
                .statusCode(200)
                .body(CoreMatchers.startsWith(DataTest.JSON_ARRAY_Vocabulary_0));
    }
}