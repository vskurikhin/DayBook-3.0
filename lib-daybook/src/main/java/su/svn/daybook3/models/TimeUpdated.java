/*
 * This file was last modified at 2024-10-29 23:33 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * TimeUpdated.java
 * $Id$
 */

package su.svn.daybook3.models;

import java.time.LocalDateTime;

public interface TimeUpdated extends Timed {
    LocalDateTime updateTime();
}
