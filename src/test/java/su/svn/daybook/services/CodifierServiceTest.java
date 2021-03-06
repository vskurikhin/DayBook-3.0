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
import su.svn.daybook.domain.dao.CodifierDao;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.messages.DictionaryResponse;
import su.svn.daybook.domain.model.Codifier;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@QuarkusTest
class CodifierServiceTest {

    @Inject
    CodifierService service;

    static CodifierDao mock;

    static Uni<Optional<Codifier>> optionalUniTest = Uni.createFrom().item(Optional.of(DataTest.OBJECT_Codifier_0));

    static Uni<Optional<String>> optionalUniId = Uni.createFrom().item(Optional.of("code"));

    static Uni<Optional<String>> optionalUniEmptyId = Uni.createFrom().item(Optional.empty());

    static Multi<Codifier> multiTest = Multi.createFrom().item(DataTest.OBJECT_Codifier_0);

    static Multi<Codifier> multiEmpties = Multi.createFrom().empty();

    static Multi<Codifier> multiWithNull = Multi.createFrom().item(() -> null);

    @BeforeAll
    public static void setup() {
        mock = Mockito.mock(CodifierDao.class);
        Mockito.when(mock.findByCode("code")).thenReturn(optionalUniTest);
        QuarkusMock.installMockForType(mock, CodifierDao.class);
    }

    @Test
    void testMethod_getAll() {
        Mockito.when(mock.findAll()).thenReturn(multiTest);
        List<Answer> result = service.getAll()
                .subscribe()
                .asStream()
                .peek(actual -> Assertions.assertEquals(Answer.of(DataTest.OBJECT_Codifier_0), actual))
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
    void testMethod_codeGet() {
        service.codeGet("code")
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.of(DataTest.OBJECT_Codifier_0), actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_codeGet_whenNullParameter() {
        service.codeGet(null)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_codeAdd() {
        var expected = Answer.of(DictionaryResponse.code("code"));
        Mockito.when(mock.insert(DataTest.OBJECT_Codifier_0)).thenReturn(optionalUniId);
        service.codeAdd(DataTest.OBJECT_Codifier_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_codeAdd_whithEmptyResult() {
        Mockito.when(mock.insert(DataTest.OBJECT_Codifier_0)).thenReturn(optionalUniEmptyId);
        service.codeAdd(DataTest.OBJECT_Codifier_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_codePut() {
        Mockito.when(mock.update(DataTest.OBJECT_Codifier_0)).thenReturn(optionalUniId);
        var expected = Answer.of(DictionaryResponse.code("code"));
        service.codePut(DataTest.OBJECT_Codifier_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_codePut_whithEmptyResult() {
        Mockito.when(mock.update(DataTest.OBJECT_Codifier_0)).thenReturn(optionalUniEmptyId);
        service.codePut(DataTest.OBJECT_Codifier_0)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_codeDelete() {
        Mockito.when(mock.delete("code")).thenReturn(optionalUniId);
        var expected = Answer.of(DictionaryResponse.code("code"));
        service.codeDelete("code")
                .onItem()
                .invoke(actual -> Assertions.assertEquals(expected, actual))
                .await()
                .indefinitely();
    }

    @Test
    void testMethod_codeDelete_whenNullParameter() {
        service.codeDelete(null)
                .onItem()
                .invoke(actual -> Assertions.assertEquals(Answer.empty(), actual))
                .await()
                .indefinitely();
    }
}