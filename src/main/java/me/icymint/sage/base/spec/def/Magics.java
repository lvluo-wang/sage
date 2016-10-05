package me.icymint.sage.base.spec.def;

import org.springframework.core.Ordered;

import java.time.Duration;

/**
 * Created by daniel on 16/9/2.
 */
public class Magics {
    // Basic Constants
    public static final String CLAIM_GLOBAL_UNIQUE = "-";

    // Cache name
    public static final String CACHE_IDENTITY = "identity";
    public static final String CACHE_SIGNATURE = "signature";
    public static final String CACHE_CLAIM = "claim";
    public static final String CACHE_TOKEN = "token";
    public static final String CACHE_GRANT = "grant";

    // Props
    public static final String PROP_PREFIX = "sage.";  //自定义配置属性键值前缀
    public static final String PROP_DEV_MODE = PROP_PREFIX + "dev.mode";
    public static final String PROP_BASE_URL = PROP_PREFIX + "baseUrl";
    public static final String PROP_ENABLE_DB_CLOCK = PROP_PREFIX + "enable.db.clock";
    public static final String PROP_ENABLE_JOB = PROP_PREFIX + "enable.job";
    public static final String PROP_ALWAYS_SAVE_LOG = PROP_PREFIX + "always.save.log";
    public static final String PROP_JOB_EVENT_CRON = PROP_PREFIX + "job.event.cron";
    public static final String PROP_ENABLE_API_ = PROP_PREFIX + "enable.api.";

    // AOP Order From Highest to Lowest Order

    public static final int AOP_ORDER_EXCEPTION = Ordered.HIGHEST_PRECEDENCE + 1;
    public static final int AOP_ORDER_EVENT_LOG = AOP_ORDER_EXCEPTION + 10;
    public static final int AOP_ORDER_PERMISSION = AOP_ORDER_EXCEPTION + 11;
    public static final int AOP_ORDER_TOKEN = AOP_ORDER_EXCEPTION + 12;
    public static final int AOP_ORDER_REQUEST_VALIDATE = Ordered.LOWEST_PRECEDENCE - 3;
    public static final int AOP_ORDER_EVENT = Ordered.LOWEST_PRECEDENCE - 3;
    public static final int AOP_ORDER_TRANSACTION = Ordered.LOWEST_PRECEDENCE - 2;
    public static final int AOP_ORDER_JOB = Ordered.LOWEST_PRECEDENCE - 1;
    public static final int AOP_ORDER_EVENT_IN_TRANSACTION = Ordered.LOWEST_PRECEDENCE;


    // Http Constants
    public static final String HEADER_X_ENUM_MODE = "X-Enum-Mode"; //定义枚举转换方式
    public static final String HEADER_CORRELATION_ID = "X-Correlation-Id";
    public static final String HEADER_FORWARDED_FOR_HEADER = "X-Forwarded-For";
    public static final String HEADER_X_FORWARDED_PROTO = "X-Forwarded-Proto";
    public static final String HEADER_X_FORWARDED_HOST = "X-Forwarded-Host";
    public static final String HEADER_X_FORWARDED_PORT = "X-Forwarded-Port";
    public static final String HEADER_REFERER = "Referer";
    public static final String HEADER_USER_AGENT = "User-Agent";
    public static final String HEADER_X_TIMEZONE = "X-TIMEZONE";
    public static final String HEADER_X_TIMESTAMP = "X-TIMESTAMP";
    public static final String HEADER_X_PAGING = "X-PAGING";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_DATE = "date";

    //Token
    public static final String TOKEN_AUTHORIZATION_HEAD = "SAGE-";
    public static final String TOKEN_OBJECT = "token";
    public static final long TOKEN_SPAN = Duration.ofMinutes(10L).getSeconds();

    //Job config
    public static final long JOB_LOCK_DURATION = 1000 * 30L;
    public static final long JOB_LOCK_REFRESH_DURATION = JOB_LOCK_DURATION * 5;
    public static final long JOB_LOCK_EXPIRE_DURATION = JOB_LOCK_REFRESH_DURATION * 2;

    //Page
    public static final int PAGE_DEFAULT_LIMIT = 10;
    public static final int PAGE_MAX_LIMIT = 1000;

    //API tag
    public static final String API_DEV = "Dev APIs";
}
