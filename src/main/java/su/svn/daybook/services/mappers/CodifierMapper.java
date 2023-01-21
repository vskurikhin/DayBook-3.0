/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * CodifierMapper.java
 * $Id$
 */

package su.svn.daybook.services.mappers;

import org.jboss.logging.Logger;
import su.svn.daybook.converters.mappers.AbstractMapper;
import su.svn.daybook.domain.model.CodifierTable;
import su.svn.daybook.models.domain.Codifier;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;

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
