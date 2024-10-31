/*
 * This file was last modified at 2024-10-30 14:17 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * AbstractService.java
 * $Id$
 */

package su.svn.daybook3.auth.services.models;

import su.svn.daybook3.domain.messages.Answer;
import su.svn.daybook3.domain.messages.ApiResponse;
import su.svn.daybook3.models.Identification;

import java.io.Serializable;
import java.util.Objects;

public abstract class AbstractService
        <K extends Comparable<? extends Serializable>, V extends Identification<K>> {

    protected Answer apiResponseOkAnswer(K id) {
        return Answer.of(new ApiResponse<>(id, 200));
    }

    protected Answer apiResponseCreatedAnswer(K id) {
        return Answer.from(new ApiResponse<>(id, 201), 201);
    }

    protected Answer apiResponseAcceptedAnswer(K id) {
        return Answer.from(new ApiResponse<>(id, 202), 202);
    }

    protected Answer apiResponsePutAcceptedOrNotFoundAnswer(K id) {
        if (Objects.isNull(id)) {
            return new Answer("Not Found", 404);
        }
        return Answer.from(new ApiResponse<>(id, 202), 202);
    }
}
