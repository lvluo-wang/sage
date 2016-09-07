package me.icymint.sage.base.rest.support;

import me.icymint.sage.base.spec.api.RuntimeContext;
import me.icymint.sage.base.spec.api.SessionService;
import me.icymint.sage.base.spec.def.MagicConstants;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by daniel on 16/9/3.
 */
@Component
public class DefaultRuntimeContext implements RuntimeContext {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    SessionService sessionService;

    @Override
    public String getSessionId() {
        String sessionId = sessionService.fetchSession(this);
        set(normalizeKey(SESSION_ID), sessionId);
        return sessionId;
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
