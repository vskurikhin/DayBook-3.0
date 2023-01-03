package su.svn.daybook.services.mappers;

import org.jboss.logging.Logger;
import su.svn.daybook.converters.mappers.AbstractMapper;
import su.svn.daybook.domain.model.SettingTable;
import su.svn.daybook.models.domain.Setting;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SettingMapper extends AbstractMapper<Long, Setting, SettingTable> {

    private static final Logger LOG = Logger.getLogger(SettingMapper.class);

    protected SettingMapper() throws NoSuchMethodException {
        super(Setting.class, Setting::builder, SettingTable.class, SettingTable::builder, LOG);
    }

    @Override
    public SettingTable convertToDomain(Setting model) {
        return super.convertModelToDomain(model);
    }

    @Override
    public Setting convertToModel(SettingTable domain) {
        return super.convertDomainToModel(domain);
    }
}
