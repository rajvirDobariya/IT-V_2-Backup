package com.suryoday.Counterfeit.Service;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

public interface DenominationService {

	public JSONObject getDenominations(String encryptRequestString, String channel, String X_Session_ID,
			String X_encode_ID, HttpServletRequest request, boolean isDownload, boolean isEncrypt);

	public JSONObject downloadPDF(String encryptRequestString, String channel, String X_Session_ID, String X_encode_ID,
			HttpServletRequest request, boolean isEncrypt);

	public JSONObject updateDenominations(String requestString, String channel, String x_Session_ID, String x_encode_ID,
			HttpServletRequest request, boolean isEncrypt);

	public void deleteAll();

	void deleteByCounterfeitId(Long counterfeitId);

}
