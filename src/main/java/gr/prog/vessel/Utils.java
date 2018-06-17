package gr.prog.vessel;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Utils {

	public static Timestamp convertToUTC(LocalDateTime localDateTime, ZoneId originalZone) {
		ZonedDateTime zoned = ZonedDateTime.of(localDateTime, originalZone);
		LocalDateTime localDateUTC = zoned.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
		return Timestamp.valueOf(localDateUTC);
	}

	public static Timestamp convertToUTC(Timestamp timestamp, ZoneId originalZone) {
		return convertToUTC(timestamp.toLocalDateTime(), originalZone);
	}

}
