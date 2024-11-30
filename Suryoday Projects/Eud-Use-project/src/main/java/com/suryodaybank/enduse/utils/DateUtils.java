package com.suryodaybank.enduse.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

import com.suryodaybank.enduse.exception.EndUseException;

public class DateUtils {

	public static boolean isFirstDayOfMonth(LocalDate date) {
		return date.getDayOfMonth() == 1;
	}

	public static boolean isDateMonday(LocalDate date) {
		return date.getDayOfWeek() == DayOfWeek.MONDAY;
	}

	// Check Saturday and Sunday
	// Method to check if a date is a holiday (Sunday, 2nd Saturday, or 4th
	// Saturday)
	public static boolean isDateHoliday(LocalDate date) {
		return date.getDayOfWeek() == DayOfWeek.SUNDAY || (date.getDayOfWeek() == DayOfWeek.SATURDAY
				&& ((date.getDayOfMonth() > 7 && date.getDayOfMonth() <= 14)
						|| (date.getDayOfMonth() > 21 && date.getDayOfMonth() <= 28)));
	}

	public static LocalDateTime getDateStringIntoDateTime(String dateString) {
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		LocalDateTime dateTime = null;
		try {
			dateTime = LocalDateTime.parse(dateString, inputFormatter);
		} catch (DateTimeParseException e) {
			e.printStackTrace();
		}
		return dateTime;
	}

	public static LocalDate getDateStringIntoLocalDate(String dateString) {
		LocalDate date;
		try {
			// Split the input string
			String[] parts = dateString.split("-");

			// Parse year, month, and day from the split parts
			int year = Integer.parseInt(parts[0]);
			int month = Integer.parseInt(parts[1]);
			int day = Integer.parseInt(parts[2]);

			// Create a LocalDate object
			date = LocalDate.of(year, month, day);
			return date;
		} catch (Exception e) {
			throw new EndUseException("Please enter date in YYYY-mm-DD format!");
		}
	}

	public static LocalDate getStartDateOfMonth(String yearMonthString) {
		YearMonth yearMonth = YearMonth.parse(yearMonthString, DateTimeFormatter.ofPattern("yyyy-MM"));
		return yearMonth.atDay(1);
	}

	public static LocalDate getEndDateOfMonth(String yearMonthString) {
		YearMonth yearMonth = YearMonth.parse(yearMonthString, DateTimeFormatter.ofPattern("yyyy-MM"));
		return yearMonth.atEndOfMonth();
	}

	 // Method to return Date with start of the day (00:00:00)
    public static Date getStartOfDay(String dateStr) {
        LocalDate localDate = LocalDate.parse(dateStr);
        LocalDateTime startOfDay = localDate.atStartOfDay();
        return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    // Method to return Date with end of the day (23:59:59.999)
    public static Date getEndOfDay(String dateStr) {
        LocalDate localDate = LocalDate.parse(dateStr);
        LocalDateTime endOfDay = localDate.atTime(23, 59, 59, 999000000); // Set time to 23:59:59.999
        return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }
}
