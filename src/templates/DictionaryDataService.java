/*
 * This file was last modified at 2023.01.07 11:52 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * @Name@DataService.java
 * $Id$
 */

package su.svn.daybook.services.domain;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.dao.@Name@Dao;
import su.svn.daybook.domain.model.@Name@Table;
import su.svn.daybook.models.domain.@Name@;
import su.svn.daybook.services.mappers.@Name@Mapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

@ApplicationScoped
public class @Name@DataService implements DataService<@IdType@, @Name@Table, @Name@> {

    private static final Logger LOG = Logger.getLogger(@Name@DataService.class);

    @Inject
    @Name@Dao @name@Dao;

    @Inject
    @Name@Mapper @name@Mapper;

    public Uni<@IdType@> add(@Name@ o) {
        LOG.tracef("add(%s)", o);
        return addEntry(@name@Mapper.convertToDomain(o));
    }

    private Uni<@IdType@> addEntry(@Name@Table entry) {
        return @name@Dao
                .insert(entry)
                .map(o -> lookup(o, entry));
    }

    public Uni<Long> count() {
        return @name@Dao
                .count()
                .map(o -> lookupLong(o, "count for @Name@Table"));
    }

    public Multi<@Name@> findRange(long offset, long limit) {
        LOG.tracef("findRange(%d, %d)", offset, limit);
        return @name@Dao
                .findRange(offset, limit)
                .map(@name@Mapper::convertToModel);
    }

    public Uni<@Name@> get(@IdType@ id) {
        LOG.tracef("get(%s)", id);
        return @name@Dao
                .findById(id)
                .map(o -> lookup(o, id))
                .map(@name@Mapper::convertToModel);
    }

    public Multi<@Name@> getAll() {
        LOG.tracef("getAll()");
        return @name@Dao
                .count()
                .onItem()
                .transformToMulti(count -> getAllIfNotOverSize(count, @name@Dao::findAll))
                .map(@name@Mapper::convertToModel);
    }

    public Uni<@IdType@> put(@Name@ o) {
        LOG.tracef("put(%s)", o);
        return putEntry(@name@Mapper.convertToDomain(o));
    }

    private Uni<@IdType@> putEntry(@Name@Table entry) {
        return @name@Dao
                .update(entry)
                .map(o -> lookup(o, entry));
    }

    public Uni<@IdType@> delete(@IdType@ id) {
        LOG.tracef("delete(%s)", id);
        return deleteEntry(id);
    }

    private Uni<@IdType@> deleteEntry(@IdType@ id) {
        return @name@Dao
                .delete(id)
                .map(o -> lookupId(o, id));
    }
}
