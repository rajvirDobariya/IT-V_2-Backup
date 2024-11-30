package com.suryodaybank.enduse.service;

import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

public interface EndUseActivityService {

	public JSONObject add(MultipartFile file);

	public JSONObject getActivities(String request, String channel, String x_Session_ID, String x_User_ID, boolean b);
}
