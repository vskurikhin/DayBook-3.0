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

import java.util.Optional;
import java.util.UUID;

public class TestData {

    public static final String STRING_ZERO_UUID = "00000000-0000-0000-0000-000000000000";

    public static final String NO_SUCH_ELEMENT = "no such element";

    public static final UUID ZERO_UUID = new UUID(0, 0);
    public static final UUID ONE_UUID = new UUID(0, 1);
    public static final UUID RANDOM1_UUID = UUID.randomUUID();
    public static final UUID RANDOM2_UUID = UUID.randomUUID();
    public static final Uni<Optional<Long>> UNI_OPTIONAL_EMPTY_LONG = Uni.createFrom().item(Optional.empty());
    public static final Uni<Optional<String>> UNI_OPTIONAL_EMPTY_STRING = Uni.createFrom().item(Optional.empty());
    public static Uni<Answer> UNI_ANSWER_API_RESPONSE_ZERO_UUID = Uni.createFrom().item(Answer.of(new ApiResponse<>(ZERO_UUID)));
    public static Uni<Optional<UUID>> UNI_OPTIONAL_ZERO_UUID = Uni.createFrom().item(Optional.of(ZERO_UUID));
    public static Uni<Optional<UUID>> UNI_OPTIONAL_EMPTY_UUID = Uni.createFrom().item(Optional.empty());
    public static Answer ANSWER_ERROR_NoNumber = new Answer("For input string: \"noNumber\"", 404);
    public static Answer ANSWER_ERROR_EMPTY = Answer.empty();
    public static Uni<Answer> UNI_ANSWER_EMPTY = Uni.createFrom().item(Answer.empty());
    public static Uni<Answer> UNI_ANSWER_NULL = Uni.createFrom().item(() -> null);
    public static Uni<Answer> UNI_ANSWER_API_RESPONSE_ZERO_LONG = Uni.createFrom().item(Answer.of(new ApiResponse<>(0L)));
    public static Uni<Optional<Long>> UNI_OPTIONAL_ZERO_LONG = Uni.createFrom().item(Optional.of(0L));

    public static <T> Multi<T> createMultiEmpties(Class<T> t) {
        return Multi.createFrom().empty();
    }

    public static <T> Multi<T> createMultiWithNull(Class<T> t) {
        return Multi.createFrom().item(() -> null);
    }

    public static class CODIFIER {
        public static final Codifier OBJECT_0 = new Codifier(
                Codifier.NONE, null, null, null, null, false, true, 0
        );
        public static final String JSON_0 = """
                {"code":"\
                """ + Codifier.NONE + """
                ","enabled":false,"visible":true,"flags":0}\
                """;
        public static final String JSON_ARRAY_SINGLETON_0 = "[" + JSON_0 + "]";
        public static final String JSON_ID_0 = "{\"id\":\"" + Codifier.NONE + "\"}";
    }

    public static class I18N {
        public static final I18n OBJECT_0 = new I18n(
                0L, 0L, null, null, null, null, null, false, true, 0
        );
        public static final String JSON_0 = """
                {"id":0,"languageId":0,"enabled":false,"visible":true,"flags":0}\
                """;
        public static final String JSON_ARRAY_SINGLETON_0 = "[" + JSON_0 + "]";
        public static final String JSON_ID_0 = "{\"id\":0}";
    }

    public static class LANGUAGE {
        public static final Language OBJECT_0 = new Language(
                0L, null, null, null, null, false, true, 0
        );
        public static final String JSON_0 = """
                {"id":0,"enabled":false,"visible":true,"flags":0}\
                """;
        public static final String JSON_ARRAY_SINGLETON_0 = "[" + JSON_0 + "]";
        public static final String JSON_ID_0 = "{\"id\":0}";
    }

    public static class TAG_LABEL {
        public static final String ID = TagLabel.NONE.replace("-", "").substring(0, 16);
        public static final Uni<Optional<String>> UNI_OPTIONAL_ID = Uni.createFrom().item(Optional.of(ID));
        public static final TagLabel OBJECT_0 = new TagLabel(
                ID, TagLabel.NONE, null, null, null, false, true, 0
        );
        public static final String JSON_0 = """
                {"id":"\
                """ + ID + """
                ","label":"\
                """ + TagLabel.NONE + """
                ","enabled":false,"visible":true,"flags":0}\
                """;
        public static final String JSON_ARRAY_SINGLETON_0 = "[" + JSON_0 + "]";
        public static Uni<Answer> UNI_ANSWER_API_RESPONSE_ID = Uni.createFrom().item(Answer.of(new ApiResponse<>(ID)));
    }

    public static class USERNAME {
        public static final UserName OBJECT_0 = new UserName(
                ZERO_UUID, "root", "password", null, null, false, true, 0
        );
        public static final String JSON_0 = """
                {"id":"00000000-0000-0000-0000-000000000000","userName":"root","password":"password","enabled":false,"visible":true,"flags":0}\
                """;
        public static final String JSON_ARRAY_SINGLETON_0 = "[" + JSON_0 + "]";
        public static final String JSON_ID_0 = """
                {"id":"00000000-0000-0000-0000-000000000000"}\
                """;
    }
    public static class VALUE_TYPE {
        public static final ValueType OBJECT_0 = new ValueType(
                0L, ValueType.NONE, null, null, null, false, true, 0
        );
        public static final String JSON_0 = """
                {"id":0,"valueType":"\
                """ + ValueType.NONE + """
                ","enabled":false,"visible":true,"flags":0}\
                """;
        public static final String JSON_ARRAY_SINGLETON_0 = "[" + JSON_0 + "]";
        public static final String JSON_ID_0 = "{\"id\":0}";
    }

    public static class VOCABULARY {
        public static final Vocabulary OBJECT_0 = new Vocabulary(
                0L, Vocabulary.NONE, null, null, null, null, false, true, 0
        );
        public static final String JSON_0 = """
                {"id":0,"word":"\
                """ + Vocabulary.NONE + """
                ","enabled":false,"visible":true,"flags":0}\
                """;
        public static final String JSON_ARRAY_SINGLETON_0 = "[" + JSON_0 + "]";
        public static final String JSON_ID_0 = "{\"id\":0";
    }

    public static class WORD {
        public static final Word OBJECT_0 = new Word(
                Word.NONE, null, null, null, false, true, 0
        );
        public static final String JSON_0 = """
                {"word":"\
                """ + Word.NONE + """
                ","enabled":false,"visible":true,"flags":0}\
                """;
        public static final String JSON_ARRAY_SINGLETON_0 = "[" + JSON_0 + "]";
        public static final String JSON_ID_0 = "{\"id\":\"" + Word.NONE + "\"}";
    }
}
