/*
 * This file was last modified at 2024-10-30 09:48 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * PanacheDataService.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.services.domain;

import io.smallrye.mutiny.Uni;
import su.svn.daybook3.api.gateway.models.pagination.Page;
import su.svn.daybook3.api.gateway.models.pagination.PageRequest;
import su.svn.daybook3.domain.messages.Answer;
import su.svn.daybook3.models.Identification;

import java.io.Serializable;

interface PanacheDataService
        <I extends Comparable<? extends Serializable>, D extends Identification<I>, M extends Identification<I>>
        extends DomainService<I, D, M> {

    Uni<Page<Answer>> getPage(PageRequest pageRequest);
}