/*
 * This file was last modified at 2024-05-14 23:10 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * TestData.java
 * $Id$
 */

package su.svn.daybook3.api;

import io.smallrye.mutiny.Uni;
import org.jose4j.jwt.JwtClaims;
import su.svn.daybook3.api.domain.entities.BaseRecord;
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
import su.svn.daybook3.api.models.domain.Codifier;
import su.svn.daybook3.api.models.domain.I18n;
import su.svn.daybook3.api.models.domain.KeyValue;
import su.svn.daybook3.api.models.domain.Language;
import su.svn.daybook3.api.models.domain.Setting;
import su.svn.daybook3.api.models.domain.Stanza;
import su.svn.daybook3.api.models.domain.TagLabel;
import su.svn.daybook3.api.models.domain.ValueType;
import su.svn.daybook3.api.models.domain.Vocabulary;
import su.svn.daybook3.api.models.domain.Word;
import su.svn.daybook3.api.models.dto.ResourceBaseRecord;
import su.svn.daybook3.api.models.pagination.Page;
import su.svn.daybook3.api.models.pagination.PageRequest;
import su.svn.daybook3.api.models.security.SessionPrincipal;
import su.svn.daybook3.domain.messages.Answer;
import su.svn.daybook3.domain.messages.ApiResponse;
import su.svn.daybook3.domain.messages.Request;

import java.math.BigInteger;
import java.security.Principal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static su.svn.daybook3.api.TestUtils.localDateTime;

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
        public static Uni<List<Long>> UNI_LIST_EMPTY = Uni.createFrom().item(Collections.emptyList());
        public static Uni<List<Long>> UNI_SINGLETON_LIST_ZERO = Uni.createFrom().item(Collections.singletonList(0L));
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
        public static Uni<Answer> UNI_ANSWER_API_RESPONSE_ZERO_201 = Uni.createFrom().item(Answer.from(new ApiResponse<>(uuid.ZERO, 201), 201));
        public static Uni<Optional<UUID>> UNI_OPTIONAL_EMPTY = Uni.createFrom().item(Optional.empty());
        public static Uni<Optional<UUID>> UNI_OPTIONAL_ZERO = Uni.createFrom().item(Optional.of(ZERO));
        public static Uni<UUID> UNI_ZERO = Uni.createFrom().item(ZERO);
    }

    public static class BASE_RECORD {
        public static final ResourceBaseRecord MODEL_0 = ResourceBaseRecord.builder().id(uuid.ZERO).visible(true).build();
        public static final BaseRecord TABLE_0 = new BaseRecord();
        public static final Uni<Page<Answer>> UNI_PAGE_ANSWER_SINGLETON_TEST = Uni.createFrom()
                .item(
                        Page.<Answer>builder()
                                .content(Collections.singletonList(Answer.of(MODEL_0)))
                                .build()
                );
        public static final String JSON_0 = """
                {"id":"\
                """ + uuid.ZERO + """
                ","visible":true,"flags":0}\
                """;
        public static final String JSON_NEW = """
                {"visible":true,"flags":0}\
                """;
        public static final String JSON_ID_0_200 = "{\"id\":\"00000000-0000-0000-0000-000000000000\",\"code\":200}";
        public static final String JSON_ID_0_201 = "{\"id\":\"00000000-0000-0000-0000-000000000000\",\"code\":201}";
        public static final String JSON_ARRAY_SINGLETON_0 = "[" + JSON_0 + "]";
        public static final String JSON_PAGE_ARRAY_0 = """
                {"page":0,"totalRecords":0,"nextPage":false,"prevPage":false,"content":\
                """ + JSON_ARRAY_SINGLETON_0 + """
                }""";

        static {
            TABLE_0.id(uuid.ZERO);
        }
    }

    public static class CODIFIER {
        public static final Codifier MODEL_0 = new Codifier(
                CodifierTable.NONE, null, true, 0
        );
        public static final CodifierTable TABLE_0 = new CodifierTable(
                CodifierTable.NONE, null, null, null, null, true, true, true, 0
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

    public static class DURATION {
        public static final Duration TIMEOUT_DURATION = Duration.ofMillis(750);
    }

    public static class I18N {
        public static final I18n MODEL_0 = new I18n(
                0L, Language.NONE, I18n.NONE, null, true, 0
        );
        public static final I18nTable TABLE_0 = new I18nTable(
                0L, 0L, I18n.NONE, null, null, null, null, true, true, true, 0
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
                uuid.ZERO, BigInteger.ZERO, null, null, null, null, true, true, true, 0
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
                0L, LanguageTable.NONE, null, null, null, true, true, true, 0
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

    public static class PRINCIPAL {
        public static final Principal EMPTY_SET = new SessionPrincipal(
                null, new JwtClaims(), null, Collections.emptySet(), null
        );
    }

    public static class SETTING {
        public static final Setting MODEL_0 = new Setting(
                0L, SettingTable.NONE, null, Setting.DEFAULT_TYPE, 0L, true, 0
        );
        public static final SettingTable TABLE_0 = new SettingTable(
                0L, SettingTable.NONE, null, 0L, 0L, null, null, null, true, true, true, 0
        );
        public static final SettingView VIEW_0 = new SettingView(
                0L, SettingTable.NONE, null, Setting.DEFAULT_TYPE, 0L, null, null, null, true, true, 0
        );
        public static final Uni<Page<Answer>> UNI_PAGE_ANSWER_SINGLETON_TEST = Uni.createFrom()
                .item(
                        Page.<Answer>builder()
                                .content(Collections.singletonList(Answer.of(MODEL_0)))
                                .build()
                );
        public static final String JSON_0 = """
                {"id":0,"key":"\
                """ + SettingTable.NONE + """
                ","valueTypeId":0,"visible":true,"flags":0}\
                """;
        public static final String JSON_ARRAY_SINGLETON_0 = "[" + JSON_0 + "]";
        public static final String JSON_ID_0 = "{\"id\":0}";
        public static final String JSON_PAGE_ARRAY_0 = """
                {"page":0,"totalRecords":0,"nextPage":false,"prevPage":false,"content":\
                """ + JSON_ARRAY_SINGLETON_0 + """
                }""";
    }

    public static class STANZA {
        public static final Stanza MODEL_0 = new Stanza(
                0L, StanzaTable.NONE, null, Stanza.ROOT, Collections.emptySet(), true, 0
        );
        public static final StanzaTable TABLE_0 = new StanzaTable(
                0L, StanzaTable.NONE, null, 0L, null, null, null, true, true, true, 0
        );
        public static final StanzaView VIEW_0 = new StanzaView(
                0L, StanzaTable.NONE, null, StanzaTable.ROOT, Collections.emptySet(), null, null, null, true, true, 0
        );
        public static final Uni<Page<Answer>> UNI_PAGE_ANSWER_SINGLETON_TEST = Uni.createFrom()
                .item(
                        Page.<Answer>builder()
                                .content(Collections.singletonList(Answer.of(MODEL_0)))
                                .build()
                );
        public static final String JSON_0 = """
                {"id":0,"key":"\
                """ + StanzaTable.NONE + """
                ","valueTypeId":0,"visible":true,"flags":0}\
                """;
        public static final String JSON_ARRAY_SINGLETON_0 = "[" + JSON_0 + "]";
        public static final String JSON_ID_0 = "{\"id\":0}";
        public static final String JSON_PAGE_ARRAY_0 = """
                {"page":0,"totalRecords":0,"nextPage":false,"prevPage":false,"content":\
                """ + JSON_ARRAY_SINGLETON_0 + """
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
                ID, TagLabelTable.NONE, null, null, null, true, true, true, 0
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

    public static class VALUE_TYPE {
        public static final ValueType MODEL_0 = new ValueType(
                0L, ValueTypeTable.NONE, true, 0
        );
        public static final ValueTypeTable TABLE_0 = new ValueTypeTable(
                0L, ValueTypeTable.NONE, null, null, null, true, true, true, 0
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
        public static final String JSON_PAGE_ARRAY_0 = """
                {"page":0,"totalRecords":0,"nextPage":false,"prevPage":false,"content":\
                """ + JSON_ARRAY_SINGLETON_0 + """
                }""";
        public static final String JSON_ID_0 = "{\"id\":0";
    }

    public static class VOCABULARY {
        public static final Vocabulary MODEL_0 = new Vocabulary(
                0L, VocabularyTable.NONE, null, true, 0
        );
        public static final VocabularyTable TABLE_0 = new VocabularyTable(
                0L, VocabularyTable.NONE, null, null, null, null, true, true, true, 0
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
                WordTable.NONE, null, null, null, true, true, true, 0
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
