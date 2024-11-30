package com.suryoday.aocpv.service;

import java.io.IOException;

import org.json.JSONObject;

public interface MdmBlockingService {

	JSONObject tab(String bearer, String deviceId) throws IOException;

	JSONObject samsungknox() throws IOException;
}
