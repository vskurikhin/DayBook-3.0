/*
 * This file was last modified at 2024-05-20 22:13 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * CodifierDataService.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.services.domain;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook3.api.gateway.domain.dao.CodifierDao;
import su.svn.daybook3.api.gateway.domain.model.CodifierTable;
import su.svn.daybook3.api.gateway.models.domain.Codifier;
import su.svn.daybook3.api.gateway.services.mappers.CodifierMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CodifierDataService implements DataService<String, CodifierTable, Codifier> {

    private static final Logger LOG = Logger.getLogger(CodifierDataService.class);

    @Inject
    CodifierDao codifierDao;

    @Inject
    CodifierMapper codifierMapper;

    @Override
    public Uni<String> add(Codifier o) {
        LOG.tracef("add(%s)", o);
        return addEntry(codifierMapper.convertToDomain(o));
    }

    @Override
    public Uni<Long> count() {
        return codifierDao
                .count()
                .map(o -> lookupLong(o, "count for CodifierTable"));
    }

    @Override
    public Uni<String> delete(String id) {
        LOG.tracef("delete(%s)", id);
        return deleteEntry(id);
    }

    @Override
    public Multi<Codifier> findRange(long offset, long limit) {
        LOG.tracef("findRange(%d, %d)", offset, limit);
        return codifierDao
                .findRange(offset, limit)
                .map(codifierMapper::convertToModel);
    }

    @Override
    public Uni<Codifier> get(String id) {
        LOG.tracef("get(%s)", id);
        return codifierDao
                .findById(id)
                .map(o -> lookup(o, id))
                .map(codifierMapper::convertToModel);
    }

    @Override
    public Multi<Codifier> getAll() {
        LOG.tracef("getAll()");
        return codifierDao
                .count()
                .onItem()
                .transformToMulti(count -> getAllIfNotOverSize(count, codifierDao::findAll))
                .map(codifierMapper::convertToModel);
    }

    @Override
    public Uni<String> put(Codifier o) {
        LOG.tracef("put(%s)", o);
        return putEntry(codifierMapper.convertToDomain(o));
    }

    private Uni<String> addEntry(CodifierTable entry) {
        return codifierDao
                .insert(entry)
                .map(o -> lookup(o, entry));
    }

    private Uni<String> deleteEntry(String id) {
        return codifierDao
                .delete(id)
                .map(o -> lookupId(o, id));
    }

    private Uni<String> putEntry(CodifierTable entry) {
        return codifierDao
                .update(entry)
                .map(o -> lookup(o, entry));
    }
}
