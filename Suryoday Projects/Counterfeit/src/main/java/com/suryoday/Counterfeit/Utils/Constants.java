package com.suryoday.Counterfeit.Utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public class Constants {

	public static String YEAR_MONTH_PATTERN = "\\d{4}-\\d{2}";
	public static String YEAR_MONTH_DATE_PATTERN = "\\d{4}-\\d{2}-\\d{2}";

	public static Long getApplicationId() {
		String uniqueIdString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss"))
				+ String.format("%03d", ThreadLocalRandom.current().nextInt(1000));
		return Long.parseLong(uniqueIdString);
	}

	public static Long getApplicationId(String branchId) {
		// Generate the unique part of the ID
		String uniqueIdString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss"));

		// Combine branchId with the uniqueIdString
		String fullIdString = branchId + uniqueIdString;
		// Return the combined string as a Long
		return Long.parseLong(fullIdString);
	}
}
