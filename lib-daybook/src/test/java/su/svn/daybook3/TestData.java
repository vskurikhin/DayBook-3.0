/*
 * This file was last modified at 2024-05-14 23:10 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * TestData.java
 * $Id$
 */

package su.svn.daybook3;

import io.smallrye.mutiny.Uni;
import su.svn.daybook3.domain.messages.Answer;
import su.svn.daybook3.domain.messages.ApiResponse;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static su.svn.daybook3.TestUtils.localDateTime;

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

    public static class DURATION {
        public static final Duration TIMEOUT_DURATION = Duration.ofMillis(750);
    }
}
