/*
 * This file was last modified at 2024-10-29 18:25 by Victor N. Skurikhin.
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
import su.svn.daybook3.api.gateway.models.dto.NewJsonRecord;
import su.svn.daybook3.api.gateway.models.dto.ResourceJsonRecord;
import su.svn.daybook3.api.gateway.models.dto.UpdateJsonRecord;

@Mapper(componentModel = "cdi")
public interface JsonRecordMapper {

    @ValueMapping(source = "UNRECOGNIZED", target = MappingConstants.NULL)
    @Mapping(target = "parentId", source = "baseRecord.parentId")
    ResourceJsonRecord toResource(JsonRecord record);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userName", ignore = true)
    @Mapping(target = "refreshAt", ignore = true)
    ResourceJsonRecord toResource(NewJsonRecord record);

    @Mapping(target = "userName", ignore = true)
    @Mapping(target = "parentId", ignore = true)
    @Mapping(target = "postAt", ignore = true)
    ResourceJsonRecord toResource(UpdateJsonRecord record);

    @Mapping(target = "type", constant = "Json")
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    BaseRecord toBaseRecord(ResourceJsonRecord record);

    @Mapping(target = "baseRecord", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    JsonRecord toEntity(ResourceJsonRecord record);
}
