/*
 * This file was last modified at 2024-05-14 23:08 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * RoleDataService.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.services.domain;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook3.api.gateway.domain.dao.RoleDao;
import su.svn.daybook3.api.gateway.domain.model.RoleTable;
import su.svn.daybook3.api.gateway.models.domain.Role;
import su.svn.daybook3.api.gateway.services.mappers.RoleMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.UUID;

@ApplicationScoped
public class RoleDataService implements DataService<UUID, RoleTable, Role> {

    private static final Logger LOG = Logger.getLogger(RoleDataService.class);

    @Inject
    RoleDao roleDao;

    @Inject
    RoleMapper roleMapper;

    public Uni<UUID> add(Role o) {
        LOG.tracef("add(%s)", o);
        return addEntry(roleMapper.convertToDomain(o));
    }

    private Uni<UUID> addEntry(RoleTable entry) {
        return roleDao
                .insert(entry)
                .map(o -> lookup(o, entry));
    }

    public Uni<Long> count() {
        return roleDao
                .count()
                .map(o -> lookupLong(o, "count for RoleTable"));
    }

    public Multi<Role> findRange(long offset, long limit) {
        LOG.tracef("findRange(%d, %d)", offset, limit);
        return roleDao
                .findRange(offset, limit)
                .map(roleMapper::convertToModel);
    }

    public Uni<Role> get(UUID id) {
        LOG.tracef("get(%s)", id);
        return roleDao
                .findById(id)
                .map(o -> lookup(o, id))
                .map(roleMapper::convertToModel);
    }

    public Multi<Role> getAll() {
        LOG.tracef("getAll()");
        return roleDao
                .count()
                .onItem()
                .transformToMulti(count -> getAllIfNotOverSize(count, roleDao::findAll))
                .map(roleMapper::convertToModel);
    }

    public Uni<UUID> put(Role o) {
        LOG.tracef("put(%s)", o);
        return putEntry(roleMapper.convertToDomain(o));
    }

    private Uni<UUID> putEntry(RoleTable entry) {
        return roleDao
                .update(entry)
                .map(o -> lookup(o, entry));
    }

    public Uni<UUID> delete(UUID id) {
        LOG.tracef("delete(%s)", id);
        return deleteEntry(id);
    }

    private Uni<UUID> deleteEntry(UUID id) {
        return roleDao
                .delete(id)
                .map(o -> lookupId(o, id));
    }
}
