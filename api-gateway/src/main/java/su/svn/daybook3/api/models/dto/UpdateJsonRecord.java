/*
 * This file was last modified at 2024-10-30 17:26 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * UpdateJsonRecord.java
 * $Id$
 */

package su.svn.daybook3.api.models.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import su.svn.daybook3.models.UUIDIdentification;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UpdateJsonRecord(
        @JsonProperty UUID id,
        @JsonProperty String title,
        @JsonProperty Map<String, String> values,
        @JsonProperty OffsetDateTime refreshAt,
        @JsonProperty boolean visible,
        @JsonProperty int flags)
        implements UUIDIdentification, Serializable {
    @Builder
    public UpdateJsonRecord {
    }
}
