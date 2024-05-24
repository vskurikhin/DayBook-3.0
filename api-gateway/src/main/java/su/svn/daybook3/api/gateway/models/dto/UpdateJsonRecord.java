/*
 * This file was last modified at 2024-05-24 11:59 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * UpdateJsonRecord.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.models.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import su.svn.daybook3.api.gateway.models.UUIDIdentification;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UpdateJsonRecord(
        @JsonProperty UUID id,
        @JsonProperty Map<String, String> values,
        @JsonProperty boolean visible,
        @JsonProperty int flags)
        implements UUIDIdentification, Serializable {
    @Builder
    public UpdateJsonRecord {
    }
}
