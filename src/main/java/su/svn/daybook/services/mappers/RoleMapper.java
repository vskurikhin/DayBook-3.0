/*
 * This file was last modified at 2023.09.06 17:04 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * RoleMapper.java
 * $Id$
 */

package su.svn.daybook.services.mappers;

import org.jboss.logging.Logger;
import su.svn.daybook.converters.mappers.AbstractMapper;
import su.svn.daybook.domain.model.RoleTable;
import su.svn.daybook.models.domain.Role;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class RoleMapper extends AbstractMapper<UUID, Role, RoleTable> {

    private static final Logger LOG = Logger.getLogger(RoleMapper.class);

    protected RoleMapper() throws NoSuchMethodException {
        super(Role.class, Role::builder, RoleTable.class, RoleTable::builder, LOG);
    }

    @Override
    public RoleTable convertToDomain(Role model) {
        return super.convertModelToDomain(model);
    }

    @Override
    public Role convertToModel(RoleTable domain) {
        return super.convertDomainToModel(domain);
    }
}
