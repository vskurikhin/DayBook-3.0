package su.svn.daybook.services;

import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Multi;
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

    static TagLabelDao mock;

    @BeforeAll
    public static void setup() {
        Uni<Optional<TagLabel>> tezd = Uni.createFrom()
                .item(Optional.of(DataTest.TEZD_TagLabel));
        Uni<Answer> empty = Uni.createFrom()
                .item(new Answer("empty", 1));
        Uni<Optional<String>> tezdString = Uni.createFrom().item(Optional.of("tezd"));
        Multi<TagLabel> tezds = Multi.createFrom()
                .item(DataTest.TEZD_TagLabel);
                //.item(DataTest.TEZD_TagLabel);

        mock = Mockito.mock(TagLabelDao.class);
        Mockito.when(mock.findById("tezd")).thenReturn(tezd);
        Mockito.when(mock.insert(DataTest.TEZD_TagLabel)).thenReturn(tezdString);
        Mockito.when(mock.findAll()).thenReturn(tezds);
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
                .invoke(actual -> Assertions.assertEquals(Answer.of("tezd"), actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_getAll() {
        service.getAll()
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.of(DataTest.TEZD_TagLabel), actual))
                .toUni()
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_getAll_return_null() {
        Mockito.when(mock.findAll()).thenReturn(Multi.createFrom().empty());
        service.getAll()
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.of(DataTest.TEZD_TagLabel), actual))
                .toUni()
                .await()
                .indefinitely();
    }
}