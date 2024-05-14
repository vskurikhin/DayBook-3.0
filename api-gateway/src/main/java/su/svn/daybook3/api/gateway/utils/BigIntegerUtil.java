/*
 * This file was last modified at 2024-05-14 20:56 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * BigIntegerUtil.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.utils;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class BigIntegerUtil {
    public static final BigInteger BILLION = new BigInteger("1000000000");

    public static BigInteger getUTCEpoch(LocalDateTime localDateTime) {
        return BigInteger.valueOf(localDateTime.toEpochSecond(ZoneOffset.UTC));
    }

    public static BigInteger generateBigIntegerId(LocalDateTime now) {
        long nano = now.getNano();
        BigInteger epoch = getUTCEpoch(now);

        return epoch.multiply(BILLION).add(BigInteger.valueOf(nano));
    }
}
