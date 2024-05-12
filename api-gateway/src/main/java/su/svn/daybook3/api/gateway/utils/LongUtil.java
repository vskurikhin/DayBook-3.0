/*
 * This file was last modified at 2024-05-14 19:08 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * LongUtil.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class LongUtil {
    public static final long THOUSAND = 1000;

    public static final long MILLION = 1000000;

    public static long getUTCEpoch(LocalDateTime localDateTime) {
        return localDateTime.toEpochSecond(ZoneOffset.UTC);
    }

    public static long generateLongId(LocalDateTime now) {
        long milliseconds = now.getNano() / MILLION;
        long epoch = getUTCEpoch(now);

        return epoch * THOUSAND + milliseconds;
    }

    public static long generateLongId() {
        return generateLongId(LocalDateTime.now());
    }
}
