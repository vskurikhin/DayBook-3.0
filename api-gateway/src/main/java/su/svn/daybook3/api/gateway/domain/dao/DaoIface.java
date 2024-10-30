/*
 * This file was last modified at 2024-10-30 00:11 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * DaoIface.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.domain.dao;

import io.smallrye.mutiny.Uni;
import su.svn.daybook3.domain.model.CasesOfId;

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
