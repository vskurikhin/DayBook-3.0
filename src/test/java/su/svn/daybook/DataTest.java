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

import java.util.UUID;

import static su.svn.daybook.domain.model.Vocabulary.NONE;

public class DataTest {

    public static Answer errorNoNumber = new Answer("For input string: \"noNumber\"", 404);

    public static Answer errorEmpty = new Answer("EMPTY", 404);

    public static final Codifier OBJECT_Codifier_0 = new Codifier(
            "code", null, null, null, null, null, null, null
    );
    public static final String JSON_Codifier_0 = "{\"code\":\"code\"}";

    public static final String JSON_ARRAY_Codifier_0 = "[{\"code\":\"code\"}]";

    public static final Word OBJECT_Word_0 = new Word(
            Word.NONE, null, null, null, false, true, 0
    );

    public static final String JSON_Word_0 = """
            {"word":"\
            """ + Word.NONE + """
            ","enabled":false,"visible":true,"flags":0,"id":"__NONE__"}\
            """;


    public static final String JSON_Word_Id_0 = "{\"word\":\"" + Word.NONE + "\"}";

    public static final String JSON_ARRAY_Word_0 = "[" + JSON_Word_0 + "]";

    public static final Language OBJECT_Language_0 = new Language(
            0L, null, null, null, null, null, null, null
    );
    public static final String JSON_Language_0 = "{\"id\":0}";

    public static final String JSON_ARRAY_Language_0 = "[{\"id\":0}]";

    public static final Vocabulary OBJECT_Vocabulary_0 = new Vocabulary(
            0L, Vocabulary.NONE, null, null, null, null, false, true, 0
    );
    public static final String JSON_Vocabulary_0 = """
            {"id":0,"word":"\
            """ + Vocabulary.NONE + """
            ","enabled":false,"visible":true,"flags":0}\
            """;

    public static final String JSON_ARRAY_Vocabulary_0 = "[" + JSON_Vocabulary_0 + "]";

    public static final I18n OBJECT_I18n_0 = new I18n(
            0L, null, null, null, null, null, null, null, null, null
    );
    public static final String JSON_I18n_0 = "{\"id\":0}";

    public static final TagLabel OBJECT_TagLabel_0 = new TagLabel(
            "test", null, null, null, null, null, null, null
    );

    public static final String JSON_TagLabel_0
            = "{\"id\":\"test\"}";

    public static final String JSON_ARRAY_TagLabel_0
            = "[{\"id\":\"test\"}]";

    public static final UserName OBJECT_UserName_0 = new UserName(
            new UUID(0, 0), "root", "password", null, null, false, true, 0
    );

    public static final String JSON_UserName_0 = """
            {"id":"00000000-0000-0000-0000-000000000000","userName":"root","password":"password","enabled":false,"visible":true,"flags":0}\
            """;

    public static final String JSON_ARRAY_UserName_0 = "[" + JSON_UserName_0 + "]";
}
