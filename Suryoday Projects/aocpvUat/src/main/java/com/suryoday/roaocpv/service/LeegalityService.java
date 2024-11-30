package com.suryoday.roaocpv.service;

import org.json.JSONObject;

public interface LeegalityService {

	JSONObject leegalityCheck(long applicationNoInLong, JSONObject header);

	JSONObject statusCheck(long applicationNoInLong, JSONObject header);

}
