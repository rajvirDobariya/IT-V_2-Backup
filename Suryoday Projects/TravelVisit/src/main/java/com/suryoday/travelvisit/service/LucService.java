package com.suryoday.travelvisit.service;

import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

public interface LucService {

	JSONObject addLuc(String request, String channel, String x_Session_ID, String x_User_ID, boolean b,
			MultipartFile image);

	JSONObject getLucs(String request, String channel, String x_Session_ID, String x_User_ID, boolean b);

	JSONObject getExcelLuc(String request, String channel, String x_Session_ID, String x_User_ID, boolean b);

}
