package com.suryoday.Counterfeit.Mapper;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class DenominationSummaryMapper {

	public static JSONObject convertToJsonObject(List<Object[]> dailySummary, List<Object[]> monthlySummary) {
		JSONArray summary = new JSONArray();
		long grandTotal = 0;
		long grandCount = 0;

		// Daily Summary
		for (Object[] obj : dailySummary) {
			JSONObject summaryObj = new JSONObject();
			int denominationNote = (Integer) obj[0];
			long count = (Long) obj[1];
			long total = (Long) obj[2];

			summaryObj.put("denominationNote", denominationNote);
			summaryObj.put("count", count);
			summaryObj.put("total", total);
			summary.put(summaryObj);

			// Calculate grandCount & grandTotal
			grandCount += count;
			grandTotal += total;
		}

		// Monthly Summary
		if (monthlySummary != null) {
			for (Object[] obj : monthlySummary) {
				JSONObject summaryObj = new JSONObject();
				int denominationNote = (Integer) obj[0];
				long count = (Long) obj[1];
				long total = (Long) obj[2];

				// Check if a matching denomination exists in daily summary
				boolean existsInDaily = false;
				for (int i = 0; i < summary.length(); i++) {
					JSONObject dailyObj = summary.getJSONObject(i);
					int dailyDenominationNote = dailyObj.getInt("denominationNote");

					if (dailyDenominationNote == denominationNote) {
						long dailyCount = dailyObj.getLong("count");
						long dailyTotal = dailyObj.getLong("total");
						dailyObj.put("count", dailyCount + count);
						dailyObj.put("total", dailyTotal + total);
						existsInDaily = true;
						break;
					}
				}

				// If not exists in daily summary, add a new entry
				if (!existsInDaily) {
					summaryObj.put("denominationNote", denominationNote);
					summaryObj.put("count", count);
					summaryObj.put("total", total);
					summary.put(summaryObj);
				}

				// Calculate grandCount & grandTotal
				grandCount += count;
				grandTotal += total;
			}
		}

		// Final Summary
		JSONObject result = new JSONObject();
		result.put("summary", summary);
		result.put("grandCount", grandCount);
		result.put("grandTotal", grandTotal);
		return result;
	}
}
