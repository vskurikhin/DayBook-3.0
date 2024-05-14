/*
 * This file was last modified at 2024-05-14 21:36 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * ResponseBaseRecord.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.models.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import su.svn.daybook3.api.gateway.annotations.ModelField;

import java.util.UUID;

@JsonPropertyOrder({"id", "title"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ResponseBaseRecord(
        UUID id,
        String title,
        @JsonProperty String description,
        String userName,
        @ModelField boolean visible,
        @ModelField int flags) {
    @Builder
    public ResponseBaseRecord {}
}
