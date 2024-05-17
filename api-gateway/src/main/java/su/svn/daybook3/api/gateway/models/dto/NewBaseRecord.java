/*
 * This file was last modified at 2024-05-17 09:49 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * NewBaseRecord.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.models.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;

import java.io.Serializable;

@JsonPropertyOrder({"visible", "flags"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public record NewBaseRecord(boolean visible, int flags) implements Serializable {
    @Builder
    public NewBaseRecord {
    }
}
