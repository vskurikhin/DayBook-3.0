package su.svn.daybook.utils;

import java.time.LocalDateTime;

public class StringUtil {
    public static int ID_RADIX = 32;

    public static int TAG_ID_LENGTH = 16;

    public static String generateStringId() {
        return BigIntegerUtil.generateBigIntegerId(LocalDateTime.now()).toString(ID_RADIX);
    }

    public static String generateTagId(String tag) {
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < TAG_ID_LENGTH; ++i) {
            id.append('0');
        }

        if (tag.length() >= TAG_ID_LENGTH) {
            String num = StringUtil.generateStringId();
            id.replace(TAG_ID_LENGTH - num.length(), TAG_ID_LENGTH + 1, num);
        } else {
            long num = LongUtil.generateLongId(LocalDateTime.now());
            String sufix = Long.toString(num, ID_RADIX);
            int min = Math.min(tag.length(), TAG_ID_LENGTH - tag.length());
            id.replace(0, min, tag.substring(0, min));
            id.replace(TAG_ID_LENGTH - sufix.length(), TAG_ID_LENGTH + 1, sufix);
        }
        return id.toString();
    }
}
