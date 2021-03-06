/*
 * This file was last modified at 2022.01.15 20:59 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * EventAddress.java
 * $Id$
 */

package su.svn.daybook.domain.enums;

public enum EventAddress {
    Null(null),
    CodeAdd(EventAddress.CODE_ADD),
    CodeGet(EventAddress.CODE_GET),
    CodePut(EventAddress.CODE_PUT),
    CodeDel(EventAddress.CODE_DEL),
    I18nAdd(EventAddress.I18N_ADD),
    I18nGet(EventAddress.I18N_DEL),
    I18nDel(EventAddress.I18N_GET),
    I18nPut(EventAddress.I18N_PUT),
    LanguageAdd(EventAddress.LANGUAGE_ADD),
    LanguageDel(EventAddress.LANGUAGE_DEL),
    LanguageGet(EventAddress.LANGUAGE_GET),
    LanguagePut(EventAddress.LANGUAGE_PUT),
    TagAdd(EventAddress.TAG_ADD),
    TagDel(EventAddress.TAG_DEL),
    TagGet(EventAddress.TAG_GET),
    TagPut(EventAddress.TAG_PUT),
    VocabularyAdd(EventAddress.VOCABULARY_ADD),
    VocabularyGet(EventAddress.VOCABULARY_GET),
    VocabularyPut(EventAddress.VOCABULARY_PUT),
    VocabularyDel(EventAddress.VOCABULARY_DEL),
    WordAdd(EventAddress.WORD_ADD),
    WordGet(EventAddress.WORD_GET),
    WordPut(EventAddress.WORD_PUT),
    WordDel(EventAddress.WORD_DEL);

    public static final String CODE_ADD = "code_add";

    public static final String CODE_GET = "code_get";

    public static final String CODE_PUT = "code_put";

    public static final String CODE_DEL = "code_del";

    public static final String I18N_ADD = "i18n_add";

    public static final String I18N_DEL = "i18n_del";

    public static final String I18N_GET = "i18n_get";

    public static final String I18N_PUT = "i18n_put";

    public static final String LANGUAGE_ADD = "language_add";

    public static final String LANGUAGE_DEL = "language_del";

    public static final String LANGUAGE_GET = "language_get";

    public static final String LANGUAGE_PUT = "language_put";

    public static final String TAG_ADD = "tag_add";

    public static final String TAG_GET = "tag_get";

    public static final String TAG_PUT = "tag_put";

    public static final String TAG_DEL = "tag_del";

    public static final String VOCABULARY_ADD = "vocabulary_add";

    public static final String VOCABULARY_GET = "vocabulary_get";

    public static final String VOCABULARY_PUT = "vocabulary_put";

    public static final String VOCABULARY_DEL = "vocabulary_del";

    public static final String WORD_ADD = "word_add";

    public static final String WORD_GET = "word_get";

    public static final String WORD_PUT = "word_put";

    public static final String WORD_DEL = "word_del";

    private final String value;

    EventAddress(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public boolean stringEquals(String other) {
        return this.value != null && this.value.equals(other) || other == null;
    }

    public static EventAddress lookup(String key) {

        if (null == key) {
            return Null;
        }
        for (EventAddress v : values()) {

            if (v.getValue() != null && v.getValue().equals(key)) {
                return v;
            }
        }
        return null;
    }
}
