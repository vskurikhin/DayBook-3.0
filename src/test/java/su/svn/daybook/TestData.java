/*
 * This file was last modified at 2023.09.07 14:07 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * TestData.java
 * $Id$
 */

package su.svn.daybook;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.messages.ApiResponse;
import su.svn.daybook.domain.messages.Request;
import su.svn.daybook.domain.model.*;
import su.svn.daybook.models.domain.*;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static su.svn.daybook.TestUtils.localDateTime;

public class TestData {

    public static final String NO_SUCH_ELEMENT = "no such element";

    public static final Uni<Optional<Long>> UNI_OPTIONAL_EMPTY_LONG = Uni.createFrom().item(Optional.empty());
    public static final Uni<Optional<String>> UNI_OPTIONAL_EMPTY_STRING = Uni.createFrom().item(Optional.empty());
    public static Uni<Answer> UNI_ANSWER_API_RESPONSE_ZERO_UUID = Uni.createFrom()
            .item(Answer.of(new ApiResponse<>(uuid.ZERO)));
    public static Uni<Answer> UNI_ANSWER_EMPTY = Uni.createFrom().item(Answer.empty());
    public static Uni<Answer> UNI_ANSWER_NULL = Uni.createFrom().item(() -> null);
    public static Uni<Answer> UNI_ANSWER_API_RESPONSE_ZERO_LONG = Uni.createFrom().item(Answer.of(new ApiResponse<>(0L)));
    public static Uni<Optional<Long>> UNI_OPTIONAL_ZERO_LONG = Uni.createFrom().item(Optional.of(0L));

    public static Uni<Optional<Long>> UNI_OPTIONAL_ONE_LONG = Uni.createFrom().item(Optional.of(1L));
    public static Uni<Optional<Long>> UNI_OPTIONAL_MINUS_ONE_LONG = Uni.createFrom().item(Optional.of(-1L));
    public static Uni<Page<Answer>> UNI_PAGE_ANSWER_EMPTY = Uni
            .createFrom()
            .item(Page.<Answer>builder()
                    .page(0)
                    .rows((short) 0)
                    .totalPages(0L)
                    .totalRecords(0L)
                    .content(Collections.emptyList())
                    .build());

    public static final String JSON_AUTH_LOGIN = """
            {
               "username": "root",
               "password": "password"
            }""";

    public static class lng {
        public static final long RANDOM1 = new Random(System.currentTimeMillis() - 1).nextLong();
        public static final long RANDOM2 = new Random(System.currentTimeMillis() + 2).nextLong();
        public static Uni<Answer> UNI_ANSWER_API_RESPONSE_ZERO = Uni.createFrom()
                .item(Answer.of(new ApiResponse<>(Long.valueOf(0), 200)));
        public static Uni<Optional<Long>> UNI_OPTIONAL_EMPTY = Uni.createFrom().item(Optional.empty());
        public static Uni<Optional<Long>> UNI_OPTIONAL_ZERO = Uni.createFrom().item(Optional.of(0L));
    }

    public static class request {
        public static final PageRequest PAGE_REQUEST = new PageRequest(0, (short) 1);
        public static final Request<PageRequest> REQUEST_4 = new Request<>(PAGE_REQUEST, null);
        public static final Request<Long> LONG_REQUEST_0 = new Request<>(0L, null);
        public static final Request<Long> LONG_REQUEST_1 = new Request<>(1L, null);
        public static final Request<Long> LONG_REQUEST_2 = new Request<>(2L, null);
        public static final Request<Long> LONG_REQUEST_3 = new Request<>(3L, null);
        public static final Request<String> STRING_REQUEST_0 = new Request<>(String.valueOf(0), null);
        public static final Request<String> STRING_REQUEST_1 = new Request<>(String.valueOf(1), null);
        public static final Request<String> STRING_REQUEST_2 = new Request<>(String.valueOf(2), null);
        public static final Request<String> STRING_REQUEST_3 = new Request<>(String.valueOf(3), null);
        public static final Request<UUID> UUID_REQUEST_0 = new Request<>(uuid.ZERO, null);
        public static final Request<UUID> UUID_REQUEST_1 = new Request<>(uuid.ONE, null);
        public static final Request<UUID> UUID_REQUEST_2 = new Request<>(uuid.RANDOM1, null);
        public static final Request<UUID> UUID_REQUEST_3 = new Request<>(uuid.RANDOM2, null);
    }

    public static class string {
        public static final Uni<Optional<String>> UNI_OPTIONAL_STRING_ZERO_UUID = Uni.createFrom().item(Optional.of(
                uuid.STRING_ZERO
        ));
    }

    public static class time {
        public static final LocalDateTime NOW = localDateTime(LocalDateTime.now());
        public static final LocalDateTime MAX = localDateTime(LocalDateTime.MAX);
        public static final LocalDateTime MIN = localDateTime(LocalDateTime.MIN);
        public static final LocalDateTime EPOCH_TIME = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
    }

    public static class uuid {
        public static final String STRING_ZERO = "00000000-0000-0000-0000-000000000000";
        public static final String STRING_ONE = "00000000-0000-0000-0000-000000000001";
        public static final String STRING_TWO = "00000000-0000-0000-0000-000000000002";
        public static final String STRING_TEN = "00000000-0000-0000-0000-000000000010";
        public static final UUID ZERO = new UUID(0, 0);
        public static final UUID ONE = new UUID(0, 1);
        public static final UUID RANDOM1 = UUID.randomUUID();
        public static final UUID RANDOM2 = UUID.randomUUID();
        public static Uni<Answer> UNI_ANSWER_API_RESPONSE_ZERO = Uni.createFrom().item(Answer.of(new ApiResponse<>(uuid.ZERO, 200)));
        public static Uni<Optional<UUID>> UNI_OPTIONAL_EMPTY = Uni.createFrom().item(Optional.empty());
        public static Uni<Optional<UUID>> UNI_OPTIONAL_ZERO = Uni.createFrom().item(Optional.of(ZERO));
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
        public static final String JSON_ID_0 = "{\"code\":\"" + CodifierTable.NONE + "\"}";
        public static final String JSON_PAGE_ARRAY_0 = """
                {"page":0,"totalRecords":0,"nextPage":false,"prevPage":false,"content":\
                """ + JSON_ARRAY_SINGLETON_0 + """
                }""";
    }

    public static class I18N {
        public static final I18n MODEL_0 = new I18n(
                0L, Language.NONE, I18n.NONE, null, true, 0
        );
        public static final I18nTable TABLE_0 = new I18nTable(
                0L, 0L, I18n.NONE, null, null, null, null, true, true, 0
        );
        public static final I18nView VIEW_0 = new I18nView(
                0L, Language.NONE, I18n.NONE, null, null, null, null, true, true, 0
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
                {"page":0,"totalRecords":0,"nextPage":false,"prevPage":false,"content":\
                """ + JSON_ARRAY_SINGLETON_0 + """
                }""";
    }

    public static class KEY_VALUE {
        public static final KeyValue MODEL_0 = new KeyValue(
                uuid.ZERO, BigInteger.ZERO, null, true, 0
        );
        public static final KeyValueTable TABLE_0 = new KeyValueTable(
                uuid.ZERO, BigInteger.ZERO, null, null, null, null, true, true, 0
        );
        public static final Uni<Page<Answer>> UNI_PAGE_ANSWER_SINGLETON_TEST = Uni.createFrom()
                .item(
                        Page.<Answer>builder()
                                .content(Collections.singletonList(Answer.of(MODEL_0)))
                                .build()
                );
        public static final String JSON_0 = """
                {"id":"\
                """ + uuid.ZERO + """
                ","key":0,"visible":true,"flags":0}\
                """;
        public static final String JSON_ARRAY_SINGLETON_0 = "[" + JSON_0 + "]";
        public static final String JSON_ID_0 = "{\"id\":\"" + uuid.ZERO + "\"}";
        public static final String JSON_ID_0_200 = "{\"id\":\"00000000-0000-0000-0000-000000000000\",\"code\":200}";
        public static final String JSON_PAGE_ARRAY_0 = """
                {"page":0,"totalRecords":0,"nextPage":false,"prevPage":false,"content":\
                """ + JSON_ARRAY_SINGLETON_0 + """
                }""";
    }

    public static class LANGUAGE {
        public static final Language MODEL_0 = new Language(
                0L, Language.NONE, true, 0
        );
        public static final LanguageTable TABLE_0 = new LanguageTable(
                0L, LanguageTable.NONE, null, null, null, true, true, 0
        );
        public static final Uni<Page<Answer>> UNI_PAGE_ANSWER_SINGLETON_TEST = Uni.createFrom()
                .item(
                        Page.<Answer>builder()
                                .content(Collections.singletonList(Answer.of(MODEL_0)))
                                .build()
                );
        public static final String JSON_0 = """
                {"id":0,"language":"\
                """ + Language.NONE + """
                ","visible":true,"flags":0}\
                """;
        public static final String JSON_ARRAY_SINGLETON_0 = "[" + JSON_0 + "]";
        public static final String JSON_ID_0 = "{\"id\":0";
        public static final String JSON_PAGE_ARRAY_0 = """
                {"page":0,"totalRecords":0,"nextPage":false,"prevPage":false,"content":\
                """ + JSON_ARRAY_SINGLETON_0 + """
                }""";
    }

    public static class ROLE {
        public static final Role MODEL_0 = new Role(
                uuid.ZERO, Role.NONE, null, true, 0
        );
        public static final RoleTable TABLE_0 = new RoleTable(
                uuid.ZERO, Role.NONE, null, null, null, null, true, true, 0
        );
        public static final Uni<Page<Answer>> UNI_PAGE_ANSWER_SINGLETON_TEST = Uni.createFrom()
                .item(
                        Page.<Answer>builder()
                                .content(Collections.singletonList(Answer.of(MODEL_0)))
                                .build()
                );
        public static final String JSON_0 = """
                {"id":"00000000-0000-0000-0000-000000000000","role":"\
                """ + RoleTable.NONE + """
                ","visible":true,"flags":0}\
                """;
        public static final String JSON_ARRAY_SINGLETON_0 = "[" + JSON_0 + "]";
        public static final String JSON_ID_0 = "{\"id\":\"00000000-0000-0000-0000-000000000000\"}";
        public static final String JSON_PAGE_ARRAY_0 = """
                {"page":0,"totalRecords":0,"nextPage":false,"prevPage":false,"content":\
                """ + JSON_ARRAY_SINGLETON_0 + """
                }""";
    }

    public static class SESSION {
        public static final Session MODEL_0 = new Session(
                uuid.ZERO, Session.NONE, Collections.emptySet(), time.EPOCH_TIME, true, 0
        );
        public static final SessionTable TABLE_0 = new SessionTable(
                uuid.ZERO, SessionTable.NONE, Collections.emptySet(), time.EPOCH_TIME, null, null, true, true, 0
        );
        public static final Uni<Page<Answer>> UNI_PAGE_ANSWER_SINGLETON_TEST = Uni.createFrom()
                .item(
                        Page.<Answer>builder()
                                .content(Collections.singletonList(Answer.of(MODEL_0)))
                                .build()
                );
        public static final String JSON_0 = """
                {"id":"\
                """ + uuid.ZERO + """
                ","key":0,"visible":true,"flags":0}\
                """;
        public static final String JSON_ARRAY_SINGLETON_0 = "[" + JSON_0 + "]";
        public static final String JSON_ID_0 = "{\"id\":\"" + uuid.ZERO + "\"}";
        public static final String JSON_ID_0_200 = "{\"id\":\"00000000-0000-0000-0000-000000000000\",\"error\":200}";
        public static final String JSON_PAGE_ARRAY_0 = """
                {"page":0,"totalRecords":0,"nextPage":false,"prevPage":false,"content":\
                """ + JSON_ARRAY_SINGLETON_0 + """
                }""";
    }

//    public static class SETTING {
//        public static final Setting MODEL_0 = new Setting(
//                0L, SettingTable.NONE, null, 0L, true, 0
//        );
//        public static final SettingTable TABLE_0 = new SettingTable(
//                0L, SettingTable.NONE, null, 0L, null, null, null, true, true, 0
//        );
//        public static final Uni<Page<Answer>> UNI_PAGE_ANSWER_SINGLETON_TEST = Uni.createFrom()
//                .item(
//                        Page.<Answer>builder()
//                                .content(Collections.singletonList(Answer.of(MODEL_0)))
//                                .build()
//                );
//        public static final String JSON_0 = """
//                {"id":0,"key":"\
//                """ + SettingTable.NONE + """
//                ","valueTypeId":0,"visible":true,"flags":0}\
//                """;
//        public static final String JSON_ARRAY_SINGLETON_0 = "[" + JSON_0 + "]";
//        public static final String JSON_ID_0 = "{\"id\":0}";
//        public static final String JSON_PAGE_ARRAY_0 = """
//                {"page":0,"totalRecords":0,"nextPage":false,"prevPage":false,"content":\
//                """ + JSON_ARRAY_SINGLETON_0 + """
//                }""";
//    }

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
        public static final String JSON_ID_0 = "{\"id\":0}";
        public static final String JSON_ARRAY_SINGLETON_0 = "[" + JSON_0 + "]";
        public static final String JSON_PAGE_ARRAY_0 = """
                {"page":0,"totalRecords":0,"nextPage":false,"prevPage":false,"content":\
                """ + JSON_ARRAY_SINGLETON_0 + """
                }""";
    }

    public static class USER {
        public static class NAME {

            public static final UserName MODEL_0 = new UserName(
                    uuid.ZERO, "root", "password", true, 0
            );
            public static final Uni<Page<Answer>> UNI_PAGE_ANSWER_SINGLETON_TEST = Uni.createFrom()
                    .item(
                            Page.<Answer>builder()
                                    .content(Collections.singletonList(Answer.of(MODEL_0)))
                                    .build()
                    );
            public static final UserNameTable TABLE_0 = new UserNameTable(
                    uuid.ZERO, "root", "password", null, null, true, true, 0
            );
        }

        public static class VIEW {
            public static final UserView TABLE_0 = new UserView(
                    uuid.ZERO, "guest", "password", Collections.emptySet(), null, null, true, true, 0
            );
        }

        public static final su.svn.daybook.models.domain.User MODEL_0 = new su.svn.daybook.models.domain.User(
                uuid.ZERO, "guest", null, Collections.emptySet(), true, 0
        );
        public static final Uni<Page<Answer>> UNI_PAGE_ANSWER_SINGLETON_TEST = Uni.createFrom()
                .item(
                        Page.<Answer>builder()
                                .content(Collections.singletonList(Answer.of(MODEL_0)))
                                .build()
                );

        public static final String JSON_0 = """
                {"id":"00000000-0000-0000-0000-000000000000","userName":"guest","roles":[],"visible":true,"flags":0}\
                """;
        public static final String JSON_0_1 = """
                {"id":"00000000-0000-0000-0000-000000000000","userName":"guest","password":null,"roles":[],"visible":true,"flags":0}\
                """;
        public static final String JSON_ARRAY_SINGLETON_0 = "[" + JSON_0 + "]";
        public static final String JSON_ID_0 = """
                {"id":"00000000-0000-0000-0000-000000000000"}\
                """;
        public static final String JSON_PAGE_ARRAY_0 = """
                {"page":0,"totalRecords":0,"nextPage":false,"prevPage":false,"content":\
                """ + JSON_ARRAY_SINGLETON_0 + """
                }""";
    }

//    public static class VALUE_TYPE {
//        public static final ValueType MODEL_0 = new ValueType(
//                0L, ValueTypeTable.NONE, true, 0
//        );
//        public static final ValueTypeTable TABLE_0 = new ValueTypeTable(
//                0L, ValueTypeTable.NONE, null, null, null, true, true, 0
//        );
//        public static final Uni<Page<Answer>> UNI_PAGE_ANSWER_SINGLETON_TEST = Uni.createFrom()
//                .item(
//                        Page.<Answer>builder()
//                                .content(Collections.singletonList(Answer.of(MODEL_0)))
//                                .build()
//                );
//        public static final String JSON_0 = """
//                {"id":0,"valueType":"\
//                """ + ValueTypeTable.NONE + """
//                ","visible":true,"flags":0}\
//                """;
//        public static final String JSON_ARRAY_SINGLETON_0 = "[" + JSON_0 + "]";
//        public static final String JSON_ID_0 = "{\"id\":0}";
//        public static final String JSON_PAGE_ARRAY_0 = """
//                {"page":0,"totalRecords":0,"nextPage":false,"prevPage":false,"content":\
//                """ + JSON_ARRAY_SINGLETON_0 + """
//                }""";
//    }

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
                {"page":0,"totalRecords":0,"nextPage":false,"prevPage":false,"content":\
                """ + JSON_ARRAY_SINGLETON_0 + """
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
                {"page":0,"totalRecords":0,"nextPage":false,"prevPage":false,"content":\
                """ + JSON_ARRAY_SINGLETON_0 + """
                }""";
    }
}
