package su.svn.daybook.services.mappers;

import org.jboss.logging.Logger;
import su.svn.daybook.converters.mappers.AbstractMapper;
import su.svn.daybook.domain.model.ValueTypeTable;
import su.svn.daybook.models.domain.ValueType;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ValueTypeMapper extends AbstractMapper<Long, ValueType, ValueTypeTable> {

    private static final Logger LOG = Logger.getLogger(ValueTypeMapper.class);

    protected ValueTypeMapper() throws NoSuchMethodException {
        super(ValueType.class, ValueType::builder, ValueTypeTable.class, ValueTypeTable::builder, LOG);
    }

    @Override
    public ValueTypeTable convertToDomain(ValueType model) {
        return super.convertModelToDomain(model);
    }

    @Override
    public ValueType convertToModel(ValueTypeTable domain) {
        return super.convertDomainToModel(domain);
    }
}
