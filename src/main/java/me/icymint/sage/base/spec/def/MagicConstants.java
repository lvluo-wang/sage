package me.icymint.sage.base.spec.def;

/**
 * Created by daniel on 16/9/2.
 */
public class MagicConstants {
    public static final String PROP_PREFIX = "sage.";  //自定义配置属性键值前缀
    public static final String HEADER_X_DISABLE_TO_LABEL = "X-Disable-To-Label"; //定义是否拒绝转换
    public static final String CORRELATION_ID_HEADER = "X-Correlation-Id";
    public static final String FORWARDED_FOR_HEADER = "X-Forwarded-For";
    public static final String X_FORWARDED_PROTO = "X-Forwarded-Proto";
    public static final String X_FORWARDED_HOST = "X-Forwarded-Host";
    public static final String X_FORWARDED_PORT = "X-Forwarded-Port";
    public static final String REFERER_HEADER = "Referer";
    public static final String USER_AGENT_HEADER = "User-Agent";
    public static final String X_TIMEZONE = "X-TIMEZONE";
    public static final String X_TIMESTAMP = "X-TIMESTAMP";
    public static final String X_PAGING = "X-PAGING";
    public static final String AUTHORIZATION = "Authorization";

    public static final String CACHE_SIGNATURE = "signature";

    //Token时效
    public static final long TOKEN_SPAN = 600L;

    public static final String CLAIM_GLOBAL_UNIQUE = "-";
}
