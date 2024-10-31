/*
 * This file was last modified at 2024-10-30 17:26 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * NewBaseRecord.java
 * $Id$
 */

package su.svn.daybook3.api.models.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;

import java.io.Serializable;
import java.util.UUID;

@JsonPropertyOrder({"visible", "flags"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public record NewBaseRecord(UUID parentId, boolean visible, int flags) implements Serializable {
    @Builder
    public NewBaseRecord {
    }
}
