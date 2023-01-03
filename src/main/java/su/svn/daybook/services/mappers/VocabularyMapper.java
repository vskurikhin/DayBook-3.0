package su.svn.daybook.services.mappers;

import org.jboss.logging.Logger;
import su.svn.daybook.converters.mappers.AbstractMapper;
import su.svn.daybook.domain.model.VocabularyTable;
import su.svn.daybook.models.domain.Vocabulary;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class VocabularyMapper extends AbstractMapper<Long, Vocabulary, VocabularyTable> {

    private static final Logger LOG = Logger.getLogger(VocabularyMapper.class);

    protected VocabularyMapper() throws NoSuchMethodException {
        super(Vocabulary.class, Vocabulary::builder, VocabularyTable.class, VocabularyTable::builder, LOG);
    }

    @Override
    public VocabularyTable convertToDomain(Vocabulary model) {
        return super.convertModelToDomain(model);
    }

    @Override
    public Vocabulary convertToModel(VocabularyTable domain) {
        return super.convertDomainToModel(domain);
    }
}
