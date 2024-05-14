/*
 * This file was last modified at 2024-05-14 23:10 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * VocabularyMapper.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.services.mappers;

import org.jboss.logging.Logger;
import su.svn.daybook3.api.gateway.converters.mappers.AbstractMapper;
import su.svn.daybook3.api.gateway.domain.model.VocabularyTable;
import su.svn.daybook3.api.gateway.models.domain.Vocabulary;

import jakarta.enterprise.context.ApplicationScoped;

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
