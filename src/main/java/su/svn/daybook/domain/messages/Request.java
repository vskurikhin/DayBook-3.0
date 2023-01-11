/*
 * This file was last modified at 2023.01.11 17:39 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * Request.java
 * $Id$
 */

package su.svn.daybook.domain.messages;

import javax.annotation.Nonnull;
import java.security.Principal;

public record Request<T>(@Nonnull T payload, Principal principal) {
}
