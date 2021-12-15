package su.svn.daybook;

import su.svn.daybook.domain.model.Codifier;
import su.svn.daybook.domain.model.TagLabel;

public class DataTest {

    public static final TagLabel TEZD_TagLabel = new TagLabel(
            "tezd", null, null, null, null, null, null, null
    );

    public static final Codifier TEZD_Codifier = new Codifier(
            0L, null, null, null, null, null, null, null, null
    );

    public static final String TEZD_TagLabel_JSON
            = "{\"id\":\"tezd\",\"label\":null,\"userName\":null,\"createTime\":null,\"updateTime\":null,"
            + "\"enabled\":null,\"visible\":null,\"flags\":null}";

    public static final String TEZD_TagLabel_JSON_ARRAY
            = "[{\"id\":\"tezd\",\"label\":null,\"userName\":null,\"createTime\":null,\"updateTime\":null,\""
            + "enabled\":null,\"visible\":null,\"flags\":null}]";

    public static final String TEZD_Codifier_JSON
            = "{\"id\":0,\"code\":null,\"value\":null,\"userName\":null,\"createTime\":null,\"updateTime\":null,"
            + "\"enabled\":null,\"visible\":null,\"flags\":null}";
}
