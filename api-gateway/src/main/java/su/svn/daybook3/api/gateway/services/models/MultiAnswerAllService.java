/*
 * This file was last modified at 2024-10-30 08:52 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * MultiAnswerAllService.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.services.models;

import io.smallrye.mutiny.Multi;
import su.svn.daybook3.domain.messages.Answer;

public interface MultiAnswerAllService {
    Multi<Answer> getAll();
}
