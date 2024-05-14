/*
 * This file was last modified at 2024-05-14 20:56 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * StringUtil.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.utils;

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
