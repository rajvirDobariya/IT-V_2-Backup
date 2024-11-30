package com.suryoday.CustomerIntraction.Util;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class TimeUtils {
	
	public static Timestamp convertTimeStringToTimestamp(String timeString, LocalDate meetingDate) {
	    // Define the formatter for the input time format
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);

	    // Parse the time string into a LocalTime
	    LocalTime localTime = LocalTime.parse(timeString.trim(), formatter);

	    // Combine LocalTime with the current date to create LocalDateTime
	    LocalDateTime localDateTime = LocalDateTime.of(meetingDate, localTime);

	    // Convert LocalDateTime to Timestamp
	    return Timestamp.valueOf(localDateTime);
	}

}
