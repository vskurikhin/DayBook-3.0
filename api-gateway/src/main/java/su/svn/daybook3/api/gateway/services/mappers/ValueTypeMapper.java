/*
 * This file was last modified at 2024-10-30 09:54 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * ValueTypeMapper.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.services.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;
import su.svn.daybook3.api.gateway.domain.model.ValueTypeTable;
import su.svn.daybook3.api.gateway.models.domain.ValueType;
import su.svn.daybook3.converters.mappers.AbstractMapper;

@ApplicationScoped
public class ValueTypeMapper extends AbstractMapper<Long, ValueType, ValueTypeTable> {

    private static final Logger LOG = Logger.getLogger(ValueTypeMapper.class);

    protected ValueTypeMapper() throws NoSuchMethodException {
        super(ValueType.class, ValueType::builder, ValueTypeTable.class, ValueTypeTable::builder, LOG);
    }

    @Override
    public ValueTypeTable convertToDomain(ValueType model) {
        return super.convertModelToDomain(model);
    }

    @Override
    public ValueType convertToModel(ValueTypeTable domain) {
        return super.convertDomainToModel(domain);
    }
}
