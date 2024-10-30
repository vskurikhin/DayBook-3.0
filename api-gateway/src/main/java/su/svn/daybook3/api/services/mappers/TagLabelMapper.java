/*
 * This file was last modified at 2024-10-30 17:27 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * TagLabelMapper.java
 * $Id$
 */

package su.svn.daybook3.api.services.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;
import su.svn.daybook3.api.domain.model.TagLabelTable;
import su.svn.daybook3.api.models.domain.TagLabel;
import su.svn.daybook3.converters.mappers.AbstractMapper;

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
