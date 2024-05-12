/*
 * This file was last modified at 2024-05-14 23:08 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * AbstractService.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.services.models;

import io.smallrye.mutiny.Multi;
import su.svn.daybook3.api.gateway.domain.messages.Answer;
import su.svn.daybook3.api.gateway.domain.messages.ApiResponse;
import su.svn.daybook3.api.gateway.models.Identification;

import java.io.Serializable;

public abstract class AbstractService<K extends Comparable<? extends Serializable>, V extends Identification<K>> {

    public abstract Multi<Answer> getAll();

    protected Answer apiResponseOkAnswer(K id) {
        return Answer.of(new ApiResponse<>(id, 200));
    }

    protected Answer apiResponseCreatedAnswer(K id) {
        return Answer.from(new ApiResponse<>(id, 201), 201);
    }

    protected Answer apiResponseAcceptedAnswer(K id) {
        return Answer.from(new ApiResponse<>(id, 202), 202);
    }
}
