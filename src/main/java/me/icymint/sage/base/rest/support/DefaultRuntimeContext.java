package me.icymint.sage.base.rest.support;

import com.google.common.base.Strings;
import me.icymint.sage.base.spec.api.SessionService;
import me.icymint.sage.base.spec.def.BaseExceptionCode;
import me.icymint.sage.base.spec.def.MagicConstants;
import me.icymint.sage.base.spec.exception.UnauthorizedException;
import me.icymint.sage.base.spec.internal.api.RuntimeContext;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

/**
 * Created by daniel on 16/9/3.
 */
@Component
public class DefaultRuntimeContext implements RuntimeContext, ApplicationListener<ContextClosedEvent> {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    SessionService sessionService;
    @Autowired
    ApplicationContext context;
    private volatile boolean shutdown = false;

    @Override
    public boolean needShutdown() {
        return shutdown;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        shutdown = true;
    }

    @Override
    public String getSessionId() {
        String sessionId = sessionService.fetchSession(this);
        set(normalizeKey(SESSION_ID), sessionId);
        return sessionId;
    }

    @Override
    public Long getUserId() {
        return toLong(get(USER_ID));
    }

    private Long toLong(String value) {
        if (Strings.isNullOrEmpty(value) || "NULL".equals(value)) {
            return null;
        }
        return Long.valueOf(value);
    }

    @Override
    public void setUserId(Long id) {
        setLong(USER_ID, id);
    }

    private void setLong(String key, Long id) {
        if (get(key) != null) {
            throw new UnauthorizedException(context, BaseExceptionCode.AUTHORIZATION_DOUBLE_CHECKED);
        }
        if (id == null) {
            set(key, "NULL");
        } else {
            set(key, String.valueOf(id));
        }
    }

    @Override
    public Long getTokenId() {
        return toLong(get(TOKEN_ID));
    }

    @Override
    public void setTokenId(Long id) {
        setLong(TOKEN_ID, id);
    }


    private String normalizeKey(String key) {
        return MagicConstants.PROP_PREFIX + key;
    }

    @Override
    public String get(String key) {
        return MDC.get(normalizeKey(key));
    }

    @Override
    public void set(String key, String value) {
        MDC.put(normalizeKey(key), value);
    }

    @Override
    public void remove(String key) {
        MDC.remove(normalizeKey(key));
    }

    @Override
    public void clear() {
        MDC.clear();
    }
}
