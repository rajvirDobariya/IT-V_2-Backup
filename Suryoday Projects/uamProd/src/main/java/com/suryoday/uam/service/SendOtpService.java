package com.suryoday.uam.service;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

public interface SendOtpService {

	JSONObject sendOtp(JSONObject jsonObject, JSONObject header);

	JSONObject validateOTP(String oTP, JSONObject header);

}
