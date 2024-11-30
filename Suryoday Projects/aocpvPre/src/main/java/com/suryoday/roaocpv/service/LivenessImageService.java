package com.suryoday.roaocpv.service;

import org.json.JSONObject;

public interface LivenessImageService {

	JSONObject checkLivenessOfImage(String encoded, JSONObject header);

}
