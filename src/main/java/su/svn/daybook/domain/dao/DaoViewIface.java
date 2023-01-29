/*
 * This file was last modified at 2023.01.12 13:23 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * AbstractDao.java
 * $Id$
 */

package su.svn.daybook.domain.dao;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import su.svn.daybook.models.Identification;

import java.io.Serializable;
import java.util.Optional;

public interface DaoViewIface<I extends Comparable<? extends Serializable>, D extends Identification<I>> {

    Uni<Optional<Long>> count();

    Multi<D> findAll();

    Uni<Optional<D>> findById(I id);

    Multi<D> findRange(long offset, long limit);
}
