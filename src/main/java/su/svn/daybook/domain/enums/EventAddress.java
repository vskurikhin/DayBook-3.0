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
    I18nPage(EventAddress.I18N_PAGE),
    I18nPut(EventAddress.I18N_PUT),
    KeyValueAdd(EventAddress.KEY_VALUE_ADD),
    KeyValueDel(EventAddress.KEY_VALUE_DEL),
    KeyValueGet(EventAddress.KEY_VALUE_GET),
    KeyValuePage(EventAddress.KEY_VALUE_PAGE),
    KeyValuePut(EventAddress.KEY_VALUE_PUT),
    LanguageAdd(EventAddress.LANGUAGE_ADD),
    LanguageDel(EventAddress.LANGUAGE_DEL),
    LanguageGet(EventAddress.LANGUAGE_GET),
    LanguagePage(EventAddress.LANGUAGE_PAGE),
    LanguagePut(EventAddress.LANGUAGE_PUT),
    RoleAdd(EventAddress.ROLE_ADD),
    RoleDel(EventAddress.ROLE_DEL),
    RoleGet(EventAddress.ROLE_GET),
    RolePage(EventAddress.ROLE_PAGE),
    RolePut(EventAddress.ROLE_PUT),
    SessionAdd(EventAddress.SESSION_ADD),
    SessionDel(EventAddress.SESSION_DEL),
    SessionGet(EventAddress.SESSION_GET),
    SessionPage(EventAddress.SESSION_PAGE),
    SessionPut(EventAddress.SESSION_PUT),
    SettingAdd(EventAddress.SETTING_ADD),
    SettingDel(EventAddress.SETTING_DEL),
    SettingGet(EventAddress.SETTING_GET),
    SettingPage(EventAddress.SETTING_PAGE),
    SettingPut(EventAddress.SETTING_PUT),
    TagLabelAdd(EventAddress.TAG_LABEL_ADD),
    TagLabelDel(EventAddress.TAG_LABEL_DEL),
    TagLabelGet(EventAddress.TAG_LABEL_GET),
    TagLabelPage(EventAddress.TAG_LABEL_PAGE),
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
    public static final String CODIFIER_PAGE = "code_page_page";
    public static final String CODIFIER_DEL = "code_del";
    public static final String I18N_ADD = "i18n_add";
    public static final String I18N_DEL = "i18n_del";
    public static final String I18N_GET = "i18n_get";
    public static final String I18N_PAGE = "i18n_page";
    public static final String I18N_PUT = "i18n_put";
    public static final String KEY_VALUE_ADD = "key_value_add";
    public static final String KEY_VALUE_DEL = "key_value_del";
    public static final String KEY_VALUE_GET = "key_value_get";
    public static final String KEY_VALUE_PAGE = "key_value_page_page";
    public static final String KEY_VALUE_PUT = "key_value_put";
    public static final String LANGUAGE_ADD = "language_add";
    public static final String LANGUAGE_DEL = "language_del";
    public static final String LANGUAGE_GET = "language_get";
    public static final String LANGUAGE_PAGE = "language_page";
    public static final String ROLE_ADD = "role_add";
    public static final String ROLE_DEL = "role_del";
    public static final String ROLE_GET = "role_get";
    public static final String ROLE_PAGE= "role_page";
    public static final String ROLE_PUT = "role_put";
    public static final String LANGUAGE_PUT = "language_put";

    public static final String SESSION_ADD = "session_add";
    public static final String SESSION_DEL = "session_del";
    public static final String SESSION_GET = "session_get";
    public static final String SESSION_PAGE= "session_page";
    public static final String SESSION_PUT = "session_put";

    public static final String SETTING_ADD = "setting_add";
    public static final String SETTING_DEL = "setting_del";
    public static final String SETTING_GET = "setting_get";
    public static final String SETTING_PAGE = "setting_page";
    public static final String SETTING_PUT = "setting_put";
    public static final String TAG_LABEL_ADD = "tag_add";
    public static final String TAG_LABEL_GET = "tag_get";
    public static final String TAG_LABEL_PAGE = "tag_page";
    public static final String TAG_LABEL_PUT = "tag_put";
    public static final String TAG_LABEL_DEL = "tag_del";
    public static final String USER_ADD = "user_add";
    public static final String USER_GET = "user_get";
    public static final String USER_PAGE = "user_page";
    public static final String USER_PUT = "user_put";
    public static final String USER_DEL = "user_del";
    public static final String VALUE_TYPE_ADD = "value_type_add";
    public static final String VALUE_TYPE_GET = "value_type_get";
    public static final String VALUE_TYPE_PUT = "value_type_put";
    public static final String VALUE_TYPE_PAGE = "value_type_page";
    public static final String VALUE_TYPE_DEL = "value_type_del";
    public static final String VOCABULARY_ADD = "vocabulary_add";
    public static final String VOCABULARY_GET = "vocabulary_get";
    public static final String VOCABULARY_PAGE = "vocabulary_page";
    public static final String VOCABULARY_PUT = "vocabulary_put";
    public static final String VOCABULARY_DEL = "vocabulary_del";
    public static final String WORD_ADD = "word_add";
    public static final String WORD_GET = "word_get";
    public static final String WORD_PAGE = "word_page";
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
