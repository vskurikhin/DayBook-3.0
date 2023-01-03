package su.svn.daybook.services.mappers;

import org.jboss.logging.Logger;
import su.svn.daybook.converters.mappers.AbstractMapper;
import su.svn.daybook.domain.model.TagLabelTable;
import su.svn.daybook.models.domain.TagLabel;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TagLabelMapper extends AbstractMapper<String, TagLabel, TagLabelTable> {

    private static final Logger LOG = Logger.getLogger(TagLabelMapper.class);

    protected TagLabelMapper() throws NoSuchMethodException {
        super(TagLabel.class, TagLabel::builder, TagLabelTable.class, TagLabelTable::builder, LOG);
    }

    @Override
    public TagLabelTable convertToDomain(TagLabel model) {
        return super.convertModelToDomain(model);
    }

    @Override
    public TagLabel convertToModel(TagLabelTable domain) {
        return super.convertDomainToModel(domain);
    }
}
