package com.suryoday.uam.service;

import org.json.JSONObject;

public interface SendOtpService {

	JSONObject sendOtp(JSONObject jsonObject, JSONObject header);

	JSONObject validateOTP(String oTP, JSONObject header);

}
