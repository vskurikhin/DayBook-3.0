/*
 * This file was last modified at 2023.09.07 16:35 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * @Name@DataBaseIT.java
 * $Id$
 */

package su.svn.daybook;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.*;
import su.svn.daybook.domain.dao.@Name@Dao;
import su.svn.daybook.domain.model.@Name@Table;
import su.svn.daybook.resources.PostgresDatabaseTestResource;

import jakarta.inject.Inject;
import java.util.UUID;

@QuarkusTest
@QuarkusTestResource(@value@ = PostgresDatabaseTestResource.class, restrictToAnnotatedClass = true)
public class @Name@DataBaseIT {
    @Inject
    @Name@Dao @name@Dao;

    @Nested
    @DisplayName("@Name@Dao")
    class @Name@DaoTest extends AbstractDaoTest<@IdType@, @Name@Table> {

        @BeforeEach
        void setUp() {
            var entry = @Name@Table.builder()
                    .@key@(@KType@.ONE)
                    .enabled(true)
                    .build();
            @IdType@ customId = TestData.uuid.TWO;
            super.setUp(@name@Dao, entry, customId);
        }

        @AfterEach
        void tearDown() {
            super.tearDown();
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
            super.whenFindByIdThenEntry((id, test) -> expected(id, @KType@.ONE, test));

            var update = @Name@Table.builder().id(super.id).@key@(@KType@.TWO).build();
            super.whenUpdateAndFindByIdThenEntry((id, test) -> expected(id, @KType@.TWO, test), update);

            super.whenFindAllThenMultiWithOneItem();
            super.whenFindRangeZeroThenEmptiestMulti();
            super.whenFindRangeFromZeroLimitOneThenMultiWithOneItem();

            var custom = @Name@Table.builder()
                    .id(customId)
                    .@key@(@KType@.@value@Of(7))
                    .@value@(new @VType@("{}"))
                    .build();
            super.whenInsertCustomThenEntry(
                    (id, test) -> expected(id, @KType@.@value@Of(7), new @VType@("{}"), test),
                    custom
            );
            var customUpdate = @Name@Table.builder()
                    .id(customId)
                    .@key@(@KType@.TEN)
                    .@value@(new @VType@("{}"))
                    .build();
            super.whenUpdateCustomAndFindByIdThenEntry(
                    (id, test) -> expected(id, @KType@.TEN, new @VType@("{}"), test),
                    customUpdate
            );

            super.whenFindRangeFromZeroToOneThenMultiWithOneItemCustom(
                    (id, test) -> expected(id, @KType@.TEN, new @VType@("{}"), test)
            );
            super.whenFindRangeFromZeroToMax@Value@ThenMultiWithTwoItems();
            super.whenFindRangeFromOneLimitOneMultiWithOneItem();

            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(@name@Dao.findBy@Key@(@KType@.TWO));
                var expected = expected(super.id, @KType@.TWO, test);
                Assertions.assertEquals(expected, test);
                Assertions.assertNotNull(test.createTime());
                Assertions.assertNotNull(test.updateTime());
            });

            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(@name@Dao.findBy@Value@(new @VType@("{}")));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
                var expected = expected(customId, @KType@.TEN, new @VType@("{}"), test.get(0));
                Assertions.assertEquals(expected, test.get(0));
            });

            super.whenDeleteCustomThenOk();
        }
    }
}
