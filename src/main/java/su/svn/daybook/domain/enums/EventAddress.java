package su.svn.daybook.domain.enums;

public enum EventAddress {
    Null(null),
    CodeAdd(EventAddress.CODE_ADD),
    CodeGet(EventAddress.CODE_GET),
    CodePut(EventAddress.CODE_PUT),
    CodeDel(EventAddress.CODE_DEL),
    I18nAdd(EventAddress.I18N_ADD),
    I18nGet(EventAddress.I18N_GET),
    I18nPut(EventAddress.I18N_PUT),
    TagAdd(EventAddress.TAG_ADD),
    TagGet(EventAddress.TAG_GET),
    WordAdd(EventAddress.WORD_ADD),
    WordGet(EventAddress.WORD_GET),
    WordPut(EventAddress.WORD_PUT),
    WordDel(EventAddress.WORD_DEL);

    public static final String CODE_ADD = "code_add";

    public static final String CODE_GET = "code_get";

    public static final String CODE_PUT = "code_put";

    public static final String CODE_DEL = "code_del";

    public static final String I18N_ADD = "i18n_add";

    public static final String I18N_GET = "i18n_get";

    public static final String I18N_PUT = "i18n_put";

    public static final String TAG_ADD = "tag_add";

    public static final String TAG_GET = "tag_get";

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
