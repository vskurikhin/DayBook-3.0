/*
 * This file was last modified at 2024-05-14 21:36 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * Request.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.domain.messages;

import jakarta.annotation.Nonnull;

import java.security.Principal;

public record Request<T>(@Nonnull T payload, Principal principal) {
}
