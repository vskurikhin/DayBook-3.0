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
    CodifierAdd(EventAddress.CODIFIER_ADD),
    CodifierGet(EventAddress.CODIFIER_GET),
    CodifierPut(EventAddress.CODIFIER_PUT),
    CodifierDel(EventAddress.CODIFIER_DEL),
    I18nAdd(EventAddress.I18N_ADD),
    I18nGet(EventAddress.I18N_DEL),
    I18nDel(EventAddress.I18N_GET),
    I18nPut(EventAddress.I18N_PUT),
    LanguageAdd(EventAddress.LANGUAGE_ADD),
    LanguageDel(EventAddress.LANGUAGE_DEL),
    LanguageGet(EventAddress.LANGUAGE_GET),
    LanguagePut(EventAddress.LANGUAGE_PUT),
    SettingAdd(EventAddress.SETTING_ADD),
    SettingDel(EventAddress.SETTING_DEL),
    SettingGet(EventAddress.SETTING_GET),
    SettingPut(EventAddress.SETTING_PUT),
    TagLabelAdd(EventAddress.TAG_LABEL_ADD),
    TagLabelDel(EventAddress.TAG_LABEL_DEL),
    TagLabelGet(EventAddress.TAG_LABEL_GET),
    TagLabelPut(EventAddress.TAG_LABEL_PUT),
    UserAdd(EventAddress.USER_ADD),
    UserGet(EventAddress.USER_GET),
    UserPut(EventAddress.USER_PUT),
    UserDel(EventAddress.USER_DEL),
    ValueTypeAdd(EventAddress.VALUE_TYPE_ADD),
    ValueTypeGet(EventAddress.VALUE_TYPE_GET),
    ValueTypePut(EventAddress.VALUE_TYPE_PUT),
    ValueTypeDel(EventAddress.VALUE_TYPE_DEL),
    VocabularyAdd(EventAddress.VOCABULARY_ADD),
    VocabularyGet(EventAddress.VOCABULARY_GET),
    VocabularyPut(EventAddress.VOCABULARY_PUT),
    VocabularyDel(EventAddress.VOCABULARY_DEL),
    WordAdd(EventAddress.WORD_ADD),
    WordGet(EventAddress.WORD_GET),
    WordPut(EventAddress.WORD_PUT),
    WordDel(EventAddress.WORD_DEL);

    public static final String CODIFIER_ADD = "code_add";
    public static final String CODIFIER_GET = "code_get";
    public static final String CODIFIER_PUT = "code_put";
    public static final String CODIFIER_DEL = "code_del";
    public static final String I18N_ADD = "i18n_add";
    public static final String I18N_DEL = "i18n_del";
    public static final String I18N_GET = "i18n_get";
    public static final String I18N_PUT = "i18n_put";
    public static final String LANGUAGE_ADD = "language_add";
    public static final String LANGUAGE_DEL = "language_del";
    public static final String LANGUAGE_GET = "language_get";
    public static final String LANGUAGE_PUT = "language_put";
    public static final String SETTING_ADD = "setting_add";
    public static final String SETTING_DEL = "setting_del";
    public static final String SETTING_GET = "setting_get";
    public static final String SETTING_PUT = "setting_put";
    public static final String TAG_LABEL_ADD = "tag_add";
    public static final String TAG_LABEL_GET = "tag_get";
    public static final String TAG_LABEL_PUT = "tag_put";
    public static final String TAG_LABEL_DEL = "tag_del";
    public static final String USER_ADD = "user_add";
    public static final String USER_GET = "user_get";
    public static final String USER_PUT = "user_put";
    public static final String USER_DEL = "user_del";
    public static final String VALUE_TYPE_ADD = "value_type_add";
    public static final String VALUE_TYPE_GET = "value_type_get";
    public static final String VALUE_TYPE_PUT = "value_type_put";
    public static final String VALUE_TYPE_DEL = "value_type_del";
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

    public String getValue() {
        return value;
    }

    public boolean stringEquals(String other) {
        return this.value != null && this.value.equals(other) || other == null;
    }
}
