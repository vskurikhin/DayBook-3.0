/*
 * This file was last modified at 2024-05-20 23:51 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * BaseRecordMapper.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.services.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ValueMapping;
import su.svn.daybook3.api.gateway.domain.entities.BaseRecord;
import su.svn.daybook3.api.gateway.models.dto.NewBaseRecord;
import su.svn.daybook3.api.gateway.models.dto.ResourceBaseRecord;
import su.svn.daybook3.api.gateway.models.dto.UpdateBaseRecord;

@Mapper(componentModel = "cdi")
public interface BaseRecordMapper {

    @ValueMapping(source = "UNRECOGNIZED", target = MappingConstants.NULL)
    ResourceBaseRecord toResource(BaseRecord record);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userName", ignore = true)
    ResourceBaseRecord toResource(NewBaseRecord record);

    @Mapping(target = "userName", ignore = true)
    ResourceBaseRecord toResource(UpdateBaseRecord record);

    @Mapping(target = "type", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    BaseRecord toEntity(ResourceBaseRecord record);
}
