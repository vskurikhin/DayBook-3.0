/*
 * This file was last modified at 2023.09.06 17:04 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * DaoIface.java
 * $Id$
 */

package su.svn.daybook.domain.dao;

import io.smallrye.mutiny.Uni;
import su.svn.daybook.domain.model.CasesOfId;

import java.io.Serializable;
import java.util.Optional;

public interface DaoIface<I extends Comparable<? extends Serializable>, D extends CasesOfId<I>>
        extends DaoViewIface<I, D> {

    Uni<Optional<I>> delete(I id);

    Uni<Optional<I>> insert(D entry);

    Uni<Optional<D>> insertEntry(D entry);

    Uni<Optional<I>> update(D entry);

    Uni<Optional<D>> updateEntry(D entry);
}
