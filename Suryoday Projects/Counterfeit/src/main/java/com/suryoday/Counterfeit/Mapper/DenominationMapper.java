package com.suryoday.Counterfeit.Mapper;

import java.time.LocalDate;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.suryoday.Counterfeit.Pojo.Denomination;

@Service
public class DenominationMapper {

	public JSONObject convertToJson(Denomination denomination) {
		JSONObject denominationJson = new JSONObject();

		denominationJson.put("id", denomination.getId());
		denominationJson.put("counterfeitId", denomination.getCounterfeitId());
		denominationJson.put("denominationNote", denomination.getDenominationNote());
		denominationJson.put("tendererAccountNumber", denomination.getTendererAccountNumber());
		denominationJson.put("tendererCustomerName", denomination.getTendererCustomerName());
		denominationJson.put("serialNumber", denomination.getSerialNumber());
		denominationJson.put("securityFeatureBreached", denomination.getSecurityFeatureBreached());
		denominationJson.put("createdDate", denomination.getCreatedDate());
		denominationJson.put("createdBy", denomination.getCreatedBy());
		denominationJson.put("updateDate", denomination.getUpdatedDate());
		denominationJson.put("updateBy", denomination.getUpdateBy());
		return denominationJson;
	}

	public JSONObject convertToJson(Object[] denomination) {
		JSONObject denominationJson = new JSONObject();
		int i = 0;
		denominationJson.put("id", denomination[i++]);
		denominationJson.put("craetedDate", denomination[i++]);
		LocalDate detectDate = (LocalDate) denomination[i++];
		denominationJson.put("detectDate", detectDate.toString());
		denominationJson.put("denominationNote", denomination[i++]);
		denominationJson.put("tendererAccountNumber", denomination[i++]);
		denominationJson.put("tendererCustomerName", denomination[i++]);
		denominationJson.put("serialNumber", denomination[i++]);
		denominationJson.put("securityFeatureBreached", denomination[i++]);
		denominationJson.put("counterfeitId", denomination[i++]);
		denominationJson.put("dailyMonthly", denomination[i++]);
		denominationJson.put("branchCode", denomination[i++]);
		denominationJson.put("branchName", denomination[i++]);
		return denominationJson;
	}

}