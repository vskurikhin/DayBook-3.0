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
import su.svn.daybook.domain.dao.TagLabelDao;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.messages.ApiResponse;
import su.svn.daybook.domain.model.TagLabel;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@QuarkusTest
class TagLabelServiceTest {

    @Inject
    TagLabelService service;

    static TagLabelDao mock;

    static Uni<Optional<TagLabel>> optionalUniTest = Uni.createFrom().item(Optional.of(DataTest.OBJECT_TagLabel_0));

    static Uni<Optional<String>> optionalUniId = Uni.createFrom().item(Optional.of("test"));

    static Uni<Optional<String>> optionalUniEmptyId = Uni.createFrom().item(Optional.empty());

    static Multi<TagLabel> multiTest = Multi.createFrom().item(DataTest.OBJECT_TagLabel_0);

    static Multi<TagLabel> multiEmpties = Multi.createFrom().empty();

    static Multi<TagLabel> multiWithNull = Multi.createFrom().item(() -> null);

    @BeforeAll
    public static void setup() {
        mock = Mockito.mock(TagLabelDao.class);
        Mockito.when(mock.findById("test")).thenReturn(optionalUniTest);
        QuarkusMock.installMockForType(mock, TagLabelDao.class);
    }

    @Test
    void testMethod_getAll() {
        Mockito.when(mock.findAll()).thenReturn(multiTest);
        List<Answer> result = service.getAll()
                .subscribe()
                .asStream()
                .peek(actual -> Assertions.assertEquals(Answer.of(DataTest.OBJECT_TagLabel_0), actual))
                .collect(Collectors.toList());
        Assertions.assertTrue(result.size() > 0);
    }


    @Test
    void testMethod_getAll_whithEmptyResult() {
        Mockito.when(mock.findAll()).thenReturn(multiEmpties);
        List<Answer> result = service.getAll()
                .subscribe()
                .asStream()
                .collect(Collectors.toList());
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void testMethod_getAll_whithNullResult() {
        Mockito.when(mock.findAll()).thenReturn(multiWithNull);
        List<Answer> result = service.getAll()
                .subscribe()
                .asStream()
                .collect(Collectors.toList());
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void testMethod_tagGet() {
        service.tagGet("test")
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.of(Optional.of(DataTest.OBJECT_TagLabel_0)), actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_tagGet_whenNullParameter() {
        service.tagGet(null)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_tagAdd() {
        var expected = Answer.of(new ApiResponse<>("test"));
        Mockito.when(mock.insert(DataTest.OBJECT_TagLabel_0)).thenReturn(optionalUniId);
        service.tagAdd(DataTest.OBJECT_TagLabel_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_tagAdd_whithEmptyResult() {
        Mockito.when(mock.insert(DataTest.OBJECT_TagLabel_0)).thenReturn(optionalUniEmptyId);
        service.tagAdd(DataTest.OBJECT_TagLabel_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_tagPut() {
        Mockito.when(mock.update(DataTest.OBJECT_TagLabel_0)).thenReturn(optionalUniId);
        var expected = Answer.of(new ApiResponse<>("test"));
        service.tagPut(DataTest.OBJECT_TagLabel_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_tagPut_whithEmptyResult() {
        Mockito.when(mock.update(DataTest.OBJECT_TagLabel_0)).thenReturn(optionalUniEmptyId);
        service.tagPut(DataTest.OBJECT_TagLabel_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_tagDelete() {
        Mockito.when(mock.delete("test")).thenReturn(optionalUniId);
        var expected = Answer.of(new ApiResponse<>("test"));
        service.tagDelete("test")
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_tagDelete_whenNullParameter() {
        service.tagDelete(null)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely();
    }
}