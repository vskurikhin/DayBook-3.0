/*
 * This file was last modified at 2024-05-22 13:01 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * ResourceBaseRecord.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import su.svn.daybook3.api.gateway.models.Owned;
import su.svn.daybook3.api.gateway.models.UUIDIdentification;

import java.io.Serializable;
import java.util.UUID;

@JsonPropertyOrder({"id", "visible", "flags"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ResourceBaseRecord(
        @JsonProperty UUID id,
        @JsonIgnore String userName,
        @JsonProperty boolean visible,
        @JsonProperty int flags)
        implements UUIDIdentification, Owned, Serializable {
    @Builder(toBuilder = true)
    public ResourceBaseRecord {
    }
}
