package su.svn.daybook.services;

import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import su.svn.daybook.DataTest;
import su.svn.daybook.domain.dao.TagLabelDao;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.TagLabel;

import javax.inject.Inject;
import java.util.Optional;

@QuarkusTest
class TagLabelServiceTest {

    @Inject
    TagLabelService service;

    @BeforeAll
    public static void setup() {
        Uni<Optional<TagLabel>> tezd = Uni.createFrom()
                .item(1)
                .onItem()
                .transform(i -> Optional.of(DataTest.TEZD_TagLabel));
        Uni<Answer> empty = Uni.createFrom()
                .item(new Answer("empty", 1));
        Uni<Optional<String>> tezdString = Uni.createFrom().item(Optional.of("tezd"));

        TagLabelDao mock = Mockito.mock(TagLabelDao.class);
        Mockito.when(mock.findById("tezd")).thenReturn(tezd);
        Mockito.when(mock.insert(DataTest.TEZD_TagLabel)).thenReturn(tezdString);
        QuarkusMock.installMockForType(mock, TagLabelDao.class);
    }

    @Test
    void testTagGet() {
        service.tagGet("tezd")
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.of(Optional.of(DataTest.TEZD_TagLabel)), actual))
                .await()
                .indefinitely();
    }

    @Test
    void testNullTagGet() {
        service.tagGet(null)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely();
    }

    @Test
    void testTagAdd() {
        service.tagAdd(DataTest.TEZD_TagLabel)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.of("tezd"), actual));
    }
}