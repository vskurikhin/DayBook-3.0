/*
 * This file was last modified at 2024-10-30 17:27 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * CodifierMapper.java
 * $Id$
 */

package su.svn.daybook3.api.services.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;
import su.svn.daybook3.api.domain.model.CodifierTable;
import su.svn.daybook3.api.models.domain.Codifier;
import su.svn.daybook3.converters.mappers.AbstractMapper;

@ApplicationScoped
public class CodifierMapper extends AbstractMapper<String, Codifier, CodifierTable> {

    private static final Logger LOG = Logger.getLogger(CodifierMapper.class);

    protected CodifierMapper() throws NoSuchMethodException {
        super(Codifier.class, Codifier::builder, CodifierTable.class, CodifierTable::builder, LOG);
    }

    @Override
    public CodifierTable convertToDomain(Codifier model) {
        return super.convertModelToDomain(model);
    }

    @Override
    public Codifier convertToModel(CodifierTable domain) {
        return super.convertDomainToModel(domain);
    }
}
