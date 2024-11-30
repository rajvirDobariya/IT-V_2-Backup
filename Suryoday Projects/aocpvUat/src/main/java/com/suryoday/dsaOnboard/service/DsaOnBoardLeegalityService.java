package com.suryoday.dsaOnboard.service;

import java.util.List;

import org.json.JSONObject;

import com.suryoday.dsaOnboard.pojo.DsaOnBoardLeegalityInfo;

public interface DsaOnBoardLeegalityService {

	JSONObject sendLeegality(String applicationNo, String base64pdf, String documentType);

	void updateDocumentId(JSONObject data1, String applicationNo, String documentType);

	List<DsaOnBoardLeegalityInfo> getByApplicationNoAndDocument(String applicationNo, String documentType);

	JSONObject checkLeegality(String applicationNo, JSONObject header, String documentType, String documentId);

	List<DsaOnBoardLeegalityInfo> updateLeegalityVerify(String applicationNo, JSONObject data1, String documentType);

}
