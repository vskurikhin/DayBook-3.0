/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * DataTest.java
 * $Id$
 */

package su.svn.daybook;

import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.*;

public class DataTest {

    public static Answer errorNoNumber = new Answer("For input string: \"noNumber\"", 404);

    public static final Codifier OBJECT_Codifier_0 = new Codifier(
            0L, null, null, null, null, null, null, null, null
    );
    public static final String JSON_Codifier_0 = "{\"id\":0}";

    public static final String JSON_ARRAY_Codifier_0 = "[{\"id\":0}]";

    public static final Word OBJECT_Word_0 = new Word(
            0L, null, null, null, null, null, null, null
    );
    public static final String JSON_Word_0 = "{\"id\":0}";

    public static final String JSON_ARRAY_Word_0 = "[{\"id\":0}]";

    public static final Vocabulary OBJECT_Vocabulary_0 = new Vocabulary(
            0L, null, null, null, null, null, null, null, null
    );
    public static final String JSON_Vocabulary_0 = "{\"id\":0}";

    public static final String JSON_ARRAY_Vocabulary_0 = "[{\"id\":0}]";

    public static final I18n OBJECT_I18n_0 = new I18n(
            0L, null, null, null, null, null, null, null, null, null
    );
    public static final String JSON_I18n_0
            = "{\"id\":0,\"languageId\":null,\"message\":null,\"translation\":null,\"userName\":null,"
            + "\"createTime\":null,\"updateTime\":null,\"enabled\":null,\"visible\":null,\"flags\":null}";

    public static final TagLabel TEZD_TagLabel = new TagLabel(
            "tezd", null, null, null, null, null, null, null
    );

    public static final String TEZD_TagLabel_JSON
            = "{\"id\":\"tezd\",\"label\":null,\"userName\":null,\"createTime\":null,\"updateTime\":null,"
            + "\"enabled\":null,\"visible\":null,\"flags\":null}";

    public static final String TEZD_TagLabel_JSON_ARRAY
            = "[{\"id\":\"tezd\",\"label\":null,\"userName\":null,\"createTime\":null,\"updateTime\":null,\""
            + "enabled\":null,\"visible\":null,\"flags\":null}]";
}
