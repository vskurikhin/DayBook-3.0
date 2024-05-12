/*
 * This file was last modified at 2024-05-14 21:00 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * BaseRecordMapper.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.converters.mappers;

import org.mapstruct.Mapper;
import su.svn.daybook3.api.gateway.domain.entities.BaseRecord;
import su.svn.daybook3.api.gateway.models.dto.ResponseBaseRecord;

@Mapper(componentModel = "cdi")
public interface BaseRecordMapper {
    ResponseBaseRecord toResource(BaseRecord record);
}
