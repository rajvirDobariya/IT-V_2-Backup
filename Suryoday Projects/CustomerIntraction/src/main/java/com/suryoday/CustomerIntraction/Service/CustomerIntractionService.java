package com.suryoday.CustomerIntraction.Service;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;

import org.json.JSONObject;

public interface CustomerIntractionService {

	public JSONObject fetchCustomerName(String requestString, String channel, String x_Session_ID, String x_encode_ID,
			HttpServletRequest request) throws Exception;

	public JSONObject saveMeetingDetails(String requestString, String channel, String x_Session_ID, String x_encode_ID,
			HttpServletRequest request) throws Exception;

	public JSONObject getById(String requestString, @NotBlank String channel, @NotBlank String x_Session_ID,
			@NotBlank String x_encode_ID, HttpServletRequest request)throws Exception;

}
