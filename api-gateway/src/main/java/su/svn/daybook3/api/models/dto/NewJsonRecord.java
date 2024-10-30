/*
 * This file was last modified at 2024-10-30 17:26 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * NewJsonRecord.java
 * $Id$
 */

package su.svn.daybook3.api.models.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@JsonPropertyOrder({"visible", "flags"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public record NewJsonRecord(
        @Schema(defaultValue = "00000000-0000-0000-0000-000000000000")
        @JsonProperty UUID parentId,
        @JsonProperty String title,
        @JsonProperty Map<String, String> values,
        @JsonProperty OffsetDateTime postAt,
        @JsonProperty boolean visible,
        @JsonProperty int flags) implements Serializable {
    @Builder
    public NewJsonRecord {
    }
}
