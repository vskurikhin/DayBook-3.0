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
@QuarkusTestResource(PostgresDatabaseTestResource.class)
public class DataBaseIT {


    @Inject
    CodifierDao codifierDao;

    @Inject
    UserNameDao userNameDao;

    @Inject
    TagLabelDao tagLabelDao;

    @Inject
    VocabularyDao vocabularyDao;

    @Inject
    WordDao wordDao;

    @Nested
    @DisplayName("UserNameDao")
    class CodifierDaoTest {

        String id;

        String str = "str";

        Codifier entry;

        @BeforeEach
        void setUp() {
            entry = Codifier.builder()
                    .code(str)
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
            var expected1 = Codifier.builder()
                    .code(id)
                    .build();
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
            var expected2 = Codifier.builder()
                    .code(id)
                    .value("value")
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
                        var test = codifierDao.findByCode(id)
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
    @DisplayName("TagLabelDao")
    class TagLabelDaoTest {

        String id;

        String str = "str";

        TagLabel entry;

        @BeforeEach
        void setUp() {
            entry = TagLabel.builder()
                    .id(str)
                    .label(str)
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
            var expected1 = TagLabel.builder()
                    .id(id)
                    .label(str)
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
            var expected2 = TagLabel.builder()
                    .id(id)
                    .label("value")
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
            var test = TagLabel.builder()
                    .label("label")
                    .build();
            var strId = new AtomicReference<String>();
            Assertions.assertDoesNotThrow(
                    () -> strId.set(tagLabelDao.insert(test)
                            .subscribeAsCompletionStage()
                            .get()
                            .orElse(null))
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

        UserName entry;

        @BeforeEach
        void setUp() {
            entry = UserName.builder()
                    .id(id)
                    .userName("user")
                    .password("password")
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
            var expected1 = UserName.builder()
                    .id(id)
                    .userName("user")
                    .password("password")
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
            var expected2 = UserName.builder()
                    .id(id)
                    .userName("none")
                    .password("oops")
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
        }
    }


    @Nested
    @DisplayName("VocabularyDao")
    class VocabularyDaoAndWordDaoTest {

        String veryLongWordIdForTest = "veryLongWordIdForTest";

        Long vocabularyId;

        Vocabulary vocabulary;

        Word word;

        @BeforeEach
        void setUp() {
            vocabulary = Vocabulary.builder()
                    .word(veryLongWordIdForTest)
                    .build();
            word = Word.builder()
                    .word(veryLongWordIdForTest)
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
                    () -> vocabularyId = vocabularyDao.insert(vocabulary)
                            .subscribeAsCompletionStage()
                            .get()
                            .orElse(null)
            );
        }

        @AfterEach
        void tearDown() {
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(
                            vocabularyId, vocabularyDao.delete(vocabularyId.longValue())
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
            var expected1 = Word.builder()
                    .word(veryLongWordIdForTest)
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
            var expected2 = Word.builder()
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
        }

        @Test
        void testVocabularyDao() {
            var expected1 = Vocabulary.builder()
                    .id(1L)
                    .word(veryLongWordIdForTest)
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
            var expected2 = Vocabulary.builder()
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
        }
    }
}
