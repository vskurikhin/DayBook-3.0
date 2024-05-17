/*
 * This file was last modified at 2024-05-20 21:59 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * DataService.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.services.domain;

import io.smallrye.mutiny.Multi;
import su.svn.daybook3.api.gateway.models.Identification;

import java.io.Serializable;

interface DataService
        <I extends Comparable<? extends Serializable>, D extends Identification<I>, M extends Identification<I>>
        extends DomainService<I, D, M> {

    Multi<M> findRange(long offset, long limit);

    Multi<M> getAll();
}