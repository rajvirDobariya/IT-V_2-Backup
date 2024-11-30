package com.digitisation.branchreports.mapper;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BranchUserMakerModelMapper {

	@Value("${file-path}")
	private String filePath;

	public JSONObject convertToJson(Object[] object) {
		JSONObject report = new JSONObject();

		// Ensure we handle each index explicitly
		int i = 0;
		report.put("id", object[i] != null ? object[i] : JSONObject.NULL);
		i++;
		report.put("action", object[i] != null ? object[i] : JSONObject.NULL);
		i++;
		report.put("createdDate", object[i] != null ? object[i] : JSONObject.NULL);
		i++;
		report.put("updatedDate", object[i] != null ? object[i] : JSONObject.NULL);
		i++;
		report.put("reportDate", object[i] != null ? object[i] : JSONObject.NULL);
		i++;
		report.put("branchname", object[i] != null ? object[i] : JSONObject.NULL);
		i++;
		report.put("branchcode", object[i] != null ? object[i] : JSONObject.NULL);
		i++;
		report.put("status", object[i] != null ? object[i] : JSONObject.NULL);
		i++;
		report.put("submissiondate", object[i] != null ? object[i] : JSONObject.NULL);
		i++;
		report.put("reportId", object[i] != null ? object[i] : JSONObject.NULL);
		i++;
		report.put("reportfrequency", object[i] != null ? object[i] : JSONObject.NULL);
		i++;
		report.put("reportname", object[i] != null ? object[i] : JSONObject.NULL);
		i++;
		report.put("roId", object[i] != null ? object[i] : JSONObject.NULL);
		i++;
		report.put("roRemarks", object[i] != null ? object[i] : "");
		i++;
		report.put("roDocId", object[i] != null ? object[i] : JSONObject.NULL);
		i++;
		report.put("roDocName", object[i] != null ? object[i] : JSONObject.NULL);
		i++;
		report.put("roDocType", object[i] != null ? object[i] : JSONObject.NULL);
		i++;
		Object object2 = object[i] != null ? object[i] : JSONObject.NULL;
//		report.put("roDocBase64", FileUtils.convertFileToBase64(String.valueOf(object2)));
//		report.put("roDocBytes", FileUtils.convertFileToBytes(String.valueOf(object2)));
		i++;

		report.put("bmId", object[i] != null ? object[i] : JSONObject.NULL);
		i++;
		report.put("bmRemarks", object[i] != null ? object[i] : "");
		i++;
		report.put("hoId", object[i] != null ? object[i] : JSONObject.NULL);
		i++;
		report.put("hoRemarks", object[i] != null ? object[i] : "");
		i++;
		report.put("auditorId", object[i] != null ? object[i] : JSONObject.NULL);
		i++;
		report.put("auditorRemarks", object[i] != null ? object[i] : "");
		i++;
		report.put("audDocId", object[i] != null ? object[i] : JSONObject.NULL);
		i++;
		report.put("audDocName", object[i] != null ? object[i] : JSONObject.NULL);
		i++;
		report.put("audDocType", object[i] != null ? object[i] : JSONObject.NULL);
		i++;
		Object object3 = object[i] != null ? object[i] : JSONObject.NULL;
//		report.put("audDocBase64", FileUtils.convertFileToBase64(String.valueOf(object3)));
//		report.put("audDocBytes", FileUtils.convertFileToBytes(String.valueOf(object3)));

		return report;
	}

}
