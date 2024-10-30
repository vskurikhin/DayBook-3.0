/*
 * This file was last modified at 2024-10-30 17:26 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * AuthRequest.java
 * $Id$
 */

package su.svn.daybook3.api.models.security;

public record AuthRequest(String username, String password) {
}
