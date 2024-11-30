package com.suryoday.customerOnboard.service;

import org.json.JSONObject;
public interface CustomerOnboardAddressService {

	JSONObject addAddress(JSONObject jsonObject,String applicationNo, JSONObject header);

}
