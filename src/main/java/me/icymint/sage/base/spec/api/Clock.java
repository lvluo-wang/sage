package me.icymint.sage.base.spec.api;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by daniel on 16/9/3.
 */
@FunctionalInterface
public interface Clock {
    default Date nowDate() {
        return Date.from(now());
    }

    Instant now();

    default LocalDate localDate() {
        return now().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    default LocalDateTime localDateTime() {
        return now().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    default LocalTime localTime() {
        return now().atZone(ZoneId.systemDefault()).toLocalTime();
    }

    default Long timestamp() {
        return now().getEpochSecond();
    }
}
