package org.joelson.mattias.turfgame.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public final class TimeUtil {
    
    private static final int TIMESTAMP_END_INDEX = 19;
    
    private TimeUtil() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated!");
    }
    
    /**
     * Converts a Turf API timestamp to a Java Instant.
     * @param timestamp on format "2016-02-10T20:47:08+0000", "+0000" can be omitted
     * @return instant of timestamp
     */
    public static Instant turfTimestampToInstant(String timestamp) {
        TemporalAccessor temporalAccessor = DateTimeFormatter.ISO_DATE_TIME.parse(timestamp.substring(0, TIMESTAMP_END_INDEX));
        LocalDateTime localDateTime = LocalDateTime.from(temporalAccessor);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
        return Instant.from(zonedDateTime);
    }
}
