package com.suryoday.loantrackingphase2.mapper;

import org.json.JSONObject;

import com.suryoday.loantrackingphase2.model.HealthCheckActivity;

public class HealthCheckActivityMapper {

	public static JSONObject toJSONObject(HealthCheckActivity activity, JSONObject product) {
		JSONObject jsonObject = new JSONObject();

		jsonObject.put("id", activity.getId());
		jsonObject.put("panNo", activity.getPanNo());
		jsonObject.put("custId", activity.getCustId());
		jsonObject.put("custName", activity.getCustName());
		jsonObject.put("fileReceiveDate", activity.getFileReceiveDate().toString()); // Convert LocalDate to String
		jsonObject.put("BDMEmpId", activity.getBDMEmpId());
		jsonObject.put("SMEmpId", activity.getSMEmpId());
		jsonObject.put("ASMEmpId", activity.getASMEmpId());
		jsonObject.put("BDMEmpName", activity.getBDMEmpName());
		jsonObject.put("SMEmpName", activity.getSMEmpName());
		jsonObject.put("ASMEmpName", activity.getASMEmpName());
		jsonObject.put("custSourcingBranchName", activity.getCustSourcingBranchName());
		jsonObject.put("productCode", activity.getProductCode());
		jsonObject.put("productData", product);
		jsonObject.put("queries", activity.getQueries());
		jsonObject.put("remarks", activity.getRemarks());
		jsonObject.put("applicationId", activity.getApplicationId());
		jsonObject.put("status", activity.getStatus());
		jsonObject.put("loanAmount", activity.getLoanAmount());
		jsonObject.put("firstTimeRight", activity.getFirstTimeRight());
		jsonObject.put("billedOrDispatched", activity.getBilledOrDispatched());

		return jsonObject;
	}
}
