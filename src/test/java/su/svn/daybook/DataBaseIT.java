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
import su.svn.daybook.domain.model.*;
import su.svn.daybook.domain.transact.UserTransactionalJob;
import su.svn.daybook.models.domain.User;
import su.svn.daybook.resources.PostgresDatabaseTestResource;
import su.svn.daybook.services.models.UserService;

import javax.inject.Inject;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static su.svn.daybook.TestUtils.*;

@QuarkusTest
@QuarkusTestResource(value = PostgresDatabaseTestResource.class, restrictToAnnotatedClass = true)
public class DataBaseIT {
    @Inject
    CodifierDao codifierDao;
    @Inject
    I18nDao i18nDao;
    @Inject
    KeyValueDao keyValueDao;
    @Inject
    LanguageDao languageDao;
    @Inject
    RoleDao roleDao;
    @Inject
    SessionDao sessionDao;
    @Inject
    SettingDao settingDao;
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
    @Inject
    ValueTypeDao valueTypeDao;
    @Inject
    VocabularyDao vocabularyDao;
    @Inject
    WordDao wordDao;

    @Nested
    @DisplayName("CodifierDao")
    class CodifierDaoTest {
        String id;
        String str = "str";
        CodifierTable entry;

        @BeforeEach
        void setUp() {
            entry = CodifierTable.builder()
                    .code(str)
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(() -> {
                id = uniOptionalHelper(codifierDao.insert(entry));
                Assertions.assertEquals(str, id);
            });
        }

        @AfterEach
        void tearDown() {
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id, uniOptionalHelper(codifierDao.delete(id))));
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(0, uniOptionalHelper(codifierDao.count())));
        }

        @Test
        void test() {
            var expected1 = CodifierTable.builder()
                    .code(id)
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(codifierDao.findById(id));
                Assertions.assertNotNull(test);
                Assertions.assertEquals(expected1, test);
                Assertions.assertNotNull(test.getCreateTime());
                Assertions.assertNull(test.getUpdateTime());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(codifierDao.findByCode(id));
                Assertions.assertNotNull(test);
                Assertions.assertEquals(expected1, test);
                Assertions.assertNotNull(test.getCreateTime());
                Assertions.assertNull(test.getUpdateTime());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(codifierDao.findRange(0, 0));
                Assertions.assertNotNull(test);
                Assertions.assertTrue(test.isEmpty());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(codifierDao.findRange(0, 1));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
            });
            var expected2 = CodifierTable.builder()
                    .code(id)
                    .value("value")
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id, uniOptionalHelper(codifierDao.update(expected2))));
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(codifierDao.findById(id));
                Assertions.assertNotNull(test);
                Assertions.assertEquals(expected2, test);
                Assertions.assertNotNull(test.getCreateTime());
                Assertions.assertNotNull(test.getUpdateTime());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(codifierDao.findAll());
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
            });
        }
    }

    @Nested
    @DisplayName("I18nDao")
    class I18nDaoTest {
        Long id;
        Long customId = Long.MIN_VALUE;
        Long languageId = 0L;
        I18nTable entry;
        String str = "str";

        @BeforeEach
        void setUp() {
            entry = I18nTable.builder()
                    .languageId(languageId)
                    .enabled(true)
                    .build();
            var language = LanguageTable.builder()
                    .id(languageId)
                    .build();
            Assertions.assertDoesNotThrow(() -> {
                id = uniOptionalHelper(languageDao.insert(language));
            });
            Assertions.assertDoesNotThrow(() -> {
                id = uniOptionalHelper(i18nDao.insert(entry));
            });
        }

        @AfterEach
        void tearDown() {
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id, uniOptionalHelper(i18nDao.delete(id))));
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(0, uniOptionalHelper(i18nDao.count())));
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(languageId, uniOptionalHelper(languageDao.delete(languageId))));
        }

        @Test
        void test() {
            var expected1 = I18nTable.builder()
                    .id(id)
                    .languageId(languageId)
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(i18nDao.findById(id));
                Assertions.assertNotNull(test);
                Assertions.assertEquals(expected1, test);
                Assertions.assertNotNull(test.getCreateTime());
                Assertions.assertNull(test.getUpdateTime());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(i18nDao.findRange(0, 0));
                Assertions.assertNotNull(test);
                Assertions.assertTrue(test.isEmpty());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(i18nDao.findRange(0, 1));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
            });
            var expected2 = I18nTable.builder()
                    .id(id)
                    .languageId(languageId)
                    .message(str)
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id, uniOptionalHelper(i18nDao.update(expected2))));
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(i18nDao.findById(id));
                Assertions.assertNotNull(test);
                Assertions.assertEquals(expected2, test);
                Assertions.assertNotNull(test.getCreateTime());
                Assertions.assertNotNull(test.getUpdateTime());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(i18nDao.findAll());
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
            });
            var test = I18nTable.builder()
                    .id(customId)
                    .languageId(languageId)
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(customId, uniOptionalHelper(i18nDao.insert(test))));
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(customId, uniOptionalHelper(i18nDao.delete(customId))));

        }
    }

    @Nested
    @DisplayName("KeyValueDao")
    class KeyValueDaoTest {
        UUID id;
        UUID customId = TestData.uuid.ONE;
        KeyValueTable entry;
        JsonObject str = new JsonObject("{}");

        @BeforeEach
        void setUp() {
            entry = KeyValueTable.builder()
                    .key(BigInteger.ZERO)
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(() -> {
                id = uniOptionalHelper(keyValueDao.insert(entry));
            });
        }

        @AfterEach
        void tearDown() {
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id, uniOptionalHelper(keyValueDao.delete(id))));
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(0, uniOptionalHelper(keyValueDao.count())));
        }

        @Test
        void test() {
            var expected1 = KeyValueTable.builder()
                    .id(id)
                    .key(BigInteger.ZERO)
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(keyValueDao.findById(id));
                Assertions.assertNotNull(test);
                Assertions.assertEquals(expected1, test);
                Assertions.assertNotNull(test.getCreateTime());
                Assertions.assertNull(test.getUpdateTime());
            });
            var expected2 = KeyValueTable.builder()
                    .id(id)
                    .key(BigInteger.ZERO)
                    .value(str)
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id, uniOptionalHelper(keyValueDao.update(expected2))));
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(keyValueDao.findById(id));
                Assertions.assertNotNull(test);
                Assertions.assertEquals(expected2, test);
                Assertions.assertNotNull(test.getCreateTime());
                Assertions.assertNotNull(test.getUpdateTime());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper((keyValueDao.findAll()));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(keyValueDao.findRange(0, 0));
                Assertions.assertNotNull(test);
                Assertions.assertTrue(test.isEmpty());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(keyValueDao.findRange(0, 1));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
            });
            var custom = KeyValueTable.builder()
                    .id(customId)
                    .key(BigInteger.TEN)
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(customId, uniOptionalHelper(keyValueDao.insert(custom))));
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(keyValueDao.findRange(0, 1));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
                Assertions.assertEquals(custom, test.get(0));
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(keyValueDao.findRange(0, Long.MAX_VALUE));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(2, test.size());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(keyValueDao.findRange(1, 1));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
            });
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(customId, uniOptionalHelper(keyValueDao.delete(customId)))
            );
        }
    }

    @Nested
    @DisplayName("LanguageDao")
    class LanguageDaoTest {
        Long id;
        Long customId = Long.MIN_VALUE;
        LanguageTable entry;
        String str = "str";

        @BeforeEach
        void setUp() {
            entry = LanguageTable.builder()
                    .language(str)
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(() -> {
                id = uniOptionalHelper(languageDao.insert(entry));
            });
        }

        @AfterEach
        void tearDown() {
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id, uniOptionalHelper(languageDao.delete(id))));
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(0, uniOptionalHelper(languageDao.count())));
        }

        @Test
        void test() {
            var expected1 = LanguageTable.builder()
                    .id(id)
                    .language(str)
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(languageDao.findById(id));
                Assertions.assertNotNull(test);
                Assertions.assertEquals(expected1, test);
                Assertions.assertNotNull(test.getCreateTime());
                Assertions.assertNull(test.getUpdateTime());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(languageDao.findRange(0, 0));
                Assertions.assertNotNull(test);
                Assertions.assertTrue(test.isEmpty());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(languageDao.findRange(0, 1));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
            });
            var expected2 = LanguageTable.builder()
                    .id(id)
                    .language("value")
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id, uniOptionalHelper(languageDao.update(expected2))));
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(languageDao.findById(id));
                Assertions.assertNotNull(test);
                Assertions.assertEquals(expected2, test);
                Assertions.assertNotNull(test.getCreateTime());
                Assertions.assertNotNull(test.getUpdateTime());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(languageDao.findAll());
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
            });
            var custom = LanguageTable.builder()
                    .id(customId)
                    .language("language")
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(customId, uniOptionalHelper(languageDao.insert(custom))));
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(languageDao.findRange(0, 1));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
                Assertions.assertEquals(custom, test.get(0));
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(languageDao.findRange(0, Long.MAX_VALUE));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(2, test.size());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(languageDao.findRange(1, 1));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
            });
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(customId, uniOptionalHelper(languageDao.delete(customId))));
        }
    }

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

        @Test
        void test() {
            var expected1 = RoleTable.builder()
                    .id(id)
                    .role("role")
                    .build();
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(roleDao.findById(id));
                Assertions.assertNotNull(test);
                Assertions.assertEquals(expected1, test);
                Assertions.assertNotNull(test.getCreateTime());
                Assertions.assertNull(test.getUpdateTime());
            });
            var expected2 = RoleTable.builder()
                    .id(id)
                    .role("none")
                    .description("oops")
                    .build();
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id, uniOptionalHelper(roleDao.update(expected2))));
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(roleDao.findById(id));
                Assertions.assertNotNull(test);
                Assertions.assertEquals(expected2, test);
                Assertions.assertNotNull(test.getCreateTime());
                Assertions.assertNotNull(test.getUpdateTime());
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
                Assertions.assertEquals(custom, test.get(0));
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
        JsonObject str = new JsonObject("{}");

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

        @Test
        void test() {
            var expected1 = SessionTable.builder()
                    .id(id)
                    .userName(SessionTable.NONE)
                    .roles(Collections.emptySet())
                    .validTime(TestData.time.EPOCH_TIME)
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(sessionDao.findById(id));
                Assertions.assertNotNull(test);
                Assertions.assertEquals(expected1, test);
                Assertions.assertNotNull(test.getCreateTime());
                Assertions.assertNull(test.getUpdateTime());
            });
            var expected2 = SessionTable.builder()
                    .id(id)
                    .userName(SessionTable.NONE)
                    .roles(Collections.emptySet())
                    .validTime(TestData.time.NOW)
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id, uniOptionalHelper(sessionDao.update(expected2))));
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(sessionDao.findById(id));
                Assertions.assertNotNull(test);
                Assertions.assertEquals(expected2, test);
                Assertions.assertNotNull(test.getCreateTime());
                Assertions.assertNotNull(test.getUpdateTime());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(sessionDao.findAll());
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
            });
        }
    }

    @Nested
    @DisplayName("SettingDao")
    class SettingDaoTest {
        Long id;
        Long customId = Long.MIN_VALUE;
        Long valueTypeId = 0L;
        SettingTable entry;
        String str = "str";

        @BeforeEach
        void setUp() {
            entry = SettingTable.builder()
                    .key(str)
                    .valueTypeId(valueTypeId)
                    .enabled(true)
                    .build();
            var valueType = ValueTypeTable.builder()
                    .id(valueTypeId)
                    .valueType(str)
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(() -> {
                id = uniOptionalHelper(valueTypeDao.insert(valueType));
            });
            Assertions.assertDoesNotThrow(() -> {
                id = uniOptionalHelper(settingDao.insert(entry));
            });
        }

        @AfterEach
        void tearDown() {
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id, uniOptionalHelper(settingDao.delete(id))));
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(0, uniOptionalHelper(settingDao.count())));
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(valueTypeId, uniOptionalHelper(valueTypeDao.delete(valueTypeId))));
        }

        @Test
        void test() {
            var expected1 = SettingTable.builder()
                    .id(id)
                    .key(str)
                    .valueTypeId(valueTypeId)
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(settingDao.findById(id));
                Assertions.assertNotNull(test);
                Assertions.assertEquals(expected1, test);
                Assertions.assertNotNull(test.getCreateTime());
                Assertions.assertNull(test.getUpdateTime());
            });
            var expected2 = SettingTable.builder()
                    .id(id)
                    .key(str)
                    .valueTypeId(valueTypeId)
                    .value("value")
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id, uniOptionalHelper(settingDao.update(expected2))));
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(settingDao.findById(id));
                Assertions.assertNotNull(test);
                Assertions.assertEquals(expected2, test);
                Assertions.assertNotNull(test.getCreateTime());
                Assertions.assertNotNull(test.getUpdateTime());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(settingDao.findAll());
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(settingDao.findRange(0, 0));
                Assertions.assertNotNull(test);
                Assertions.assertTrue(test.isEmpty());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(settingDao.findRange(0, 1));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
            });
            var custom = SettingTable.builder()
                    .id(customId)
                    .key("key")
                    .valueTypeId(valueTypeId)
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(customId, uniOptionalHelper(settingDao.insert(custom))));
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(settingDao.findRange(0, 1));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
                Assertions.assertEquals(custom, test.get(0));
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(settingDao.findRange(0, Long.MAX_VALUE));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(2, test.size());
            });
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(customId, uniOptionalHelper(settingDao.delete(customId))));
        }
    }

    @Nested
    @DisplayName("TagLabelDao")
    class TagLabelDaoTest {
        String id;
        String str = "str";
        TagLabelTable entry;

        @BeforeEach
        void setUp() {
            entry = TagLabelTable.builder()
                    .id(str)
                    .label(str)
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(() -> {
                id = uniOptionalHelper(tagLabelDao.insert(entry));
            });
        }

        @AfterEach
        void tearDown() {
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id, uniOptionalHelper(tagLabelDao.delete(id))));
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(0, uniOptionalHelper(tagLabelDao.count())));
        }

        @Test
        void test() {
            var expected1 = TagLabelTable.builder()
                    .id(id)
                    .label(str)
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(tagLabelDao.findById(id));
                Assertions.assertNotNull(test);
                Assertions.assertEquals(expected1, test);
                Assertions.assertNotNull(test.getCreateTime());
                Assertions.assertNull(test.getUpdateTime());
            });
            var expected2 = TagLabelTable.builder()
                    .id(id)
                    .label("value")
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id, uniOptionalHelper(tagLabelDao.update(expected2))));
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(tagLabelDao.findById(id));
                Assertions.assertNotNull(test);
                Assertions.assertEquals(expected2, test);
                Assertions.assertNotNull(test.getCreateTime());
                Assertions.assertNotNull(test.getUpdateTime());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(tagLabelDao.findAll());
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(tagLabelDao.findRange(0, 0));
                Assertions.assertNotNull(test);
                Assertions.assertTrue(test.isEmpty());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(tagLabelDao.findRange(0, 1));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
            });
            var custom = TagLabelTable.builder()
                    .label("label")
                    .enabled(true)
                    .build();
            var strId = new AtomicReference<String>();
            Assertions.assertDoesNotThrow(() -> strId.set(uniOptionalHelper(tagLabelDao.insert(custom))));
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(tagLabelDao.findRange(0, Long.MAX_VALUE));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(2, test.size());
            });
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(strId.get(), uniOptionalHelper(tagLabelDao.delete(strId.get()))));
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

        @Test
        void test() {
            var expected1 = UserNameTable.builder()
                    .id(id)
                    .userName("user")
                    .password("password")
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(userNameDao.findById(id));
                Assertions.assertNotNull(test);
                Assertions.assertEquals(expected1, test);
                Assertions.assertNotNull(test.getCreateTime());
                Assertions.assertNull(test.getUpdateTime());
            });
            var expected2 = UserNameTable.builder()
                    .id(id)
                    .userName("none")
                    .password("oops")
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(id, uniOptionalHelper(userNameDao.update(expected2))));
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(userNameDao.findById(id));
                Assertions.assertNotNull(test);
                Assertions.assertEquals(expected2, test);
                Assertions.assertNotNull(test.getCreateTime());
                Assertions.assertNotNull(test.getUpdateTime());
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
            var custom = UserNameTable.builder()
                    .id(customId)
                    .userName("userName")
                    .password("password")
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(customId, uniOptionalHelper(userNameDao.insert(custom))));
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(userNameDao.findRange(1, 1));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
                Assertions.assertEquals(custom, test.get(0));
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
                var actual = uniToAnswerHelper(userService.add(user));
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
                    var actual = uniToAnswerHelper(userService.add(user));
                    Assertions.assertEquals(expected, actual);
                    var userView = uniOptionalHelper(userViewDao.findById(id));
                    Assertions.assertNotNull(userView);
                    Assertions.assertEquals(set.size(), userView.getRoles().size());
                });
                Assertions.assertDoesNotThrow(() -> {
                    var expected200 = Answer.builder()
                            .message(Answer.DEFAULT_MESSAGE)
                            .error(200)
                            .payload(new ApiResponse<>(TestData.uuid.ZERO, 200))
                            .build();
                    var actual = uniToAnswerHelper(userService.delete(user.getId()));
                    Assertions.assertNotNull(actual);
                    Assertions.assertEquals(expected200, actual);
                    if (actual.getPayload() instanceof ApiResponse apiResponse) {
                        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(
                                id, UUID.fromString(apiResponse.getId().toString())
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
                    var actual = uniToAnswerHelper(userService.add(user));
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
                    var actual = uniToAnswerHelper(userService.put(user));
                    Assertions.assertEquals(expected2, actual);
                    var userView = uniOptionalHelper(userViewDao.findById(id));
                    Assertions.assertNotNull(userView);
                    Assertions.assertEquals(set.size(), userView.getRoles().size());
                });
                // Thread.sleep(25_000);
                Assertions.assertDoesNotThrow(() -> {
                    var actual = uniToAnswerHelper(userService.delete(user.getId()));
                    Assertions.assertNotNull(actual);
                    if (actual.getPayload() instanceof ApiResponse apiResponse) {
                        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(
                                id, UUID.fromString(apiResponse.getId().toString())
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
                Answer actual = uniToAnswerHelper(userService.add(custom));
                if (actual.getPayload() instanceof ApiResponse apiResponse) {
                    customId = UUID.fromString(apiResponse.getId().toString());
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
                var test = uniOptionalHelper(userTransactionalJob.insert(userName, Collections.emptySet(), UserNameTable::getUserName));
                Assertions.assertNotNull(test);
                Assertions.assertEquals(id, test);
                var userView = uniOptionalHelper(userViewDao.findById(id));
                Assertions.assertNotNull(userView);
                Assertions.assertEquals(0, userView.getRoles().size());
            });
            deleteUserName();
            checkUserNameTableIsEmpty();
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(userTransactionalJob.insert(userName, Collections.singleton("role1"), UserNameTable::getUserName));
                Assertions.assertNotNull(test);
                Assertions.assertEquals(id, test);
                var userView = uniOptionalHelper(userViewDao.findById(id));
                Assertions.assertNotNull(userView);
                Assertions.assertEquals(1, userView.getRoles().size());
            });
            deleteUserName();
            checkUserNameTableIsEmpty();
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(userTransactionalJob.insert(userName, Set.of("role1", "role2"), UserNameTable::getUserName));
                Assertions.assertNotNull(test);
                Assertions.assertEquals(id, test);
                var userView = uniOptionalHelper(userViewDao.findById(id));
                Assertions.assertNotNull(userView);
                Assertions.assertEquals(2, userView.getRoles().size());
            });
            deleteUserName();
            checkUserNameTableIsEmpty();
            Assertions.assertThrows(java.util.concurrent.ExecutionException.class,
                    () -> uniOptionalHelper(userTransactionalJob.insert(userName, Set.of("role1", "role2", "role3"), UserNameTable::getUserName)));
            checkUserNameTableIsEmpty();
            var custom = UserNameTable.builder()
                    .userName("userName")
                    .password("password")
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(() -> {
                customId = uniOptionalHelper(userTransactionalJob.insert(custom, Set.of("role1", "role2"), UserNameTable::getUserName));
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

    @Nested
    @DisplayName("ValueTypeDao")
    class ValueTypeDaoTest {
        Long id;
        String str = "str";
        ValueTypeTable entry;

        @BeforeEach
        void setUp() {
            entry = ValueTypeTable.builder()
                    .id(id)
                    .valueType(str)
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(() -> {
                id = uniOptionalHelper(valueTypeDao.insert(entry));
            });
        }

        @AfterEach
        void tearDown() {
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id, uniOptionalHelper(valueTypeDao.delete(id))));
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(0, uniOptionalHelper(valueTypeDao.count())));
        }

        @Test
        void test() {
            var expected1 = ValueTypeTable.builder()
                    .id(id)
                    .valueType(str)
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(valueTypeDao.findById(id));
                Assertions.assertNotNull(test);
                Assertions.assertEquals(expected1, test);
                Assertions.assertNotNull(test.getCreateTime());
                Assertions.assertNull(test.getUpdateTime());
            });
            var expected2 = ValueTypeTable.builder()
                    .id(id)
                    .valueType("value")
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id, uniOptionalHelper(valueTypeDao.update(expected2))));
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(valueTypeDao.findById(id));
                Assertions.assertNotNull(test);
                Assertions.assertEquals(expected2, test);
                Assertions.assertNotNull(test.getCreateTime());
                Assertions.assertNotNull(test.getUpdateTime());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(valueTypeDao.findAll());
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(valueTypeDao.findRange(0, 0));
                Assertions.assertNotNull(test);
                Assertions.assertTrue(test.isEmpty());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(valueTypeDao.findRange(0, 1));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
            });
        }
    }

    @Nested
    @DisplayName("VocabularyDao and WordDao")
    class VocabularyDaoAndWordDaoTest {
        String veryLongWordIdForTest = "veryLongWordIdForTest";
        Long vocabularyId;
        VocabularyTable entry;
        WordTable word;

        @BeforeEach
        void setUp() {
            entry = VocabularyTable.builder()
                    .word(veryLongWordIdForTest)
                    .enabled(true)
                    .build();
            word = WordTable.builder()
                    .word(veryLongWordIdForTest)
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(veryLongWordIdForTest, uniOptionalHelper(wordDao.insert(word))));
            Assertions.assertDoesNotThrow(
                    () -> vocabularyId = uniOptionalHelper(vocabularyDao.insert(entry)));
        }

        @AfterEach
        void tearDown() {
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(vocabularyId, uniOptionalHelper(vocabularyDao.delete(vocabularyId))));
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(0, uniOptionalHelper(vocabularyDao.count())));
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(veryLongWordIdForTest, uniOptionalHelper(wordDao.delete(veryLongWordIdForTest))));
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(0, uniOptionalHelper(wordDao.count())));
        }

        @Test
        void testWordDao() {
            var expected1 = WordTable.builder()
                    .word(veryLongWordIdForTest)
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(wordDao.findByWord(veryLongWordIdForTest));
                Assertions.assertNotNull(test);
                Assertions.assertEquals(expected1, test);
                Assertions.assertNotNull(test.getCreateTime());
                Assertions.assertNull(test.getUpdateTime());
            });
            var expected2 = WordTable.builder()
                    .word(veryLongWordIdForTest)
                    .enabled(true)
                    .visible(true)
                    .build();
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(veryLongWordIdForTest, uniOptionalHelper(wordDao.update(expected2))));
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(wordDao.findByWord(veryLongWordIdForTest));
                Assertions.assertNotNull(test);
                Assertions.assertEquals(expected2, test);
                Assertions.assertNotNull(test.getCreateTime());
                Assertions.assertNotNull(test.getUpdateTime());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(wordDao.findAll());
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(wordDao.findRange(0, 0));
                Assertions.assertNotNull(test);
                Assertions.assertTrue(test.isEmpty());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(wordDao.findRange(0, 1));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
            });
        }

        @Test
        void testVocabularyDao() {
            var expected1 = VocabularyTable.builder()
                    .id(1L)
                    .word(veryLongWordIdForTest)
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(vocabularyDao.findById(vocabularyId));
                Assertions.assertNotNull(test);
                Assertions.assertEquals(expected1, test);
                Assertions.assertNotNull(test.getCreateTime());
                Assertions.assertNull(test.getUpdateTime());
            });
            var expected2 = VocabularyTable.builder()
                    .id(1L)
                    .word(veryLongWordIdForTest)
                    .value("value")
                    .enabled(true)
                    .visible(true)
                    .build();
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(vocabularyId, uniOptionalHelper(vocabularyDao.update(expected2))));
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(vocabularyDao.findById(vocabularyId));
                Assertions.assertNotNull(test);
                Assertions.assertEquals(expected2, test);
                Assertions.assertNotNull(test.getCreateTime());
                Assertions.assertNotNull(test.getUpdateTime());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(vocabularyDao.findAll());
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(vocabularyDao.findRange(0, 0));
                Assertions.assertNotNull(test);
                Assertions.assertTrue(test.isEmpty());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(vocabularyDao.findRange(0, 1));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
            });
        }
    }
}
