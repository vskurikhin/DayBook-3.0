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
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.*;
import su.svn.daybook.domain.dao.*;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.messages.ApiResponse;
import su.svn.daybook.domain.messages.Request;
import su.svn.daybook.domain.model.*;
import su.svn.daybook.domain.transact.UserTransactionalJob;
import su.svn.daybook.models.domain.User;
import su.svn.daybook.resources.PostgresDatabaseTestResource;
import su.svn.daybook.services.models.UserService;

import javax.inject.Inject;
import java.math.BigInteger;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

import static su.svn.daybook.TestUtils.*;

@QuarkusTest
@QuarkusTestResource(value = PostgresDatabaseTestResource.class, restrictToAnnotatedClass = true)
public class DataBaseIT {
    @Inject
    CodifierDao codifierDao;
//    @Inject
//    I18nDao i18nDao;
    @Inject
    KeyValueDao keyValueDao;
//    @Inject
//    LanguageDao languageDao;
    @Inject
    RoleDao roleDao;
    @Inject
    SessionDao sessionDao;
//    @Inject
//    SettingDao settingDao;
    @Inject
    TagLabelDao tagLabelDao;
    @Inject
    UserNameDao userNameDao;
    @Inject
    UserService userService;
    @Inject
    UserTransactionalJob userTransactionalJob;
    @Inject
    UserViewDao userViewDao;
//    @Inject
//    ValueTypeDao valueTypeDao;
    @Inject
    VocabularyDao vocabularyDao;
    @Inject
    WordDao wordDao;

    @Nested
    @DisplayName("CodifierDao")
    class CodifierDaoTest extends AbstractDaoTest<String, CodifierTable> {
        String customId = "";
        String str = "str";

        @BeforeEach
        void setUp() {
            var entry = CodifierTable.builder()
                    .code(str)
                    .enabled(true)
                    .build();
            super.setUp(codifierDao, entry, customId);
        }

        @AfterEach
        void tearDown() {
            super.tearDown();
        }

        CodifierTable.Builder builder(String id, CodifierTable test) {
            return CodifierTable.builder()
                    .code(id)
                    .createTime(test.createTime())
                    .updateTime(test.updateTime())
                    .enabled(true);
        }

        CodifierTable expected(String id, CodifierTable test) {
            Assertions.assertNotNull(test);
            return builder(id, test).build();
        }

        CodifierTable expected(String id, String value, CodifierTable test) {
            Assertions.assertNotNull(test);
            return builder(id, test).value(value).build();
        }

        @Test
        void test() {
            super.whenFindByIdThenEntry(this::expected);

            var update = CodifierTable.builder().code(super.id).value("value1").build();
            super.whenUpdateAndFindByIdThenEntry((id, test) -> expected(id, "value1", test), update);

            super.whenFindAllThenMultiWithOneItem();
            super.whenFindRangeZeroThenEmptiestMulti();
            super.whenFindRangeFromZeroLimitOneThenMultiWithOneItem();

            var custom = CodifierTable.builder()
                    .code(customId)
                    .value("value2")
                    .build();
            super.whenInsertCustomThenEntry((id, test) -> expected(id, "value2", test), custom);
            var customUpdate = CodifierTable.builder()
                    .code(super.customId)
                    .value("value3")
                    .build();
            super.whenUpdateCustomAndFindByIdThenEntry(
                    (id, test) -> expected(id, "value3", test),
                    customUpdate
            );

            super.whenFindRangeFromZeroToOneThenMultiWithOneItemCustom(
                    (id, test) -> expected(id, "value3", test)
            );
            super.whenFindRangeFromZeroToMaxValueThenMultiWithTwoItems();
            super.whenFindRangeFromOneLimitOneMultiWithOneItem();

            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(codifierDao.findByKey(super.id));
                var expected = expected(super.id, "value1", test);
                Assertions.assertEquals(expected, test);
                Assertions.assertNotNull(test.createTime());
                Assertions.assertNotNull(test.updateTime());
            });

            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(codifierDao.findByValue("value3"));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
                var expected = expected(customId, "value3", test.get(0));
                Assertions.assertEquals(expected, test.get(0));
            });

            super.whenDeleteCustomThenOk();
        }
    }
//
//    @Nested
//    @DisplayName("I18nDao")
//    class I18nDaoTest {
//        Long id;
//        Long customId = Long.MIN_VALUE;
//        Long languageId = 0L;
//        I18nTable entry;
//        String str = "str";
//
//        @BeforeEach
//        void setUp() {
//            entry = I18nTable.builder()
//                    .languageId(languageId)
//                    .enabled(true)
//                    .build();
//            var language = LanguageTable.builder()
//                    .id(languageId)
//                    .build();
//            Assertions.assertDoesNotThrow(() -> {
//                id = uniOptionalHelper(languageDao.insert(language));
//            });
//            Assertions.assertDoesNotThrow(() -> {
//                id = uniOptionalHelper(i18nDao.insert(entry));
//            });
//        }
//
//        @AfterEach
//        void tearDown() {
//            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id, uniOptionalHelper(i18nDao.delete(id))));
//            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(0, uniOptionalHelper(i18nDao.count())));
//            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(languageId, uniOptionalHelper(languageDao.delete(languageId))));
//        }
//
//        @Test
//        void test() {
//            var expected1 = I18nTable.builder()
//                    .id(id)
//                    .languageId(languageId)
//                    .enabled(true)
//                    .build();
//            Assertions.assertDoesNotThrow(() -> {
//                var test = uniOptionalHelper(i18nDao.findById(id));
//                Assertions.assertNotNull(test);
//                Assertions.assertEquals(expected1, test);
//                Assertions.assertNotNull(test.getCreateTime());
//                Assertions.assertNull(test.getUpdateTime());
//            });
//            Assertions.assertDoesNotThrow(() -> {
//                var test = multiAsListHelper(i18nDao.findRange(0, 0));
//                Assertions.assertNotNull(test);
//                Assertions.assertTrue(test.isEmpty());
//            });
//            Assertions.assertDoesNotThrow(() -> {
//                var test = multiAsListHelper(i18nDao.findRange(0, 1));
//                Assertions.assertNotNull(test);
//                Assertions.assertFalse(test.isEmpty());
//                Assertions.assertEquals(1, test.size());
//            });
//            var expected2 = I18nTable.builder()
//                    .id(id)
//                    .languageId(languageId)
//                    .message(str)
//                    .enabled(true)
//                    .build();
//            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id, uniOptionalHelper(i18nDao.update(expected2))));
//            Assertions.assertDoesNotThrow(() -> {
//                var test = uniOptionalHelper(i18nDao.findById(id));
//                Assertions.assertNotNull(test);
//                Assertions.assertEquals(expected2, test);
//                Assertions.assertNotNull(test.getCreateTime());
//                Assertions.assertNotNull(test.getUpdateTime());
//            });
//            Assertions.assertDoesNotThrow(() -> {
//                var test = multiAsListHelper(i18nDao.findAll());
//                Assertions.assertNotNull(test);
//                Assertions.assertFalse(test.isEmpty());
//                Assertions.assertEquals(1, test.size());
//            });
//            var test = I18nTable.builder()
//                    .id(customId)
//                    .languageId(languageId)
//                    .enabled(true)
//                    .build();
//            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(customId, uniOptionalHelper(i18nDao.insert(test))));
//            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(customId, uniOptionalHelper(i18nDao.delete(customId))));
//
//        }
//    }

    @Nested
    @DisplayName("KeyValueDao")
    class KeyValueDaoTest extends AbstractDaoTest<UUID, KeyValueTable> {

        @BeforeEach
        void setUp() {
            var entry = KeyValueTable.builder()
                    .key(BigInteger.ONE)
                    .enabled(true)
                    .build();
            UUID customId = TestData.uuid.ONE;
            super.setUp(keyValueDao, entry, customId);
        }

        @AfterEach
        void tearDown() {
            super.tearDown();
        }

        KeyValueTable.Builder builder(UUID id, BigInteger key, KeyValueTable test) {
            return KeyValueTable.builder()
                    .id(id)
                    .key(key)
                    .createTime(test.createTime())
                    .updateTime(test.updateTime())
                    .enabled(true);
        }

        KeyValueTable expected(UUID id, BigInteger key, KeyValueTable test) {
            Assertions.assertNotNull(test);
            return builder(id, key, test).build();
        }

        KeyValueTable expected(UUID id, BigInteger key, JsonObject value, KeyValueTable test) {
            Assertions.assertNotNull(test);
            return builder(id, key, test).value(value).build();
        }

        @Test
        void test() {
            super.whenFindByIdThenEntry((id, test) -> expected(id, BigInteger.ONE, test));

            var update = KeyValueTable.builder().id(super.id).key(BigInteger.TWO).build();
            super.whenUpdateAndFindByIdThenEntry((id, test) -> expected(id, BigInteger.TWO, test), update);

            super.whenFindAllThenMultiWithOneItem();
            super.whenFindRangeZeroThenEmptiestMulti();
            super.whenFindRangeFromZeroLimitOneThenMultiWithOneItem();

            var custom = KeyValueTable.builder()
                    .id(customId)
                    .key(BigInteger.valueOf(7))
                    .value(new JsonObject("{}"))
                    .build();
            super.whenInsertCustomThenEntry(
                    (id, test) -> expected(id, BigInteger.valueOf(7), new JsonObject("{}"), test),
                    custom
            );
            var customUpdate = KeyValueTable.builder()
                    .id(customId)
                    .key(BigInteger.TEN)
                    .value(new JsonObject("{}"))
                    .build();
            super.whenUpdateCustomAndFindByIdThenEntry(
                    (id, test) -> expected(id, BigInteger.TEN, new JsonObject("{}"), test),
                    customUpdate
            );

            super.whenFindRangeFromZeroToOneThenMultiWithOneItemCustom(
                    (id, test) -> expected(id, BigInteger.TEN, new JsonObject("{}"), test)
            );
            super.whenFindRangeFromZeroToMaxValueThenMultiWithTwoItems();
            super.whenFindRangeFromOneLimitOneMultiWithOneItem();

            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(keyValueDao.findByKey(BigInteger.TWO));
                var expected = expected(super.id, BigInteger.TWO, test);
                Assertions.assertEquals(expected, test);
                Assertions.assertNotNull(test.createTime());
                Assertions.assertNotNull(test.updateTime());
            });

            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(keyValueDao.findByValue(new JsonObject("{}")));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
                var expected = expected(customId, BigInteger.TEN, new JsonObject("{}"), test.get(0));
                Assertions.assertEquals(expected, test.get(0));
            });

            super.whenDeleteCustomThenOk();
        }
    }

//    @Nested
//    @DisplayName("LanguageDao")
//    class LanguageDaoTest {
//        Long id;
//        Long customId = Long.MIN_VALUE;
//        LanguageTable entry;
//        String str = "str";
//
//        @BeforeEach
//        void setUp() {
//            entry = LanguageTable.builder()
//                    .language(str)
//                    .enabled(true)
//                    .build();
//            Assertions.assertDoesNotThrow(() -> {
//                id = uniOptionalHelper(languageDao.insert(entry));
//            });
//        }
//
//        @AfterEach
//        void tearDown() {
//            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id, uniOptionalHelper(languageDao.delete(id))));
//            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(0, uniOptionalHelper(languageDao.count())));
//        }
//
//        @Test
//        void test() {
//            var expected1 = LanguageTable.builder()
//                    .id(id)
//                    .language(str)
//                    .enabled(true)
//                    .build();
//            Assertions.assertDoesNotThrow(() -> {
//                var test = uniOptionalHelper(languageDao.findById(id));
//                Assertions.assertNotNull(test);
//                Assertions.assertEquals(expected1, test);
//                Assertions.assertNotNull(test.getCreateTime());
//                Assertions.assertNull(test.getUpdateTime());
//            });
//            Assertions.assertDoesNotThrow(() -> {
//                var test = multiAsListHelper(languageDao.findRange(0, 0));
//                Assertions.assertNotNull(test);
//                Assertions.assertTrue(test.isEmpty());
//            });
//            Assertions.assertDoesNotThrow(() -> {
//                var test = multiAsListHelper(languageDao.findRange(0, 1));
//                Assertions.assertNotNull(test);
//                Assertions.assertFalse(test.isEmpty());
//                Assertions.assertEquals(1, test.size());
//            });
//            var expected2 = LanguageTable.builder()
//                    .id(id)
//                    .language("value")
//                    .enabled(true)
//                    .build();
//            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id, uniOptionalHelper(languageDao.update(expected2))));
//            Assertions.assertDoesNotThrow(() -> {
//                var test = uniOptionalHelper(languageDao.findById(id));
//                Assertions.assertNotNull(test);
//                Assertions.assertEquals(expected2, test);
//                Assertions.assertNotNull(test.getCreateTime());
//                Assertions.assertNotNull(test.getUpdateTime());
//            });
//            Assertions.assertDoesNotThrow(() -> {
//                var test = multiAsListHelper(languageDao.findAll());
//                Assertions.assertNotNull(test);
//                Assertions.assertFalse(test.isEmpty());
//                Assertions.assertEquals(1, test.size());
//            });
//            var custom = LanguageTable.builder()
//                    .id(customId)
//                    .language("language")
//                    .enabled(true)
//                    .build();
//            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(customId, uniOptionalHelper(languageDao.insert(custom))));
//            Assertions.assertDoesNotThrow(() -> {
//                var test = multiAsListHelper(languageDao.findRange(0, 1));
//                Assertions.assertNotNull(test);
//                Assertions.assertFalse(test.isEmpty());
//                Assertions.assertEquals(1, test.size());
//                Assertions.assertEquals(custom, test.get(0));
//            });
//            Assertions.assertDoesNotThrow(() -> {
//                var test = multiAsListHelper(languageDao.findRange(0, Long.MAX_VALUE));
//                Assertions.assertNotNull(test);
//                Assertions.assertFalse(test.isEmpty());
//                Assertions.assertEquals(2, test.size());
//            });
//            Assertions.assertDoesNotThrow(() -> {
//                var test = multiAsListHelper(languageDao.findRange(1, 1));
//                Assertions.assertNotNull(test);
//                Assertions.assertFalse(test.isEmpty());
//                Assertions.assertEquals(1, test.size());
//            });
//            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(customId, uniOptionalHelper(languageDao.delete(customId))));
//        }
//    }

    @Nested
    @DisplayName("RoleDao")
    class RoleDaoTest {
        UUID id = new UUID(0, 1);
        UUID customId = UUID.randomUUID();
        RoleTable entry;

        @BeforeEach
        void setUp() {
            entry = RoleTable.builder()
                    .id(id)
                    .role("role")
                    .build();
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id, uniOptionalHelper(roleDao.insert(entry))));
        }

        @AfterEach
        void tearDown() {
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id, uniOptionalHelper(roleDao.delete(id))));
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(0, uniOptionalHelper(roleDao.count())));
        }

        RoleTable expected(UUID id, String role, RoleTable test) {
            Assertions.assertNotNull(test);
            return RoleTable.builder()
                    .id(id)
                    .role(role)
                    .createTime(test.createTime())
                    .updateTime(test.updateTime())
                    .enabled(true)
                    .build();
        }

        @Test
        void test() {
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(roleDao.findById(id));
                var expected = expected(id, "role", test);
                Assertions.assertEquals(expected, test);
                Assertions.assertNotNull(test.createTime());
                Assertions.assertNull(test.updateTime());
            });
            var update = RoleTable.builder().id(id).role("none").build();
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id, uniOptionalHelper(roleDao.update(update))));
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(roleDao.findById(id));
                var expected = expected(id, "none", test);
                Assertions.assertEquals(expected, test);
                Assertions.assertNotNull(test.createTime());
                Assertions.assertNotNull(test.updateTime());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(roleDao.findAll());
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(roleDao.findRange(0, 0));
                Assertions.assertNotNull(test);
                Assertions.assertTrue(test.isEmpty());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(roleDao.findRange(0, 1));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
            });
            var custom = RoleTable.builder()
                    .id(customId)
                    .role("null")
                    .build();
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(customId, uniOptionalHelper(roleDao.insert(custom))));
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(roleDao.findRange(1, 1));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
                var expected = expected(customId, "null", test.get(0));
                Assertions.assertEquals(expected, test.get(0));
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(roleDao.findRange(0, Long.MAX_VALUE));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(2, test.size());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(roleDao.findRange(1, 1));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
            });
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(customId, uniOptionalHelper(roleDao.delete(customId))));
        }
    }

    @Nested
    @DisplayName("SessionDao")
    class SessionDaoTest {
        UUID id = TestData.uuid.ZERO;
        UUID customId = TestData.uuid.ONE;
        UUID userId = TestData.uuid.ZERO;
        SessionTable entry;

        @BeforeEach
        void setUp() {
            var userName = UserNameTable.builder()
                    .id(userId)
                    .userName(SessionTable.NONE)
                    .password("password")
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(userId, uniOptionalHelper(userNameDao.insert(userName))));
            entry = SessionTable.builder()
                    .userName(SessionTable.NONE)
                    .roles(Collections.emptySet())
                    .validTime(TestData.time.EPOCH_TIME)
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(() -> {
                id = uniOptionalHelper(sessionDao.insert(entry));
            });
        }

        @AfterEach
        void tearDown() {
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id, uniOptionalHelper(sessionDao.delete(id))));
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(0, uniOptionalHelper(sessionDao.count())));
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(userId, uniOptionalHelper(userNameDao.delete(userId))));
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(0, uniOptionalHelper(userNameDao.count())));
        }

        SessionTable expected(UUID id, LocalDateTime validTime, SessionTable test) {
            Assertions.assertNotNull(test);
            return SessionTable.builder()
                    .id(id)
                    .userName(SessionTable.NONE)
                    .roles(Collections.emptySet())
                    .validTime(validTime)
                    .createTime(test.createTime())
                    .updateTime(test.updateTime())
                    .enabled(true)
                    .build();
        }

        @Test
        void test() {
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(sessionDao.findById(id));
                var expected = expected(id, TestData.time.EPOCH_TIME, test);
                Assertions.assertEquals(expected, test);
                Assertions.assertNotNull(test.createTime());
                Assertions.assertNull(test.updateTime());
            });
            var update = SessionTable.builder()
                    .id(id)
                    .userName(SessionTable.NONE)
                    .roles(Collections.emptySet())
                    .validTime(TestData.time.NOW)
                    .build();
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id, uniOptionalHelper(sessionDao.update(update))));
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(sessionDao.findById(id));
                var expected = expected(id, TestData.time.NOW, test);
                Assertions.assertEquals(expected, test);
                Assertions.assertNotNull(test.createTime());
                Assertions.assertNotNull(test.updateTime());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(sessionDao.findAll());
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
            });
        }
    }

//    @Nested
//    @DisplayName("SettingDao")
//    class SettingDaoTest {
//        Long id;
//        Long customId = Long.MIN_VALUE;
//        Long valueTypeId = 0L;
//        SettingTable entry;
//        String str = "str";
//
//        @BeforeEach
//        void setUp() {
//            entry = SettingTable.builder()
//                    .key(str)
//                    .valueTypeId(valueTypeId)
//                    .enabled(true)
//                    .build();
//            var valueType = ValueTypeTable.builder()
//                    .id(valueTypeId)
//                    .valueType(str)
//                    .enabled(true)
//                    .build();
//            Assertions.assertDoesNotThrow(() -> {
//                id = uniOptionalHelper(valueTypeDao.insert(valueType));
//            });
//            Assertions.assertDoesNotThrow(() -> {
//                id = uniOptionalHelper(settingDao.insert(entry));
//            });
//        }
//
//        @AfterEach
//        void tearDown() {
//            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id, uniOptionalHelper(settingDao.delete(id))));
//            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(0, uniOptionalHelper(settingDao.count())));
//            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(valueTypeId, uniOptionalHelper(valueTypeDao.delete(valueTypeId))));
//        }
//
//        @Test
//        void test() {
//            var expected1 = SettingTable.builder()
//                    .id(id)
//                    .key(str)
//                    .valueTypeId(valueTypeId)
//                    .enabled(true)
//                    .build();
//            Assertions.assertDoesNotThrow(() -> {
//                var test = uniOptionalHelper(settingDao.findById(id));
//                Assertions.assertNotNull(test);
//                Assertions.assertEquals(expected1, test);
//                Assertions.assertNotNull(test.getCreateTime());
//                Assertions.assertNull(test.getUpdateTime());
//            });
//            var expected2 = SettingTable.builder()
//                    .id(id)
//                    .key(str)
//                    .valueTypeId(valueTypeId)
//                    .value("value")
//                    .enabled(true)
//                    .build();
//            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id, uniOptionalHelper(settingDao.update(expected2))));
//            Assertions.assertDoesNotThrow(() -> {
//                var test = uniOptionalHelper(settingDao.findById(id));
//                Assertions.assertNotNull(test);
//                Assertions.assertEquals(expected2, test);
//                Assertions.assertNotNull(test.getCreateTime());
//                Assertions.assertNotNull(test.getUpdateTime());
//            });
//            Assertions.assertDoesNotThrow(() -> {
//                var test = multiAsListHelper(settingDao.findAll());
//                Assertions.assertNotNull(test);
//                Assertions.assertFalse(test.isEmpty());
//                Assertions.assertEquals(1, test.size());
//            });
//            Assertions.assertDoesNotThrow(() -> {
//                var test = multiAsListHelper(settingDao.findRange(0, 0));
//                Assertions.assertNotNull(test);
//                Assertions.assertTrue(test.isEmpty());
//            });
//            Assertions.assertDoesNotThrow(() -> {
//                var test = multiAsListHelper(settingDao.findRange(0, 1));
//                Assertions.assertNotNull(test);
//                Assertions.assertFalse(test.isEmpty());
//                Assertions.assertEquals(1, test.size());
//            });
//            var custom = SettingTable.builder()
//                    .id(customId)
//                    .key("key")
//                    .valueTypeId(valueTypeId)
//                    .enabled(true)
//                    .build();
//            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(customId, uniOptionalHelper(settingDao.insert(custom))));
//            Assertions.assertDoesNotThrow(() -> {
//                var test = multiAsListHelper(settingDao.findRange(0, 1));
//                Assertions.assertNotNull(test);
//                Assertions.assertFalse(test.isEmpty());
//                Assertions.assertEquals(1, test.size());
//                Assertions.assertEquals(custom, test.get(0));
//            });
//            Assertions.assertDoesNotThrow(() -> {
//                var test = multiAsListHelper(settingDao.findRange(0, Long.MAX_VALUE));
//                Assertions.assertNotNull(test);
//                Assertions.assertFalse(test.isEmpty());
//                Assertions.assertEquals(2, test.size());
//            });
//            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(customId, uniOptionalHelper(settingDao.delete(customId))));
//        }
//    }

    @Nested
    @DisplayName("TagLabelDao")
    class TagLabelDaoTest extends AbstractDaoTest<String, TagLabelTable> {

        String ZERO = TestData.uuid.STRING_ZERO.replace("-", "").substring(16, 32);
        String ONE = TestData.uuid.STRING_ONE.replace("-", "").substring(16, 32);
        String TWO = TestData.uuid.STRING_TWO.replace("-", "").substring(16, 32);
        String TEN = TestData.uuid.STRING_TEN.replace("-", "").substring(16, 32);

        @BeforeEach
        void setUp() {
            var entry = TagLabelTable.builder()
                    .label(ZERO)
                    .enabled(true)
                    .build();
            String customId = ZERO;
            super.setUp(tagLabelDao, entry, customId);
        }

        @AfterEach
        void tearDown() {
            super.tearDown();
        }

        TagLabelTable.Builder builder(String id, String label, TagLabelTable test) {
            return TagLabelTable.builder()
                    .id(id)
                    .label(label)
                    .createTime(test.createTime())
                    .updateTime(test.updateTime())
                    .enabled(true);
        }

        TagLabelTable expected(String id, String label, TagLabelTable test) {
            Assertions.assertNotNull(test);
            return builder(id, label, test).build();
        }

        @Test
        void test() {
            super.whenFindByIdThenEntry((id, test) -> expected(id, ZERO, test));

            var update = TagLabelTable.builder().id(super.id).label(ONE).build();
            super.whenUpdateAndFindByIdThenEntry((id, test) -> expected(id, ONE, test), update);

            super.whenFindAllThenMultiWithOneItem();
            super.whenFindRangeZeroThenEmptiestMulti();
            super.whenFindRangeFromZeroLimitOneThenMultiWithOneItem();

            var custom = TagLabelTable.builder()
                    .id(customId)
                    .label(TWO)
                    .build();
            super.whenInsertCustomThenEntry(
                    (id, test) -> expected(id, TWO, test),
                    custom
            );
            var customUpdate = TagLabelTable.builder()
                    .id(customId)
                    .label(TEN)
                    .build();
            super.whenUpdateCustomAndFindByIdThenEntry(
                    (id, test) -> expected(id, TEN, test),
                    customUpdate
            );

            super.whenFindRangeFromZeroToOneThenMultiWithOneItemCustom(
                    (id, test) -> expected(id, TEN, test)
            );
            super.whenFindRangeFromZeroToMaxValueThenMultiWithTwoItems();
            super.whenFindRangeFromOneLimitOneMultiWithOneItem();

            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(tagLabelDao.findByLabel(ONE));
                var expected = expected(super.id, ONE, test);
                Assertions.assertEquals(expected, test);
                Assertions.assertNotNull(test.createTime());
                Assertions.assertNotNull(test.updateTime());
            });

            super.whenDeleteCustomThenOk();
        }
    }

    @Nested
    @DisplayName("UserNameDao")
    class UserNameDaoTest {

        UUID id = new UUID(0, 1);
        UUID customId = UUID.randomUUID();
        UserNameTable entry;

        @BeforeEach
        void setUp() {
            entry = UserNameTable.builder()
                    .id(id)
                    .userName("user")
                    .password("password")
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id, uniOptionalHelper(userNameDao.insert(entry))));
        }

        @AfterEach
        void tearDown() {
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id, uniOptionalHelper(userNameDao.delete(id))));
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(0, uniOptionalHelper(userNameDao.count())));
        }

        UserNameTable expected(UUID id, String userName, UserNameTable test) {
            Assertions.assertNotNull(test);
            return UserNameTable.builder()
                    .id(id)
                    .userName(userName)
                    .createTime(test.createTime())
                    .updateTime(test.updateTime())
                    .enabled(true)
                    .build();
        }

        @Test
        void test() {
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(userNameDao.findById(id));
                var expected = expected(id, "user", test);
                Assertions.assertEquals(expected, test);
                Assertions.assertNotNull(test.createTime());
                Assertions.assertNull(test.updateTime());
            });
            var update = expected(id, "none", UserNameTable.builder().build());
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(id, uniOptionalHelper(userNameDao.update(update))));
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(userNameDao.findById(id));
                var expected = expected(id, "none", test);
                Assertions.assertEquals(expected, test);
                Assertions.assertNotNull(test.createTime());
                Assertions.assertNotNull(test.updateTime());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(userNameDao.findAll());
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(userNameDao.findRange(0, 0));
                Assertions.assertNotNull(test);
                Assertions.assertTrue(test.isEmpty());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(userNameDao.findRange(0, 1));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
            });
            var custom = expected(customId, "userName", UserNameTable.builder().build());
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(customId, uniOptionalHelper(userNameDao.insert(custom))));
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(userNameDao.findRange(1, 1));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
                var expected = expected(customId, "userName", test.get(0));
                Assertions.assertEquals(expected, test.get(0));
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(userNameDao.findRange(0, Long.MAX_VALUE));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(2, test.size());
            });

            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(userNameDao.findRange(1, 1));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
            });
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(customId, uniOptionalHelper(userNameDao.delete(customId))));
        }
    }

    @Nested
    @DisplayName("UserService")
    class UserServiceTest {

        UUID id = new UUID(0, 0);
        UUID id1 = new UUID(0, 1);
        UUID id2 = new UUID(0, 2);
        UUID customId = UUID.randomUUID();
        RoleTable role1;
        RoleTable role2;
        User user;
        Principal principal;

        @BeforeEach
        void setUp() {
            role1 = RoleTable.builder().id(id1).role("role1").build();
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id1, uniOptionalHelper(roleDao.insert(role1))));
            role2 = RoleTable.builder().id(id2).role("role2").build();
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id2, uniOptionalHelper(roleDao.insert(role2))));
            user = User.builder()
                    .id(id)
                    .userName("user")
                    .password("password")
                    .roles(Collections.emptySet())
                    .build();
            principal = null/*new QuarkusPrincipal(null)*/;
        }

        @AfterEach
        void tearDown() {
            checkUserNameTableIsEmpty();
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id2, uniOptionalHelper(roleDao.delete(id2))));
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id1, uniOptionalHelper(roleDao.delete(id1))));
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(0, uniOptionalHelper(roleDao.count())));
        }

        @Test
        void test() {
            var expected = Answer.builder()
                    .message(Answer.DEFAULT_MESSAGE)
                    .error(201)
                    .payload(new ApiResponse<>(TestData.uuid.ZERO, 201))
                    .build();
            Assertions.assertDoesNotThrow(() -> {
                var actual = uniToAnswerHelper(userService.add(new Request<>(user, principal)));
                Assertions.assertEquals(expected, actual);
            });
            deleteUserName();
            checkUserNameTableIsEmpty();
            for (var a : new String[][]{{"role1"}, {"role1", "role2"}}) {
                var set = Set.of(a);
                user = User.builder()
                        .id(id)
                        .userName("user")
                        .password("password")
                        .roles(set)
                        .build();
                Assertions.assertDoesNotThrow(() -> {
                    var actual = uniToAnswerHelper(userService.add(new Request<>(user, principal)));
                    Assertions.assertEquals(expected, actual);
                    var userView = uniOptionalHelper(userViewDao.findById(id));
                    Assertions.assertNotNull(userView);
                    Assertions.assertEquals(set.size(), userView.roles().size());
                });
                Assertions.assertDoesNotThrow(() -> {
                    var expected200 = Answer.builder()
                            .message(Answer.DEFAULT_MESSAGE)
                            .error(200)
                            .payload(new ApiResponse<>(TestData.uuid.ZERO, 200))
                            .build();
                    var actual = uniToAnswerHelper(userService.delete(new Request<>(user.id(), principal)));
                    Assertions.assertNotNull(actual);
                    Assertions.assertEquals(expected200, actual);
                    if (actual.payload() instanceof ApiResponse apiResponse) {
                        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(
                                id, UUID.fromString(apiResponse.id().toString())
                        ));
                    }
                });
                checkUserNameTableIsEmpty();
            }
            var expected2 = Answer.builder()
                    .message(Answer.DEFAULT_MESSAGE)
                    .error(202)
                    .payload(new ApiResponse<>(TestData.uuid.ZERO, 202))
                    .build();
            for (var a : new String[][]{{"role1"}, {"role1", "role2"}}) {
                Assertions.assertDoesNotThrow(() -> {
                    var actual = uniToAnswerHelper(userService.add(new Request<>(user, principal)));
                    Assertions.assertEquals(expected, actual);
                });
                var set = Set.of(a);
                var first = set.stream().findFirst();
                Assertions.assertFalse(first.isEmpty());
                user = User.builder()
                        .id(id)
                        .userName("user")
                        .password(first.get())
                        .roles(set)
                        .build();
                Assertions.assertDoesNotThrow(() -> {
                    var actual = uniToAnswerHelper(userService.put(new Request<>(user, principal)));
                    Assertions.assertEquals(expected2, actual);
                    var userView = uniOptionalHelper(userViewDao.findById(id));
                    Assertions.assertNotNull(userView);
                    Assertions.assertEquals(set.size(), userView.roles().size());
                });
                // Thread.sleep(25_000);
                Assertions.assertDoesNotThrow(() -> {
                    var actual = uniToAnswerHelper(userService.delete(new Request<>(user.id(), principal)));
                    Assertions.assertNotNull(actual);
                    if (actual.payload() instanceof ApiResponse apiResponse) {
                        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(
                                id, UUID.fromString(apiResponse.id().toString())
                        ));
                    }
                });
                checkUserNameTableIsEmpty();
            }
            var custom = User.builder()
                    .userName("userName")
                    .password("password")
                    .roles(Set.of("role1", "role2"))
                    .build();
            Assertions.assertDoesNotThrow(() -> {
                Answer actual = uniToAnswerHelper(userService.add(new Request<>(custom, principal)));
                if (actual.payload() instanceof ApiResponse apiResponse) {
                    customId = UUID.fromString(apiResponse.id().toString());
                    Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(
                            customId, uniOptionalHelper(userNameDao.delete(customId))
                    ));
                }
            });
        }

        private void checkUserNameTableIsEmpty() {
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(0, uniOptionalHelper(userNameDao.count()))
            );
        }

        private void deleteUserName() {
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(
                    id, uniOptionalHelper(userNameDao.delete(id))
            ));
        }
    }

    @Nested
    @DisplayName("UserTransactionalJob")
    class UserTransactionalJobTest {
        UUID id = new UUID(0, 0);
        UUID id1 = new UUID(0, 1);
        UUID id2 = new UUID(0, 2);
        UUID customId = UUID.randomUUID();
        RoleTable role1;
        RoleTable role2;
        UserNameTable userName;

        @BeforeEach
        void setUp() {
            role1 = RoleTable.builder()
                    .id(id1)
                    .role("role1")
                    .build();
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id1, uniOptionalHelper(roleDao.insert(role1))));
            role2 = RoleTable.builder()
                    .id(id2)
                    .role("role2")
                    .build();
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id2, uniOptionalHelper(roleDao.insert(role2))));
            userName = UserNameTable.builder()
                    .id(id)
                    .userName("user")
                    .password("password")
                    .enabled(true)
                    .build();
        }

        @AfterEach
        void tearDown() {
            checkUserNameTableIsEmpty();
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id2, uniOptionalHelper(roleDao.delete(id2))));
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id1, uniOptionalHelper(roleDao.delete(id1))));
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(0, uniOptionalHelper(roleDao.count())));
        }

        @Test
        void test() {
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(userTransactionalJob.insert(userName, Collections.emptySet(), UserNameTable::userName));
                Assertions.assertNotNull(test);
                Assertions.assertEquals(id, test);
                var userView = uniOptionalHelper(userViewDao.findById(id));
                Assertions.assertNotNull(userView);
                Assertions.assertEquals(0, userView.roles().size());
            });
            deleteUserName();
            checkUserNameTableIsEmpty();
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(userTransactionalJob.insert(userName, Collections.singleton("role1"), UserNameTable::userName));
                Assertions.assertNotNull(test);
                Assertions.assertEquals(id, test);
                var userView = uniOptionalHelper(userViewDao.findById(id));
                Assertions.assertNotNull(userView);
                Assertions.assertEquals(1, userView.roles().size());
            });
            deleteUserName();
            checkUserNameTableIsEmpty();
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(userTransactionalJob.insert(userName, Set.of("role1", "role2"), UserNameTable::userName));
                Assertions.assertNotNull(test);
                Assertions.assertEquals(id, test);
                var userView = uniOptionalHelper(userViewDao.findById(id));
                Assertions.assertNotNull(userView);
                Assertions.assertEquals(2, userView.roles().size());
            });
            deleteUserName();
            checkUserNameTableIsEmpty();
            Assertions.assertThrows(java.util.concurrent.ExecutionException.class,
                    () -> uniOptionalHelper(userTransactionalJob.insert(userName, Set.of("role1", "role2", "role3"), UserNameTable::userName)));
            checkUserNameTableIsEmpty();
            var custom = UserNameTable.builder()
                    .userName("userName")
                    .password("password")
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(() -> {
                customId = uniOptionalHelper(userTransactionalJob.insert(custom, Set.of("role1", "role2"), UserNameTable::userName));
            });
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(customId, uniOptionalHelper(userNameDao.delete(customId))));
        }

        private void checkUserNameTableIsEmpty() {
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(0, uniOptionalHelper(userNameDao.count())));
        }

        private void deleteUserName() {
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(id, uniOptionalHelper(userNameDao.delete(id))));
        }
    }

//    @Nested
//    @DisplayName("ValueTypeDao")
//    class ValueTypeDaoTest {
//        Long id;
//        String str = "str";
//        ValueTypeTable entry;
//
//        @BeforeEach
//        void setUp() {
//            entry = ValueTypeTable.builder()
//                    .id(id)
//                    .valueType(str)
//                    .enabled(true)
//                    .build();
//            Assertions.assertDoesNotThrow(() -> {
//                id = uniOptionalHelper(valueTypeDao.insert(entry));
//            });
//        }
//
//        @AfterEach
//        void tearDown() {
//            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id, uniOptionalHelper(valueTypeDao.delete(id))));
//            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(0, uniOptionalHelper(valueTypeDao.count())));
//        }
//
//        @Test
//        void test() {
//            var expected1 = ValueTypeTable.builder()
//                    .id(id)
//                    .valueType(str)
//                    .enabled(true)
//                    .build();
//            Assertions.assertDoesNotThrow(() -> {
//                var test = uniOptionalHelper(valueTypeDao.findById(id));
//                Assertions.assertNotNull(test);
//                Assertions.assertEquals(expected1, test);
//                Assertions.assertNotNull(test.getCreateTime());
//                Assertions.assertNull(test.getUpdateTime());
//            });
//            var expected2 = ValueTypeTable.builder()
//                    .id(id)
//                    .valueType("value")
//                    .enabled(true)
//                    .build();
//            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id, uniOptionalHelper(valueTypeDao.update(expected2))));
//            Assertions.assertDoesNotThrow(() -> {
//                var test = uniOptionalHelper(valueTypeDao.findById(id));
//                Assertions.assertNotNull(test);
//                Assertions.assertEquals(expected2, test);
//                Assertions.assertNotNull(test.getCreateTime());
//                Assertions.assertNotNull(test.getUpdateTime());
//            });
//            Assertions.assertDoesNotThrow(() -> {
//                var test = multiAsListHelper(valueTypeDao.findAll());
//                Assertions.assertNotNull(test);
//                Assertions.assertFalse(test.isEmpty());
//                Assertions.assertEquals(1, test.size());
//            });
//            Assertions.assertDoesNotThrow(() -> {
//                var test = multiAsListHelper(valueTypeDao.findRange(0, 0));
//                Assertions.assertNotNull(test);
//                Assertions.assertTrue(test.isEmpty());
//            });
//            Assertions.assertDoesNotThrow(() -> {
//                var test = multiAsListHelper(valueTypeDao.findRange(0, 1));
//                Assertions.assertNotNull(test);
//                Assertions.assertFalse(test.isEmpty());
//                Assertions.assertEquals(1, test.size());
//            });
//        }
//    }
//

    @Nested
    @DisplayName("VocabularyDao and WordDao")
    class VocabularyDaoAndWordDaoTest extends AbstractDaoTest<Long, VocabularyTable> {

        String ZERO = String.valueOf(0);
        String ONE = String.valueOf(1);
        String TWO = String.valueOf(2);
        String TEN = String.valueOf(10);

        String word1 = "word1";
        String wordCustom = "custom";

        AbstractDaoTest<String, WordTable> wordDaoTest = new AbstractDaoTest<>();

        @BeforeEach
        void setUp() {
            var word = WordTable.builder()
                    .word(word1)
                    .enabled(true)
                    .build();
            wordDaoTest.setUp(wordDao, word, wordCustom);
            var entry = VocabularyTable.builder()
                    .word(word1)
                    .enabled(true)
                    .build();
            Long customId = 0L;
            super.setUp(vocabularyDao, entry, customId);
        }

        @AfterEach
        void tearDown() {
            super.tearDown();
            wordDaoTest.tearDown();
        }

        VocabularyTable.Builder builder(Long id, String word, VocabularyTable test) {
            return VocabularyTable.builder()
                    .id(id)
                    .word(word)
                    .createTime(test.createTime())
                    .updateTime(test.updateTime())
                    .enabled(true);
        }

        VocabularyTable expected(Long id, String word, VocabularyTable test) {
            Assertions.assertNotNull(test);
            return builder(id, word, test).build();
        }

        VocabularyTable expected(Long id, String word, String value, VocabularyTable test) {
            Assertions.assertNotNull(test);
            return builder(id, word, test).value(value).build();
        }

        @Test
        void test() {
            super.whenFindByIdThenEntry((id, test) -> expected(id, word1, test));

            var update = VocabularyTable.builder()
                    .id(super.id)
                    .word(word1)
                    .value("value1")
                    .build();
            super.whenUpdateAndFindByIdThenEntry((id, test) -> expected(id, word1, "value1", test), update);

            super.whenFindAllThenMultiWithOneItem();
            super.whenFindRangeZeroThenEmptiestMulti();
            super.whenFindRangeFromZeroLimitOneThenMultiWithOneItem();

            var customWord = WordTable.builder()
                    .word(wordCustom)
                    .build();
            wordDaoTest.whenInsertCustomThenEntry(
                    (id, test) -> WordTable.builder()
                            .word(wordCustom)
                            .createTime(test.createTime())
                            .updateTime(test.updateTime())
                            .enabled(true).build(),
                    customWord
            );
            var custom = VocabularyTable.builder()
                    .id(customId)
                    .word(wordCustom)
                    .value("custom1")
                    .build();
            super.whenInsertCustomThenEntry(
                    (id, test) -> expected(id, wordCustom, "custom1", test),
                    custom
            );
            var customUpdate = VocabularyTable.builder()
                    .id(customId)
                    .word(wordCustom)
                    .value("custom2")
                    .build();
            super.whenUpdateCustomAndFindByIdThenEntry(
                    (id, test) -> expected(id, wordCustom, "custom2", test),
                    customUpdate
            );

            super.whenFindRangeFromZeroToOneThenMultiWithOneItemCustom(
                    (id, test) -> expected(id, wordCustom, "custom2", test)
            );
            super.whenFindRangeFromZeroToMaxValueThenMultiWithTwoItems();
            super.whenFindRangeFromOneLimitOneMultiWithOneItem();

            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(vocabularyDao.findByWord(wordCustom));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
                var expected = expected(customId, wordCustom, "custom2", test.get(0));
                Assertions.assertEquals(expected, test.get(0));
            });

            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(vocabularyDao.findByValue("custom2"));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
                var expected = expected(customId, wordCustom, "custom2", test.get(0));
                Assertions.assertEquals(expected, test.get(0));
            });

            super.whenDeleteCustomThenOk();
            wordDaoTest.whenDeleteCustomThenOk();
        }
    }
}
