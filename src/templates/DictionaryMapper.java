/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * @Name@Mapper.java
 * $Id$
 */

package su.svn.daybook.services.mappers;

import org.jboss.logging.Logger;
import su.svn.daybook.converters.mappers.AbstractMapper;
import su.svn.daybook.domain.model.@Name@Table;
import su.svn.daybook.models.domain.@Name@;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class @Name@Mapper extends AbstractMapper<@IdType@, @Name@, @Name@Table> {

    private static final Logger LOG = Logger.getLogger(@Name@Mapper.class);

    protected @Name@Mapper() throws NoSuchMethodException {
        super(@Name@.class, @Name@::builder, @Name@Table.class, @Name@Table::builder, LOG);
    }

    @Override
    public @Name@Table convertToDomain(@Name@ model) {
        return super.convertModelToDomain(model);
    }

    @Override
    public @Name@ convertToModel(@Name@Table domain) {
        return super.convertDomainToModel(domain);
    }
}
