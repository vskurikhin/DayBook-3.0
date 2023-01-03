package su.svn.daybook.services.mappers;

import org.jboss.logging.Logger;
import su.svn.daybook.converters.mappers.AbstractMapper;
import su.svn.daybook.domain.model.KeyValueTable;
import su.svn.daybook.models.domain.KeyValue;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class KeyValueMapper extends AbstractMapper<Long, KeyValue, KeyValueTable> {

    private static final Logger LOG = Logger.getLogger(KeyValueMapper.class);

    protected KeyValueMapper() throws NoSuchMethodException {
        super(KeyValue.class, KeyValue::builder, KeyValueTable.class, KeyValueTable::builder, LOG);
    }

    @Override
    public KeyValueTable convertToDomain(KeyValue model) {
        return super.convertModelToDomain(model);
    }

    @Override
    public KeyValue convertToModel(KeyValueTable domain) {
        return super.convertDomainToModel(domain);
    }
}
