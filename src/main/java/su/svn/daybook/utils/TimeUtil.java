/*
 * This file was last modified at 2023.01.11 10:12 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * TimeUtil.java
 * $Id$
 */

package su.svn.daybook.utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public enum TimeUtil {
    Null();
    public static final LocalDateTime EPOCH_UTC = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
}
