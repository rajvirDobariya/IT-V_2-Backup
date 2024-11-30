package com.suryoday.CustomerIntraction.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
	
	public static LocalDate convertDateStringIntoDate(String dateString) {
		LocalDate date;

		// Split the input string
		String[] parts = dateString.split("-");

		// Parse year, month, and day from the split parts
		int year = Integer.parseInt(parts[0]);
		int month = Integer.parseInt(parts[1]);
		int day = Integer.parseInt(parts[2]);

		// Create a LocalDate object
		date = LocalDate.of(year, month, day);
		return date;
	}
	
	public static String getMonthName(String meetingDate) throws ParseException {
		
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = inputFormat.parse(meetingDate);
        
        // Format to get the month name
        SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
        String monthName = outputFormat.format(date);
        
        return monthName;
	}
	
	public static String getYear(String meetingDate) throws ParseException{
		 SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
         Date date = inputFormat.parse(meetingDate);
         
         // Format to get the year
         SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
         String year = yearFormat.format(date);
         
         return year;
	}

}
