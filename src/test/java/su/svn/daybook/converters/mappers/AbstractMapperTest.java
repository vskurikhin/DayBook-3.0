package su.svn.daybook.converters.mappers;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import su.svn.daybook.domain.model.TestTable;
import su.svn.daybook.models.domain.TestModel;

class AbstractMapperTest {

    private static final Logger LOG = Logger.getLogger(AbstractMapperTest.class);

    TestMapper mapper;

    TestModel model;

    TestTable domain;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        mapper = new TestMapper();
        model = TestModel.builder().id(12L).test("test1").build();
        domain = TestTable.builder().id(13L).test("test2").build();
    }

    @Test
    void convertToDomain() {
        var expected = TestTable.builder().id(12L).test("test1").build();
        Assertions.assertDoesNotThrow(() -> {
            Assertions.assertEquals(expected, mapper.convertModelToDomain(model));
        });
    }

    @Test
    void convertToModel() {
        var expected = TestModel.builder().id(13L).test("test2").build();
        Assertions.assertDoesNotThrow(() -> {
            Assertions.assertEquals(expected, mapper.convertDomainToModel(domain));
        });
    }

    static class TestMapper extends AbstractMapper<Long, TestModel, TestTable> {

        TestMapper() throws NoSuchMethodException {
            super(TestModel.class, TestModel::builder, TestTable.class, TestTable::builder, LOG);
        }

        @Override
        public TestTable convertToDomain(TestModel model) {
            return super.convertModelToDomain(model);
        }

        @Override
        public TestModel convertToModel(TestTable domain) {
            return super.convertDomainToModel(domain);
        }
    }
}