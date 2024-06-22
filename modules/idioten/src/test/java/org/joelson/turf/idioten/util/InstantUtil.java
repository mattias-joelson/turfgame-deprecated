package org.joelson.turf.idioten.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.TimeZone;

public class InstantUtil {

    private InstantUtil() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated.");
    }

    public static Instant toInstant(String datetime) {
        return LocalDateTime.parse(datetime).atZone(TimeZone.getTimeZone("UTC/Greenwich").toZoneId()).toInstant();
    }

    public static Instant getInstantNowTruncatedtoSecond() {
        return Instant.now().truncatedTo(ChronoUnit.SECONDS);
    }

    public static Instant addMinutes(Instant instant, long minutes) {
        return instant.plus(minutes, ChronoUnit.MINUTES);
    }

    public static Instant addDays(Instant instant, long days) {
        return instant.plus(days, ChronoUnit.DAYS);
    }

}
