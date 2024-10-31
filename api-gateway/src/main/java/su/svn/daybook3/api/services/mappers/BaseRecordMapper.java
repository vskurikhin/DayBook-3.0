/*
 * This file was last modified at 2024-10-30 17:27 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * BaseRecordMapper.java
 * $Id$
 */

package su.svn.daybook3.api.services.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ValueMapping;
import su.svn.daybook3.api.domain.entities.BaseRecord;
import su.svn.daybook3.api.models.dto.NewBaseRecord;
import su.svn.daybook3.api.models.dto.ResourceBaseRecord;
import su.svn.daybook3.api.models.dto.UpdateBaseRecord;

@Mapper(componentModel = "cdi")
public interface BaseRecordMapper {

    @ValueMapping(source = "UNRECOGNIZED", target = MappingConstants.NULL)
    ResourceBaseRecord toResource(BaseRecord record);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userName", ignore = true)
    ResourceBaseRecord toResource(NewBaseRecord record);

    @Mapping(target = "userName", ignore = true)
    @Mapping(target = "parentId", ignore = true)
    ResourceBaseRecord toResource(UpdateBaseRecord record);

    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    BaseRecord toEntity(ResourceBaseRecord record);
}
