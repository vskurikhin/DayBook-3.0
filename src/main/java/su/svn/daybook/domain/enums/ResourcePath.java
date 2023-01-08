/*
 * This file was last modified at 2022.01.11 17:59 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * ResourcePath.java
 * $Id$
 */

package su.svn.daybook.domain.enums;

public enum ResourcePath {
    Null(null),
    All(ResourcePath.ALL),
    Auth(ResourcePath.AUTH),
    Codifier(ResourcePath.CODIFIER),
    I18n(ResourcePath.I18N),
    Id(ResourcePath.ID),
    KeyValue(ResourcePath.KEY_VALUE),
    Language(ResourcePath.LANGUAGE),
    Login(ResourcePath.LOGIN),
    Put(ResourcePath.PUT),
    Role(ResourcePath.ROLE),
    Session(ResourcePath.SESSION),
    Sessions(ResourcePath.SESSIONS),
    Setting(ResourcePath.SETTING),
    Settings(ResourcePath.SETTINGS),
    TagLabel(ResourcePath.TAG_LABEL),
    TagLabels(ResourcePath.TAG_LABELS),
    User(ResourcePath.USER),
    Users(ResourcePath.USERS),
    ValueType(ResourcePath.VALUE_TYPE),
    ValueTypes(ResourcePath.VALUE_TYPES),
    Vocabulary(ResourcePath.VOCABULARY),
    Vocabularies(ResourcePath.VOCABULARIES),
    Word(ResourcePath.WORD);

    public static final String ALL = "/";
    public static final String AUTH = "/auth";
    public static final String CODIFIER = "/code";
    public static final String CODIFIERS = "/codes";
    public static final String I18N = "/i18n";
    public static final String I18NS = "/i18ns";
    public static final String ID = "/{id}";
    public static final String KEY_VALUE = "/key-value";
    public static final String KEY_VALUES = "/key-values";
    public static final String LANGUAGE = "/lang";
    public static final String LANGUAGES = "/langs";
    public static final String LOGIN = "/login";
    public static final String PUT = "/put";
    public static final String ROLE = "/role";
    public static final String ROLES = "/roles";
    public static final String SESSION = "/session";
    public static final String SESSIONS = "/sessions";
    public static final String SETTING = "/setting";
    public static final String SETTINGS = "/settings";
    public static final String TAG_LABEL = "/tag";
    public static final String TAG_LABELS = "/tags";
    public static final String USER = "/user";
    public static final String USERS = "/users";
    public static final String VALUE_TYPE = "/value-type";
    public static final String VALUE_TYPES = "/value-types";
    public static final String VOCABULARY = "/vocabulary";
    public static final String VOCABULARIES = "/vocabularies";
    public static final String WORD = "/word";
    public static final String WORDS = "/words";

    private final String value;

    ResourcePath(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public boolean stringEquals(String other) {
        return this.value != null && this.value.equals(other) || other == null;
    }

    public static ResourcePath lookup(String key) {

        if (null == key) {
            return Null;
        }
        for (ResourcePath v : values()) {

            if (v.getValue() != null && v.getValue().equals(key)) {
                return v;
            }
        }
        return null;
    }
}
