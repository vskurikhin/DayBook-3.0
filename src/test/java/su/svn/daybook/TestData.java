/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * DataTest.java
 * $Id$
 */

package su.svn.daybook;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.messages.ApiResponse;
import su.svn.daybook.domain.model.*;
import su.svn.daybook.models.domain.*;
import su.svn.daybook.models.pagination.Page;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

public class TestData {

    public static final String STRING_ZERO_UUID = "00000000-0000-0000-0000-000000000000";

    public static final String NO_SUCH_ELEMENT = "no such element";

    public static final UUID ZERO_UUID = new UUID(0, 0);
    public static final UUID RANDOM1_UUID = UUID.randomUUID();
    public static final UUID RANDOM2_UUID = UUID.randomUUID();
    public static final Uni<Optional<Long>> UNI_OPTIONAL_EMPTY_LONG = Uni.createFrom().item(Optional.empty());
    public static final Uni<Optional<String>> UNI_OPTIONAL_EMPTY_STRING = Uni.createFrom().item(Optional.empty());
    public static Uni<Answer> UNI_ANSWER_API_RESPONSE_ZERO_UUID = Uni.createFrom().item(Answer.of(new ApiResponse<>(ZERO_UUID)));
    public static Uni<Optional<UUID>> UNI_OPTIONAL_ZERO_UUID = Uni.createFrom().item(Optional.of(ZERO_UUID));
    public static Uni<Optional<UUID>> UNI_OPTIONAL_EMPTY_UUID = Uni.createFrom().item(Optional.empty());
    public static Answer ANSWER_ERROR_NoNumber = new Answer("For input string: \"noNumber\"", 404);
    public static Uni<Answer> UNI_ANSWER_EMPTY = Uni.createFrom().item(Answer.empty());
    public static Uni<Answer> UNI_ANSWER_NULL = Uni.createFrom().item(() -> null);
    public static Uni<Answer> UNI_ANSWER_API_RESPONSE_ZERO_LONG = Uni.createFrom().item(Answer.of(new ApiResponse<>(0L)));
    public static Uni<Optional<Long>> UNI_OPTIONAL_ZERO_LONG = Uni.createFrom().item(Optional.of(0L));

    public static Uni<Optional<Long>> UNI_OPTIONAL_ONE_LONG = Uni.createFrom().item(Optional.of(1L));
    public static Uni<Optional<Long>> UNI_OPTIONAL_MINUS_ONE_LONG = Uni.createFrom().item(Optional.of(-1L));

    public static <T> Multi<T> createMultiEmpties(Class<T> t) {
        return Multi.createFrom().empty();
    }

    public static <T> Multi<T> createMultiWithNull(Class<T> t) {
        return Multi.createFrom().item(() -> null);
    }

    public static class CODIFIER {
        public static final Codifier MODEL_0 = new Codifier(
                CodifierTable.NONE, null, true, 0
        );
        public static final CodifierTable TABLE_0 = new CodifierTable(
                CodifierTable.NONE, null, null, null, null, true, true, 0
        );
        public static final Uni<Page<Answer>> UNI_PAGE_ANSWER_SINGLETON_TEST = Uni.createFrom()
                .item(
                        Page.<Answer>builder()
                                .content(Collections.singletonList(Answer.of(MODEL_0)))
                                .build()
                );
        public static final String JSON_0 = """
                {"code":"\
                """ + CodifierTable.NONE + """
                ","visible":true,"flags":0}\
                """;
        public static final String JSON_ARRAY_SINGLETON_0 = "[" + JSON_0 + "]";
        public static final String JSON_ID_0 = "{\"id\":\"" + CodifierTable.NONE + "\"}";
        public static final String JSON_PAGE_ARRAY_0 = """
                        {"pageNumber":0,"totalElements":0,"nextPage":false,"prevPage":false,"content":\
                        """ + JSON_ARRAY_SINGLETON_0 +"""
                        }""";
    }

    public static class I18N {
        public static final I18n MODEL_0 = new I18n(
                0L, 0L, null, null, true, 0
        );
        public static final I18nTable TABLE_0 = new I18nTable(
                0L, 0L, null, null, null, null, null, true, true, 0
        );
        public static final Uni<Page<Answer>> UNI_PAGE_ANSWER_SINGLETON_TEST = Uni.createFrom()
                .item(
                        Page.<Answer>builder()
                                .content(Collections.singletonList(Answer.of(MODEL_0)))
                                .build()
                );
        public static final String JSON_0 = """
                {"id":0,"languageId":0,"visible":true,"flags":0}\
                """;
        public static final String JSON_ARRAY_SINGLETON_0 = "[" + JSON_0 + "]";
        public static final String JSON_ID_0 = "{\"id\":0}";
        public static final String JSON_PAGE_ARRAY_0 = """
                        {"pageNumber":0,"totalElements":0,"nextPage":false,"prevPage":false,"content":\
                        """ + JSON_ARRAY_SINGLETON_0 +"""
                        }""";
    }

    public static class KEY_VALUE {
        public static final KeyValue MODEL_0 = new KeyValue(
                0L, KeyValueTable.NONE, null, true, 0
        );
        public static final KeyValueTable TABLE_0 = new KeyValueTable(
                0L, KeyValueTable.NONE, null, null, null, null, true, true, 0
        );
        public static final Uni<Page<Answer>> UNI_PAGE_ANSWER_SINGLETON_TEST = Uni.createFrom()
                .item(
                        Page.<Answer>builder()
                                .content(Collections.singletonList(Answer.of(MODEL_0)))
                                .build()
                );
        public static final String JSON_0 = """
                {"id":0,"key":"\
                """ + KeyValueTable.NONE +"""
                ","visible":true,"flags":0}\
                """;
        public static final String JSON_ARRAY_SINGLETON_0 = "[" + JSON_0 + "]";
        public static final String JSON_ID_0 = "{\"id\":0}";
        public static final String JSON_PAGE_ARRAY_0 = """
                        {"pageNumber":0,"totalElements":0,"nextPage":false,"prevPage":false,"content":\
                        """ + JSON_ARRAY_SINGLETON_0 +"""
                        }""";
    }

    public static class LANGUAGE {
        public static final Language MODEL_0 = new Language(
                0L, null, true, 0
        );
        public static final LanguageTable TABLE_0 = new LanguageTable(
                0L, null, null, null, null, true, true, 0
        );
        public static final Uni<Page<Answer>> UNI_PAGE_ANSWER_SINGLETON_TEST = Uni.createFrom()
                .item(
                        Page.<Answer>builder()
                                .content(Collections.singletonList(Answer.of(MODEL_0)))
                                .build()
                );
        public static final String JSON_0 = """
                {"id":0,"visible":true,"flags":0}\
                """;
        public static final String JSON_ARRAY_SINGLETON_0 = "[" + JSON_0 + "]";
        public static final String JSON_ID_0 = "{\"id\":0}";
        public static final String JSON_PAGE_ARRAY_0 = """
                        {"pageNumber":0,"totalElements":0,"nextPage":false,"prevPage":false,"content":\
                        """ + JSON_ARRAY_SINGLETON_0 +"""
                        }""";
    }

    /* public static class ROLE {
        public static final Role MODEL_0 = new Role(
                ZERO_UUID, Role.NONE, null, true, 0
        );
        public static final RoleTable TABLE_0 = new RoleTable(
                ZERO_UUID, Role.NONE, null, null, null, null, true, true, 0
        );
        public static final Uni<Page<Answer>> UNI_PAGE_ANSWER_SINGLETON_TEST = Uni.createFrom()
                .item(
                        Page.<Answer>builder()
                                .content(Collections.singletonList(Answer.of(MODEL_0)))
                                .build()
                );
        public static final String JSON_0 = """
                {"id":"00000000-0000-0000-0000-000000000000","visible":true,"flags":0}\
                """;
        public static final String JSON_ARRAY_SINGLETON_0 = "[" + JSON_0 + "]";
        public static final String JSON_ID_0 = "{\"id\":0}";
        public static final String JSON_PAGE_ARRAY_0 = """
                        {"pageNumber":0,"totalElements":0,"nextPage":false,"prevPage":false,"content":\
                        """ + JSON_ARRAY_SINGLETON_0 +"""
                        }""";
    } */

    public static class SETTING {
        public static final Setting MODEL_0 = new Setting(
                0L, SettingTable.NONE, null, 0L, true, 0
        );
        public static final SettingTable TABLE_0 = new SettingTable(
                0L, SettingTable.NONE, null, 0L, null, null, null, true, true, 0
        );
        public static final Uni<Page<Answer>> UNI_PAGE_ANSWER_SINGLETON_TEST = Uni.createFrom()
                .item(
                        Page.<Answer>builder()
                                .content(Collections.singletonList(Answer.of(MODEL_0)))
                                .build()
                );
        public static final String JSON_0 = """
                {"id":0,"key":"\
                """ + SettingTable.NONE +"""
                ","valueTypeId":0,"visible":true,"flags":0}\
                """;
        public static final String JSON_ARRAY_SINGLETON_0 = "[" + JSON_0 + "]";
        public static final String JSON_ID_0 = "{\"id\":0}";
        public static final String JSON_PAGE_ARRAY_0 = """
                        {"pageNumber":0,"totalElements":0,"nextPage":false,"prevPage":false,"content":\
                        """ + JSON_ARRAY_SINGLETON_0 +"""
                        }""";
    }

    public static class TAG_LABEL {
        public static final String ID = TagLabelTable.NONE.replace("-", "").substring(0, 16);
        public static final Uni<Optional<String>> UNI_OPTIONAL_ID = Uni.createFrom().item(Optional.of(ID));
        public static Uni<Answer> UNI_ANSWER_API_RESPONSE_ID = Uni.createFrom().item(Answer.of(new ApiResponse<>(ID)));
        public static final TagLabel MODEL_0 = new TagLabel(
                ID, TagLabelTable.NONE, true, 0
        );
        public static final TagLabelTable TABLE_0 = new TagLabelTable(
                ID, TagLabelTable.NONE, null, null, null, true, true, 0
        );
        public static final Uni<Page<Answer>> UNI_PAGE_ANSWER_SINGLETON_TEST = Uni.createFrom()
                .item(
                        Page.<Answer>builder()
                                .content(Collections.singletonList(Answer.of(MODEL_0)))
                                .build()
                );
        public static final String JSON_0 = """
                {"id":"\
                """ + ID + """
                ","label":"\
                """ + TagLabelTable.NONE + """
                ","visible":true,"flags":0}\
                """;
        public static final String JSON_ARRAY_SINGLETON_0 = "[" + JSON_0 + "]";
        public static final String JSON_PAGE_ARRAY_0 = """
                        {"pageNumber":0,"totalElements":0,"nextPage":false,"prevPage":false,"content":\
                        """ + JSON_ARRAY_SINGLETON_0 +"""
                        }""";
    }

    public static class USERNAME {
        public static final UserName MODEL_0 = new UserName(
                ZERO_UUID, "root", "password", true, 0
        );
        public static final UserNameTable TABLE_0 = new UserNameTable(
                ZERO_UUID, "root", "password", null, null, true, true, 0
        );
        public static final Uni<Page<Answer>> UNI_PAGE_ANSWER_SINGLETON_TEST = Uni.createFrom()
                .item(
                        Page.<Answer>builder()
                                .content(Collections.singletonList(Answer.of(MODEL_0)))
                                .build()
                );
        public static final String JSON_0 = """
                {"id":"00000000-0000-0000-0000-000000000000","userName":"root","password":"password","visible":true,"flags":0}\
                """;
        public static final String JSON_ARRAY_SINGLETON_0 = "[" + JSON_0 + "]";
        public static final String JSON_ID_0 = """
                {"id":"00000000-0000-0000-0000-000000000000"}\
                """;
        public static final String JSON_PAGE_ARRAY_0 = """
                        {"pageNumber":0,"totalElements":0,"nextPage":false,"prevPage":false,"content":\
                        """ + JSON_ARRAY_SINGLETON_0 +"""
                        }""";
    }
    public static class VALUE_TYPE {
        public static final ValueType MODEL_0 = new ValueType(
                0L, ValueTypeTable.NONE, true, 0
        );
        public static final ValueTypeTable TABLE_0 = new ValueTypeTable(
                0L, ValueTypeTable.NONE, null, null, null, true, true, 0
        );
        public static final Uni<Page<Answer>> UNI_PAGE_ANSWER_SINGLETON_TEST = Uni.createFrom()
                .item(
                        Page.<Answer>builder()
                                .content(Collections.singletonList(Answer.of(MODEL_0)))
                                .build()
                );
        public static final String JSON_0 = """
                {"id":0,"valueType":"\
                """ + ValueTypeTable.NONE + """
                ","visible":true,"flags":0}\
                """;
        public static final String JSON_ARRAY_SINGLETON_0 = "[" + JSON_0 + "]";
        public static final String JSON_ID_0 = "{\"id\":0}";
        public static final String JSON_PAGE_ARRAY_0 = """
                        {"pageNumber":0,"totalElements":0,"nextPage":false,"prevPage":false,"content":\
                        """ + JSON_ARRAY_SINGLETON_0 +"""
                        }""";
    }

    public static class VOCABULARY {
        public static final Vocabulary MODEL_0 = new Vocabulary(
                0L, VocabularyTable.NONE, null, true, 0
        );
        public static final VocabularyTable TABLE_0 = new VocabularyTable(
                0L, VocabularyTable.NONE, null, null, null, null, true, true, 0
        );
        public static final Uni<Page<Answer>> UNI_PAGE_ANSWER_SINGLETON_TEST = Uni.createFrom()
                .item(
                        Page.<Answer>builder()
                                .content(Collections.singletonList(Answer.of(MODEL_0)))
                                .build()
                );
        public static final String JSON_0 = """
                {"id":0,"word":"\
                """ + VocabularyTable.NONE + """
                ","visible":true,"flags":0}\
                """;
        public static final String JSON_ARRAY_SINGLETON_0 = "[" + JSON_0 + "]";
        public static final String JSON_ID_0 = "{\"id\":0";
        public static final String JSON_PAGE_ARRAY_0 = """
                        {"pageNumber":0,"totalElements":0,"nextPage":false,"prevPage":false,"content":\
                        """ + JSON_ARRAY_SINGLETON_0 +"""
                        }""";
    }

    public static class WORD {
        public static final Word MODEL_0 = new Word(
                Word.NONE, true, 0
        );
        public static final WordTable TABLE_0 = new WordTable(
                WordTable.NONE, null, null, null, true, true, 0
        );
        public static final Uni<Page<Answer>> UNI_PAGE_ANSWER_SINGLETON_TEST = Uni.createFrom()
                .item(
                        Page.<Answer>builder()
                                .content(Collections.singletonList(Answer.of(MODEL_0)))
                                .build()
                );
        public static final Uni<Answer> UNI_ANSWER_API_RESPONSE_NONE_STRING = Uni.createFrom()
                .item(Answer.of(new ApiResponse<>(WordTable.NONE)));
        public static final String JSON_0 = """
                {"word":"\
                """ + WordTable.NONE + """
                ","visible":true,"flags":0}\
                """;
        public static final String JSON_ARRAY_SINGLETON_0 = "[" + JSON_0 + "]";
        public static final String JSON_ID_0 = "{\"id\":\"" + WordTable.NONE + "\"}";
        public static final String JSON_PAGE_ARRAY_0 = """
                        {"pageNumber":0,"totalElements":0,"nextPage":false,"prevPage":false,"content":\
                        """ + JSON_ARRAY_SINGLETON_0 +"""
                        }""";
    }
}
