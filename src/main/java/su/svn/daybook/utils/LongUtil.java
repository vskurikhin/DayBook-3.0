package su.svn.daybook.utils;

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
