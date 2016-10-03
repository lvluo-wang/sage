package me.icymint.sage.user.core.service;

import me.icymint.sage.base.spec.api.Clock;
import me.icymint.sage.base.spec.def.Magics;
import me.icymint.sage.user.data.mapper.ClockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * Created by daniel on 16/9/3.
 */
@Profile("default")
@Service
@ConditionalOnProperty(name = Magics.PROP_ENABLE_DB_CLOCK, havingValue = "true", matchIfMissing = true)
public class ClockImpl implements Clock {

    @Autowired
    ClockMapper clockMapper;

    @Override
    public Instant now() {
        return clockMapper.now();
    }
}
