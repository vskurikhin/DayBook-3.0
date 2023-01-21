/*
 * This file was last modified at 2022.12.24 21:17 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * su.svn.daybook.DataBase@Name@TableIT.java
 * $Id$
 */

package su.svn.daybook;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.*;
import su.svn.daybook.TestData;
import su.svn.daybook.domain.dao.@Name@Dao;
import su.svn.daybook.domain.model.@Name@Table;
import su.svn.daybook.resources.PostgresDatabaseTestResource;

import javax.inject.Inject;
import java.util.UUID;

import static su.svn.daybook.TestUtils.multiAsListHelper;
import static su.svn.daybook.TestUtils.uniOptionalHelper;

@QuarkusTest
@QuarkusTestResource(value = PostgresDatabaseTestResource.class, restrictToAnnotatedClass = true)
public class @Name@DataBaseIT {
    @Inject
    @Name@Dao @name@Dao;

    @Nested
    @DisplayName("@Name@Dao")
    class @Name@DaoTest {
        @IdType@ id;
        @IdType@ customId = TestData.uuid.ONE;
        @Name@Table entry;

        @BeforeEach
        void setUp() {
            entry = @Name@Table.builder()
                    .@key@(@KType@.ONE)
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(() -> {
                id = uniOptionalHelper(@name@Dao.insert(entry));
            });
        }

        @AfterEach
        void tearDown() {
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id, uniOptionalHelper(@name@Dao.delete(id))));
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(0, uniOptionalHelper(@name@Dao.count())));
        }

        @Name@Table.Builder builder(@IdType@ id, @KType@ @key@, @Name@Table test) {
            return @Name@Table.builder()
                    .id(id)
                    .@key@(@key@)
                    .createTime(test.createTime())
                    .updateTime(test.updateTime())
                    .enabled(true);
        }

        @Name@Table expected(@IdType@ id, @KType@ @key@, @Name@Table test) {
            Assertions.assertNotNull(test);
            return builder(id, @key@, test).build();
        }

        @Name@Table expected(@IdType@ id, @KType@ @key@, @VType@ @value@, @Name@Table test) {
            Assertions.assertNotNull(test);
            return builder(id, @key@, test).@value@(@value@).build();
        }

        @Test
        void test() {
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(@name@Dao.findById(id));
                var expected = expected(id, @KType@.ONE, test);
                Assertions.assertEquals(expected, test);
                Assertions.assertNotNull(test.createTime());
                Assertions.assertNull(test.updateTime());
            });
            var update = @Name@Table.builder().id(id).@key@(@KType@.TWO).build();
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id, uniOptionalHelper(@name@Dao.update(update))));
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(@name@Dao.findById(id));
                var expected = expected(id, @KType@.TWO, test);
                Assertions.assertEquals(expected, test);
                Assertions.assertNotNull(test.createTime());
                Assertions.assertNotNull(test.updateTime());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(@name@Dao.findBy@Key@(@KType@.TWO));
                var expected = expected(id, @KType@.TWO, test);
                Assertions.assertEquals(expected, test);
                Assertions.assertNotNull(test.createTime());
                Assertions.assertNotNull(test.updateTime());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper((@name@Dao.findAll()));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(@name@Dao.findRange(0, 0));
                Assertions.assertNotNull(test);
                Assertions.assertTrue(test.isEmpty());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(@name@Dao.findRange(0, 1));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
            });
            var custom = @Name@Table.builder()
                    .id(customId)
                    .@key@(@KType@.@value@Of(7))
                    .@value@(new @VType@("{}"))
                    .build();
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(@name@Dao.insertEntry(custom));
                var expected = expected(customId, @KType@.@value@Of(7), new @VType@("{}"), test);
                Assertions.assertEquals(expected, test);
            });
            var customUpdate = @Name@Table.builder()
                    .id(customId)
                    .@key@(@KType@.TEN)
                    .@value@(new @VType@("{}"))
                    .build();
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(@name@Dao.updateEntry(customUpdate));
                var expected = expected(customId, @KType@.TEN, new @VType@("{}"), test);
                Assertions.assertEquals(expected, test);
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(@name@Dao.findBy@Value@(new @VType@("{}")));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
                var expected = expected(customId, @KType@.TEN, new @VType@("{}"), test.get(0));
                Assertions.assertEquals(expected, test.get(0));
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(@name@Dao.findRange(0, 1));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
                var expected = expected(customId, @KType@.TEN, new @VType@("{}"), test.get(0));
                Assertions.assertEquals(expected, test.get(0));
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(@name@Dao.findRange(0, Long.MAX_VALUE));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(2, test.size());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(@name@Dao.findRange(1, 1));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
            });
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(customId, uniOptionalHelper(@name@Dao.delete(customId)))
            );
        }
    }
}
