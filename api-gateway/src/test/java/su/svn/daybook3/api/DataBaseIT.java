/*
 * This file was last modified at 2024-05-14 23:10 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * DataBaseIT.java
 * $Id$
 */

package su.svn.daybook3.api;

import io.quarkus.test.common.WithTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import su.svn.daybook3.api.domain.dao.CodifierDao;
import su.svn.daybook3.api.domain.dao.I18nDao;
import su.svn.daybook3.api.domain.dao.I18nViewDao;
import su.svn.daybook3.api.domain.dao.KeyValueDao;
import su.svn.daybook3.api.domain.dao.LanguageDao;
import su.svn.daybook3.api.domain.dao.SettingDao;
import su.svn.daybook3.api.domain.dao.SettingViewDao;
import su.svn.daybook3.api.domain.dao.StanzaDao;
import su.svn.daybook3.api.domain.dao.StanzaViewDao;
import su.svn.daybook3.api.domain.dao.TagLabelDao;
import su.svn.daybook3.api.domain.dao.ValueTypeDao;
import su.svn.daybook3.api.domain.dao.VocabularyDao;
import su.svn.daybook3.api.domain.dao.WordDao;
import su.svn.daybook3.api.domain.model.CodifierTable;
import su.svn.daybook3.api.domain.model.I18nTable;
import su.svn.daybook3.api.domain.model.I18nView;
import su.svn.daybook3.api.domain.model.KeyValueTable;
import su.svn.daybook3.api.domain.model.LanguageTable;
import su.svn.daybook3.api.domain.model.SettingTable;
import su.svn.daybook3.api.domain.model.SettingView;
import su.svn.daybook3.api.domain.model.StanzaTable;
import su.svn.daybook3.api.domain.model.StanzaView;
import su.svn.daybook3.api.domain.model.TagLabelTable;
import su.svn.daybook3.api.domain.model.ValueTypeTable;
import su.svn.daybook3.api.domain.model.VocabularyTable;
import su.svn.daybook3.api.domain.model.WordTable;
import su.svn.daybook3.api.domain.transact.I18nTransactionalJob;
import su.svn.daybook3.api.domain.transact.SettingTransactionalJob;
import su.svn.daybook3.api.domain.transact.StanzaTransactionalJob;
import su.svn.daybook3.api.resources.PostgresDatabaseTestResource;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static su.svn.daybook3.api.TestUtils.uniListHelper;
import static su.svn.daybook3.api.TestUtils.uniOptionalHelper;

@SuppressWarnings({"SameParameterValue"})
@QuarkusTest
@WithTestResource(value = PostgresDatabaseTestResource.class)
public class DataBaseIT {
    @Inject
    CodifierDao codifierDao;
    @Inject
    I18nDao i18nDao;
    @Inject
    I18nTransactionalJob i18nTransactionalJob;
    @Inject
    I18nViewDao i18nViewDao;
    @Inject
    KeyValueDao keyValueDao;
    @Inject
    LanguageDao languageDao;
    @Inject
    SettingDao settingDao;
    @Inject
    SettingTransactionalJob settingTransactionalJob;
    @Inject
    SettingViewDao settingViewDao;
    @Inject
    StanzaDao stanzaDao;
    @Inject
    StanzaViewDao stanzaViewDao;
    @Inject
    StanzaTransactionalJob stanzaTransactionalJob;
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
    class CodifierDaoTest extends AbstractDaoTest<String, CodifierTable> {
        String customId = "";
        String str = "str";

        @BeforeEach
        void setUp() {
            var entry = CodifierTable.builder()
                    .code(str)
                    .userName("root")
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
            return builder(id, test).userName("root").build();
        }

        CodifierTable expected(String id, String value, CodifierTable test) {
            Assertions.assertNotNull(test);
            return builder(id, test).value(value).userName("root").build();
        }

        @Test
        void test() {
            super.whenFindByIdThenEntry(this::expected);

            var update = CodifierTable.builder().code(super.id).value("value1").userName("root").build();
            super.whenUpdateAndFindByIdThenEntry((id, test) -> expected(id, "value1", test), update);

            super.whenFindAllThenMultiWithOneItem();
            super.whenFindRangeZeroThenEmptiestMulti();
            super.whenFindRangeFromZeroLimitOneThenMultiWithOneItem();

            var custom = CodifierTable.builder()
                    .code(customId)
                    .value("value2")
                    .userName("root")
                    .build();
            super.whenInsertCustomThenEntry((id, test) -> expected(id, "value2", test), custom);
            var customUpdate = CodifierTable.builder()
                    .code(super.customId)
                    .value("value3")
                    .userName("root")
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

    @Nested
    @DisplayName("I18nDao")
    class I18nDaoTest extends AbstractDaoTest<Long, I18nTable> {

        AbstractDaoTest<Long, LanguageTable> languageDaoTest = new AbstractDaoTest<>();

        @BeforeEach
        void setUp() {
            var language = LanguageTable.builder()
                    .language(LanguageTable.NONE)
                    .userName("root")
                    .enabled(true)
                    .build();
            languageDaoTest.setUp(languageDao, language, 0L);
            var entry = I18nTable.builder()
                    .languageId(languageDaoTest.id)
                    .message(I18nTable.NONE)
                    .userName("root")
                    .enabled(true)
                    .build();
            Long customId = 0L;
            super.setUp(i18nDao, entry, customId);
        }

        @AfterEach
        void tearDown() {
            super.tearDown();
            languageDaoTest.tearDown();
        }

        I18nTable.Builder builder(Long id, Long languageId, I18nTable test) {
            return I18nTable.builder()
                    .id(id)
                    .languageId(languageId)
                    .message(I18nTable.NONE)
                    .createTime(test.createTime())
                    .updateTime(test.updateTime())
                    .enabled(true);
        }

        I18nTable expected(Long id, Long languageId, I18nTable test) {
            Assertions.assertNotNull(test);
            return builder(id, languageId, test).userName("root").build();
        }

        I18nTable expected(Long id, Long languageId, String message, I18nTable test) {
            Assertions.assertNotNull(test);
            return builder(id, languageId, test).message(message).userName("root").build();
        }

        @Test
        void test() {
            super.whenFindByIdThenEntry((id, test) -> expected(id, languageDaoTest.id, test));

            var update = I18nTable.builder()
                    .id(super.id)
                    .languageId(languageDaoTest.id)
                    .message(I18nTable.NONE)
                    .userName("root")
                    .build();
            super.whenUpdateAndFindByIdThenEntry((id, test) -> expected(id, languageDaoTest.id, test), update);

            super.whenFindAllThenMultiWithOneItem();
            super.whenFindRangeZeroThenEmptiestMulti();
            super.whenFindRangeFromZeroLimitOneThenMultiWithOneItem();

            var customMessage = UUID.randomUUID().toString();
            var custom = I18nTable.builder()
                    .id(customId)
                    .languageId(languageDaoTest.id)
                    .message(customMessage)
                    .userName("root")
                    .build();
            super.whenInsertCustomThenEntry(
                    (id, test) -> expected(id, languageDaoTest.id, customMessage, test),
                    custom
            );
            var customMessageUpdate = UUID.randomUUID().toString();
            var customUpdate = I18nTable.builder()
                    .id(customId)
                    .languageId(languageDaoTest.id)
                    .message(customMessageUpdate)
                    .userName("root")
                    .build();
            super.whenUpdateCustomAndFindByIdThenEntry(
                    (id, test) -> expected(id, languageDaoTest.id, customMessageUpdate, test),
                    customUpdate
            );

            super.whenFindRangeFromZeroToOneThenMultiWithOneItemCustom(
                    (id, test) -> expected(id, languageDaoTest.id, customMessageUpdate, test)
            );
            super.whenFindRangeFromZeroToMaxValueThenMultiWithTwoItems();
            super.whenFindRangeFromOneLimitOneMultiWithOneItem();

            Assertions.assertDoesNotThrow(() -> whenFindByIdThenOk(customUpdate));
            Assertions.assertDoesNotThrow(() -> whenFindByKeyThenOk(customUpdate));
            Assertions.assertDoesNotThrow(() -> whenFindByLanguageIdThenOk(customUpdate));
            Assertions.assertDoesNotThrow(() -> whenFindByMessageThenOk(customUpdate));

            super.whenDeleteCustomThenOk();
        }

        private void whenFindByIdThenOk(I18nTable entry) throws Exception {
            var test = uniOptionalHelper(i18nDao.findById(entry.id()));
            var expected = expected(test.id(), languageDaoTest.id, entry.message(), test);
            Assertions.assertEquals(expected, test);
            Assertions.assertNotNull(test.createTime());
            Assertions.assertNotNull(test.updateTime());
        }

        private void whenFindByKeyThenOk(I18nTable entry) throws Exception {
            var test = uniOptionalHelper(i18nDao.findByKey(entry.languageId(), entry.message()));
            var expected = expected(test.id(), languageDaoTest.id, entry.message(), test);
            Assertions.assertEquals(expected, test);
            Assertions.assertNotNull(test.createTime());
            Assertions.assertNotNull(test.updateTime());
        }

        private void whenFindByLanguageIdThenOk(I18nTable customUpdate) throws Exception {
            List<I18nTable> list = multiAsListHelper(i18nDao.findByLanguageId(customUpdate.languageId()));
            Assertions.assertFalse(list.isEmpty());
            var optional = list.stream().filter(e -> customUpdate.id().equals(e.id())).findFirst();
            Assertions.assertTrue(optional.isPresent());
            var test = optional.get();
            var expected = expected(test.id(), languageDaoTest.id, customUpdate.message(), test);
            Assertions.assertEquals(expected, test);
            Assertions.assertNotNull(test.createTime());
            Assertions.assertNotNull(test.updateTime());
        }

        private void whenFindByMessageThenOk(I18nTable customUpdate) throws Exception {
            List<I18nTable> list = multiAsListHelper(i18nDao.findByMessage(customUpdate.message()));
            Assertions.assertFalse(list.isEmpty());
            var optional = list.stream().filter(e -> customUpdate.id().equals(e.id())).findFirst();
            Assertions.assertTrue(optional.isPresent());
            var test = optional.get();
            var expected = expected(test.id(), languageDaoTest.id, customUpdate.message(), test);
            Assertions.assertEquals(expected, test);
            Assertions.assertNotNull(test.createTime());
            Assertions.assertNotNull(test.updateTime());
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    @Nested
    @DisplayName("I18nTransactionalJob")
    class I18nTransactionalJobTest {

        I18nTable insertEntry1 = I18nTable.builder()
                .message(I18nTable.NONE)
                .userName("root")
                .build();

        I18nTable insertEntry2 = I18nTable.builder()
                .message(UUID.randomUUID().toString())
                .userName("root")
                .build();

        @Test
        void test() {
            var result1 = whenInsertEntry1ForLangEnThenOk();
            var result2 = whenInsertEntry2ForLangEnThenOk();
            var result3 = whenInsertEntry1ForLangRuThenOk();
            var result4 = whenInsertEntry2ForLangRuThenOk();

            var updateEntry1 = I18nTable.builder()
                    .id(result1)
                    .message(I18nTable.NONE)
                    .userName("root")
                    .build();
            var updateEntry2 = I18nTable.builder()
                    .id(result2)
                    .message(I18nTable.NONE)
                    .userName("root")
                    .build();
            var updateEntry3 = I18nTable.builder()
                    .id(result3)
                    .message(I18nTable.NONE)
                    .userName("root")
                    .build();
            var updateEntry4 = I18nTable.builder()
                    .id(result4)
                    .message(I18nTable.NONE)
                    .userName("root")
                    .build();

            whenUpdateEntryForLangThenOk(updateEntry1, "en");
            whenUpdateEntryForLangThenOk(updateEntry1, "jp");

            i18nTransactionalJob.delete(updateEntry1).await().indefinitely();
            i18nTransactionalJob.delete(updateEntry2).await().indefinitely();
            i18nTransactionalJob.delete(updateEntry3).await().indefinitely();
            i18nTransactionalJob.delete(updateEntry4).await().indefinitely();

            var count = i18nDao.count().await().indefinitely();
            Assertions.assertTrue(count.isPresent());
            Assertions.assertEquals(0, count.get());
        }

        long whenInsertEntry1ForLangEnThenOk() {
            var result = i18nTransactionalJob.insert(insertEntry1, "en").await().indefinitely();
            Assertions.assertTrue(result.isPresent());
            return result.get();
        }

        long whenInsertEntry2ForLangEnThenOk() {
            var result = i18nTransactionalJob.insert(insertEntry2, "en").await().indefinitely();
            Assertions.assertTrue(result.isPresent());
            return result.get();
        }

        long whenInsertEntry1ForLangRuThenOk() {
            var result = i18nTransactionalJob.insert(insertEntry1, "ru").await().indefinitely();
            Assertions.assertTrue(result.isPresent());
            return result.get();
        }

        long whenInsertEntry2ForLangRuThenOk() {
            var result = i18nTransactionalJob.insert(insertEntry2, "ru").await().indefinitely();
            Assertions.assertTrue(result.isPresent());
            return result.get();
        }

        long whenUpdateEntryForLangThenOk(I18nTable entry, String lang) {
            var result = i18nTransactionalJob.update(entry, lang).await().indefinitely();
            Assertions.assertTrue(result.isPresent());
            return result.get();
        }
    }

    @Nested
    @DisplayName("I18nViewDao")
    class I18nViewDaoTest extends AbstractViewDaoTest<Long, I18nTable, I18nView> {

        String messageEntry2;

        AbstractDaoTest<Long, LanguageTable> languageDaoTest = new AbstractDaoTest<>();

        @BeforeEach
        void setUp() {
            var language = LanguageTable.builder()
                    .language(LanguageTable.NONE)
                    .userName("root")
                    .enabled(true)
                    .build();
            messageEntry2 = UUID.randomUUID().toString();
            languageDaoTest.setUp(languageDao, language, 0L);
            var entry1 = I18nTable.builder()
                    .languageId(languageDaoTest.id)
                    .message(I18nTable.NONE)
                    .userName("root")
                    .enabled(true)
                    .build();
            var entry2 = I18nTable.builder()
                    .languageId(languageDaoTest.id)
                    .message(messageEntry2)
                    .userName("root")
                    .enabled(true)
                    .build();
            super.setUp(i18nDao, i18nViewDao, entry1, entry2);
        }

        @AfterEach
        void tearDown() {
            super.tearDown();
            languageDaoTest.tearDown();
        }

        I18nView.Builder builder(Long id, String message, I18nView test) {
            return I18nView.builder()
                    .id(id)
                    .language(LanguageTable.NONE)
                    .message(message)
                    .createTime(test.createTime())
                    .updateTime(test.updateTime())
                    .enabled(true);
        }

        I18nView expected(Long id, String message, I18nView test) {
            Assertions.assertNotNull(test);
            return builder(id, message, test).userName("root").build();
        }

        I18nView expected(Long id, String language, String message, I18nView test) {
            Assertions.assertNotNull(test);
            return builder(id, message, test).language(language).userName("root").build();
        }

        List<I18nView> expectedSingletonList(Long id, String language, String message, List<I18nView> test) {
            Assertions.assertNotNull(test);
            var entry1 = test
                    .stream()
                    .findFirst()
                    .orElse(null);
            var entry2 = expected(id, language, message, entry1);
            return Collections.singletonList(entry2);
        }

        @Test
        void test() {
            super.checkCount2();

            super.whenFindById1ThenEntry((id, test) -> expected(id, I18nTable.NONE, test));
            super.whenFindById2ThenEntry((id, test) -> expected(id, messageEntry2, test));

            super.whenSupplierThenEntry(
                    () -> i18nViewDao.findByKey(LanguageTable.NONE, I18nTable.NONE),
                    test -> expected(super.id1, LanguageTable.NONE, I18nTable.NONE, test));

            super.whenSupplierThenList(
                    () -> i18nViewDao.findByValue(I18nTable.NONE),
                    test -> expectedSingletonList(super.id1, LanguageTable.NONE, I18nTable.NONE, test));

            super.whenFindAllThenMultiWithOneItem();
            super.whenFindRangeZeroThenEmptiestMulti();

            super.whenFindRangeFromZeroLimitOneThenMultiWithOneItem(
                    (id, test) -> expected(id, I18nTable.NONE, test)
            );
            super.whenFindRangeFromOneLimitOneMultiWithOneItem(
                    (id, test) -> expected(id, messageEntry2, test)
            );
            super.whenFindRangeFromZeroToMaxValueThenMultiWithTwoItems();
        }
    }

    @Nested
    @DisplayName("KeyValueDao")
    class KeyValueDaoTest extends AbstractDaoTest<UUID, KeyValueTable> {

        @BeforeEach
        void setUp() {
            var entry = KeyValueTable.builder()
                    .key(BigInteger.ONE)
                    .userName("root")
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
            return builder(id, key, test).userName("root").build();
        }

        KeyValueTable expected(UUID id, BigInteger key, JsonObject value, KeyValueTable test) {
            Assertions.assertNotNull(test);
            return builder(id, key, test).value(value).userName("root").build();
        }

        @Test
        void test() {
            super.whenFindByIdThenEntry((id, test) -> expected(id, BigInteger.ONE, test));

            var update = KeyValueTable.builder().id(super.id).key(BigInteger.TWO).userName("root").build();
            super.whenUpdateAndFindByIdThenEntry((id, test) -> expected(id, BigInteger.TWO, test), update);

            super.whenFindAllThenMultiWithOneItem();
            super.whenFindRangeZeroThenEmptiestMulti();
            super.whenFindRangeFromZeroLimitOneThenMultiWithOneItem();

            var custom = KeyValueTable.builder()
                    .id(customId)
                    .key(BigInteger.valueOf(7))
                    .value(new JsonObject("{}"))
                    .userName("root")
                    .build();
            super.whenInsertCustomThenEntry(
                    (id, test) -> expected(id, BigInteger.valueOf(7), new JsonObject("{}"), test),
                    custom
            );
            var customUpdate = KeyValueTable.builder()
                    .id(customId)
                    .key(BigInteger.TEN)
                    .value(new JsonObject("{}"))
                    .userName("root")
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

    @Nested
    @DisplayName("LanguageDao")
    class LanguageDaoTest extends AbstractDaoTest<Long, LanguageTable> {

        String ZERO = TestData.uuid.STRING_ZERO;
        String ONE = TestData.uuid.STRING_ONE;
        String TWO = TestData.uuid.STRING_TWO;
        String TEN = TestData.uuid.STRING_TEN;

        @BeforeEach
        void setUp() {
            var entry = LanguageTable.builder()
                    .language(ONE)
                    .userName("root")
                    .enabled(true)
                    .build();
            Long customId = 0L;
            super.setUp(languageDao, entry, customId);
        }

        @AfterEach
        void tearDown() {
            super.tearDown();
        }

        LanguageTable.Builder builder(Long id, String language, LanguageTable test) {
            return LanguageTable.builder()
                    .id(id)
                    .language(language)
                    .createTime(test.createTime())
                    .updateTime(test.updateTime())
                    .enabled(true);
        }

        LanguageTable expected(Long id, String language, LanguageTable test) {
            Assertions.assertNotNull(test);
            return builder(id, language, test).userName("root").build();
        }

        @Test
        void test() {
            super.whenFindByIdThenEntry((id, test) -> expected(id, ONE, test));

            var update = LanguageTable.builder().id(super.id).language(TWO).userName("root").build();
            super.whenUpdateAndFindByIdThenEntry((id, test) -> expected(id, TWO, test), update);

            super.whenFindAllThenMultiWithOneItem();
            super.whenFindRangeZeroThenEmptiestMulti();
            super.whenFindRangeFromZeroLimitOneThenMultiWithOneItem();

            var custom = LanguageTable.builder()
                    .id(customId)
                    .language(ZERO)
                    .userName("root")
                    .build();
            super.whenInsertCustomThenEntry(
                    (id, test) -> expected(id, ZERO, test),
                    custom
            );
            var customUpdate = LanguageTable.builder()
                    .id(customId)
                    .language(ZERO)
                    .userName("root")
                    .build();
            super.whenUpdateCustomAndFindByIdThenEntry(
                    (id, test) -> expected(id, ZERO, test),
                    customUpdate
            );

            super.whenFindRangeFromZeroToOneThenMultiWithOneItemCustom(
                    (id, test) -> expected(id, ZERO, test)
            );
            super.whenFindRangeFromZeroToMaxValueThenMultiWithTwoItems();
            super.whenFindRangeFromOneLimitOneMultiWithOneItem();

            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(languageDao.findByLanguage(TWO));
                var expected = expected(super.id, TWO, test);
                Assertions.assertEquals(expected, test);
                Assertions.assertNotNull(test.createTime());
                Assertions.assertNotNull(test.updateTime());
            });

            super.whenDeleteCustomThenOk();
        }
    }

    @Nested
    @DisplayName("SettingDao")
    class SettingDaoTest extends AbstractDaoTest<Long, SettingTable> {

        static Long valueTypeId;

        static final String ONE = new UUID(0, 1).toString();
        static final String TWO = new UUID(0, 2).toString();
        static final String NINE = new UUID(0, 9).toString();
        static final String TEN = new UUID(0, 10).toString();

        @BeforeEach
        void setUp() {
            var valueType = ValueTypeTable.builder()
                    .id(0L)
                    .valueType(ValueTypeTable.NONE)
                    .userName("root")
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(() -> {
                valueTypeId = uniOptionalHelper(valueTypeDao.insert(valueType));
            });
            var entry = SettingTable.builder()
                    .variable(ONE)
                    .valueTypeId(valueTypeId)
                    .stanzaId(0L)
                    .userName("root")
                    .enabled(true)
                    .build();
            Long customId = 2L;
            super.setUp(settingDao, entry, customId);
        }

        @AfterEach
        void tearDown() {
            super.tearDown();
        }

        SettingTable.Builder builder(Long id, String variable, SettingTable test) {
            return SettingTable.builder()
                    .id(id)
                    .variable(variable)
                    .valueTypeId(valueTypeId)
                    .stanzaId(0)
                    .createTime(test.createTime())
                    .updateTime(test.updateTime())
                    .enabled(true);
        }

        SettingTable expected(Long id, String variable, SettingTable test) {
            Assertions.assertNotNull(test);
            return builder(id, variable, test).userName("root").build();
        }

        SettingTable expected(Long id, String variable, String value, SettingTable test) {
            Assertions.assertNotNull(test);
            return builder(id, variable, test)
                    .value(value)
                    .valueTypeId(valueTypeId)
                    .userName("root")
                    .build();
        }

        @Test
        void test() {
            super.whenFindByIdThenEntry((id, test) -> expected(id, ONE, test));

            var update = SettingTable.builder()
                    .id(super.id)
                    .variable(TWO)
                    .valueTypeId(valueTypeId)
                    .stanzaId(0L)
                    .userName("root")
                    .build();
            super.whenUpdateAndFindByIdThenEntry((id, test) -> expected(id, TWO, test), update);

            super.whenFindAllThenMultiWithOneItem();
            super.whenFindRangeZeroThenEmptiestMulti();
            super.whenFindRangeFromZeroLimitOneThenMultiWithOneItem();

            var custom = SettingTable.builder()
                    .id(customId)
                    .variable(String.valueOf(7))
                    .value("{}")
                    .valueTypeId(valueTypeId)
                    .stanzaId(0)
                    .userName("root")
                    .build();
            super.whenInsertCustomThenEntry(
                    (id, test) -> expected(id, String.valueOf(7), "{}", test),
                    custom
            );
            var customUpdate = SettingTable.builder()
                    .id(customId)
                    .variable(TEN)
                    .value("{}")
                    .valueTypeId(valueTypeId)
                    .stanzaId(0)
                    .userName("root")
                    .build();
            super.whenUpdateCustomAndFindByIdThenEntry(
                    (id, test) -> expected(id, TEN, "{}", test),
                    customUpdate
            );

            super.whenFindRangeFromZeroToOneThenMultiWithOneItem();
            super.whenFindRangeFromZeroToMaxValueThenMultiWithTwoItems();
            super.whenFindRangeFromOneLimitOneMultiWithOneItem();

            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(settingDao.findByVariable(TWO));
                var expected = expected(super.id, TWO, test);
                Assertions.assertEquals(expected, test);
                Assertions.assertNotNull(test.createTime());
                Assertions.assertNotNull(test.updateTime());
            });

            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(settingDao.findByValue("{}"));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
                var expected = expected(customId, TEN, "{}", test.get(0));
                Assertions.assertEquals(expected, test.get(0));
            });

            super.whenDeleteCustomThenOk();
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    @Nested
    @DisplayName("SettingTransactionalJob")
    class SettingTransactionalJobTest {

        SettingTable insertEntry1 = SettingTable.builder()
                .variable("variable1")
                .value(SettingTable.NONE)
                .valueTypeId(0L)
                .stanzaId(0L)
                .userName("root")
                .build();

        SettingTable insertEntry2 = SettingTable.builder()
                .variable("variable2")
                .value(SettingTable.NONE)
                .valueTypeId(0L)
                .stanzaId(0L)
                .userName("root")
                .build();

        @AfterEach
        void tearDown() {
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(0, uniOptionalHelper(valueTypeDao.count())));
        }

        @Test
        void test() {
            var result1 = whenInsertEntry1ForValueTypeObjectThenOk();
            var result2 = whenInsertEntry2ForValueTypeStringThenOk();

            var updateEntry1 = SettingTable.builder()
                    .id(result1)
                    .variable(insertEntry1.variable())
                    .value(SettingTable.NONE)
                    .valueTypeId(0L)
                    .stanzaId(0L)
                    .userName("root")
                    .build();
            var updateEntry2 = SettingTable.builder()
                    .id(result2)
                    .variable(insertEntry2.variable())
                    .value(SettingTable.NONE)
                    .valueTypeId(0L)
                    .stanzaId(0L)
                    .userName("root")
                    .build();

            whenUpdateEntryForValueTypehenOk(updateEntry1, "Object");
            whenUpdateEntryForValueTypehenOk(updateEntry2, "String");

            settingTransactionalJob.delete(updateEntry1).await().indefinitely();
            settingTransactionalJob.delete(updateEntry2).await().indefinitely();

            var count = settingDao.count().await().indefinitely();
            Assertions.assertTrue(count.isPresent());
            Assertions.assertEquals(0, count.get());
        }

        long whenInsertEntry1ForValueTypeObjectThenOk() {
            var result = settingTransactionalJob
                    .insert(insertEntry1, "Object")
                    .await()
                    .indefinitely();
            Assertions.assertTrue(result.isPresent());
            return result.get();
        }

        long whenInsertEntry2ForValueTypeStringThenOk() {
            var result = settingTransactionalJob
                    .insert(insertEntry2, "String")
                    .await()
                    .indefinitely();
            Assertions.assertTrue(result.isPresent());
            return result.get();
        }

        long whenUpdateEntryForValueTypehenOk(SettingTable entry, String lang) {
            var result = settingTransactionalJob
                    .update(entry, lang)
                    .await()
                    .indefinitely();
            Assertions.assertTrue(result.isPresent());
            return result.get();
        }
    }

    @Nested
    @DisplayName("SettingViewDao")
    class SettingViewDaoTest extends AbstractViewDaoTest<Long, SettingTable, SettingView> {

        String messageEntry2;

        AbstractDaoTest<Long, ValueTypeTable> valueTypeDaoTest = new AbstractDaoTest<>();

        @BeforeEach
        void setUp() {
            var valueType = ValueTypeTable.builder()
                    .valueType(ValueTypeTable.NONE)
                    .userName("root")
                    .enabled(true)
                    .build();
            messageEntry2 = UUID.randomUUID().toString();
            valueTypeDaoTest.setUp(valueTypeDao, valueType, 0L);
            var entry1 = SettingTable.builder()
                    .variable("variable1")
                    .valueTypeId(valueTypeDaoTest.id)
                    .value(SettingTable.NONE)
                    .stanzaId(0L)
                    .userName("root")
                    .enabled(true)
                    .build();
            var entry2 = SettingTable.builder()
                    .variable("variable2")
                    .valueTypeId(valueTypeDaoTest.id)
                    .value(messageEntry2)
                    .stanzaId(0L)
                    .userName("root")
                    .enabled(true)
                    .build();
            super.setUp(settingDao, settingViewDao, entry1, entry2);
        }

        @AfterEach
        void tearDown() {
            super.tearDown();
            valueTypeDaoTest.tearDown();
        }

        SettingView.Builder builder(Long id, String value, SettingView test) {
            return SettingView.builder()
                    .id(id)
                    .variable(test.variable())
                    .valueType(ValueTypeTable.NONE)
                    .value(value)
                    .stanzaId(0L)
                    .createTime(test.createTime())
                    .updateTime(test.updateTime())
                    .enabled(true);
        }

        SettingView expected(Long id, String value, SettingView test) {
            Assertions.assertNotNull(test);
            return builder(id, value, test).userName("root").build();
        }

        List<SettingView> expectedSingletonList(Long id, String value, List<SettingView> test) {
            Assertions.assertNotNull(test);
            var entry1 = test
                    .stream()
                    .findFirst()
                    .orElse(null);
            var entry2 = expected(id, value, entry1);
            return Collections.singletonList(entry2);
        }

        @Test
        void test() {
            super.checkCount2();

            super.whenFindById1ThenEntry((id, test) -> expected(id, SettingTable.NONE, test));
            super.whenFindById2ThenEntry((id, test) -> expected(id, messageEntry2, test));

            super.whenSupplierThenEntry(
                    () -> settingViewDao.findByKey("variable1"),
                    test -> expected(super.id1, SettingTable.NONE, test));

            super.whenSupplierThenList(
                    () -> settingViewDao.findByValue(SettingTable.NONE),
                    test -> expectedSingletonList(super.id1, SettingTable.NONE, test));

            super.whenFindAllThenMultiWithOneItem();
            super.whenFindRangeZeroThenEmptiestMulti();

            super.whenFindRangeFromZeroLimitOneThenMultiWithOneItem(
                    (id, test) -> expected(id, SettingTable.NONE, test)
            );
            super.whenFindRangeFromOneLimitOneMultiWithOneItem(
                    (id, test) -> expected(id, messageEntry2, test)
            );
            super.whenFindRangeFromZeroToMaxValueThenMultiWithTwoItems();
        }
    }

    @Nested
    @DisplayName("StanzaDao")
    class StanzaDaoTest extends AbstractDaoTest<Long, StanzaTable> {

        public static final String ONE = "00000000-0000-0000-0000-000000000001";
        public static final String TWO = "00000000-0000-0000-0000-000000000002";
        public static final String NINE = "00000000-0000-0000-0000-000000000009";
        public static final String TEN = "00000000-0000-0000-0000-00000000000a";

        @BeforeEach
        void setUp() {
            var entry = StanzaTable.builder()
                    .id(1L)
                    .name(ONE)
                    .parentId(0L)
                    .userName("root")
                    .enabled(true)
                    .visible(true)
                    .build();
            Long customId = 2L;
            super.setUp(stanzaDao, entry, customId);
        }

        @AfterEach
        void tearDown() {
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id, uniOptionalHelper(super.dao.delete(super.id))));
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(1, uniOptionalHelper(super.dao.count())));
        }

        StanzaTable.Builder builder(Long id, String name, StanzaTable test) {
            return StanzaTable.builder()
                    .id(id)
                    .name(name)
                    .parentId(0L)
                    .createTime(test.createTime())
                    .updateTime(test.updateTime())
                    .enabled(true)
                    .visible(true);
        }

        StanzaTable expected(Long id, String name, StanzaTable test) {
            Assertions.assertNotNull(test);
            return builder(id, name, test).userName("root").build();
        }

        StanzaTable expected(Long id, String name, Long parentId, StanzaTable test) {
            Assertions.assertNotNull(test);
            return builder(id, name, test).parentId(parentId).userName("root").build();
        }

        @Test
        void test() {
            super.whenFindByIdThenEntry((id, test) -> expected(id, ONE, test));

            var update = StanzaTable.builder()
                    .id(super.id)
                    .name(TWO)
                    .userName("root")
                    .visible(true)
                    .build();
            super.whenUpdateAndFindByIdThenEntry((id, test) -> expected(id, TWO, test), update);

            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper((super.dao.findAll()));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(2, test.size());
            });
            super.whenFindRangeZeroThenEmptiestMulti();
            super.whenFindRangeFromZeroLimitOneThenMultiWithOneItem();

            var custom = StanzaTable.builder()
                    .id(customId)
                    .name(NINE)
                    .parentId(0L)
                    .userName("root")
                    .visible(true)
                    .build();
            super.whenInsertCustomThenEntry(
                    (id, test) -> expected(id, NINE, 0L, test),
                    custom
            );
            var customUpdate = StanzaTable.builder()
                    .id(customId)
                    .name(TEN)
                    .parentId(0L)
                    .userName("root")
                    .visible(true)
                    .build();
            super.whenUpdateCustomAndFindByIdThenEntry(
                    (id, test) -> expected(id, TEN, 0L, test),
                    customUpdate
            );

            super.whenFindRangeFromZeroToOneThenMultiWithOneItemCustom(
                    (id, test) -> expected(0L, StanzaTable.NONE, 0L, test)
            );
            super.whenFindRangeFromOneLimitOneMultiWithOneItem();

            Assertions.assertDoesNotThrow(() -> {
                var list = multiAsListHelper(stanzaDao.findByName(TWO));
                Assertions.assertFalse(list.isEmpty());
                var test = list.get(0);
                var expected = expected(super.id, TWO, test);
                Assertions.assertEquals(expected, test);
                Assertions.assertNotNull(test.createTime());
                Assertions.assertNotNull(test.updateTime());
            });

            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(stanzaDao.findByParentId(0L));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(3, test.size());
            });

            super.whenDeleteCustomThenOk();
        }
    }

    @Nested
    @DisplayName("StanzaViewDao")
    class StanzaViewDaoTest extends AbstractViewDaoTest<Long, StanzaTable, StanzaView> {

        @BeforeEach
        void setUp() {
            var entry1 = StanzaTable.builder()
                    .parentId(0L)
                    .userName("root")
                    .enabled(true)
                    .build();
            var entry2 = StanzaTable.builder()
                    .name("name2")
                    .parentId(0L)
                    .userName("root")
                    .enabled(true)
                    .build();
            super.setUp(stanzaDao, stanzaViewDao, entry1, entry2);
        }

        @AfterEach
        void tearDown() {
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(super.id2, uniOptionalHelper(super.dao.delete(id2))));
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(super.id1, uniOptionalHelper(super.dao.delete(id1))));
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(1, uniOptionalHelper(super.dao.count())));
        }

        StanzaView.Builder builder(Long id, StanzaView test) {
            return StanzaView.builder()
                    .id(id)
                    .name(test.name())
                    .parent(test.parent())
                    .settings(Collections.emptySet())
                    .createTime(test.createTime())
                    .updateTime(test.updateTime())
                    .enabled(true);
        }

        StanzaView expected(Long id, StanzaView test) {
            Assertions.assertNotNull(test);
            return builder(id, test).userName("root").build();
        }


        StanzaView expected(Long id, String name, StanzaView test) {
            Assertions.assertNotNull(test);
            return builder(id, test).name(name).userName("root").build();
        }

        List<StanzaView> expectedSingletonList(Long id, String name, List<StanzaView> test) {
            Assertions.assertNotNull(test);
            var entry1 = test
                    .stream()
                    .findFirst()
                    .orElse(null);
            var entry2 = expected(id, name, entry1);
            return Collections.singletonList(entry2);
        }

        @Test
        void test() {
            super.checkCount3();

            super.whenFindById1ThenEntry(this::expected);
            super.whenFindById2ThenEntry(this::expected);

            super.whenSupplierThenList(
                    () -> stanzaViewDao.findByName("name2"),
                    test -> expectedSingletonList(super.id2, "name2", test));


            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(stanzaViewDao.findByParentId(0L));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(3, test.size());
            });

            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(stanzaViewDao.findAll());
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(3, test.size());
            });

            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(stanzaViewDao.findRange(0, Long.MAX_VALUE));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(3, test.size());
            });

            super.whenFindRangeZeroThenEmptiestMulti();

            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(stanzaViewDao.findRange(1, 2));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(2, test.size());
                Assertions.assertEquals(expected(super.id1, test.get(0)), test.get(0));
            });

            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(stanzaViewDao.findRange(0, Long.MAX_VALUE));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(3, test.size());
            });

            Assertions.assertDoesNotThrow(() -> {
                var valueTypeTable = ValueTypeTable.builder()
                        .id(0L)
                        .valueType(ValueTypeTable.NONE)
                        .userName("root")
                        .enabled(true)
                        .build();
                var settingTable = SettingTable.builder()
                        .id(0L)
                        .variable(SettingTable.NONE)
                        .value(SettingTable.NONE)
                        .valueTypeId(0L)
                        .stanzaId(0L)
                        .userName("root")
                        .build();
                var id1 = uniOptionalHelper(valueTypeDao.insert(valueTypeTable));
                var id2 = uniOptionalHelper(settingDao.insert(settingTable));
                var test = multiAsListHelper(stanzaViewDao.findRange(0, Long.MAX_VALUE));
                uniOptionalHelper(settingDao.delete(id2));
                uniOptionalHelper(valueTypeDao.delete(id1));
            });
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    @Nested
    @DisplayName("StanzaTransactionalJob")
    class StanzaTransactionalJobTest {

        long valueTypeId;
        UUID userNameId;
        SettingTable setting;

        @BeforeEach
        void setUp() {
            userNameId = new UUID(0, 0);
// todo
//            var userName = UserNameTable.builder()
//                    .id(userNameId)
//                    .userName("user")
//                    .password("password")
//                    .enabled(true)
//                    .build();
//            Assertions.assertDoesNotThrow(() -> {
//                uniOptionalHelper(userNameDao.insert(userName));
//            });
            var valueType = ValueTypeTable.builder()
                    .valueType(ValueTypeTable.NONE)
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(() -> {
                valueTypeId = uniOptionalHelper(valueTypeDao.insert(valueType));
            });
            setting = SettingTable.builder()
                    .id(13L)
                    .variable("variable1")
                    .value(SettingTable.NONE)
                    .valueTypeId(valueTypeId)
                    .stanzaId(0L)
                    .userName("root")
                    .build();
            Assertions.assertDoesNotThrow(() -> {
                uniOptionalHelper(settingDao.insert(setting));
            });
        }

        @AfterEach
        void tearDown() {
            Assertions.assertDoesNotThrow(() -> {
                uniOptionalHelper(settingDao.delete(13L));
            });
            Assertions.assertDoesNotThrow(() -> {
                uniOptionalHelper(valueTypeDao.delete(valueTypeId));
            });
// todo
//            Assertions.assertDoesNotThrow(() -> {
//                uniOptionalHelper(userNameDao.delete(userNameId));
//            });
        }

        @Test
        void test() {
            var stanza1 = StanzaTable.builder()
                    .id(13L)
                    .name("name1")
                    .description("description1")
                    .parentId(0L)
                    .userName("user")
                    .createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .build();
            var setting1 = setting.toBuilder().stanzaId(13L).build();

            Collection<SettingTable> settings = Collections.singleton(setting1);
            Pair<StanzaTable, Collection<SettingTable>> pair = Pair.of(stanza1, settings);
            Collection<Pair<StanzaTable, Collection<SettingTable>>> stanzas = Collections.singleton(pair);

            Assertions.assertDoesNotThrow(() -> {
                uniListHelper(stanzaTransactionalJob.upsert(stanzas));
            });
            Assertions.assertDoesNotThrow(() -> {
                uniOptionalHelper(stanzaTransactionalJob.delete(stanza1));
            });
        }
    }

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
                    .userName("root")
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
                    .userName("root")
                    .createTime(test.createTime())
                    .updateTime(test.updateTime())
                    .enabled(true);
        }

        TagLabelTable expected(String id, String label, TagLabelTable test) {
            Assertions.assertNotNull(test);
            return builder(id, label, test).userName("root").build();
        }

        @Test
        void test() {
            super.whenFindByIdThenEntry((id, test) -> expected(id, ZERO, test));

            var update = TagLabelTable.builder().id(super.id).label(ONE).userName("root").build();
            super.whenUpdateAndFindByIdThenEntry((id, test) -> expected(id, ONE, test), update);

            super.whenFindAllThenMultiWithOneItem();
            super.whenFindRangeZeroThenEmptiestMulti();
            super.whenFindRangeFromZeroLimitOneThenMultiWithOneItem();

            var custom = TagLabelTable.builder()
                    .id(customId)
                    .label(TWO)
                    .userName("root")
                    .build();
            super.whenInsertCustomThenEntry(
                    (id, test) -> expected(id, TWO, test),
                    custom
            );
            var customUpdate = TagLabelTable.builder()
                    .id(customId)
                    .label(TEN)
                    .userName("root")
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
    @DisplayName("ValueTypeDao")
    class ValueTypeDaoTest extends AbstractDaoTest<Long, ValueTypeTable> {

        static final String TWO = new UUID(0, 2).toString();
        static final String NINE = new UUID(0, 9).toString();
        static final String TEN = new UUID(0, 10).toString();

        @BeforeEach
        void setUp() {
            var entry = ValueTypeTable.builder()
                    .valueType(ValueTypeTable.NONE)
                    .enabled(true)
                    .build();
            Long customId = 2L;
            super.setUp(valueTypeDao, entry, customId);
        }

        @AfterEach
        void tearDown() {
            super.tearDown();
        }

        ValueTypeTable.Builder builder(Long id, String valueType, ValueTypeTable test) {
            return ValueTypeTable.builder()
                    .id(id)
                    .valueType(valueType)
                    .createTime(test.createTime())
                    .updateTime(test.updateTime())
                    .enabled(true);
        }

        ValueTypeTable expected(Long id, String valueType, ValueTypeTable test) {
            Assertions.assertNotNull(test);
            return builder(id, valueType, test).build();
        }

        @Test
        void test() {
            super.whenFindByIdThenEntry((id, test) -> expected(id, ValueTypeTable.NONE, test));

            var update = ValueTypeTable.builder().id(super.id).valueType(TWO).build();
            super.whenUpdateAndFindByIdThenEntry((id, test) -> expected(id, TWO, test), update);

            super.whenFindAllThenMultiWithOneItem();
            super.whenFindRangeZeroThenEmptiestMulti();
            super.whenFindRangeFromZeroLimitOneThenMultiWithOneItem();

            var custom = ValueTypeTable.builder()
                    .id(customId)
                    .valueType(NINE)
                    .build();
            super.whenInsertCustomThenEntry(
                    (id, test) -> expected(id, NINE, test),
                    custom
            );
            var customUpdate = ValueTypeTable.builder()
                    .id(customId)
                    .valueType(TEN)
                    .build();
            super.whenUpdateCustomAndFindByIdThenEntry(
                    (id, test) -> expected(id, TEN, test),
                    customUpdate
            );

            super.whenFindRangeFromZeroToOneThenMultiWithOneItemCustom(
                    (id, test) -> expected(1L, TWO, test)
            );
            super.whenFindRangeFromZeroToMaxValueThenMultiWithTwoItems();
            super.whenFindRangeFromOneLimitOneMultiWithOneItem();

            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(valueTypeDao.findByKey(TWO));
                var expected = expected(super.id, TWO, test);
                Assertions.assertEquals(expected, test);
                Assertions.assertNotNull(test.createTime());
                Assertions.assertNotNull(test.updateTime());
            });

            super.whenDeleteCustomThenOk();
        }
    }

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
                    .userName("root")
                    .enabled(true)
                    .build();
            wordDaoTest.setUp(wordDao, word, wordCustom);
            var entry = VocabularyTable.builder()
                    .word(word1)
                    .userName("root")
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
            return builder(id, word, test).userName("root").build();
        }

        VocabularyTable expected(Long id, String word, String value, VocabularyTable test) {
            Assertions.assertNotNull(test);
            return builder(id, word, test).value(value).userName("root").build();
        }

        @Test
        void test() {
            super.whenFindByIdThenEntry((id, test) -> expected(id, word1, test));

            var update = VocabularyTable.builder()
                    .id(super.id)
                    .word(word1)
                    .value("value1")
                    .userName("root")
                    .build();
            super.whenUpdateAndFindByIdThenEntry((id, test) -> expected(id, word1, "value1", test), update);

            super.whenFindAllThenMultiWithOneItem();
            super.whenFindRangeZeroThenEmptiestMulti();
            super.whenFindRangeFromZeroLimitOneThenMultiWithOneItem();

            var customWord = WordTable.builder()
                    .word(wordCustom)
                    .userName("root")
                    .build();
            wordDaoTest.whenInsertCustomThenEntry(
                    (id, test) -> WordTable.builder()
                            .word(wordCustom)
                            .userName("root")
                            .createTime(test.createTime())
                            .updateTime(test.updateTime())
                            .enabled(true).build(),
                    customWord
            );
            var custom = VocabularyTable.builder()
                    .id(customId)
                    .word(wordCustom)
                    .value("custom1")
                    .userName("root")
                    .build();
            super.whenInsertCustomThenEntry(
                    (id, test) -> expected(id, wordCustom, "custom1", test),
                    custom
            );
            var customUpdate = VocabularyTable.builder()
                    .id(customId)
                    .word(wordCustom)
                    .value("custom2")
                    .userName("root")
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

    @Nested
    @DisplayName("WordDao")
    class WordDaoTest extends AbstractDaoTest<String, WordTable> {

        @BeforeEach
        void setUp() {
            var entry = WordTable.builder()
                    .word("word1")
                    .userName("root")
                    .enabled(true)
                    .build();
            var customId = UUID.randomUUID().toString();
            super.setUp(wordDao, entry, customId);
        }

        @AfterEach
        void tearDown() {
            super.tearDown();
        }

        WordTable.Builder builder(String id, WordTable test) {
            return WordTable.builder()
                    .id(id)
                    .word(id)
                    .createTime(test.createTime())
                    .updateTime(test.updateTime())
                    .enabled(true);
        }

        WordTable expected(String id, WordTable test) {
            Assertions.assertNotNull(test);
            return builder(id, test).userName("root").build();
        }

        @Test
        void test() {
            super.whenFindByIdThenEntry(this::expected);

            var update = WordTable
                    .builder()
                    .id(super.id)
                    .userName("root")
                    .visible(true)
                    .build();
            super.whenUpdateAndFindByIdThenEntry(
                    (id1, test1) -> builder(id1, test1)
                            .userName("root")
                            .visible(true)
                            .build(),
                    update
            );

            super.whenFindAllThenMultiWithOneItem();
            super.whenFindRangeZeroThenEmptiestMulti();
            super.whenFindRangeFromZeroLimitOneThenMultiWithOneItem();

            var custom = WordTable
                    .builder()
                    .id(customId)
                    .userName("root")
                    .build();
            super.whenInsertCustomThenEntry(this::expected, custom);
            var customUpdate = WordTable
                    .builder()
                    .id(customId)
                    .userName("root")
                    .visible(true)
                    .build();
            super.whenUpdateCustomAndFindByIdThenEntry(
                    (id1, test1) -> builder(id1, test1)
                            .userName("root")
                            .visible(true)
                            .build(),
                    customUpdate
            );

            super.whenFindRangeFromZeroToOneThenMultiWithOneItemCustom(
                    (id1, test1) -> builder(id1, test1)
                            .userName("root")
                            .visible(true)
                            .build()
            );
            super.whenFindRangeFromZeroToMaxValueThenMultiWithTwoItems();
            super.whenFindRangeFromOneLimitOneMultiWithOneItem();

            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(wordDao.findByWord("word1"));
                var expected = builder(super.id, test)
                        .userName("root")
                        .visible(true)
                        .build();
                Assertions.assertEquals(expected, test);
                Assertions.assertNotNull(test.createTime());
                Assertions.assertNotNull(test.updateTime());
            });

            super.whenDeleteCustomThenOk();
        }
    }
}
