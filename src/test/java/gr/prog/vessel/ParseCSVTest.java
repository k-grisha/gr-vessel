package gr.prog.vessel;

import org.junit.Test;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class ParseCSVTest {

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss[.SSS][.SS][.S]");
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss[.S]");

	@Test
	public void simple() throws ParseException {
		Date parsedDate = simpleDateFormat.parse("2015-01-04 11:31:39.111");
		Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
		System.out.println(timestamp);

		Date parsedDate2 = simpleDateFormat.parse("2015-01-04 11:31:39");
		Timestamp timestamp2 = new java.sql.Timestamp(parsedDate2.getTime());
		System.out.println(timestamp2);
	}

	@Test
	public void base() throws ParseException {
		System.out.println(TimeZone.getAvailableIDs());

		String date = "2015-01-04 11:31:39.055";
		LocalDateTime localDate = LocalDateTime.parse(date, formatter);

		ZonedDateTime zoned = ZonedDateTime.of(LocalDateTime.parse(date, formatter), ZoneId.of("Europe/Berlin"));
		LocalDateTime localDateUTC = zoned.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();

		Timestamp timestamp = Timestamp.valueOf(localDate);
		Timestamp timestampUTC = Timestamp.valueOf(localDateUTC);
//		1420360299055
		System.out.println(timestamp.getTime());
		System.out.println(timestampUTC.getTime());
//		System.out.println(localDateUTC);
	}

	@Test
	public void timeParseTest() {
		String date = "2015-01-04 11:31:39.055";
		LocalDateTime localDate = LocalDateTime.parse(date, formatter);
		System.out.println(localDate);

		String date2 = "2015-01-05 14:47:44.2";
		LocalDateTime localDate2 = LocalDateTime.parse(date2, formatter);
		System.out.println(localDate2);
//		2016-04-20 14:05:48
		String date3 = "2016-04-20 14:05:48";
		LocalDateTime localDate3 = LocalDateTime.parse(date3, formatter);
		System.out.println(localDate3);
	}

}