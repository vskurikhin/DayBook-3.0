/*
 * This file was last modified at 2024-05-14 21:35 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * Department.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.protobuf;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Getter
@Builder
@AllArgsConstructor
@Accessors(fluent = true, chain = false)
public class Department {
	@NonNull
	@Builder.Default
	String name = "";
}
