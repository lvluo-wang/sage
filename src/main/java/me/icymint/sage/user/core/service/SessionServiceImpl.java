package me.icymint.sage.user.core.service;

import me.icymint.sage.base.spec.api.Clock;
import me.icymint.sage.base.spec.api.SessionService;
import me.icymint.sage.base.spec.def.Bool;
import me.icymint.sage.base.spec.def.Magics;
import me.icymint.sage.base.spec.internal.api.RuntimeContext;
import me.icymint.sage.user.data.mapper.SessionMapper;
import me.icymint.sage.user.spec.entity.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by daniel on 16/9/6.
 */
@ConditionalOnProperty(name = Magics.PROP_ENABLE_SESSION_STORAGE, havingValue = "true")
@Service
public class SessionServiceImpl implements SessionService {
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    SessionMapper sessionMapper;
    @Autowired
    SessionServiceImpl sessionService;
    @Autowired
    TokenServiceImpl tokenService;
    @Autowired
    Clock clock;

    @Override
    @Transactional
    public String fetchSession(RuntimeContext context) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        while (true) {
            try {
                sessionService.update(attributes, context);
                break;
            } catch (DuplicateKeyException e) {
                // ignore exception
            }
        }
        return attributes.getSessionId();
    }

    @Transactional
    public void update(ServletRequestAttributes attributes, RuntimeContext context) {
        if (context.getUserId() == null) {
            return;
        }
        Long userId = context.getUserId();
        Session session = sessionMapper.findBySessionId(attributes.getSessionId());
        if (session == null) {
            HttpSession hs = attributes.getRequest().getSession(false);
            sessionMapper.save(new Session()
                    .setOwnerId(userId)
                    .setSessionId(attributes.getSessionId())
                    .setClientId(context.getClientId())
                    .setIp(context.getUserAddress())
                    .setExpireTime(Instant.ofEpochMilli(hs.getLastAccessedTime())
                            .plusSeconds(hs.getMaxInactiveInterval()))
                    .setTimeZone(Optional.ofNullable(context.getTimeZone())
                            .map(ZoneId::getId).orElse(null)));
        } else if (!Objects.equals(session.getOwnerId(), userId)
                || session.getExpireTime().isBefore(clock.now())) {
            HttpSession hs = attributes.getRequest().getSession(false);
            if (hs != null) {
                hs.invalidate();
            }
            sessionMapper.update(new Session()
                    .setId(session.getId())
                    .setIsDeleted(Bool.Y));
            tokenService.expireBySessionId(session.getSessionId());
            update(attributes, context);
        }
    }
}
