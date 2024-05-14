/*
 * This file was last modified at 2024-05-14 21:36 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * Constants.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.models;

public class Constants {
    public static final String COUNT = "count";
    public static final String CHECK_NOT_IN_RELATIVE = "checkNotInRelative";
    public static final String CLEAR_JOIN_ID = "clearJoinId";
    public static final String CLEAR_HAS_RELATION = "clearHasRelation";
    public static final String CLEAR_RELATION = "clearRelation";
    public static final String CLEAR_ALL_HAS_RELATION_BY_ID = "clearAllHasRelationById";
    public static final String CLEAR_ALL_HAS_RELATION_BY_FIELD = "clearAllHasRelationByField";
    public static final String DELETE = "delete";
    public static final String DELETE_MAIN = "deleteMain";
    public static final String FIND_FIELD_ID = "findFieldId";
    public static final String ID = "id";
    public static final String INSERT = "insert";
    public static final String INSERT_INTO_RELATION = "insertJoin";
    public static final String INSERT_INTO_RELATION_BY_2_VALUES = "insertJoin2";
    public static final String INSERT_INTO_RELATION_BY_4_VALUES = "insertJoin4";
    public static final String INSERT_INTO_MAIN = "insertMain";
    public static final String UPDATE = "update";
    public static final String UPDATE_MAIN_TABLE = "updateMain";
    public static final String UPDATE_RELATION = "updateRelation";
    public static final String UPSERT = "upsert";
    public static final String UPSERT_MAIN_TABLE = "upsertMain";

    private Constants() {
    }
}
