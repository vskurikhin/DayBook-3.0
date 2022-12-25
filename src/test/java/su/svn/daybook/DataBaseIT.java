/*
 * This file was last modified at 2022.12.24 21:17 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * DatabaseIT.java
 * $Id$
 */

package su.svn.daybook;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import su.svn.daybook.resources.PostgresDatabaseTestResource;

import javax.inject.Inject;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.ExecutionException;

import static io.restassured.RestAssured.given;

@QuarkusTest
@QuarkusTestResource(PostgresDatabaseTestResource.class)
public class DataBaseIT {

    @Inject
    io.vertx.mutiny.pgclient.PgPool client;

    @Test
    public void testMigration() throws InterruptedException, ExecutionException {
        Assertions.assertNotNull(client);
        Assertions.assertNotNull(client.getDelegate());
        var del = client.getDelegate().toString();
        System.err.println("del = " + del);
        Assertions.assertNotNull(client.getConnection());
        var con = client.getConnection().subscribe().asCompletionStage().get().toString();
        Assertions.assertNotNull(con);
        System.err.println("con = " + con);
    }
}
