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
import su.svn.daybook.domain.model.CasesOfId;

import java.io.Serializable;
import java.util.Optional;

public interface DaoIface<I extends Comparable<? extends Serializable>, D extends CasesOfId<I>> {

    Uni<Optional<Long>> count();

    Uni<Optional<I>> delete(I id);

    Multi<D> findAll();

    Uni<Optional<D>> findById(I id);

    Multi<D> findRange(long offset, long limit);

    Uni<Optional<I>> insert(D entry);

    Uni<Optional<D>> insertEntry(D entry);

    Uni<Optional<I>> update(D entry);

    Uni<Optional<D>> updateEntry(D entry);
}
