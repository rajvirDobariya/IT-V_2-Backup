package com.suryoday.travelvisit.service;

import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

public interface VisitToService {

	JSONObject addVisitTo(String request, String channel, String x_Session_ID, String x_User_ID, boolean isEncrypt,MultipartFile image);

	JSONObject getVisitTos(String request, String channel, String x_Session_ID, String x_User_ID, boolean isEncrypt);

	JSONObject getVisitToExcel(String request, String channel, String x_Session_ID, String x_User_ID, boolean isEncrypt);

	String base64ToExcel(String request);

}
