/*
 * This file was last modified at 2023.09.06 17:04 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * LanguageMapper.java
 * $Id$
 */

package su.svn.daybook.services.mappers;

import org.jboss.logging.Logger;
import su.svn.daybook.converters.mappers.AbstractMapper;
import su.svn.daybook.domain.model.LanguageTable;
import su.svn.daybook.models.domain.Language;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class LanguageMapper extends AbstractMapper<Long, Language, LanguageTable> {

    private static final Logger LOG = Logger.getLogger(LanguageMapper.class);

    protected LanguageMapper() throws NoSuchMethodException {
        super(Language.class, Language::builder, LanguageTable.class, LanguageTable::builder, LOG);
    }

    @Override
    public LanguageTable convertToDomain(Language model) {
        return super.convertModelToDomain(model);
    }

    @Override
    public Language convertToModel(LanguageTable domain) {
        return super.convertDomainToModel(domain);
    }
}
