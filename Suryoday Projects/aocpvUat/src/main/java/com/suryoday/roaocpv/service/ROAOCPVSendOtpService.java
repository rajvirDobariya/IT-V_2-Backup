package com.suryoday.roaocpv.service;

import org.json.JSONObject;

public interface ROAOCPVSendOtpService {

	JSONObject sendOtp(JSONObject jsonObject, JSONObject header);

	JSONObject validateOTP(String oTP, JSONObject header);

}
