package su.svn.daybook.services;

import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.CompositeException;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import su.svn.daybook.DataTest;
import su.svn.daybook.domain.dao.UserNameDao;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.messages.ApiResponse;
import su.svn.daybook.domain.model.UserName;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@QuarkusTest
class UserNameServiceTest {

    @Inject
    UserNameService service;

    static UserNameDao mock;

    static Uni<Optional<UserName>> uniOptionalTest = Uni.createFrom().item(Optional.of(DataTest.OBJECT_UserName_0));

    static Multi<UserName> multiTest = Multi.createFrom().item(DataTest.OBJECT_UserName_0);

    static Multi<UserName> multiEmpties = Multi.createFrom().empty();

    @BeforeEach
    void setUp() {
        mock = Mockito.mock(UserNameDao.class);
        Mockito.when(mock.findById(DataTest.ZERO_UUID)).thenReturn(uniOptionalTest);
        QuarkusMock.installMockForType(mock, UserNameDao.class);
    }

    @Test
    void testWhenGetAllThenSingletonList() {
        Mockito.when(mock.findAll()).thenReturn(multiTest);
        List<Answer> result = service.getAll()
                .subscribe()
                .asStream()
                .peek(actual -> Assertions.assertEquals(Answer.of(DataTest.OBJECT_UserName_0), actual))
                .toList();
        Assertions.assertTrue(result.size() > 0);
    }

    @Test
    void testWhenGetAllThenEmpty() {
        Mockito.when(mock.findAll()).thenReturn(multiEmpties);
        List<Answer> result = service.getAll()
                .subscribe()
                .asStream()
                .toList();
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void testWhenGetThenEntry() {
        service.get(DataTest.ZERO_UUID)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.of(DataTest.OBJECT_UserName_0), actual))
                .await()
                .indefinitely();
    }

    @Test
    void testWhenGetWithStringThenEntry() {
        service.get(DataTest.STRING_ZERO_UUID)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.of(DataTest.OBJECT_UserName_0), actual))
                .await()
                .indefinitely();
    }

    @Test
    void testWhenGetThenNull() {
        service.get(null)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely();
    }

    @Test
    void testWhenAddThenId() {
        var expected = Answer.builder()
                .error(201)
                .payload(new ApiResponse<>(DataTest.ZERO_UUID))
                .build();
        Mockito.when(mock.insert(DataTest.OBJECT_UserName_0)).thenReturn(DataTest.UNI_OPTIONAL_ZERO_UUID);
        service.add(DataTest.OBJECT_UserName_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely();
    }

    @Test
    void testWhenAddThenEmpty() {
        Mockito.when(mock.insert(DataTest.OBJECT_UserName_0)).thenReturn(DataTest.UNI_OPTIONAL_EMPTY_UUID);
        service.add(DataTest.OBJECT_UserName_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely();
    }


    @Test
    void testWhenPutThenId() {
        var expected = Answer.builder()
                .error(202)
                .payload(new ApiResponse<>(DataTest.ZERO_UUID))
                .build();
        Mockito.when(mock.update(DataTest.OBJECT_UserName_0)).thenReturn(DataTest.UNI_OPTIONAL_ZERO_UUID);
        service.put(DataTest.OBJECT_UserName_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely();
    }

    @Test
    void testWhenPutThenEmpty() {
        Mockito.when(mock.update(DataTest.OBJECT_UserName_0)).thenReturn(DataTest.UNI_OPTIONAL_EMPTY_UUID);
        Mockito.when(mock.findById(DataTest.ZERO_UUID)).thenReturn(uniOptionalTest);
        Assertions.assertThrows(CompositeException.class, () -> service.put(DataTest.OBJECT_UserName_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely());;
    }

    @Test
    void testWhenDeleteThenId() {
        var expected = Answer.of(new ApiResponse<>(DataTest.ZERO_UUID));
        Mockito.when(mock.delete(DataTest.ZERO_UUID)).thenReturn(DataTest.UNI_OPTIONAL_ZERO_UUID);
        service.delete(DataTest.ZERO_UUID)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely();
    }

    @Test
    void testWhenDeleteWithNullIdThenEmpty() {
        service.delete(null)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely();
    }
}