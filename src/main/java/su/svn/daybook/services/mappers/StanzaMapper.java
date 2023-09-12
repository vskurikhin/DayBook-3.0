/*
 * This file was last modified at 2023.09.12 22:07 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * StanzaMapper.java
 * $Id$
 */

package su.svn.daybook.services.mappers;

import org.jboss.logging.Logger;
import su.svn.daybook.converters.mappers.AbstractMapper;
import su.svn.daybook.domain.model.StanzaTable;
import su.svn.daybook.models.domain.Stanza;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class StanzaMapper extends AbstractMapper<Long, Stanza, StanzaTable> {

    private static final Logger LOG = Logger.getLogger(StanzaMapper.class);

    protected StanzaMapper() throws NoSuchMethodException {
        super(Stanza.class, Stanza::builder, StanzaTable.class, StanzaTable::builder, LOG);
    }

    @Override
    public StanzaTable convertToDomain(Stanza model) {
        return super.convertModelToDomain(model);
    }

    @Override
    public Stanza convertToModel(StanzaTable domain) {
        return super.convertDomainToModel(domain);
    }
}
