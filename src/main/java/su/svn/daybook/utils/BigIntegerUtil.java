package su.svn.daybook.utils;

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
