package huangzehai.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public final class DateTimeUtils {
    private DateTimeUtils() {

    }

    public static LocalDateTime randomDate() {
        ZoneOffset zoneOffset = OffsetDateTime.now().getOffset();
        long epochSecondOfLastYear = LocalDateTime.now().minusYears(1).toEpochSecond(zoneOffset);
        long epochSecondNow = Instant.now().getEpochSecond();
        long diff = epochSecondNow - epochSecondOfLastYear;
        long randomEpochSecond = epochSecondOfLastYear + (long) (diff * Math.random());
        return LocalDateTime.ofEpochSecond(randomEpochSecond, 0, zoneOffset);
    }

}
