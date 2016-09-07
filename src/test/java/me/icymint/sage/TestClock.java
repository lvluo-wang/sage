package me.icymint.sage;

import me.icymint.sage.user.core.service.ClockImpl;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.LongAdder;

/**
 * Created by daniel on 16/9/3.
 */
@Service
public class TestClock extends ClockImpl {
    private final LongAdder longAdder = new LongAdder();

    @Override
    public Instant now() {
        return super.now().plusSeconds(longAdder.longValue());
    }

    public void shiftDuration(@NotNull Duration duration) {
        longAdder.add(duration.getSeconds());
    }

}
