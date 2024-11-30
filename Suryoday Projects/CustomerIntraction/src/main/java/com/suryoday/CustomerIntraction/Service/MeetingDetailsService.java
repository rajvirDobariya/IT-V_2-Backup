package com.suryoday.CustomerIntraction.Service;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

public interface MeetingDetailsService {
	
	public JSONObject getMeetingDetails(String channel, String x_Session_ID, String x_encode_ID,
			String x_encode_ID2, HttpServletRequest request) throws Exception;

	public JSONObject downloadPDF(String encryptRequestString, String channel, String X_Session_ID, String X_encode_ID,
			HttpServletRequest request) throws Exception;

    public JSONObject getMeetingData(JSONObject encryptRequestString, String channel, String X_Session_ID, String X_encode_ID,
			HttpServletRequest request) throws Exception;
	

}
