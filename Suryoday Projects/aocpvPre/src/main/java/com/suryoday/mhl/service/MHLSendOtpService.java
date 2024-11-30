package com.suryoday.mhl.service;

import org.json.JSONObject;

public interface MHLSendOtpService {

	JSONObject sendOtp(JSONObject jsonObject, JSONObject header);

	JSONObject validateOTP(String oTP, JSONObject header);

}
