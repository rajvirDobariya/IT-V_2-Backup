package com.suryoday.Counterfeit.Mapper;

import java.util.Date;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.suryoday.Counterfeit.Pojo.Counterfeit;

@Service
public class CounterfeitDailyMapper {

	private void putJsonSafe(JSONObject json, String key, Object value) {
		if (value == null) {
			json.put(key, JSONObject.NULL);
		} else if (value instanceof Long || value instanceof Integer) {
			json.put(key, ((Number) value).longValue());
		} else {
			json.put(key, value);
		}
	}

	public JSONObject convertToJson(Counterfeit counterfeit) {
		JSONObject counterfeitJson = new JSONObject();

		putJsonSafe(counterfeitJson, "id", counterfeit.getId());
		putJsonSafe(counterfeitJson, "dailyMonthly", counterfeit.getDailyMonthly());
		putJsonSafe(counterfeitJson, "isDetect", counterfeit.getIsDetect());
		putJsonSafe(counterfeitJson, "month", counterfeit.getMonth());
		putJsonSafe(counterfeitJson, "branchCode", counterfeit.getBranchCode());
		putJsonSafe(counterfeitJson, "branchName", counterfeit.getBranchName());
		putJsonSafe(counterfeitJson, "status", counterfeit.getStatus());
		putJsonSafe(counterfeitJson, "roId", counterfeit.getRoId());
		putJsonSafe(counterfeitJson, "roRole", counterfeit.getRoRole());
		putJsonSafe(counterfeitJson, "bmId", counterfeit.getBmId());
		putJsonSafe(counterfeitJson, "bmRole", counterfeit.getBmRole());
		putJsonSafe(counterfeitJson, "createdDate", counterfeit.getCreatedDate());
		putJsonSafe(counterfeitJson, "createdBy", counterfeit.getCreatedBy());
		putJsonSafe(counterfeitJson, "theNotesHaveBeenImpounded", counterfeit.getTheNotesHaveBeenImpounded());
		putJsonSafe(counterfeitJson, "theRegisterUpdatedWithDetails", counterfeit.getTheRegisterUpdatedWithDetails());
		putJsonSafe(counterfeitJson, "anAcknowledgmentReceiptPrepared",
				counterfeit.getAnAcknowledgmentReceiptPrepared());

		Date updatedDate = counterfeit.getUpdatedDate();
		putJsonSafe(counterfeitJson, "updateDate", updatedDate == null ? null : updatedDate.toString());
		putJsonSafe(counterfeitJson, "updateBy", counterfeit.getUpdateBy());

		putJsonSafe(counterfeitJson, "roRemarks",
				(counterfeit.getRoRemarks()) != null ? counterfeit.getRoRemarks() : "");
		putJsonSafe(counterfeitJson, "bmRemarks",
				(counterfeit.getBmRemarks()) != null ? counterfeit.getBmRemarks() : "");

		return counterfeitJson;
	}
}