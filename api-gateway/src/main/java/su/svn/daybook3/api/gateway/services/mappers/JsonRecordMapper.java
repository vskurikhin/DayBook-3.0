/*
 * This file was last modified at 2024-05-24 09:06 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * JsonRecordMapper.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.services.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ValueMapping;
import su.svn.daybook3.api.gateway.domain.entities.BaseRecord;
import su.svn.daybook3.api.gateway.domain.entities.JsonRecord;
import su.svn.daybook3.api.gateway.models.dto.ResourceJsonRecord;

@Mapper(componentModel = "cdi")
public interface JsonRecordMapper {

    @ValueMapping(source = "UNRECOGNIZED", target = MappingConstants.NULL)
    ResourceJsonRecord toResource(JsonRecord record);

    @Mapping(target = "type", constant = "Json")
    BaseRecord toBaseRecord(JsonRecord record);

    @Mapping(target = "baseRecord", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    JsonRecord toEntity(ResourceJsonRecord record);
}
