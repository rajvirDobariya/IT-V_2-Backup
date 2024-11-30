package com.suryoday.twowheeler.service;

import java.util.List;

import org.json.JSONObject;

import com.suryoday.twowheeler.pojo.LeegalityInfo;

public interface TwowheelerLeegalityService {

	JSONObject sendLeegality(String applicationNo, String base64pdf, String documentType);

	void updateDocumentId(JSONObject data1, String applicationNo, String documentType);

	JSONObject checkLeegality(String documentId, JSONObject header, String documentType);

	List<LeegalityInfo> updateLeegalityVerify(String applicationNo, JSONObject data1, String documentType);

	List<LeegalityInfo> getByApplicationNoAndDocument(String applicationNo, String documentType);

}
