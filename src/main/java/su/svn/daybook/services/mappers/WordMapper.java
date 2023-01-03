package su.svn.daybook.services.mappers;

import org.jboss.logging.Logger;
import su.svn.daybook.converters.mappers.AbstractMapper;
import su.svn.daybook.domain.model.WordTable;
import su.svn.daybook.models.domain.Word;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WordMapper extends AbstractMapper<String, Word, WordTable> {

    private static final Logger LOG = Logger.getLogger(WordMapper.class);

    protected WordMapper() throws NoSuchMethodException {
        super(Word.class, Word::builder, WordTable.class, WordTable::builder, LOG);
    }

    @Override
    public WordTable convertToDomain(Word model) {
        return super.convertModelToDomain(model);
    }

    @Override
    public Word convertToModel(WordTable domain) {
        return super.convertDomainToModel(domain);
    }
}
