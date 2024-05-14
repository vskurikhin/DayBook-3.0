/*
 * This file was last modified at 2024-05-14 20:56 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * UserMapper.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.mapper;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ValueMapping;
import org.mapstruct.factory.Mappers;
import su.svn.daybook3.api.gateway.protobuf.Department;
import su.svn.daybook3.api.gateway.protobuf.Permission;
import su.svn.daybook3.api.gateway.protobuf.User;
import su.svn.daybook3.protobuf.UserProtos;
import su.svn.daybook3.protobuf.UserProtos.DepartmentDTO;
import su.svn.daybook3.protobuf.UserProtos.PermissionDTO;
import su.svn.daybook3.protobuf.UserProtos.UserDTO;

import java.util.List;

/**
 * @author Victor N. Skurikhin
 */
@Mapper(
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);


    @Mapping(source = "permissions", target = "permissionsList")
    @Mapping(source = "mainDepartments", target = "mainDepartmentsList")
    @Mapping(source = "departments", target = "departmentsList")
    UserDTO map(User user);

    @Mapping(source = "permissionsList", target = "permissions")
    @Mapping(source = "mainDepartmentsList", target = "mainDepartments")
    @Mapping(source = "departmentsList", target = "departments")
    User map(UserDTO userDTO);

    default List<Department> departmentDTOListToDepartmentList(List<UserProtos.DepartmentDTO> list) {
        return list.stream().map(this::map).toList();
    }

    default List<Permission> permissionDTOListToPermissionList(List<UserProtos.PermissionDTO> list) {
        return list.stream().map(this::map).toList();
    }

    @ValueMapping(source = "UNRECOGNIZED", target = MappingConstants.NULL)
    Permission map(PermissionDTO permissionDTO);

    PermissionDTO map(Permission perm);


    Department map(DepartmentDTO departmentDTO);

    DepartmentDTO map(Department department);
}
