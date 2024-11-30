package com.suryoday.Counterfeit.Utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.suryoday.Counterfeit.Exception.CounterfeitException;

public class DateUtils {

	public static LocalDate convertDateStringIntoLocalDate(String dateString) {
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
			throw new CounterfeitException("Please enter date in YYYY-mm-DD format!");
		}
	}

	public static Date convertDateStringIntoDate(String dateString) {
		try {
			LocalDate localDate = LocalDate.parse(dateString);
			Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
			return date;
		} catch (Exception e) {
			throw new CounterfeitException("Please enter date in YYYY-MM-DD format!");
		}
	}

	public static YearMonth convertMonthStringIntoYearMonth(String monthString) {
		try {
			// Split the input string
			String[] parts = monthString.split("-");

			// Parse year and month from the split parts
			int year = Integer.parseInt(parts[0]);
			int month = Integer.parseInt(parts[1]);

			// Create a YearMonth object
			YearMonth yearMonth = YearMonth.of(year, month);
			return yearMonth;
		} catch (Exception e) {
			throw new CounterfeitException("Please enter month in YYYY-mm format!");
		}
	}

	public static String convertYearMonthIntoMonthString(YearMonth yearMonth) {
		String formattedString = yearMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"));
		return formattedString;
	}

	public static Date getStartDateOfMonth(YearMonth yearMonth) {
		LocalDate firstDayOfMonth = yearMonth.atDay(1);
		LocalDateTime startOfDay = firstDayOfMonth.atStartOfDay();
		return convertToDate(startOfDay);
	}

	public static Date getEndDateOfMonth(YearMonth yearMonth) {
		LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();
		LocalDateTime endOfDay = LocalDateTime.of(lastDayOfMonth, LocalTime.MAX);
		return convertToDate(endOfDay);
	}

	public static Date getStartDateOfMonth(String yearMonthString) {
		YearMonth yearMonth = YearMonth.parse(yearMonthString, DateTimeFormatter.ofPattern("yyyy-MM"));
		LocalDate firstDayOfMonth = yearMonth.atDay(1);
		LocalDateTime startOfDay = firstDayOfMonth.atStartOfDay();
		return convertToDate(startOfDay);
	}

	public static Date getEndDateOfMonth(String yearMonthString) {
		YearMonth yearMonth = YearMonth.parse(yearMonthString, DateTimeFormatter.ofPattern("yyyy-MM"));
		LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();
		LocalDateTime endOfDay = LocalDateTime.of(lastDayOfMonth, LocalTime.MAX);
		return convertToDate(endOfDay);
	}

	private static Date convertToDate(LocalDateTime localDateTime) {
		return java.sql.Timestamp.valueOf(localDateTime);
	}

	public static Date getStartOfDay(String dateString) {
		try {
			LocalDate localDate = LocalDate.parse(dateString);
			LocalDateTime startOfDay = localDate.atStartOfDay();
			return Date.from(startOfDay.toInstant(ZoneOffset.UTC));
		} catch (Exception e) {
			throw new CounterfeitException("Please enter date in YYYY-MM-DD format!");
		}
	}

	public static Date getEndOfDay(String dateString) {
		try {
			LocalDate localDate = LocalDate.parse(dateString);
			LocalDateTime endOfDay = localDate.atTime(LocalTime.MAX);
			return Date.from(endOfDay.toInstant(ZoneOffset.UTC));
		} catch (Exception e) {
			throw new CounterfeitException("Please enter date in YYYY-MM-DD format!");
		}
	}
	
	 public static Date getStartOfDay() {
	        LocalDate today = LocalDate.now();
	        LocalDateTime startOfDay = today.atStartOfDay();
	        return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
	    }

	    public static Date getEndOfDay() {
	        LocalDate today = LocalDate.now();
	        LocalDateTime endOfDay = today.atTime(23, 59, 59);
	        return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
	    }

}
