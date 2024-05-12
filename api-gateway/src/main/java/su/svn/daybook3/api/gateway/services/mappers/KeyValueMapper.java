/*
 * This file was last modified at 2024-05-14 23:08 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * KeyValueMapper.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.services.mappers;

import org.jboss.logging.Logger;
import su.svn.daybook3.api.gateway.converters.mappers.AbstractMapper;
import su.svn.daybook3.api.gateway.domain.model.KeyValueTable;
import su.svn.daybook3.api.gateway.models.domain.KeyValue;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class KeyValueMapper extends AbstractMapper<UUID, KeyValue, KeyValueTable> {

    private static final Logger LOG = Logger.getLogger(KeyValueMapper.class);

    protected KeyValueMapper() throws NoSuchMethodException {
        super(KeyValue.class, KeyValue::builder, KeyValueTable.class, KeyValueTable::builder, LOG);
    }

    @Override
    public KeyValueTable convertToDomain(KeyValue model) {
        return super.convertModelToDomain(model);
    }

    @Override
    public KeyValue convertToModel(KeyValueTable domain) {
        return super.convertDomainToModel(domain);
    }
}
