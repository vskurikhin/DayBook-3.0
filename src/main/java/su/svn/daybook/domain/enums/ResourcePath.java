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
    Add(ResourcePath.ADD),
    All(ResourcePath.ALL),
    Codifier(ResourcePath.CODIFIER),
    I18n(ResourcePath.I18N),
    Id(ResourcePath.ID),
    Put(ResourcePath.PUT),
    TagLabel(ResourcePath.TAG_LABEL),
    TagLabels(ResourcePath.TAG_LABELS);

    public static final String ADD = "/add";

    public static final String ALL = "/all";

    public static final String CODIFIER = "/code";

    public static final String CODIFIERS = "/codes";

    public static final String ID = "/{id}";

    public static final String PUT = "/put";

    public static final String I18N = "/i18n";

    public static final String TAG_LABEL = "/tag";

    public static final String TAG_LABELS = "/tags";

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
