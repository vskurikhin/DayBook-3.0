/*
 * This file was last modified at 2024-05-14 23:10 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * PostgresDatabaseTestResource.java
 * $Id$
 */

package su.svn.daybook3.api.resources;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PostgresDatabaseTestResource implements QuarkusTestResourceLifecycleManager {

    private static final Logger LOG = Logger.getLogger(PostgresDatabaseTestResource.class);

    static final String QUARKUS_DATASOURCE_REACTIVE_URL = "quarkus.datasource.reactive.url";
    static final String QUARKUS_DATASOURCE_JDBC_URL = "quarkus.datasource.jdbc.url";
    static final String QUARKUS_DATASOURCE_USERNAME = "quarkus.datasource.username";
    static final String QUARKUS_DATASOURCE_PASSWORD = "quarkus.datasource.password";

    static final String DEFAULT_DATABASE = "postgres";
    static final String DEFAULT_REACTIVE_URL = "postgresql://localhost:%d/" + DEFAULT_DATABASE;
    static final String DEFAULT_JDBC_URL = "jdbc:postgresql://localhost:%d/" + DEFAULT_DATABASE;
    static final String DEFAULT_USERNAME = "postgres";
    static final String DEFAULT_PASSWORD = "postgres";
    public static final String CHANGE_LOG = "db/changelog/db/Change-Log.xml";
    public static final String CHANGE_LOG_FIX = "db/changelog/db/Change-Log-Fix.xml";
    public static final String CHANGE_LOG_TEST = "db/changelog/db/Change-Log-Test.xml";

    private static Map<String, String> properties = new HashMap<>();

    private EmbeddedPostgres postgres;

    @Override
    public Map<String, String> start() {

        try {
            postgres = EmbeddedPostgres.start();
            properties.put(QUARKUS_DATASOURCE_USERNAME, DEFAULT_USERNAME);
            properties.put(QUARKUS_DATASOURCE_PASSWORD, DEFAULT_PASSWORD);
            properties.put(QUARKUS_DATASOURCE_REACTIVE_URL, String.format(DEFAULT_REACTIVE_URL, postgres.getPort()));
            properties.put(QUARKUS_DATASOURCE_JDBC_URL, String.format(DEFAULT_JDBC_URL, postgres.getPort()));
            liquibaseUpdate(postgres, CHANGE_LOG_FIX);
            liquibaseUpdate(postgres, CHANGE_LOG);
            liquibaseUpdate(postgres, CHANGE_LOG_TEST);
        } catch (IOException e) {
            throw new RuntimeException("Could not start Ephemeral Postgres", e);
        }
        LOG.infov(
                "Embedded Postgres started at port \"{0,number,#}\" with database \"{1}\", user \"{2}\" and password \"{3}\"",
                postgres.getPort(), DEFAULT_DATABASE, DEFAULT_USERNAME, DEFAULT_PASSWORD
        );
        return properties;
    }

    private void liquibaseUpdate(EmbeddedPostgres postgres, String changeLog) {
        var dataBase = postgres.getPostgresDatabase();
        var resourceAccessor = new ClassLoaderResourceAccessor();
        try (var connection = dataBase.getConnection()) {
            var liquibase = new Liquibase(changeLog, resourceAccessor, new JdbcConnection(connection));
            liquibase.update();
        } catch (Exception e) {
            throw new RuntimeException("Could not connect to Ephemeral Postgres", e);
        }
    }

    @Override
    public void stop() {
        if (postgres != null) {
            try {
                postgres.close();
            } catch (IOException e) {
                LOG.warn("Could not stop Ephemeral Postgres", e);
            }
            postgres = null;
        }
    }
}
