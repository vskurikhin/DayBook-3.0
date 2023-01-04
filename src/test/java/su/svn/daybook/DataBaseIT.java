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
import su.svn.daybook.domain.dao.*;
import su.svn.daybook.domain.model.*;
import su.svn.daybook.resources.PostgresDatabaseTestResource;

import javax.inject.Inject;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@QuarkusTest
@QuarkusTestResource(value = PostgresDatabaseTestResource.class, restrictToAnnotatedClass = true)
public class DataBaseIT {

    @Inject
    CodifierDao codifierDao;

    @Inject
    UserNameDao userNameDao;

    @Inject
    I18nDao i18nDao;

    @Inject
    KeyValueDao keyValueDao;

    @Inject
    LanguageDao languageDao;

    @Inject
    RoleDao roleDao;

    @Inject
    SettingDao settingDao;

    @Inject
    TagLabelDao tagLabelDao;

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
            Assertions.assertDoesNotThrow(
                    () -> {
                        id = codifierDao.insert(entry)
                                .subscribeAsCompletionStage()
                                .get()
                                .orElse(null);
                        Assertions.assertEquals(str, id);
                    }
            );
        }

        @AfterEach
        void tearDown() {
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            id, codifierDao.delete(id)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            0, codifierDao.count()
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
        }

        @Test
        void test() {
            var expected1 = CodifierTable.builder()
                    .code(id)
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = codifierDao.findById(id)
                                .subscribeAsCompletionStage()
                                .get()
                                .orElse(null);
                        Assertions.assertNotNull(test);
                        Assertions.assertEquals(expected1, test);
                        Assertions.assertNotNull(test.getCreateTime());
                        Assertions.assertNull(test.getUpdateTime());
                    }
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = codifierDao.findByCode(id)
                                .subscribeAsCompletionStage()
                                .get()
                                .orElse(null);
                        Assertions.assertNotNull(test);
                        Assertions.assertEquals(expected1, test);
                        Assertions.assertNotNull(test.getCreateTime());
                        Assertions.assertNull(test.getUpdateTime());
                    }
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = codifierDao.findRange(0, 0)
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertTrue(test.isEmpty());
                    }
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = codifierDao.findRange(0, 1)
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertFalse(test.isEmpty());
                        Assertions.assertEquals(1, test.size());
                    }
            );
            var expected2 = CodifierTable.builder()
                    .code(id)
                    .value("value")
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            id, codifierDao.update(expected2)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = codifierDao.findById(id)
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
                        var test = codifierDao.findAll()
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
            Assertions.assertDoesNotThrow(
                    () -> {
                        id = languageDao.insert(language)
                                .subscribeAsCompletionStage()
                                .get()
                                .orElse(null);
                    }
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        id = i18nDao.insert(entry)
                                .subscribeAsCompletionStage()
                                .get()
                                .orElse(null);
                    }
            );
        }

        @AfterEach
        void tearDown() {
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            id, i18nDao.delete(id)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            0, i18nDao.count()
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            languageId, languageDao.delete(languageId)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
        }

        @Test
        void test() {
            var expected1 = I18nTable.builder()
                    .id(id)
                    .languageId(languageId)
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = i18nDao.findById(id)
                                .subscribeAsCompletionStage()
                                .get()
                                .orElse(null);
                        Assertions.assertNotNull(test);
                        Assertions.assertEquals(expected1, test);
                        Assertions.assertNotNull(test.getCreateTime());
                        Assertions.assertNull(test.getUpdateTime());
                    }
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = i18nDao.findRange(0, 0)
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertTrue(test.isEmpty());
                    }
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = i18nDao.findRange(0, 1)
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertFalse(test.isEmpty());
                        Assertions.assertEquals(1, test.size());
                    }
            );
            var expected2 = I18nTable.builder()
                    .id(id)
                    .languageId(languageId)
                    .message(str)
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            id, i18nDao.update(expected2)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = i18nDao.findById(id)
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
                        var test = i18nDao.findAll()
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertFalse(test.isEmpty());
                        Assertions.assertEquals(1, test.size());
                    }
            );

            var test = I18nTable.builder()
                    .id(customId)
                    .languageId(languageId)
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            customId, i18nDao.insert(test)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null))
            );
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            customId, i18nDao.delete(customId)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );

        }
    }

    @Nested
    @DisplayName("KeyValueDao")
    class KeyValueDaoTest {

        Long id;

        Long customId = Long.MIN_VALUE;

        KeyValueTable entry;

        String str = "str";

        @BeforeEach
        void setUp() {
            entry = KeyValueTable.builder()
                    .key(KeyValueTable.NONE)
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(
                    () -> {
                        id = keyValueDao.insert(entry)
                                .subscribeAsCompletionStage()
                                .get()
                                .orElse(null);
                    }
            );
        }

        @AfterEach
        void tearDown() {
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            id, keyValueDao.delete(id)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            0, keyValueDao.count()
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
        }

        @Test
        void test() {
            var expected1 = KeyValueTable.builder()
                    .id(id)
                    .key(KeyValueTable.NONE)
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = keyValueDao.findById(id)
                                .subscribeAsCompletionStage()
                                .get()
                                .orElse(null);
                        Assertions.assertNotNull(test);
                        Assertions.assertEquals(expected1, test);
                        Assertions.assertNotNull(test.getCreateTime());
                        Assertions.assertNull(test.getUpdateTime());
                    }
            );
            var expected2 = KeyValueTable.builder()
                    .id(id)
                    .key(KeyValueTable.NONE)
                    .value(str)
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            id, keyValueDao.update(expected2)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = keyValueDao.findById(id)
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
                        var test = keyValueDao.findAll()
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertFalse(test.isEmpty());
                        Assertions.assertEquals(1, test.size());
                    }
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = keyValueDao.findRange(0, 0)
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertTrue(test.isEmpty());
                    }
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = keyValueDao.findRange(0, 1)
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertFalse(test.isEmpty());
                        Assertions.assertEquals(1, test.size());
                    }
            );

            var custom = KeyValueTable.builder()
                    .id(customId)
                    .key(UUID.randomUUID().toString())
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            customId, keyValueDao.insert(custom)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null))
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = keyValueDao.findRange(0, 1)
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertFalse(test.isEmpty());
                        Assertions.assertEquals(1, test.size());
                        Assertions.assertEquals(custom, test.get(0));
                    }
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = keyValueDao.findRange(0, Long.MAX_VALUE)
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertFalse(test.isEmpty());
                        Assertions.assertEquals(2, test.size());
                    }
            );

            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = keyValueDao.findRange(1, 1)
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertFalse(test.isEmpty());
                        Assertions.assertEquals(1, test.size());
                    }
            );
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            customId, keyValueDao.delete(customId)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
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
            Assertions.assertDoesNotThrow(
                    () -> {
                        id = languageDao.insert(entry)
                                .subscribeAsCompletionStage()
                                .get()
                                .orElse(null);
                    }
            );
        }

        @AfterEach
        void tearDown() {
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            id, languageDao.delete(id)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            0, languageDao.count()
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
        }

        @Test
        void test() {
            var expected1 = LanguageTable.builder()
                    .id(id)
                    .language(str)
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = languageDao.findById(id)
                                .subscribeAsCompletionStage()
                                .get()
                                .orElse(null);
                        Assertions.assertNotNull(test);
                        Assertions.assertEquals(expected1, test);
                        Assertions.assertNotNull(test.getCreateTime());
                        Assertions.assertNull(test.getUpdateTime());
                    }
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = languageDao.findRange(0, 0)
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertTrue(test.isEmpty());
                    }
            );

            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = languageDao.findRange(0, 1)
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertFalse(test.isEmpty());
                        Assertions.assertEquals(1, test.size());
                    }
            );
            var expected2 = LanguageTable.builder()
                    .id(id)
                    .language("value")
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            id, languageDao.update(expected2)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = languageDao.findById(id)
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
                        var test = languageDao.findAll()
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertFalse(test.isEmpty());
                        Assertions.assertEquals(1, test.size());
                    }
            );

            var custom = LanguageTable.builder()
                    .id(customId)
                    .language("language")
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            customId, languageDao.insert(custom)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null))
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = languageDao.findRange(0, 1)
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertFalse(test.isEmpty());
                        Assertions.assertEquals(1, test.size());
                        Assertions.assertEquals(custom, test.get(0));
                    }
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = languageDao.findRange(0, Long.MAX_VALUE)
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertFalse(test.isEmpty());
                        Assertions.assertEquals(2, test.size());
                    }
            );

            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = languageDao.findRange(1, 1)
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertFalse(test.isEmpty());
                        Assertions.assertEquals(1, test.size());
                    }
            );
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            customId, languageDao.delete(customId)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );

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
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            id, roleDao.insert(entry)
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
                            id, roleDao.delete(id)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            0, roleDao.count()
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
        }

        @Test
        void test() {
            var expected1 = RoleTable.builder()
                    .id(id)
                    .role("role")
                    .build();
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = roleDao.findById(id)
                                .subscribeAsCompletionStage()
                                .get()
                                .orElse(null);
                        Assertions.assertNotNull(test);
                        Assertions.assertEquals(expected1, test);
                        Assertions.assertNotNull(test.getCreateTime());
                        Assertions.assertNull(test.getUpdateTime());
                    }
            );
            var expected2 = RoleTable.builder()
                    .id(id)
                    .role("none")
                    .description("oops")
                    .build();
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            id, roleDao.update(expected2)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = roleDao.findById(id)
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
                        var test = roleDao.findAll()
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertFalse(test.isEmpty());
                        Assertions.assertEquals(1, test.size());
                    }
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = roleDao.findRange(0, 0)
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertTrue(test.isEmpty());
                    }
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = roleDao.findRange(0, 1)
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertFalse(test.isEmpty());
                        Assertions.assertEquals(1, test.size());
                    }
            );
            var custom = RoleTable.builder()
                    .id(customId)
                    .role("null")
                    .build();
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            customId, roleDao.insert(custom)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null))
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = roleDao.findRange(1, 1)
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertFalse(test.isEmpty());
                        Assertions.assertEquals(1, test.size());
                        Assertions.assertEquals(custom, test.get(0));
                    }
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = roleDao.findRange(0, Long.MAX_VALUE)
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertFalse(test.isEmpty());
                        Assertions.assertEquals(2, test.size());
                    }
            );

            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = roleDao.findRange(1, 1)
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertFalse(test.isEmpty());
                        Assertions.assertEquals(1, test.size());
                    }
            );
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            customId, roleDao.delete(customId)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );

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
            Assertions.assertDoesNotThrow(
                    () -> {
                        id = valueTypeDao.insert(valueType)
                                .subscribeAsCompletionStage()
                                .get()
                                .orElse(null);
                    }
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        id = settingDao.insert(entry)
                                .subscribeAsCompletionStage()
                                .get()
                                .orElse(null);
                    }
            );
        }

        @AfterEach
        void tearDown() {
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            id, settingDao.delete(id)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            0, settingDao.count()
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            valueTypeId, valueTypeDao.delete(valueTypeId)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
        }

        @Test
        void test() {
            var expected1 = SettingTable.builder()
                    .id(id)
                    .key(str)
                    .valueTypeId(valueTypeId)
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = settingDao.findById(id)
                                .subscribeAsCompletionStage()
                                .get()
                                .orElse(null);
                        Assertions.assertNotNull(test);
                        Assertions.assertEquals(expected1, test);
                        Assertions.assertNotNull(test.getCreateTime());
                        Assertions.assertNull(test.getUpdateTime());
                    }
            );
            var expected2 = SettingTable.builder()
                    .id(id)
                    .key(str)
                    .valueTypeId(valueTypeId)
                    .value("value")
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            id, settingDao.update(expected2)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = settingDao.findById(id)
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
                        var test = settingDao.findAll()
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertFalse(test.isEmpty());
                        Assertions.assertEquals(1, test.size());
                    }
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = settingDao.findRange(0, 0)
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertTrue(test.isEmpty());
                    }
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = settingDao.findRange(0, 1)
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertFalse(test.isEmpty());
                        Assertions.assertEquals(1, test.size());
                    }
            );

            var custom = SettingTable.builder()
                    .id(customId)
                    .key("key")
                    .valueTypeId(valueTypeId)
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            customId, settingDao.insert(custom)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null))
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = settingDao.findRange(0, 1)
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertFalse(test.isEmpty());
                        Assertions.assertEquals(1, test.size());
                        Assertions.assertEquals(custom, test.get(0));
                    }
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = settingDao.findRange(0, Long.MAX_VALUE)
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertFalse(test.isEmpty());
                        Assertions.assertEquals(2, test.size());
                    }
            );
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            customId, settingDao.delete(customId)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
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
            Assertions.assertDoesNotThrow(
                    () -> {
                        id = tagLabelDao.insert(entry)
                                .subscribeAsCompletionStage()
                                .get()
                                .orElse(null);
                    }
            );
        }

        @AfterEach
        void tearDown() {
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            id, tagLabelDao.delete(id)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            0, tagLabelDao.count()
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
        }

        @Test
        void test() {
            var expected1 = TagLabelTable.builder()
                    .id(id)
                    .label(str)
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = tagLabelDao.findById(id)
                                .subscribeAsCompletionStage()
                                .get()
                                .orElse(null);
                        Assertions.assertNotNull(test);
                        Assertions.assertEquals(expected1, test);
                        Assertions.assertNotNull(test.getCreateTime());
                        Assertions.assertNull(test.getUpdateTime());
                    }
            );
            var expected2 = TagLabelTable.builder()
                    .id(id)
                    .label("value")
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            id, tagLabelDao.update(expected2)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = tagLabelDao.findById(id)
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
                        var test = tagLabelDao.findAll()
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertFalse(test.isEmpty());
                        Assertions.assertEquals(1, test.size());
                    }
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = tagLabelDao.findRange(0, 0)
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertTrue(test.isEmpty());
                    }
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = tagLabelDao.findRange(0, 1)
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertFalse(test.isEmpty());
                        Assertions.assertEquals(1, test.size());
                    }
            );
            var custom = TagLabelTable.builder()
                    .label("label")
                    .enabled(true)
                    .build();
            var strId = new AtomicReference<String>();
            Assertions.assertDoesNotThrow(
                    () -> strId.set(tagLabelDao.insert(custom)
                            .subscribeAsCompletionStage()
                            .get()
                            .orElse(null))
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = tagLabelDao.findRange(0, Long.MAX_VALUE)
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertFalse(test.isEmpty());
                        Assertions.assertEquals(2, test.size());
                    }
            );
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            strId.get(), tagLabelDao.delete(strId.get())
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
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
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            id, userNameDao.insert(entry)
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
                            id, userNameDao.delete(id)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            0, userNameDao.count()
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
        }

        @Test
        void test() {
            var expected1 = UserNameTable.builder()
                    .id(id)
                    .userName("user")
                    .password("password")
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = userNameDao.findById(id)
                                .subscribeAsCompletionStage()
                                .get()
                                .orElse(null);
                        Assertions.assertNotNull(test);
                        Assertions.assertEquals(expected1, test);
                        Assertions.assertNotNull(test.getCreateTime());
                        Assertions.assertNull(test.getUpdateTime());
                    }
            );
            var expected2 = UserNameTable.builder()
                    .id(id)
                    .userName("none")
                    .password("oops")
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            id, userNameDao.update(expected2)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = userNameDao.findById(id)
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
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = userNameDao.findRange(0, 0)
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertTrue(test.isEmpty());
                    }
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = userNameDao.findRange(0, 1)
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertFalse(test.isEmpty());
                        Assertions.assertEquals(1, test.size());
                    }
            );
            var custom = UserNameTable.builder()
                    .id(customId)
                    .userName("userName")
                    .password("password")
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            customId, userNameDao.insert(custom)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null))
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = userNameDao.findRange(1, 1)
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertFalse(test.isEmpty());
                        Assertions.assertEquals(1, test.size());
                        Assertions.assertEquals(custom, test.get(0));
                    }
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = userNameDao.findRange(0, Long.MAX_VALUE)
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertFalse(test.isEmpty());
                        Assertions.assertEquals(2, test.size());
                    }
            );

            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = userNameDao.findRange(1, 1)
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertFalse(test.isEmpty());
                        Assertions.assertEquals(1, test.size());
                    }
            );
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            customId, userNameDao.delete(customId)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );

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
            Assertions.assertDoesNotThrow(
                    () -> {
                        id = valueTypeDao.insert(entry)
                                .subscribeAsCompletionStage()
                                .get()
                                .orElse(null);
                    }
            );
        }

        @AfterEach
        void tearDown() {
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            id, valueTypeDao.delete(id)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            0, valueTypeDao.count()
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
        }

        @Test
        void test() {
            var expected1 = ValueTypeTable.builder()
                    .id(id)
                    .valueType(str)
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = valueTypeDao.findById(id)
                                .subscribeAsCompletionStage()
                                .get()
                                .orElse(null);
                        Assertions.assertNotNull(test);
                        Assertions.assertEquals(expected1, test);
                        Assertions.assertNotNull(test.getCreateTime());
                        Assertions.assertNull(test.getUpdateTime());
                    }
            );
            var expected2 = ValueTypeTable.builder()
                    .id(id)
                    .valueType("value")
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            id, valueTypeDao.update(expected2)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = valueTypeDao.findById(id)
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
                        var test = valueTypeDao.findAll()
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertFalse(test.isEmpty());
                        Assertions.assertEquals(1, test.size());
                    }
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = valueTypeDao.findRange(0, 0)
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertTrue(test.isEmpty());
                    }
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = valueTypeDao.findRange(0, 1)
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
                    () -> Assertions.assertEquals(
                            veryLongWordIdForTest, wordDao.insert(word)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );

            Assertions.assertDoesNotThrow(
                    () -> vocabularyId = vocabularyDao.insert(entry)
                            .subscribeAsCompletionStage()
                            .get()
                            .orElse(null)
            );
        }

        @AfterEach
        void tearDown() {
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            vocabularyId, vocabularyDao.delete(vocabularyId)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            0, vocabularyDao.count()
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            veryLongWordIdForTest, wordDao.delete(veryLongWordIdForTest)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            0, wordDao.count()
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
        }

        @Test
        void testWordDao() {
            var expected1 = WordTable.builder()
                    .word(veryLongWordIdForTest)
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = wordDao.findByWord(veryLongWordIdForTest)
                                .subscribeAsCompletionStage()
                                .get()
                                .orElse(null);
                        Assertions.assertNotNull(test);
                        Assertions.assertEquals(expected1, test);
                        Assertions.assertNotNull(test.getCreateTime());
                        Assertions.assertNull(test.getUpdateTime());
                    }
            );
            var expected2 = WordTable.builder()
                    .word(veryLongWordIdForTest)
                    .enabled(true)
                    .visible(true)
                    .build();
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            veryLongWordIdForTest, wordDao.update(expected2)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = wordDao.findByWord(veryLongWordIdForTest)
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
                        var test = wordDao.findAll()
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertFalse(test.isEmpty());
                        Assertions.assertEquals(1, test.size());
                    }
            );

            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = wordDao.findRange(0, 0)
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertTrue(test.isEmpty());
                    }
            );

            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = wordDao.findRange(0, 1)
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

        @Test
        void testVocabularyDao() {
            var expected1 = VocabularyTable.builder()
                    .id(1L)
                    .word(veryLongWordIdForTest)
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = vocabularyDao.findById(vocabularyId)
                                .subscribeAsCompletionStage()
                                .get()
                                .orElse(null);
                        Assertions.assertNotNull(test);
                        Assertions.assertEquals(expected1, test);
                        Assertions.assertNotNull(test.getCreateTime());
                        Assertions.assertNull(test.getUpdateTime());
                    }
            );
            var expected2 = VocabularyTable.builder()
                    .id(1L)
                    .word(veryLongWordIdForTest)
                    .value("value")
                    .enabled(true)
                    .visible(true)
                    .build();
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            vocabularyId, vocabularyDao.update(expected2)
                                    .subscribeAsCompletionStage()
                                    .get()
                                    .orElse(null)
                    )
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = vocabularyDao.findById(vocabularyId)
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
                        var test = vocabularyDao.findAll()
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertFalse(test.isEmpty());
                        Assertions.assertEquals(1, test.size());
                    }
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = vocabularyDao.findRange(0, 0)
                                .collect()
                                .asList()
                                .subscribeAsCompletionStage()
                                .get();
                        Assertions.assertNotNull(test);
                        Assertions.assertTrue(test.isEmpty());
                    }
            );
            Assertions.assertDoesNotThrow(
                    () -> {
                        var test = vocabularyDao.findRange(0, 1)
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
