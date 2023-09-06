/*
 * This file was last modified at 2023.09.06 17:04 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * TagLabelDataService.java
 * $Id$
 */

package su.svn.daybook.services.domain;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.dao.TagLabelDao;
import su.svn.daybook.domain.model.TagLabelTable;
import su.svn.daybook.models.domain.TagLabel;
import su.svn.daybook.services.mappers.TagLabelMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.UUID;

@ApplicationScoped
public class TagLabelDataService implements DataService<String, TagLabelTable, TagLabel> {

    private static final Logger LOG = Logger.getLogger(TagLabelDataService.class);

    @Inject
    TagLabelDao tagLabelDao;

    @Inject
    TagLabelMapper tagLabelMapper;

    public Uni<String> add(TagLabel o) {
        LOG.tracef("add(%s)", o);
        return addEntry(tagLabelMapper.convertToDomain(o));
    }

    private Uni<String> addEntry(TagLabelTable entry) {
        return tagLabelDao
                .insert(entry)
                .map(o -> lookup(o, entry));
    }

    public Uni<Long> count() {
        return tagLabelDao
                .count()
                .map(o -> lookupLong(o, "count for TagLabelTable"));
    }

    public Multi<TagLabel> findRange(long offset, long limit) {
        LOG.tracef("findRange(%d, %d)", offset, limit);
        return tagLabelDao
                .findRange(offset, limit)
                .map(tagLabelMapper::convertToModel);
    }

    public Uni<TagLabel> get(String id) {
        LOG.tracef("get(%s)", id);
        return tagLabelDao
                .findById(id)
                .map(o -> lookup(o, id))
                .map(tagLabelMapper::convertToModel);
    }

    public Multi<TagLabel> getAll() {
        LOG.tracef("getAll()");
        return tagLabelDao
                .count()
                .onItem()
                .transformToMulti(count -> getAllIfNotOverSize(count, tagLabelDao::findAll))
                .map(tagLabelMapper::convertToModel);
    }

    public Uni<String> put(TagLabel o) {
        LOG.tracef("put(%s)", o);
        return putEntry(tagLabelMapper.convertToDomain(o));
    }

    private Uni<String> putEntry(TagLabelTable entry) {
        return tagLabelDao
                .update(entry)
                .map(o -> lookup(o, entry));
    }

    public Uni<String> delete(String id) {
        LOG.tracef("delete(%s)", id);
        return deleteEntry(id);
    }

    private Uni<String> deleteEntry(String id) {
        return tagLabelDao
                .delete(id)
                .map(o -> lookupId(o, id));
    }
}
