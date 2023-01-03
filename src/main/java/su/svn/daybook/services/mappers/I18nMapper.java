package su.svn.daybook.services.mappers;

import org.jboss.logging.Logger;
import su.svn.daybook.converters.mappers.AbstractMapper;
import su.svn.daybook.domain.model.I18nTable;
import su.svn.daybook.models.domain.I18n;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class I18nMapper extends AbstractMapper<Long, I18n, I18nTable> {

    private static final Logger LOG = Logger.getLogger(I18nMapper.class);

    protected I18nMapper() throws NoSuchMethodException {
        super(I18n.class, I18n::builder, I18nTable.class, I18nTable::builder, LOG);
    }

    @Override
    public I18nTable convertToDomain(I18n model) {
        return super.convertModelToDomain(model);
    }

    @Override
    public I18n convertToModel(I18nTable domain) {
        return super.convertDomainToModel(domain);
    }
}
