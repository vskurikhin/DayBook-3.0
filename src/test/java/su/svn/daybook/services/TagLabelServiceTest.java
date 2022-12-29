package su.svn.daybook.services;

import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import su.svn.daybook.TestData;
import su.svn.daybook.domain.dao.TagLabelDao;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.messages.ApiResponse;
import su.svn.daybook.domain.model.TagLabel;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@QuarkusTest
class TagLabelServiceTest {

    @Inject
    TagLabelService service;

    static TagLabelDao mock;

    static final Uni<Optional<TagLabel>> UNI_OPTIONAL_TEST = Uni.createFrom().item(Optional.of(TestData.TAG_LABEL.OBJECT_0));

    static final Multi<TagLabel> MULTI_TEST = Multi.createFrom().item(TestData.TAG_LABEL.OBJECT_0);

    static final Multi<TagLabel> MULTI_WITH_NULL = TestData.createMultiWithNull(TagLabel.class);

    static final Multi<TagLabel> MULTI_EMPTIES = TestData.createMultiEmpties(TagLabel.class);

    @BeforeEach
    void setUp() {
        mock = Mockito.mock(TagLabelDao.class);
        Mockito.when(mock.findById(TestData.TAG_LABEL.ID)).thenReturn(UNI_OPTIONAL_TEST);
        QuarkusMock.installMockForType(mock, TagLabelDao.class);
    }

    @Test
    void testWhenGetAllThenSingletonList() {
        Mockito.when(mock.findAll()).thenReturn(MULTI_TEST);
        List<Answer> result = new ArrayList<>();
        Assertions.assertDoesNotThrow(() -> result.addAll(service.getAll()
                .subscribe()
                .asStream()
                .peek(actual -> Assertions.assertEquals(Answer.of(TestData.TAG_LABEL.OBJECT_0), actual)).toList()));
        Assertions.assertTrue(result.size() > 0);
    }

    @Test
    void testWhenGetAllThenEmpty() {
        Mockito.when(mock.findAll()).thenReturn(MULTI_EMPTIES);
        List<Answer> result = new ArrayList<>();
        Assertions.assertDoesNotThrow(() -> result.addAll(service.getAll()
                .subscribe()
                .asStream()
                .toList()));
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void testWhenGetAllThenNull() {
        Mockito.when(mock.findAll()).thenReturn(MULTI_WITH_NULL);
        List<Answer> result = new ArrayList<>();
        Assertions.assertDoesNotThrow(() -> result.addAll(service.getAll()
                .subscribe()
                .asStream()
                .toList()));
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void testWhenGetThenEntry() {
        Assertions.assertDoesNotThrow(() -> service.get(TestData.TAG_LABEL.ID)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.of(TestData.TAG_LABEL.OBJECT_0), actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenGetThenNullParameter() {
        Assertions.assertDoesNotThrow(() -> service.get(null)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenAddThenId() {
        var expected = Answer.builder()
                .error(201)
                .payload(new ApiResponse<>(TestData.TAG_LABEL.ID))
                .build();
        Mockito.when(mock.insert(TestData.TAG_LABEL.OBJECT_0)).thenReturn(TestData.TAG_LABEL.UNI_OPTIONAL_ID);
        Assertions.assertDoesNotThrow(() -> service.add(TestData.TAG_LABEL.OBJECT_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenAddThenEmpty() {
        Mockito.when(mock.insert(TestData.TAG_LABEL.OBJECT_0)).thenReturn(TestData.UNI_OPTIONAL_EMPTY_STRING);
        Assertions.assertDoesNotThrow(() -> service.add(TestData.TAG_LABEL.OBJECT_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenPutThenId() {
        var expected = Answer.builder()
                .error(202)
                .payload(new ApiResponse<>(TestData.TAG_LABEL.ID))
                .build();
        Mockito.when(mock.update(TestData.TAG_LABEL.OBJECT_0)).thenReturn(TestData.TAG_LABEL.UNI_OPTIONAL_ID);
        Assertions.assertDoesNotThrow(() -> service.put(TestData.TAG_LABEL.OBJECT_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenPutThenEmpty() {
        Mockito.when(mock.update(TestData.TAG_LABEL.OBJECT_0)).thenReturn(TestData.UNI_OPTIONAL_EMPTY_STRING);
        Assertions.assertThrows(RuntimeException.class, () -> service.put(TestData.TAG_LABEL.OBJECT_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenDeleteThenId() {
        Mockito.when(mock.delete(TestData.TAG_LABEL.ID)).thenReturn(TestData.TAG_LABEL.UNI_OPTIONAL_ID);
        var expected = Answer.of(new ApiResponse<>(TestData.TAG_LABEL.ID));
        Assertions.assertDoesNotThrow(() -> service.delete(TestData.TAG_LABEL.ID)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely());
    }

    @Test
    void testWhenDeleteThenNullParameter() {
        Assertions.assertDoesNotThrow(() -> service.delete(null)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely());
    }
}