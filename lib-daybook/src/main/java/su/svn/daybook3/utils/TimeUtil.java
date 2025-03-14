/*
 * This file was last modified at 2024-10-29 23:22 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * TimeUtil.java
 * $Id$
 */

package su.svn.daybook3.utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public enum TimeUtil {
    Null();
    public static final LocalDateTime EPOCH_UTC = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
}
