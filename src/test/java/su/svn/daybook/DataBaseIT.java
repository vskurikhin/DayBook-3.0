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
import org.junit.jupiter.api.*;
import su.svn.daybook.domain.dao.UserNameDao;
import su.svn.daybook.domain.model.UserName;
import su.svn.daybook.resources.PostgresDatabaseTestResource;

import javax.inject.Inject;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@QuarkusTest
@QuarkusTestResource(PostgresDatabaseTestResource.class)
public class DataBaseIT {

    @Inject
    UserNameDao userNameDao;

    @Nested
    @DisplayName("UserNameDao")
    class UserNameDaoTest {

        UUID uuid1 = new UUID(0, 1);

        UserName userName1;

        @BeforeEach
        void setUp() throws ExecutionException, InterruptedException {
            userName1 = UserName.builder()
                    .withId(uuid1)
                    .withUserName("user")
                    .withPassword("password")
                    .build();
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            uuid1, userNameDao.insert(userName1)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
        }

        @AfterEach
        void tearDown() {
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            uuid1, userNameDao.delete(uuid1)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
        }

        @Test
        void test() throws ExecutionException, InterruptedException {
            var expected1 = UserName.builder()
                    .withId(uuid1)
                    .withUserName("user")
                    .withPassword("password")
                    .build();
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = userNameDao.findById(uuid1)
                                .subscribeAsCompletionStage()
                                .get()
                                .orElse(null);
                        Assertions.assertNotNull(test);
                        Assertions.assertEquals(expected1, test);
                        Assertions.assertNotNull(test.getCreateTime());
                        Assertions.assertNull(test.getUpdateTime());
                    }
            );
            var expected2 = UserName.builder()
                    .withId(uuid1)
                    .withUserName("none")
                    .withPassword("oops")
                    .build();
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            uuid1, userNameDao.update(expected2)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = userNameDao.findById(uuid1)
                                .subscribeAsCompletionStage()
                                .get()
                                .orElse(null);
                        Assertions.assertNotNull(test);
                        Assertions.assertEquals(expected2, test);
                        Assertions.assertNotNull(test.getCreateTime());
                        Assertions.assertNotNull(test.getUpdateTime());
                    }
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = userNameDao.findAll()
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertFalse(test.isEmpty());
                        Assertions.assertEquals(1, test.size());
                    }
            );
        }
    }
}
