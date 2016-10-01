package me.icymint.sage.base.core.service;

import me.icymint.sage.base.spec.internal.api.RuntimeContext;
import me.icymint.sage.base.spec.api.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;

import static me.icymint.sage.base.rest.util.StringLogs.append;

/**
 * Created by daniel on 16/9/6.
 */
public class DefaultSessionService implements SessionService {
    private final Logger logger = LoggerFactory.getLogger(DefaultSessionService.class);

    @Override
    public String fetchSession(RuntimeContext context) {
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        logRuntimeContext(context);
        return sessionId;
    }

    private void logRuntimeContext(RuntimeContext context) {
        StringBuilder sb = new StringBuilder();
        append(sb, "clientId:" + context.getClientId());
        append(sb, "userId:" + context.getUserId());
        append(sb, context.getUserAddress());
        append(sb, context.getRefer());
        logger.info(sb.toString());
    }

}
