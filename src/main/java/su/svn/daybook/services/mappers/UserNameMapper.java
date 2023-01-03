package su.svn.daybook.services.mappers;

import org.jboss.logging.Logger;
import su.svn.daybook.converters.mappers.AbstractMapper;
import su.svn.daybook.domain.model.UserNameTable;
import su.svn.daybook.models.domain.UserName;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class UserNameMapper extends AbstractMapper<UUID, UserName, UserNameTable> {

    private static final Logger LOG = Logger.getLogger(UserNameMapper.class);

    protected UserNameMapper() throws NoSuchMethodException {
        super(UserName.class, UserName::builder, UserNameTable.class, UserNameTable::builder, LOG);
    }

    @Override
    public UserNameTable convertToDomain(UserName model) {
        return super.convertModelToDomain(model);
    }

    @Override
    public UserName convertToModel(UserNameTable domain) {
        return super.convertDomainToModel(domain);
    }
}
