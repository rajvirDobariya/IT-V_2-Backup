package com.suryoday.dsaOnboard.service;

import org.json.JSONObject;

public interface DsaOnboardImpsService {

	JSONObject searchIfsc(String ifscCode, JSONObject header);

	JSONObject pennyLess(String accountNo, String ifsc, JSONObject header,String accountHolderName);

}
