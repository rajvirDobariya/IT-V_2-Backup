package com.suryoday.familyMember.service;

import org.json.JSONObject;

public interface SendOTPService {

	JSONObject sendOtp(JSONObject jsonObject, JSONObject header);

	JSONObject validateOTP(String oTP, JSONObject header);

}
