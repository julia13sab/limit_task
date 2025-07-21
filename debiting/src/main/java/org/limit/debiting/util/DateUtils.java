package org.limit.debiting.util;

import lombok.experimental.UtilityClass;

import java.time.OffsetDateTime;

@UtilityClass
public class DateUtils {

    public OffsetDateTime startDay() {
        return OffsetDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    public OffsetDateTime endDay() {
        return OffsetDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999);
    }
}
