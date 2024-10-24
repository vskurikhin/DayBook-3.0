/*
 * This file was last modified at 2024-05-24 11:40 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * NewJsonRecord.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.models.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

@JsonPropertyOrder({"visible", "flags"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public record NewJsonRecord(
        @JsonProperty UUID parentId,
        @JsonProperty Map<String, String> values,
        @JsonProperty boolean visible,
        @JsonProperty int flags) implements Serializable {
    @Builder
    public NewJsonRecord {
    }
}
