/*
 * This file was last modified at 2023.09.12 22:04 by Victor N. Skurikhin.
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
    Page(ResourcePath.PAGE),
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
    public static final String API_PATH = "/api/v1";
    public static final String AUTH = API_PATH + "/auth";
    public static final String CODIFIER = API_PATH + "/code";
    public static final String CODIFIERS = API_PATH + "/codes";
    public static final String I18N = API_PATH + "/i18n";
    public static final String I18NS = API_PATH + "/i18ns";
    public static final String ID = "/{id}";
    public static final String KEY_VALUE = API_PATH + "/key-value";
    public static final String KEY_VALUES = API_PATH + "/key-values";
    public static final String LANGUAGE = API_PATH + "/lang";
    public static final String LANGUAGES = API_PATH + "/langs";
    public static final String LOGIN = "/login";
    public static final String NONE = "";
    public static final String PAGE = "-";
    public static final String PUT = "/put";
    public static final String ROLE = API_PATH + "/role";
    public static final String ROLES = API_PATH + "/roles";
    public static final String SESSION = API_PATH + "/session";
    public static final String SESSIONS = API_PATH + "/sessions";
    public static final String SETTING = API_PATH + "/setting";
    public static final String SETTINGS = API_PATH + "/settings";
    public static final String STANZA = API_PATH + "/stanza";
    public static final String STANZAS = API_PATH + "/stanzas";
    public static final String TAG_LABEL = API_PATH + "/tag";
    public static final String TAG_LABELS = API_PATH + "/tags";
    public static final String USER = API_PATH + "/user";
    public static final String USERS = API_PATH + "/users";
    public static final String VALUE_TYPE = API_PATH + "/value-type";
    public static final String VALUE_TYPES = API_PATH + "/value-types";
    public static final String VOCABULARIES = API_PATH + "/vocabularies";
    public static final String VOCABULARY = API_PATH + "/vocabulary";
    public static final String WORD = API_PATH + "/word";
    public static final String WORDS = API_PATH + "/words";

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
