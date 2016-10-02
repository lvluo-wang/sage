package me.icymint.sage.base.spec.internal.api;

import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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

    boolean needShutdown();

    String getSessionId();

    default String getHeader(String header) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attributes.getRequest().getHeader(header);
    }

    default String getClientId() {
        return get(CLIENT_ID);
    }

    default void setClientId(String id) {
        set(CLIENT_ID, id);
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

}
