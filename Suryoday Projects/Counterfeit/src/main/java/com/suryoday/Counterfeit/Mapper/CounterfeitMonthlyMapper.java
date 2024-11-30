package com.suryoday.Counterfeit.Mapper;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.suryoday.Counterfeit.Pojo.Counterfeit;
import com.suryoday.Counterfeit.enums.UserRole;

@Service
public class CounterfeitMonthlyMapper {

	public JSONObject convertToJson(Counterfeit counterfeit, String role) {
		JSONObject monthlyReportJson = new JSONObject();

		monthlyReportJson.put("id", counterfeit.getId());
		monthlyReportJson.put("isDetect", counterfeit.getIsDetect());
		monthlyReportJson.put("branchCode", counterfeit.getBranchCode());
		monthlyReportJson.put("branchName", counterfeit.getBranchName());
		monthlyReportJson.put("status",
				(role.equals(UserRole.COUNTERFEIT_HO.getRole()) && !counterfeit.getStatus().equals("Submitted")) ? "Pending"
						: counterfeit.getStatus());
		monthlyReportJson.put("month", counterfeit.getMonth());
		monthlyReportJson.put("createdDate", counterfeit.getCreatedDate());
		monthlyReportJson.put("createdDate", counterfeit.getCreatedDate());

		// Set empty string if null
		monthlyReportJson.put("roRemarks", (counterfeit.getRoRemarks() == null) ? "" : counterfeit.getRoRemarks());
		monthlyReportJson.put("bmRemarks", (counterfeit.getBmRemarks() == null) ? "" : counterfeit.getBmRemarks());
		return monthlyReportJson;
	}
}