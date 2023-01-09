package su.svn.daybook;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Assertions;
import su.svn.daybook.domain.messages.Answer;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class TestUtils {

    public static <T> Multi<T> createMultiEmpties(Class<T> t) {
        return Multi.createFrom().empty();
    }

    public static <T> Multi<T> createMultiWithNull(Class<T> t) {
        return Multi.createFrom().item(() -> null);
    }

    public static LocalDateTime localDateTime(LocalDateTime localDateTime) {
        return LocalDateTime.of(
                localDateTime.toLocalDate(),
                LocalTime.of(
                        localDateTime.toLocalTime().getHour(),
                        localDateTime.toLocalTime().getMinute(),
                        localDateTime.toLocalTime().getSecond()
                )
        );
    }

    public static <T> List<T> multiAsListHelper(Multi<T> multi) throws Exception {
        return multi.collect().asList().subscribeAsCompletionStage().get();
    }

    public static <T> T uniOptionalHelper(Uni<Optional<T>> uni) throws Exception {
        var result = uni.subscribeAsCompletionStage();
        Assertions.assertNotNull(result);
        return result.get().orElse(null);
    }

    public static Answer uniToAnswerHelper(Uni<Answer> uni) throws Exception {
        var result = uni.subscribeAsCompletionStage();
        Assertions.assertNotNull(result);
        return result.get();
    }
}
