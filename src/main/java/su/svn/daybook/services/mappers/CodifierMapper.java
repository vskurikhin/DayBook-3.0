package su.svn.daybook.services.mappers;

import org.jboss.logging.Logger;
import su.svn.daybook.converters.mappers.AbstractMapper;
import su.svn.daybook.domain.model.CodifierTable;
import su.svn.daybook.models.domain.Codifier;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CodifierMapper extends AbstractMapper<String, Codifier, CodifierTable> {

    private static final Logger LOG = Logger.getLogger(CodifierMapper.class);

    protected CodifierMapper() throws NoSuchMethodException {
        super(Codifier.class, Codifier::builder, CodifierTable.class, CodifierTable::builder, LOG);
    }

    @Override
    public CodifierTable convertToDomain(Codifier model) {
        return super.convertModelToDomain(model);
    }

    @Override
    public Codifier convertToModel(CodifierTable domain) {
        return super.convertDomainToModel(domain);
    }
}
