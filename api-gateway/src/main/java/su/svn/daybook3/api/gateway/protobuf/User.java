/*
 * This file was last modified at 2024-05-14 21:36 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * User.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.protobuf;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;

@Value
@Getter
@Builder
@AllArgsConstructor
@Accessors(fluent = true, chain = false)
public class User {

    @NonNull
    String id;
    @NonNull
    String email;
    @Builder.Default
    List<Permission> permissions = Collections.emptyList();
    @Builder.Default
    List<Department> mainDepartments = Collections.emptyList();
    @Builder.Default
    List<Department> departments = Collections.emptyList();
}
