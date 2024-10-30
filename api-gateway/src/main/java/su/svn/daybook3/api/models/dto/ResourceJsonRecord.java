/*
 * This file was last modified at 2024-10-30 17:26 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * ResourceJsonRecord.java
 * $Id$
 */

package su.svn.daybook3.api.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import su.svn.daybook3.models.Owned;
import su.svn.daybook3.models.UUIDIdentification;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@JsonPropertyOrder({"id", "visible", "flags"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ResourceJsonRecord(
        @JsonProperty UUID id,
        @JsonProperty UUID parentId,
        @JsonProperty String title,
        @JsonProperty Map<String, String> values,
        @JsonProperty OffsetDateTime postAt,
        @JsonProperty OffsetDateTime refreshAt,
        @JsonIgnore String userName,
        @JsonProperty boolean visible,
        @JsonProperty int flags)
        implements UUIDIdentification, Owned, Serializable {
    @Builder(toBuilder = true)
    public ResourceJsonRecord {
    }
}
