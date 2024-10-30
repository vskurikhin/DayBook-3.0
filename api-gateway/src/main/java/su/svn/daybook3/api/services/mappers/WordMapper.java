/*
 * This file was last modified at 2024-10-30 17:27 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * WordMapper.java
 * $Id$
 */

package su.svn.daybook3.api.services.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;
import su.svn.daybook3.api.domain.model.WordTable;
import su.svn.daybook3.api.models.domain.Word;
import su.svn.daybook3.converters.mappers.AbstractMapper;

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
