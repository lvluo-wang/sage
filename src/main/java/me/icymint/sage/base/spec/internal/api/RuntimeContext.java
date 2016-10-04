package me.icymint.sage.base.spec.internal.api;

import me.icymint.sage.base.spec.def.EnumMode;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.ZoneId;

/**
 * Created by daniel on 16/9/3.
 */
public interface RuntimeContext {

    String SESSION_ID = "sessionId";

    String CLIENT_ID = "clientId";

    String USER_ID = "userId";

    String TOKEN_ID = "tokenId";

    String RESOURCE_PATH = "resourcePath";

    String METHOD = "method";

    String USER_ADDRESS = "userAddress";

    String CORRELATION_ID = "correlationId";

    String REQUEST_BASE_URL = "requestBaseUrl";

    String REFER = "refer";

    String TIME_ZONE = "timeZone";

    String ENUM_MODE = "enumMode";

    boolean needShutdown();

    String getSessionId();

    HttpServletRequest getNativeRequest();

    default String getHeader(String header) {
        HttpServletRequest httpRequest = getNativeRequest();
        if (httpRequest != null) {
            return httpRequest.getHeader(header);
        }
        return null;
    }

    default Long getClientId() {
        String cid = get(CLIENT_ID);
        return StringUtils.isEmpty(cid) ? null : Long.valueOf(cid);
    }

    default void setClientId(Long id) {
        if (id == null) {
            remove(CLIENT_ID);
        } else {
            set(CLIENT_ID, String.valueOf(id));
        }
    }

    Long getUserId();

    void setUserId(Long id);

    Long getTokenId();

    void setTokenId(Long id);

    default String getMethod() {
        return get(METHOD);
    }

    default void setMethod(String method) {
        set(METHOD, method);
    }

    default String getResourcePath() {
        return get(RESOURCE_PATH);
    }

    default void setResourcePath(String path) {
        set(RESOURCE_PATH, path);
    }

    default String getUserAddress() {
        return get(USER_ADDRESS);
    }

    default void setUserAddress(String address) {
        set(USER_ADDRESS, address);
    }

    default String getCorrelationId() {
        return get(CORRELATION_ID);
    }

    default void setCorrelationId(String id) {
        set(CORRELATION_ID, id);
    }

    default String getRequestBaseUrl() {
        return get(REQUEST_BASE_URL);
    }

    default void setRequestBaseUrl(String requestBaseUrl) {
        set(REQUEST_BASE_URL, requestBaseUrl);
    }

    default String getRefer() {
        return get(REFER);
    }

    default void setRefer(String requestBaseUrl) {
        set(REFER, requestBaseUrl);
    }

    default ZoneId getTimeZone() {
        String v = get(TIME_ZONE);
        if (StringUtils.isEmpty(v)) {
            return null;
        }
        return ZoneId.of(v);
    }

    default void setTimeZone(ZoneId zone) {
        if (zone != null) {
            set(TIME_ZONE, zone.getId());
        } else {
            remove(TIME_ZONE);
        }
    }

    String get(String key);

    void set(String key, String value);

    void remove(String key);

    void clear();

    default EnumMode getEnumMode() {
        String mode = get(ENUM_MODE);
        return StringUtils.isEmpty(mode)
                ? EnumMode.DEFAULT
                : EnumMode.valueOf(get(ENUM_MODE));
    }
}
